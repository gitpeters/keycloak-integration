package com.peters.Keycloak.service;

import com.peters.Keycloak.dto.ClientResource;
import com.peters.Keycloak.dto.KeycloakUser;
import com.peters.Keycloak.dto.UserRole;
import com.peters.Keycloak.util.KeycloakSecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.ResourceResource;
import org.keycloak.admin.client.resource.ResourcesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleResourceImpl implements RoleResources {
    private final KeycloakSecurityUtil keycloakSecurityUtil;
    @Value("${keycloak-config.realm}")
    private String realm;
    @Value("${keycloak-config.client-id}")
    private String clientId;
    @Override
    public List<UserRole> getRoles() {
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        List<RoleRepresentation> roleRep = keycloak.realm(realm).roles().list();
        if(!roleRep.isEmpty()) {
            return mapRoleToRoleReps(roleRep);
        }
        return List.of();
    }

    @Override
    public UserRole getRoleByName(String roleName) {
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        RoleRepresentation roleRep = keycloak.realm(realm).roles().get(roleName).toRepresentation();
        if(roleRep != null) {
            return buildRole(roleRep);
        }
        return null;
    }

    @Override
    public UserRole createRole(UserRole userRole) {
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        RoleRepresentation roleRep = buildRoleRep(userRole);
        keycloak.realm(realm).roles().create(roleRep);
        return userRole;
    }



    @Override
    public UserRole updateRole(String roleName, UserRole userRole) {
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        RoleRepresentation roleRep = buildRoleRep(userRole);
        keycloak.realm(realm).roles().get(roleName).update(roleRep);
        return userRole;
    }

    @Override
    public void deleteRole(String roleName) {
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        keycloak.realm(realm).roles().deleteRole(roleName);
    }

    private RoleRepresentation buildRoleRep(UserRole userRole) {
        RoleRepresentation roleRep = new RoleRepresentation();
        roleRep.setName(userRole.getRoleName());
        return roleRep;
    }

    public List<UserRole> mapRoleToRoleReps(List<RoleRepresentation> roleRep) {
        List<UserRole> roles = new ArrayList<>();
        for(RoleRepresentation role : roleRep) {
            roles.add(buildRole(role));
        }
        return roles;
    }

    private UserRole buildRole(RoleRepresentation role) {
        UserRole userRole = new UserRole();
        userRole.setId(role.getId());
        userRole.setRoleName(role.getName());
        return userRole;
    }
@Override
    public List<ClientResource> getClientResources(){
        try{
            Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
            List<ResourceRepresentation> resourceReps = keycloak.realm(realm).clients().get(clientId).authorization().resources().resources();
            return mapClientResourcesToResourceReps(resourceReps);
        }catch (Exception e){
            log.error("No resource found: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public List<UserRole> getUserRoles(String id) {
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        return mapRoleToRoleReps(keycloak.realm(realm).users().get(id).roles().realmLevel().listAll());
    }

    @Override
    public void addRoleToUser(String id, String roleName) {
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        RoleRepresentation role = keycloak.realm(realm).roles().get(roleName).toRepresentation();
        if(role != null) {
            keycloak.realm(realm).users().get(id).roles().realmLevel().add(List.of(role));
        }
    }

    private List<ClientResource> mapClientResourcesToResourceReps(List<ResourceRepresentation> resourceReps) {
        List<ClientResource> clientResources = new ArrayList<>();
        for(ResourceRepresentation rp : resourceReps){
            clientResources.add(buildClientResource(rp));
        }
        return clientResources;
    }

    private ClientResource buildClientResource(ResourceRepresentation rp) {
        return new ClientResource(rp.getId(), rp.getName());
    }

}
