package com.shopwa.entity.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "product_details")
public class ProductDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, length = 255)
	private String name;
	
	@Column(nullable = false, length = 255)
	private String value;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	public ProductDetail() {
	}
	
	public ProductDetail(Integer id, String name, String value, Product product) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
		this.product = product;
	}



	public ProductDetail(String name, String value, Product product) {
		this.name = name;
		this.value = value;
		this.product = product;
	}
}
