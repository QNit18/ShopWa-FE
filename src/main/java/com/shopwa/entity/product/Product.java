package com.shopwa.entity.product;

import com.shopwa.Constants;
import com.shopwa.entity.Brand;
import com.shopwa.entity.Category;
import com.shopwa.entity.IdBasedEntity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "products")
public class Product extends IdBasedEntity {
	
	@Column(unique = true, length = 256, nullable = false)
	private String name;
	
	@Column(unique = true, length = 256, nullable = false)
	private String alias;
	
	@Column(length = 512, nullable = false, name = "short_description")
	private String shortDescription;
	
	@Column(length = 4096, nullable = false, name = "full_description")
	private String fullDescription;
	
	@Column(name = "created_time")
	private Date createdTime;
	
	@Column(name = "updated_time")
	private Date updatedTime;
	
	private boolean enabled;
	
	@Column(name = "in_stock")
	private boolean inStock;
	
	private float cost;
	
	private float price;
	
	@Column(name = "discount_percent")
	private float discountPercent;
	
	private float length;
	private float width;
	private float height;
	private float weight;
	
	@Column(name = "main_image", nullable = false)
	private String mainImage;
		
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne
	@JoinColumn(name = "brand_id")	
	private Brand brand;

	// CascadeType là thay đổi tới (Product) sẽ thay dổi cả (ProductImage)
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ProductImage> images = new HashSet<>();
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductDetail> details = new ArrayList<>();

	private int reviewCount;
	private float averageRating;

	@Transient private boolean customerCanReview;
	@Transient private boolean reviewedByCustomer;

	public Product() {
	}

	public Product(String name) {
		this.name = name;
	}

	public Product(Integer id) {
		this.id = id;
	}

	@Transient
	public String getFormattedAverageRating() {
		// assuming averageRating is a float
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		return decimalFormat.format(averageRating);
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + "]";
	}
	
	public void addExtraImage(String imageName) {
		this.images.add(new ProductImage(imageName, this));
	}

	@Transient
	public String getMainImagePath() {
		if (id == null || mainImage == null) return "/images/image-thumbnail.png";
		
		return Constants.S3_BASE_URI + "/product-images/" + this.id + "/" + this.mainImage;
	}
	
	public void addDetail(String name, String value) {
		this.details.add(new ProductDetail(name, value, this));
	}

	public void addDetail(Integer id, String name, String value) {
		this.details.add(new ProductDetail(id, name, value, this));
	}
	
	public boolean containsImageName(String imageName) {
		Iterator<ProductImage> iterator = images.iterator();
		
		while (iterator.hasNext()) {
			ProductImage image = iterator.next();
			if (image.getName().equals(imageName)) {
				return true;
			}
		}
		
		return false;
	}

	@Transient
	public String getShortName() {
		if (name.length() > 60) {
			return name.substring(0,60).concat("...");
		}
		return name;
	}

	@Transient
	public float getDiscountPrice() {
		if (discountPercent > 0){
			return this.price * ((100 - discountPercent)/100);
		}
		return this.price;
	}

	@Transient
	public String getURI() {
		return "/p/" + this.alias;
	}
}
