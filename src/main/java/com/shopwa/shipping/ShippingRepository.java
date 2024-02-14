package com.shopwa.shipping;

import com.shopwa.entity.Country;
import com.shopwa.entity.ShippingRate;
import org.springframework.data.repository.CrudRepository;

public interface ShippingRepository extends CrudRepository<ShippingRate, Integer> {

    ShippingRate findByCountryAndState(Country country, String state);
}
