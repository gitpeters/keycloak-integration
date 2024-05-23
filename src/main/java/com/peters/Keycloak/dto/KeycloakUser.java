package com.peters.Keycloak.dto;

import lombok.Data;
/*
******************************************************************
This class will serve as DTO to map keycloak users because keycloak server
returns UserRepresentation which contains user's data that we might not want
to expose to the public
 */
@Data
public class KeycloakUser {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String userName;
}
