package com.shopwa.setting;

import com.shopwa.entity.Country;
import com.shopwa.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StateRepository extends JpaRepository<State, Integer> {
    List<State> findByCountryOrderByNameAsc(Country country);
}
