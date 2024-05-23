package com.peters.Keycloak.repositories;

import com.peters.Keycloak.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findAllByRestaurantId(String restaurantId);
}
