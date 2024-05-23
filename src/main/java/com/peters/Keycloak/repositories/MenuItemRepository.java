package com.peters.Keycloak.repositories;

import com.peters.Keycloak.entities.MenuItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MenuItemRepository extends CrudRepository<MenuItem, String> {
    List<MenuItem> findAllByMenuId(String id);
}
