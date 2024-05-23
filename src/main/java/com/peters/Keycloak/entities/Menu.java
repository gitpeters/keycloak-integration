package com.peters.Keycloak.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false, name = "ID", updatable = false)
    private String id;
    private Boolean active;
    @Transient
    private List<MenuItem> menuItems;
    private String restaurantId;
}
