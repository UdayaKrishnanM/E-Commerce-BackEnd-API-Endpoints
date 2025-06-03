package com.ecommerce.demo.controller;

import com.ecommerce.demo.dto.CartResponseDTO;
import com.ecommerce.demo.dto.ItemDTO;
import com.ecommerce.demo.dto.ProductDTO;
import com.ecommerce.demo.dto.ReviewDTO;
import com.ecommerce.demo.dto.UserOrderResponse;
import com.ecommerce.demo.exception.CartItemNotFoundException;
import com.ecommerce.demo.exception.OrderItemNotFoundException;
import com.ecommerce.demo.exception.OrderNotFoundException;
import com.ecommerce.demo.exception.ProductNotFoundException;
import com.ecommerce.demo.exception.ReviewNotFoundException;
import com.ecommerce.demo.model.CartItem;
import com.ecommerce.demo.model.Order;
import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.model.Review;
import com.ecommerce.demo.service.CartItemService;
import com.ecommerce.demo.service.OrderItemService;
import com.ecommerce.demo.service.OrderService;
import com.ecommerce.demo.service.ProductService;
import com.ecommerce.demo.service.ReviewService;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api/user")
public class UserAccessController {
	
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private CartItemService cartItemService;
    
    @Autowired
    private ProductService productService;

    
    
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
    
    // ###################################//
	// ********* ORDER ENTITY ************//
    //------------------------------------//

    
    // *********** CREATE ORDER *********//
    @PostMapping("/createOrder")
    public ResponseEntity<Optional<UserOrderResponse>> createOrder(@RequestBody Order order) {
        
    	Optional<UserOrderResponse> orderDetails = orderService.createOrder(order);
    	if(!orderDetails.isEmpty()) {
    		return new ResponseEntity<Optional<UserOrderResponse>>(orderDetails, HttpStatus.OK);
    		
    	} else {
	  		throw new OrderNotFoundException("Order already exist with id : " + order);
    	}
    }
    
    
    // *********** DELETE ORDER *********//
    @DeleteMapping("/deleteOrder/{id}")
    public ResponseEntity<String> deletOrder(@PathVariable Long id) {
        
    	String deleted =  orderService.deleteOrder(id);
    	if(deleted != "Order ID Not Exists") {
    		return new ResponseEntity<String>("Order id with " + id + " deleted succesfully" ,HttpStatus.OK);
    	} else {
    		throw new OrderNotFoundException("Order not found with id: " + id);
    	}
    }
    
    
    // *********** GET ALL ORDER ********** //
    @GetMapping("/getAllMyOrdersUserId/{id}")
    public ResponseEntity<List<UserOrderResponse>> getAllOrders(@PathVariable Long id) {
        List<UserOrderResponse> ordersList = orderService.getAllOrders(id);
        if(ordersList.isEmpty()) {
        	throw new OrderNotFoundException("No User Found in this ID");
        } else {
        	return new ResponseEntity<List<UserOrderResponse>>(ordersList, HttpStatus.OK);        
        }
        
    }
    
    // *********** GET ORDER BY ID NEW CODE (done)*********//
	@GetMapping("/getOrderById/{id}")
	public ResponseEntity<Optional<UserOrderResponse>> getOrderById(@PathVariable Long id) {
		 	
		Optional<UserOrderResponse> data = orderService.getOrderById(id);
	  	if(data.isPresent()) {
	  		return new ResponseEntity<Optional<UserOrderResponse>>(data, HttpStatus.OK);
	  	} else {
	  		throw new OrderNotFoundException("Order Not Found with id : " + id);
	  	}
	}
	
	
    // *********** ORDER EXCEPTION HANDLER*********//
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFoundException(OrderNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
	
	
    // ###################################//
	// ********* ORDER_ITEM ENTITY this created when Order is created so no need to create*******//
    //------------------------------------//

    // *********** GET ALL ORDER_ITEM *********//
    @GetMapping("/getAllOrderItems")
    public ResponseEntity<List<ItemDTO>> getAllOrderItems() {

    	List<ItemDTO> orderItems = orderItemService.getAllOrderItems();    	
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
    
    // ###################################//
	// ********* REVIEW ENTITY ***********//
    //------------------------------------//

    // *********** CREATE REVIEW *********//
    @PostMapping("/createReview")
    public ResponseEntity<ReviewDTO> createReview(@RequestBody Review review) {
        return new ResponseEntity<ReviewDTO>(reviewService.createReview(review), HttpStatus.OK);
    }


	// *********** GET REVIEW BY ID NEW CODE *********//
	@GetMapping("/getReviewById/{id}")
	public ResponseEntity<Optional<ReviewDTO>> getReviewById(@PathVariable Long id) {
	    Optional<ReviewDTO> data = reviewService.getReviewById(id);
	  	if(data.isPresent()) {
	  		return new ResponseEntity<Optional<ReviewDTO>>(data, HttpStatus.OK);
	  	} else {
	  		throw new ReviewNotFoundException("Order Not Found with id : " + id);
	  	}
	}
    

    // *********** GET ALL REVIEW *********//
    @GetMapping("/getReviewByUserId/{id}")
    public ResponseEntity<List<ReviewDTO>> getReviewByUser(@PathVariable Long id) {
        List<ReviewDTO> getAllReviews = reviewService.getMyReviews(id);
        if(getAllReviews.isEmpty()) {
     	   throw new ReviewNotFoundException("User didnt buy any product to review");
        } else {
     	   return new ResponseEntity<List<ReviewDTO>>(getAllReviews, HttpStatus.OK);
        }
     }

    
    // *********** DELETE REVIEW BY ID *********//
    @DeleteMapping("/deleteReview/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id, @RequestBody Review review) {
    	String deleted = reviewService.deleteReview(id, review);
    	if(deleted != "Review ID Not Exists") {
    		return new ResponseEntity<String>("Review ID : " + id + " deleted", HttpStatus.OK);
    	} else {
    		throw new ReviewNotFoundException("Review ID : " + id + " not found");
    	}
    }
    
    // *********** UPDATE REVIEW BY ID *********//
    @PutMapping("/updateReview/{id}")
    public ResponseEntity<Optional<ReviewDTO>> updateReview(@PathVariable Long id, @RequestParam("comment") String comment, @RequestParam("reviewUser") Long reviewUser, @RequestParam("rating") int rating) {
    	Optional<ReviewDTO> review= reviewService.updateReview(id, comment, reviewUser, rating);
    	if(review.isPresent()) {
    		return new ResponseEntity<Optional<ReviewDTO>>(review, HttpStatus.OK);
    	} else {
    		throw new ReviewNotFoundException("Review ID : " + id + " not found");
    	}
    }

    // *********** REVIEW CODE EXCEPTION HANDLER *********//
    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<String> handleReviewNotFoundException(ReviewNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }




    // ###############################//
	// ******* CART ITEM ENTITY ******//
    //--------------------------------//
    
    
    // *********** GET CART ITEMS (Done) *********//
    @GetMapping("/{userId}/orders")
    public ResponseEntity<CartResponseDTO> getCartItemByUserId(@PathVariable Long userId) {
    	CartResponseDTO userOrderResponse = cartItemService.getCartItemByUserId(userId);
    	 return ResponseEntity.ok(userOrderResponse);        
    }


    
    // *********** CREATE CART ITEMS (Done) *********//
    @PostMapping("/addcartItems")
    public ResponseEntity<String> addCartItem(@RequestBody CartItem cartItem) {

    	String msg =  cartItemService.addCartItem(cartItem);
    	if(msg == "Added to cart item") {
    		return new ResponseEntity<String>(msg, HttpStatus.OK);
    	} else {
    		throw new CartItemNotFoundException(msg);
    	}   
    
    }
    
    
    // *********** UPDATE CART ITEMS (Done) *********//
    @PutMapping("/updatecartItemsById/{id}")
    public ResponseEntity<Optional<CartResponseDTO>> updateCartItem(@PathVariable Long id, @RequestBody CartItem cartItem) {
    	Optional<CartResponseDTO> item =  cartItemService.updateOrderItem(id, cartItem);
    	if(item.isEmpty()) {
    		throw new CartItemNotFoundException("Cart Item with id : " + id + " not found");
    	} else {
    		return new ResponseEntity<Optional<CartResponseDTO>>(item,HttpStatus.OK);
    	}
    }  

    
    // *********** DELETE CART ITEMS (Done) *********//
    @DeleteMapping("/deleteCartItemById/{id}")
    public ResponseEntity<String> deleteCartItem(@PathVariable Long id,@RequestBody CartItem cartItemUserDetails) {

    	String deleted =  cartItemService.deleteCartItem(id, cartItemUserDetails);
    	if(deleted != "Cart Item ID Not Exists") {
    		return new ResponseEntity<String>("Cart Item id with " + id + " deleted succesfully", HttpStatus.OK);
    	} else {
    		throw new CartItemNotFoundException("CartItem not found with id: " + id);
    	}
	}

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFoundException(CartItemNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    

}

