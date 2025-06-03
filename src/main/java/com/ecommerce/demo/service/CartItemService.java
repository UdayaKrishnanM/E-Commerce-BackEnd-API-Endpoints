package com.ecommerce.demo.service;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ecommerce.demo.dto.CartItemResponseDTO;
import com.ecommerce.demo.dto.CartResponseDTO;
import com.ecommerce.demo.dto.ItemDTO;
import com.ecommerce.demo.dto.ProductDTO;
import com.ecommerce.demo.dto.UserDTO;
import com.ecommerce.demo.dto.UserOrderResponse;
import com.ecommerce.demo.exception.CartItemNotFoundException;
import com.ecommerce.demo.exception.ProductNotFoundException;
import com.ecommerce.demo.model.CartItem;
import com.ecommerce.demo.model.Order;
import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.repository.CartItemRepository;
import com.ecommerce.demo.repository.ProductRepository;
import com.ecommerce.demo.repository.UserRepository;

@Service
public class CartItemService {
	
	private static final Logger logger = LoggerFactory.getLogger(CartItemService.class);

	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	public List<CartItem> getCartItemsByUserId(Long userId){
		logger.info(" " + cartItemRepository.findByUserId(userId));
		List<CartItem> cart = cartItemRepository.findByUserId(userId);
		return cart;
	}
	
	public String addCartItem(CartItem cartItem) {
        
		Optional<User> orderOptional = userRepository.findById(cartItem.getUser().getId());
		Optional<Product> productOptional = productRepository.findById(cartItem.getProduct().getId());
        
		if(orderOptional.isEmpty()) {
			return "User not Found";
		} 
		if(productOptional.isEmpty()) {
			return "Product not Found";
		}
        if(cartItemRepository.findById(cartItem.getId()).isEmpty()){
        	if(cartItem.getQuantity()<=0) {
        		cartItem.setQuantity(1);
        	}
        	Product product = productRepository.findById(cartItem.getProduct().getId()).orElseThrow(()-> new ProductNotFoundException("Product not found"));
        	if((product.getStockQuantity() < cartItem.getQuantity())) {
        		if(product.getStockQuantity() == 0) {
        			return "Sorry out of stock";
        		}
        		return "Only " + product.getStockQuantity() + " left";
        	}
        	cartItemRepository.save(cartItem);
        	product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
        	productRepository.save(product);
        	
        	return "Added to cart item";
        } else{
        	return "Already in available in the cart. You can update if you want";
        }
	}
	
	
	
    public Optional<CartResponseDTO> updateOrderItem(Long id, CartItem updateCartItem) {
    	Optional<CartItem> cartItemOptional = cartItemRepository.findById(id);
    	
    	//check stored user id and sent user id matches
    	if(updateCartItem.getUser().getId()!=cartItemOptional.get().getUser().getId()) {
    		throw new CartItemNotFoundException("Unable to access user not matches");
    	} else {

	    	if(cartItemOptional.isPresent()) {
	    		CartItem cartItem = cartItemOptional.get();
	    		
    			if (updateCartItem.getQuantity() > cartItem.getProduct().getStockQuantity()) {
    				throw new CartItemNotFoundException("Out of Stock"); 
    			} else {
    				// Update the cart item quantity and reduce the stock in the database

    				cartItem.setQuantity(updateCartItem.getQuantity());
    				cartItemRepository.save(cartItem);

    				// Reduce the stock quantity in the product
    				Product product = cartItem.getProduct();
    				product.setStockQuantity(product.getStockQuantity() - updateCartItem.getQuantity());
    				productRepository.save(product);

		            User user = cartItemOptional.isEmpty() ? null : cartItemOptional.get().getUser(); // assuming all cart items have the same user
		    		UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail());
	
		            List<CartItemResponseDTO> cartItemDTOs = cartItemOptional.stream().map(item -> {
		                CartItemResponseDTO dto = new CartItemResponseDTO();
		                dto.setId(item.getId());
		                dto.setProduct(item.getProduct());
		                dto.setQuantity(item.getQuantity());
		                return dto;
		            }).collect(Collectors.toList());
	
		            CartResponseDTO responseDTO = new CartResponseDTO();
		            responseDTO.setUser(userDTO);
		            responseDTO.setCartItems(cartItemDTOs);
	
		            return Optional.of(responseDTO);
		    	}
    		} else {
	    		return Optional.empty();
	    	}  	
    	}    	
    }
    
    
    public CartResponseDTO getCartItemByUserId(Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        User user = cartItems.isEmpty() ? null : cartItems.get(0).getUser(); // assuming all cart items have the same user
		UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail());

        List<CartItemResponseDTO> cartItemDTOs = cartItems.stream().map(cartItem -> {
            CartItemResponseDTO dto = new CartItemResponseDTO();
            dto.setId(cartItem.getId());
            dto.setProduct(cartItem.getProduct());
            dto.setQuantity(cartItem.getQuantity());
            return dto;
        }).collect(Collectors.toList());

        CartResponseDTO responseDTO = new CartResponseDTO();
        responseDTO.setUser(userDTO);
        responseDTO.setCartItems(cartItemDTOs);

        return responseDTO;
    }
    
	public UserOrderResponse getUserOrders(Long userId) {
		
		Optional<User> user  = userRepository.findById(userId);
		
		if(user.isEmpty()) {
			throw new CartItemNotFoundException("User ID : " + userId + " not found");
		} else {

			List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
		
			UserDTO userDTO = new UserDTO(user.get().getId(), user.get().getUsername(), user.get().getEmail());
			List<ItemDTO> itemDTOs = cartItems.stream()
					.map(cartItem -> new ItemDTO(
							cartItem.getId(),
							new ProductDTO(cartItem.getProduct().getName(), cartItem.getProduct().getDescription(),
									cartItem.getProduct().getPrice(), cartItem.getProduct().getCategory(), cartItem.getProduct().getStockQuantity()),
									cartItem.getQuantity()
									)
							).collect(Collectors.toList());
					
			return new UserOrderResponse(userDTO, itemDTOs);
		}		
	}
	
	@ExceptionHandler(CartItemNotFoundException.class)
	public ResponseEntity<String> handleOrderNotFoundException(CartItemNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
    
    public String deleteCartItem(Long id, CartItem cartItemUser) {
    	Optional<CartItem> cartItem = cartItemRepository.findById(id);

    	if(cartItem.isEmpty()) {
    		throw new CartItemNotFoundException("Cart Item ID Not Exists");
    	}
    	else {    	
    		if(cartItem.get().getUser().getId() != cartItemUser.getUser().getId()) {
    			
    			throw new CartItemNotFoundException("Unable to access user not matches");
    		}    		
    		cartItemRepository.deleteById(cartItem.get().getId());
    		return "Deleted Cart Item Successfully ID: " + id;
    	}
    }
    
    
}
