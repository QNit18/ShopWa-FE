package com.shopwa.entity;

import com.shopwa.entity.product.Product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cart_items")
public class CartItem extends IdBasedEntity{
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    @Transient
    private float shippingCost;

    public CartItem() {
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", customer=" + customer +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }

    @Transient
    public float getSubtotal(){
        return product.getDiscountPrice()*quantity;
    }

    @Transient
    public float getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(float shippingCost) {
        this.shippingCost = shippingCost;
    }
}
