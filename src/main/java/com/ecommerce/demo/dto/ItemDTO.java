package com.ecommerce.demo.dto;


public class ItemDTO{
	
	
	private Long id;
	
	private ProductDTO product;
	
	private UserDTO user;
	
	private int quantity;
	
    public double price;
	
	
	public ItemDTO(Long id, ProductDTO product, UserDTO user, int quantity, double price) {
		super();
		this.id = id;
		this.product = product;
		this.user = user;
		this.quantity = quantity;
		this.price = price;
	}
	
	
	
//	public UserDTO getOrder() {
//		return user;
//	}
//	public void setOrder(UserDTO user) {
//		this.user = user;
//	}

    
    public double getSubtotal() {
    	return calculateSubtotal();
    }
    public double calculateSubtotal() {
    	return quantity * price;
    }

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProductDTO getProduct() {
		return product;
	}

	public void setProduct(ProductDTO product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public ItemDTO(Long id, ProductDTO product, int quantity) {
		super();
		this.id = id;
		this.product = product;
		this.quantity = quantity;
	}

	public ItemDTO(Long id, ProductDTO product, int quantity, double price) {
		super();
		this.id = id;
		this.product = product;
		this.quantity = quantity;
		this.price = price;
	}

	public ItemDTO() {
		super();
	}
    

	
	
	
}