package com.ecommerce.demo.service;

import com.ecommerce.demo.dto.ProductDTO;
import com.ecommerce.demo.dto.ProductReviewDTO;
import com.ecommerce.demo.dto.ReviewDTO;
import com.ecommerce.demo.dto.UserDTO;
import com.ecommerce.demo.exception.ReviewNotFoundException;
import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.model.Review;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.repository.ProductRepository;
import com.ecommerce.demo.repository.ReviewRepository;
import com.ecommerce.demo.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    private final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    
    public List<ReviewDTO> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                      .map(this::convertToReviewDTO)
                      .collect(Collectors.toList());
    }


    public List<ReviewDTO> getMyReviews(Long id) {
        List<Review> myReviews = reviewRepository.findByUserId(id);
        return myReviews.stream().map(
        		review -> {
        			User user = review.getUser();
        			UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail());
        			
        			Product product = review.getProduct();
        			ProductReviewDTO productDTO = new ProductReviewDTO(product.getName(), product.getDescription());       		
        			return new ReviewDTO(review.getId(), userDTO, productDTO, review.getRating(), review.getComment(), review.getReviewDate());
        		}
        		).collect(Collectors.toList());
        
        
    }

    public Optional<ReviewDTO> getReviewById(Long id) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        return reviewOptional.map(this::convertToReviewDTO);
    }

    private ReviewDTO convertToReviewDTO(Review review) {
        UserDTO userDTO = new UserDTO(
            review.getUser().getId(),
            review.getUser().getUsername(),
            review.getUser().getEmail()
        );

        ProductReviewDTO productDTO = new ProductReviewDTO(
            review.getProduct().getName(),
            review.getProduct().getDescription()
        );

        return new ReviewDTO(
            review.getId(),
            userDTO,
            productDTO,
            review.getRating(),
            review.getComment(),
            review.getReviewDate()
        );
    }


    
    public ReviewDTO createReview(Review review) {
    	Optional<Review> createReview = reviewRepository.findById(review.getId());
    	if(createReview.isEmpty()) {
    		reviewRepository.save(review);
    		Optional<User> userDetails = userRepository.findById(review.getUser().getId());
    		Optional<Product> productDetails = productRepository.findById(review.getProduct().getId());
    		
    		ReviewDTO reviewDTO = new ReviewDTO(review.getId(), 
    				new UserDTO(userDetails.get().getId(), userDetails.get().getUsername(), userDetails.get().getEmail()),
    				new ProductReviewDTO(productDetails.get().getName(),productDetails.get().getDescription()),
    				review.getRating(),
    				review.getComment(),
    				review.getReviewDate()
    				);
    		return reviewDTO;
    		
    	} else {
    		throw new ReviewNotFoundException("Review exists you can update");
    	}
    }
    
    public String deleteReview(Long id) {
    	Optional<Review> order = reviewRepository.findById(id);
        if(order.isPresent()) {
    		reviewRepository.deleteById(order.get().getId());
    		return "Deleted Review Successfully ID: " + id;
    	} else {
    		return "Review ID Not Exists";
    	}     
    }
  
    
    public String deleteReview(Long id, Review reviewUser) {
    	Optional<Review> review = reviewRepository.findById(id);
    	
    	if(review.isEmpty()) {
    		return "Review ID Not Exists";
    	} else {
    		if(review.get().getUser().getId() != reviewUser.getUser().getId()) {
    			throw new ReviewNotFoundException("User ID mismatch");
    		}    	
    		reviewRepository.deleteById(review.get().getId());
    		return "Deleted Review Successfully ID: " + id;    	
    	}
    }
    
    public Optional<ReviewDTO> updateReview(Long id, String status, Long userid, int rating) {      
    	
    	Optional<Review> reviewOptional = reviewRepository.findById(id);
    	Optional<User> reviewUser = userRepository.findById(reviewOptional.get().getUser().getId());
    	
    	if(reviewUser.isEmpty()) {
    		throw new UsernameNotFoundException("User not reviewed this product yet");
    	} else if(reviewOptional.isEmpty()) {
    		throw new ReviewNotFoundException("Review not found");
    	} else {
    		if(reviewUser.get().getId() != userid) {
    			throw new ReviewNotFoundException("User ID mismatch");
    		}
    		Review review = reviewOptional.get();
    		review.setComment(status);
    		reviewRepository.save(review);
    		return reviewOptional.map(
    				rev -> {
    					User user = rev.getUser();
            			UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail());
            			
            			Product product = rev.getProduct();
            			ProductReviewDTO productDTO = new ProductReviewDTO(product.getName(), product.getDescription());       		
            			return new ReviewDTO(rev.getId(), userDTO, productDTO, rating, rev.getComment(), rev.getReviewDate());
    				});
    		
    	} 
    }
    
}
