package com.ecommerce.demo.service;

import com.ecommerce.demo.dto.ItemDTO;
import com.ecommerce.demo.dto.ProductDTO;
import com.ecommerce.demo.dto.UserDTO;
import com.ecommerce.demo.dto.UserOrderResponse;
import com.ecommerce.demo.exception.OrderItemNotFoundException;
import com.ecommerce.demo.exception.OrderNotFoundException;
import com.ecommerce.demo.model.OrderItem;
import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.repository.OrderItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;


    public List<ItemDTO> getOrderItemById(Long id) {
        Optional<OrderItem> orderItem = orderItemRepository.findById(id);
		
    	if(orderItem.isEmpty()) {
    		throw new OrderItemNotFoundException("Order item ID : " + id + " is empty");
    	} else {
    		return orderItem.stream().map(this::convertToItemDTO).collect(Collectors.toList());
    	}
        
    }
    
    public List<ItemDTO> getAllOrderItems() {
        List<OrderItem> orderItems = orderItemRepository.findAll();
        return orderItems.stream()
                         .map(this::convertToItemDTO)
                         .collect(Collectors.toList());
    }

    private ItemDTO convertToItemDTO(OrderItem orderItem) {
        ProductDTO productDTO = new ProductDTO(
            orderItem.getProduct().getName(),
            orderItem.getProduct().getDescription(),
            orderItem.getProduct().getPrice(),
            orderItem.getProduct().getCategory(),
            orderItem.getProduct().getStockQuantity()
        );
        UserDTO userDTO = new UserDTO(
        		orderItem.getOrder().getUser().getUsername(),
        		orderItem.getOrder().getUser().getEmail()
		);
        return new ItemDTO(
            orderItem.getId(),
            productDTO,
            userDTO,
            orderItem.getQuantity(),
            orderItem.getPrice()
        );
    }

	public List<ItemDTO> getOrderItemByUserId(Long id) {
		List<OrderItem> orderItems = orderItemRepository.findAll();
		List<ItemDTO> itemDTOs = new ArrayList<>();
		for (OrderItem orderItem : orderItems) {
			if (id.equals(orderItem.getOrder().getUser().getId())) {
				itemDTOs.add(convertToItemDTO(orderItem));
			}
		}
		return itemDTOs;
	}    
    
}
