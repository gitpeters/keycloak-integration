package com.peters.Keycloak.service;

import com.peters.Keycloak.dto.KeycloakUser;

import java.util.List;

public interface UserResourceService {
    List<KeycloakUser> getUsers();

    KeycloakUser createUser(KeycloakUser user);

    KeycloakUser updateUser(KeycloakUser user, String userId);

    String deleteUser(String userId);
}
