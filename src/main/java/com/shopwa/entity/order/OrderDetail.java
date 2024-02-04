package com.shopwa.entity.order;

import com.shopwa.entity.Category;
import com.shopwa.entity.product.Product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int quantity;

    private float productCost;

    private float shippingCost;

    private float unitPrice;

    private float subtotal;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderDetail() {
    }

    public OrderDetail(String categoryName, int quantity, float productCost, float shippingCost, float subtotal) {
        this.product = new Product();
        this.product.setCategory(new Category(categoryName));
        this.quantity = quantity;
        this.productCost = productCost;
        this.shippingCost = shippingCost;
        this.subtotal = subtotal;
    }
    public OrderDetail(int quantity, String productName, float productCost, float shippingCost, float subtotal) {
        this.product = new Product(productName);
        this.quantity = quantity;
        this.productCost = productCost;
        this.shippingCost = shippingCost;
        this.subtotal = subtotal;
    }
}
