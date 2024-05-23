package com.peters.Keycloak.repositories;

import com.peters.Keycloak.entities.Menu;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MenuRepository extends CrudRepository<Menu, String> {
    Menu findByRestaurantId(String restaurantId);
    List<Menu> findAllByRestaurantId(String restaurantId);
}
