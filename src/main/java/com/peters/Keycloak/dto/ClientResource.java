package com.peters.Keycloak.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientResource {
    private String id;
    private String resourceName;
}
