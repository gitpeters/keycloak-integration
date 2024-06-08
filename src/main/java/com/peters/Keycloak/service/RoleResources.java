package com.peters.Keycloak.service;

import com.peters.Keycloak.dto.ClientResource;
import com.peters.Keycloak.dto.KeycloakUser;
import com.peters.Keycloak.dto.UserRole;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RoleResources {
    List<UserRole> getRoles();

    UserRole getRoleByName(String roleName);

    UserRole createRole(UserRole userRole);

    UserRole updateRole(String roleName, UserRole userRole);

    void deleteRole(String roleName);
    List<ClientResource> getClientResources();

    List<UserRole> getUserRoles(String id);

    void addRoleToUser(String id, String roleName);
}
