package com.ecommerce.demo.service;

import com.ecommerce.demo.dto.ProductDTO;
import com.ecommerce.demo.exception.ProductNotFoundException;
import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.repository.ProductRepository;
import com.ecommerce.demo.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
	
	
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    public Optional<ProductDTO> getProductById(Long id) {
    	
    	
    	Optional<Product> productList = productRepository.findById(id);
    	
    	if(productList.isEmpty()) {
            throw new ProductNotFoundException("Product not found with ID: " + id);
    	} else {
    		ProductDTO productDTO = new ProductDTO(productList.get().getName(),
    				productList.get().getDescription(), productList.get().getPrice(), productList.get().getCategory(),
    				productList.get().getStockQuantity());
    		return Optional.of(productDTO);
    	}
  }
    
        public String addProduct(Product product) {
    	Optional<Product> productExists = productRepository.findByName(product.getName());
    	if(productExists.isPresent()) {
    		if(productExists.get().getDescription().equalsIgnoreCase(product.getDescription())) {    			
    			return "Product already added";
    		} else {
    			productRepository.save(product);
    			return "Added Products";
    		}
    	} else {
    		productRepository.save(product);
    		return "Added Products";    		
    	}
    }
    

    public String deleteProduct(Long id) {    	
    	Optional<Product> product = productRepository.findById(id);
    	if(product.isPresent()) {
    		productRepository.deleteById(product.get().getId());
    		return "Deleted Successfully ID: " + id;
    	} else {
    		return "Product Not Exists";
    	}
    	
	}

    public Optional<Product> updateProduct(Long id, Product productDetails) {
        
    	Optional<Product> productOptional = productRepository.findById(id);
        
    	if(productOptional.isPresent()) {
	    
    		Product product = productOptional.get();
	        if(productDetails.getName() != null) {
	        	product.setName(productDetails.getName());
	        }
	        
	        if(productDetails.getPrice() != 0) {
	        	product.setPrice(productDetails.getPrice());
	        }
	        
	        if(productDetails.getStockQuantity() != 0) {
	        	product.setStockQuantity(productDetails.getStockQuantity());
	        }
	        
	        if(productDetails.getDescription() != null) {
	        	product.setDescription(productDetails.getDescription());
	        }
	        
	        if( productDetails.getCategory() != null) {
	        	product.setCategory(productDetails.getCategory());	
	        }
        	productRepository.save(product);
        	return Optional.of(product);
    	} else {
    		return Optional.empty();
    	}
    }
}
