package com.ecommerce.demo.dto;

import com.ecommerce.demo.model.Product;

public class CartItemResponseDTO {
    
	private Long id;
    
    private Product product;
    
    private int quantity;
    
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public CartItemResponseDTO(Long id, Product product, int quantity) {
		super();
		this.id = id;
		this.product = product;
		this.quantity = quantity;
	}
	public CartItemResponseDTO() {
		super();
	}

    
    
}
