package com.peters.Keycloak.controllers;

import com.peters.Keycloak.dto.KeycloakUser;
import com.peters.Keycloak.service.UserResourceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/keycloak/users")
@SecurityRequirement(name = "keycloak")
@RequiredArgsConstructor
public class UserController {
    private final UserResourceService resourceService;

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public List<KeycloakUser> getAllUsers() {
        return resourceService.getUsers();
    }

    @PutMapping("/{userId}/update")
    @PreAuthorize("hasRole('admin')")
    public Response updateUser(@RequestBody KeycloakUser user, @PathVariable String userId) {
        return Response.ok(resourceService.updateUser(user, userId)).build();
    }

    @DeleteMapping("/{userId}/delete")
    @PreAuthorize("hasRole('admin')")
    public Response deleteUser(@PathVariable String userId) {
        return Response.ok(resourceService.deleteUser(userId)).build();
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('admin')")
    public Response createUser(@RequestBody KeycloakUser user) {
        return Response.ok(resourceService.createUser(user)).build();
    }
}
