package com.shopwa.entity.section;

import com.shopwa.entity.IdBasedEntity;
import com.shopwa.entity.product.Product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "sections_products")
public class ProductSection extends IdBasedEntity {

	@Column(name = "product_order")
	private int productOrder;
	
	@ManyToOne
	@JoinColumn(name = "product_id")	
	private Product product;
}
