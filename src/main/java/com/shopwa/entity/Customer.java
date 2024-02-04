package com.shopwa.entity;

import java.util.Date;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "customers")
public class Customer extends AbstractAddress{
    @Column(nullable = false, unique = true, length = 45)
    private String email;

    @Column(nullable = false, length = 64)
    private String password;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    private boolean enabled;

    @Column(name = "created_time")
    private Date createdTime;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Enumerated(EnumType.STRING)
    @Column(name = "authentication_type", length = 10)
    private AuthenticationType authenticationType;

    @Column(name = "reset_password_token", length = 30)
    private String resetPasswordToken;

    public Customer() {
    }

    public Customer(Integer id) {
        this.id = id;
    }

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

    public String getFullName(){
        return firstName + " " + lastName;
    }

    @Transient
    public String getAddress() {
        String address = firstName;

        if(lastName != null && !lastName.isEmpty()){
            address += " " + lastName;
        }

        if (!addressLine1.isEmpty()){
            address += ", " + addressLine1;
        }

        if (addressLine2 != null && !addressLine2.isEmpty()){
            address += " " + addressLine2;
        }

        if (!city.isEmpty()){
            address += ", " + city;
        }

        if (state != null && !state.isEmpty()){
            address += ", " + state + ", ";
        }

        address += country.getName();

        if (!postalCode.isEmpty()){
            address += ".Postal Code: " + postalCode;
        }
        if (!phoneNumber.isEmpty()){
            address += ". Phone Number: " + phoneNumber;
        }

        return address;
    }
}
