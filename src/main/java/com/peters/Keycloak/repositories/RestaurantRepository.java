package com.peters.Keycloak.repositories;

import com.peters.Keycloak.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, String> {
}
