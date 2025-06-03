package com.ecommerce.demo.controller;

import com.ecommerce.demo.dto.ItemDTO;
import com.ecommerce.demo.dto.ProductDTO;
import com.ecommerce.demo.dto.ReviewDTO;
import com.ecommerce.demo.dto.UserOrderResponse;
import com.ecommerce.demo.exception.OrderItemNotFoundException;
import com.ecommerce.demo.exception.OrderNotFoundException;
import com.ecommerce.demo.exception.ProductNotFoundException;
import com.ecommerce.demo.exception.ReviewNotFoundException;
import com.ecommerce.demo.model.Order;
import com.ecommerce.demo.model.OrderItem;
import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.model.Review;
import com.ecommerce.demo.service.OrderItemService;
import com.ecommerce.demo.service.OrderService;
import com.ecommerce.demo.service.ProductService;
import com.ecommerce.demo.service.ReviewService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api/admin")
public class AdminBasedController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderService orderService;   
    
    
	private static final Logger logger = LoggerFactory.getLogger(AdminBasedController.class);

    
    // #################################//
	// ********* PRODUCTS ENTITY *******//
    //----------------------------------//

	
    // ********* GET ALL PRODUCTS ******//
    @GetMapping("/getAllProducts")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> list =  productService.getAllProducts();
        if(list.isEmpty()) {
        	throw new ProductNotFoundException("Product list is empty");
        } else {
        	return new ResponseEntity<List<Product>>(list, HttpStatus.OK);
        }
        
    }

    // *********** GET PRODUCT BY ID *********//
    @GetMapping("/getProductById/{id}")
    public ResponseEntity<Optional<ProductDTO>> getProductById(@PathVariable Long id) {
        Optional<ProductDTO> data = productService.getProductById(id);
    	return new ResponseEntity<Optional<ProductDTO>>(data, HttpStatus.OK);
    }
    
    
    // *********** CREATE PRODUCT *********//
    @PostMapping("/createProduct")
    public String createProductPost(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    
    // *********** UPDATE PRODUCT BY ID *********//
    @PatchMapping("/updateProduct/{id}")
    public ResponseEntity<Optional<Product>> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        Optional<Product> data = productService.updateProduct(id, productDetails);
        if(data.isPresent()) {
        	return new ResponseEntity<Optional<Product>>(data, HttpStatus.OK);
        } else {    
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
    }
    
    
    // *********** DELETE PRODUCT BY ID *********//
    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
    	String deleted = productService.deleteProduct(id);
    	if(deleted != "Product Not Exists") {
    		return new ResponseEntity<String>("Order id with " + id + " deleted succesfully" ,HttpStatus.OK);
    	} else {
    		throw new OrderNotFoundException("Order not found with id: " + id);
    	}
    }
    
    // *********** PRODUCT EXCEPTION HANDLER*********//
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
     
    
    // ###############################//
	// ********* ORDER ENTITY ********//
    //--------------------------------//

        
    // *********** GET ALL ORDER for User *********//
    @GetMapping("/getAllOrdersByUserId/{id}")
    public ResponseEntity<List<UserOrderResponse>> getAllOrders(@PathVariable Long id) {
        List<UserOrderResponse> ordersList = orderService.getAllOrders(id);
        if(ordersList.isEmpty()) {
        	throw new OrderNotFoundException("No orders placed yet");
        } else {
        	return new ResponseEntity<List<UserOrderResponse>>(ordersList, HttpStatus.OK);        }
        
    }


    // *********** GET ORDER BY ID *********//
    @GetMapping("/getOrderById/{id}")
    public ResponseEntity<Optional<UserOrderResponse>> getOrderById(@PathVariable Long id) {
    	
    	Optional<UserOrderResponse> data = orderService.getOrderById(id);
    	if(data.isPresent()) {
    		return new ResponseEntity<Optional<UserOrderResponse>>(data, HttpStatus.OK);
    	} else {
    		throw new OrderNotFoundException("Order Not Found with id : " + id);
    	}
    }

    

    // *********** UPDATE ORDER-status BY ID *********//
    @PutMapping("/updateOrderStatus/{id}")
    public ResponseEntity<Optional<UserOrderResponse>> updateOrderStatus(@PathVariable Long id, @RequestBody Order updateOrder) {	
    	Optional<UserOrderResponse> order = orderService.updateOrderStatus(id, updateOrder);

    	if(order.isPresent()) {
    		return new ResponseEntity<Optional<UserOrderResponse>>(order, HttpStatus.OK);
    	} else {
    		throw new OrderNotFoundException("Order with ID : " + id + " not found");
    	}
    
    }

    
    // *********** DELETE ORDER BY ID *********//
    @DeleteMapping("/deleteOrder/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
    	
    	String deleted =  orderService.deleteOrder(id);
    	if(deleted != "Order ID Not Exists") {
    		return new ResponseEntity<String>("Order id with " + id + " deleted succesfully" ,HttpStatus.OK);
    	} else {
    		throw new OrderNotFoundException("Order not found with id: " + id);
    	}
    }
    
    // *********** ORDER EXCEPTION HANDLER*********//
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFoundException(OrderNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

  	
       
    // ###################################//
	// ********* ORDER_ITEM ENTITY this created when Order is created so no need to create *******//
    //------------------------------------//
   
    // *********** GET ALL ORDER_ITEM  *********//
    @GetMapping("/getAllOrderItems")
    public ResponseEntity<List<ItemDTO>> getAllOrderItems() {

    	List<ItemDTO> orderItems = orderItemService.getAllOrderItems();    	
    	if(orderItems.isEmpty()) {
    		throw new OrderItemNotFoundException("Order Items empty");
    	} else {
    		return new ResponseEntity<List<ItemDTO>>(orderItems, HttpStatus.OK);
    	}
    
    }
    
    // *********** GET ALL ORDER_ITEM *********//
    @GetMapping("/getAllOrderItemsByUserId/{id}")
    public ResponseEntity<List<ItemDTO>> getAllOrderItemsByUserId(@PathVariable Long id) {

    	List<ItemDTO> orderItems = orderItemService.getOrderItemByUserId(id);    	
    	if(orderItems.isEmpty()) {
    		throw new OrderItemNotFoundException("Order Items empty");
    	} else {
    		return new ResponseEntity<List<ItemDTO>>(orderItems, HttpStatus.OK);
    	}
    
    }

    
    // *********** GET ORDER_ITEM BY ID *********//
    @GetMapping("/getOrderItemById/{id}")
    public ResponseEntity<List<ItemDTO>> getOrderItemById(@PathVariable Long id) {      
    	List<ItemDTO> orderItemList =  orderItemService.getOrderItemById(id);
        if(orderItemList.isEmpty()) {
        	throw new OrderItemNotFoundException("Order Item with id : " + id + " not found");
        } else {
        	return new ResponseEntity<List<ItemDTO>>(orderItemList, HttpStatus.OK);
        }        
    } 
    
    @ExceptionHandler(OrderItemNotFoundException.class)
    public ResponseEntity<String> handleOrderItemNotFoundException(OrderItemNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    
    
    
    // ###############################//
	// ********* REVIEW ENTITY *******//
    //--------------------------------//

    // *********** DELETE REVIEW BY ID *********//
    @DeleteMapping("/deleteReview/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id) {
    	String deleted = reviewService.deleteReview(id);
    	if(deleted != "Review ID Not Exists") {
    		return new ResponseEntity<String>("Review ID : " + id + " deleted", HttpStatus.OK);
    	} else {
    		throw new ReviewNotFoundException("Review ID : " + id + " not found");
    	}
    }


    // *********** GET REVIEW BY ID NEW CODE *********//
    @GetMapping("/getReviewById/{id}")
    public ResponseEntity<Optional<ReviewDTO>> getReviewById(@PathVariable Long id) {
        Optional<ReviewDTO> data = reviewService.getReviewById(id);
    	if(data.isPresent()) {
    		return new ResponseEntity<Optional<ReviewDTO>>(data, HttpStatus.OK);
    	} else {
    		throw new ReviewNotFoundException("Review ID : " + id + " not found");
    	}
    }

    
    // *********** GET ALL REVIEW *********//
    @GetMapping("/getAllReviews")
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
    	List<ReviewDTO> getAllReviews = reviewService.getAllReviews();
        if(getAllReviews.isEmpty()) {
     	   throw new ReviewNotFoundException("Reviews empty");
        } else {
     	   return new ResponseEntity<List<ReviewDTO>>(getAllReviews, HttpStatus.OK);
        }
    }
    
    // *********** REVIEW EXCEPTION HANDLER*********//
    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<String> handleReviewNotFoundException(ReviewNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    
   
}
