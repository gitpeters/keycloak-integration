package com.peters.Keycloak.repositories;

import com.peters.Keycloak.entities.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItems, String> {
    List<OrderItems> findByOrderId(String id);
}
