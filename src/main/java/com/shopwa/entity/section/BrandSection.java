package com.shopwa.entity.section;

import com.shopwa.entity.Brand;
import com.shopwa.entity.IdBasedEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "sections_brands")
public class BrandSection extends IdBasedEntity {

	@Column(name = "brand_order")
	private int brandOrder;

	@ManyToOne
	@JoinColumn(name = "brand_id")
	private Brand brand;

}
