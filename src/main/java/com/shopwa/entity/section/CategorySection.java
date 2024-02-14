package com.shopwa.entity.section;

import com.shopwa.entity.Category;
import com.shopwa.entity.IdBasedEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "sections_categories")
public class CategorySection extends IdBasedEntity {
	
	@Column(name = "category_order")
	private int categoryOrder;
	
	@ManyToOne
	@JoinColumn(name = "category_id")	
	private Category category;
	
}
