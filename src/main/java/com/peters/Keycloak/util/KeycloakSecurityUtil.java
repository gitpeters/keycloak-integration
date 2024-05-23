package com.peters.Keycloak.util;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
/*
******************************************************************
configure keycloak -> this will enable springboot to make REST call
to Keycloak server
 */
@Component
public class KeycloakSecurityUtil {
    Keycloak keycloak;

    @Value("${keycloak-config.name}")
    private String keycloakUserName;
    @Value("${keycloak-config.realm}")
    private String keycloakRealm;
    @Value("${keycloak-config.client-id}")
    private String keycloakClientId;
    @Value("${keycloak-config.grant-type}")
    private String keycloakGrantType;
    @Value("${keycloak-config.password}")
    private String keycloakPassword;
    @Value("${keycloak-config.server-url}")
    private String keycloakServerUrl;

    public Keycloak getKeycloakInstance() {
        if (keycloak == null) {
            keycloak = KeycloakBuilder
                    .builder()
                    .serverUrl(keycloakServerUrl)
                    .realm(keycloakRealm)
                    .clientId(keycloakClientId)
                    .grantType(keycloakGrantType)
                    .username(keycloakUserName)
                    .password(keycloakPassword)
                    .build();
        }
        return keycloak;
    }

}
