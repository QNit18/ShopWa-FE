package com.shopwa.shipping;

import com.shopwa.entity.Country;
import com.shopwa.entity.ShippingRate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShippingRateRepositoryTest {

    @Autowired private ShippingRepository repo;

    @Test
    public void testFindByCountryAndState(){
        Country country = new Country(234);
        String state = "New York";
        ShippingRate shippingRate = repo.findByCountryAndState(country, state);

        assertThat(shippingRate).isNotNull();
        System.out.println(shippingRate);
    }
}
