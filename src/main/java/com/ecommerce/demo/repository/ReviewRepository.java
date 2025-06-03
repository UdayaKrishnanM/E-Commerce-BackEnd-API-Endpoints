package com.ecommerce.demo.repository;

import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductId(Long productId);

	List<Review> findByUserId(Long id);


}
