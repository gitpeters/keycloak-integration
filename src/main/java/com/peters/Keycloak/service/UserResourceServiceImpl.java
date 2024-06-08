package com.peters.Keycloak.service;

import com.peters.Keycloak.dto.KeycloakUser;
import com.peters.Keycloak.dto.UserRole;
import com.peters.Keycloak.util.KeycloakSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserResourceServiceImpl implements UserResourceService {
    private final KeycloakSecurityUtil keycloakSecurityUtil;
    @Value("${keycloak-config.realm}")
    private String realm;
    @Override
    public List<KeycloakUser> getUsers() {
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        List<UserRepresentation> userRepresentations = keycloak.realm(realm).users().list();
        return mapUsers(userRepresentations);
    }

    @Override
    public KeycloakUser createUser(KeycloakUser user) {
        if(Objects.isNull(user)){
            throw new RuntimeException("user object is null");
        }
        UserRepresentation userRepresentation = mapUserRep(user);
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        try{
            keycloak.realm(realm).users().create(userRepresentation);
            return user;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public KeycloakUser updateUser(KeycloakUser user, String userId) {
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        UserRepresentation userRep = updateUserRep(user);
        keycloak.realm(realm).users().get(userId).update(userRep);
        return user;
    }

    @Override
    public KeycloakUser getUser(String userId) {
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        UserRepresentation userRep = keycloak.realm(realm).users().get(userId).toRepresentation();
        return mapUser(userRep);
    }


    @Override
    public String deleteUser(String userId) {
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        keycloak.realm(realm).users().delete(userId);
        return "User deleted";
    }

    private UserRepresentation updateUserRep(KeycloakUser user) {
        UserRepresentation userRep = new UserRepresentation();
        if(user.getEmail() != null){
            userRep.setEmail(user.getEmail());
        }
        if(user.getFirstName() != null){
            userRep.setFirstName(user.getFirstName());
        }
        if(user.getLastName() != null){
            userRep.setLastName(user.getLastName());
        }
        if(user.getUserName()!=null){
            userRep.setUsername(user.getUserName());
        }
        return userRep;
    }

    private void updatePassword(UserResource userResource, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        userResource.resetPassword(credential);
    }

    private List<KeycloakUser> mapUsers(List<UserRepresentation> userRepresentations) {
        List<KeycloakUser> users = new ArrayList<>();
        if (userRepresentations != null || !userRepresentations.isEmpty()) {
            for (UserRepresentation userRepresentation : userRepresentations) {
                users.add(mapUser(userRepresentation));
            }
        }
        return users;
    }

    private KeycloakUser mapUser(UserRepresentation userRepresentation) {
        KeycloakUser user = new KeycloakUser();
        user.setId(userRepresentation.getId());
        user.setEmail(userRepresentation.getEmail());
        user.setFirstName(userRepresentation.getFirstName());
        user.setLastName(userRepresentation.getLastName());
        user.setUserName(userRepresentation.getUsername());
        return user;
    }

    private UserRepresentation mapUserRep(KeycloakUser user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setUsername(user.getUserName());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);
        List<CredentialRepresentation> credentials = new ArrayList<>();
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setValue(user.getPassword());
        credentials.add(credential);
        userRepresentation.setCredentials(credentials);
        return userRepresentation;
    }
}
