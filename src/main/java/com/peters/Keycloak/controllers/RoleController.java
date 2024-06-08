package com.peters.Keycloak.controllers;

import com.peters.Keycloak.dto.ClientResource;
import com.peters.Keycloak.dto.UserRole;
import com.peters.Keycloak.service.RoleResources;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/roles")
@SecurityRequirement(name = "keycloak")
@RequiredArgsConstructor
public class RoleController {
    private final RoleResources roleResources;

    @GetMapping
    @PreAuthorize("hasAnyRole('admin', 'manager', 'owner')")
    public List<UserRole> getRoles() {
        return roleResources.getRoles();
    }

    @GetMapping("/{roleName}")
    @PreAuthorize("hasAnyRole('admin', 'manager', 'owner')")
    public UserRole getRole(@PathVariable("roleName") String roleName) {
        return roleResources.getRoleByName(roleName);
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public Response createRole(@RequestBody UserRole userRole) {
        return Response.ok(roleResources.createRole(userRole)).build();
    }

    @PutMapping("/{roleName}")
    @PreAuthorize("hasRole('admin')")
    public Response updateRole(@PathVariable("roleName") String roleName, @RequestBody UserRole userRole) {
        return Response.ok(roleResources.updateRole(roleName, userRole)).build();
    }

    @DeleteMapping("/{roleName}")
    @PreAuthorize("hasRole('admin')")
    public Response deleteRole(@PathVariable("roleName") String roleName) {
        roleResources.deleteRole(roleName);
        return Response.ok().build();
    }

    @GetMapping("/resources")
    @PreAuthorize("hasRole('admin')")
    public List<ClientResource> getRoleResources() {
        return roleResources.getClientResources();
    }

}
