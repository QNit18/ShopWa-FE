package com.shopwa.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "addresses")
public class Address extends AbstractAddress{
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "default_address")
    private boolean defaultForShipping;

    @Override
    public String toString() {
        String address = firstName;
        if (lastName != null && !lastName.isEmpty()) {
            address += " " + lastName;
        }
        if (!addressLine1.isEmpty()) {
            address += ", " + addressLine1;
        }
        if (addressLine2 != null && !addressLine2.isEmpty()) {
            address += " " + addressLine2;
        }
        if (!city.isEmpty()) {
            address += ", " + city;
        }
        if (state != null && !state.isEmpty()) {
            address += ", " + state + ", ";
        }
        address += country.getName();
        if (!postalCode.isEmpty()) {
            address += ".Postal Code: " + postalCode;
        }
        if (!phoneNumber.isEmpty()) {
            address += ". Phone Number: " + phoneNumber;
        }
        return address;
    }
}
