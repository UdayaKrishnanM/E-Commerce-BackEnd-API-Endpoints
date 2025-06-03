package com.ecommerce.demo.service;


import com.ecommerce.demo.controller.AdminBasedController;
import com.ecommerce.demo.dto.CartItemResponseDTO;
import com.ecommerce.demo.dto.ItemDTO;
import com.ecommerce.demo.dto.ProductDTO;
import com.ecommerce.demo.dto.UserDTO;
import com.ecommerce.demo.dto.UserOrderResponse;
import com.ecommerce.demo.exception.CartItemNotFoundException;
import com.ecommerce.demo.exception.OrderNotFoundException;
import com.ecommerce.demo.exception.ProductNotFoundException;
import com.ecommerce.demo.model.CartItem;
import com.ecommerce.demo.model.Order;
import com.ecommerce.demo.model.OrderItem;
import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.repository.OrderItemRepository;
import com.ecommerce.demo.repository.OrderRepository;
import com.ecommerce.demo.repository.ProductRepository;
import com.ecommerce.demo.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class OrderService {

	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

	@Autowired
	private ProductRepository productRepository;
	
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private UserRepository userRepository;

    public List<UserOrderResponse> getAllOrders(Long id){
    	
    	List<Order> ordersList = orderRepository.findByUserId(id);
    	
    	if(ordersList.isEmpty()) {
    		throw new OrderNotFoundException("User ID : " + id + " is empty");
    	} else {
    		return ordersList.stream().map(
    				order -> {
    					User user = order.getUser();
    					UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail());
    					
    					List<ItemDTO> itemsDTO = order.getOrderItems().stream().map(item -> {
    						Product product = item.getProduct();
    						ProductDTO productDTO = new ProductDTO(product.getName(), product.getDescription(),
    			                		product.getPrice(), product.getCategory(), product.getStockQuantity());
    			                return new ItemDTO(item.getId(), productDTO, item.getQuantity(), item.getPrice());
    			            }
    			            ).collect(Collectors.toList());
    					
    			            // Create and return the UserOrderResponse
    			            return new UserOrderResponse(userDTO, itemsDTO, order.getTotalAmount(), order.getStatus(), order.getOrderDate());
    			        }
    			        ).collect(Collectors.toList());
    	}	
    }

    public Optional<UserOrderResponse> getOrderById(Long id) {
    	
    	Optional<Order> orderList= orderRepository.findById(id);
    	    	
    	if(orderList.isEmpty()) {
    		throw new OrderNotFoundException("User ID : " + id + " is empty");
    	} else {
    			return orderList.map(
    				order -> {
    					User user = order.getUser();
    					UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail());
    					
    					List<ItemDTO> itemsDTO = order.getOrderItems().stream().map(item -> {
    						Product product = item.getProduct();
    						ProductDTO productDTO = new ProductDTO(product.getName(), product.getDescription(),
    			                		product.getPrice(), product.getCategory(), product.getStockQuantity());
    			                return new ItemDTO(item.getId(), productDTO, item.getQuantity(), item.getPrice());
    			            }
    			            ).collect(Collectors.toList());

    			            // Create and return the UserOrderResponse
    			            return new UserOrderResponse(userDTO, itemsDTO, order.getTotalAmount(), order.getStatus(), order.getOrderDate());
			        });
    	}    
    }
    
    public Optional<UserOrderResponse> createOrderMethod(Order order) {
        double totalAmount = 0.0;
        boolean flag = true;

        // Check stock for all items before saving the order
        for (OrderItem item : order.getOrderItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                                               .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStockQuantity() < item.getQuantity()) {
                flag = false;
                throw new OrderNotFoundException("Insufficient stock for product: " + product.getName());
            }
            if (item.getQuantity() <= 0) {
                flag = false;
                throw new OrderNotFoundException("Product quantity can't be 0 or negative");
            }
        }

        if (!flag) {
            throw new OrderNotFoundException("Order not placed due to insufficient products");
        }

        // Save the order first
        order = orderRepository.save(order);

        for (OrderItem item : order.getOrderItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                                               .orElseThrow(() -> new RuntimeException("Product not found"));

            // Update the product's stock quantity
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);

            // Set price per unit based on the product's current price
            item.setPrice(product.getPrice());

            // Calculate subtotal for this item
            double subtotal = item.calculateSubtotal();
            totalAmount += subtotal;

            // Set the order reference for the OrderItem
            item.setOrder(order); // Link the Order to the OrderItem
        }

        // Set total amount for the order
        order.setTotalAmount(totalAmount);

        // Save the order items
        orderItemRepository.saveAll(order.getOrderItems());

        // Create UserOrderResponse
        UserOrderResponse response = new UserOrderResponse();
        response.setUser(new UserDTO(order.getUser().getId(), order.getUser().getUsername(), order.getUser().getEmail()));
        response.setItems(order.getOrderItems().stream()
                              .map(itemDetails -> new ItemDTO(itemDetails.getId(), new ProductDTO(
                            		  itemDetails.getProduct().getName(), itemDetails.getProduct().getDescription(), 
                            		  itemDetails.getProduct().getPrice(), itemDetails.getProduct().getCategory(), 
                            		  itemDetails.getProduct().getStockQuantity()), itemDetails.getQuantity(), 
                            		  itemDetails.getPrice()))
                              
                              .collect(Collectors.toList()));
        response.setTotalAmount(totalAmount);
        response.setStatus(order.getStatus());
        response.setOrderDate(order.getOrderDate());

        // Return the response
        return Optional.of(response);
    }

    public Optional<UserOrderResponse> createOrder(Order order) {
    	
    	Optional<Order> orderDetails = orderRepository.findById(order.getId());
    	Optional<User> userDetails = userRepository.findById(order.getUser().getId());
    	
    	if(orderDetails.isPresent()) {
    		throw new OrderNotFoundException("Already exists in order");    		
    	} else {
    		if(userDetails.get().getId() != order.getUser().getId()) {
    			throw new OrderNotFoundException("User not matches");    			
    		} 
    		return createOrderMethod(order);
    	}

    }
  
    public Optional<UserOrderResponse> updateOrderStatus(Long id, Order updateOrder) {
    	
    	Optional<Order> orderOptional = orderRepository.findById(id);

    	//check stored user id and sent user id matches
    	if(updateOrder.getUser().getId()!=orderOptional.get().getUser().getId()) {
    		throw new OrderNotFoundException("Unable to access user not matches");
    	} else {

    		if(orderOptional.isPresent()) {

        		Order order= orderOptional.get();
        		order.setStatus(updateOrder.getStatus());
        		orderRepository.save(order);
    			return orderOptional.map(
        				orders -> {
        					User user = orders.getUser();
        					UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail());
        					
        					List<ItemDTO> itemsDTO = order.getOrderItems().stream().map(item -> {
        						Product product = item.getProduct();
        						ProductDTO productDTO = new ProductDTO(product.getName(), product.getDescription(),
        			                		product.getPrice(), product.getCategory(), product.getStockQuantity());
        			                return new ItemDTO(item.getId(), productDTO, item.getQuantity(), item.getPrice());
        			            }
        			            ).collect(Collectors.toList());

        			            // Create and return the UserOrderResponse
        			            return new UserOrderResponse(userDTO, itemsDTO, order.getTotalAmount(), order.getStatus(), order.getOrderDate());
    			        });            	
        	} else {
        		throw new OrderNotFoundException("Order ID : " + id + " not found");
        	}
    	
    	}    	

    }
    
    public String deleteOrder(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if(order.isPresent()) {
    		orderRepository.deleteById(order.get().getId());
    		return "Deleted Order Successfully ID: " + id;
    	} else {
    		return "Order ID Not Exists";
    	}
    }


}
