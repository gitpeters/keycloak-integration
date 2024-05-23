package com.peters.Keycloak.controllers;

import com.peters.Keycloak.entities.MenuItem;
import com.peters.Keycloak.entities.Order;
import com.peters.Keycloak.entities.OrderItems;
import com.peters.Keycloak.repositories.MenuItemRepository;
import com.peters.Keycloak.repositories.OrderItemRepository;
import com.peters.Keycloak.repositories.OrderRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "keycloak")
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MenuItemRepository menuItemRepository;

    // manager can access
    @GetMapping("/{restaurantId}/list")
    @PreAuthorize("hasRole('manager')")
    public List<Order> getOrders(@PathVariable String restaurantId) {
        List<Order> orders = orderRepository.findAllByRestaurantId(restaurantId);
        orders.forEach(order -> {
            List<OrderItems> items = orderItemRepository.findByOrderId(order.getId());
            order.setOrderItems(items);
        });
        return orders;
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('manager')")
    public Order getOrder(@PathVariable String orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isPresent()) {
            Order orderItem = order.get();
            orderItem.setOrderItems(orderItemRepository.findByOrderId(orderItem.getId()));
            return orderItem;
        }
        return null;
    }

    // authenticated users can access
    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public Order createOrder(@RequestBody Order order) {
        orderRepository.save(order);
        List<OrderItems> orderItems = order.getOrderItems();
        orderItems.forEach(item->{
            item.setOrderId(order.getId());
            orderItemRepository.save(item);
        });
        return order;
    }
}
