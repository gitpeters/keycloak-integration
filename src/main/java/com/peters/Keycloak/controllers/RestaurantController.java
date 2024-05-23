package com.peters.Keycloak.controllers;

import com.peters.Keycloak.entities.Menu;
import com.peters.Keycloak.entities.MenuItem;
import com.peters.Keycloak.entities.Restaurant;
import com.peters.Keycloak.repositories.MenuItemRepository;
import com.peters.Keycloak.repositories.MenuRepository;
import com.peters.Keycloak.repositories.RestaurantRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
@SecurityRequirement(name = "keycloak")
public class RestaurantController {
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final MenuItemRepository menuItemRepository;

    //Public API
    @GetMapping("/public/list")
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }
    @GetMapping("/public/menu/{restaurantId}")
    public List<Menu> getMenu(@PathVariable String restaurantId) {
        List<Menu> menus = menuRepository.findAllByRestaurantId(restaurantId);
        menus.forEach(menu -> {
            menu.setMenuItems(menuItemRepository.findAllByMenuId(menu.getId()));
        });
        return menus;
    }

    // manager can access
    @PostMapping("/menu")
    @PreAuthorize("hasRole('manager')")
    public Menu createMenu(@RequestBody Menu menu) {
        menuRepository.save(menu);
        menu.getMenuItems().forEach(menuItem -> {
            menuItem.setMenuId(menu.getId());
            menuItemRepository.save(menuItem);
        });
        return menu;
    }

    // owner can access
    @PutMapping("/menu/item/{itemId}/{price}")
    @PreAuthorize("hasRole('owner')")
    public MenuItem updateMenuItem(@PathVariable String itemId, @PathVariable double price) {
        Optional<MenuItem> menuItem = menuItemRepository.findById(itemId);
        if(menuItem.isPresent()) {
            MenuItem menuItemItem = menuItem.get();
            menuItemItem.setPrice(price);
            menuItemRepository.save(menuItemItem);
            return menuItemItem;
        }
        return null;
    }

    // admin can access this
    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public Restaurant createRestaurant(@RequestBody Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }
}
