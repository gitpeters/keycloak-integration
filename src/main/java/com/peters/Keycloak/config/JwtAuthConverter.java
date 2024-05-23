package com.peters.Keycloak.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthConverter implements Converter<Jwt, JwtAuthenticationToken> {
    @Override
    public JwtAuthenticationToken convert(Jwt source) {
        Collection<GrantedAuthority> roles = extractAuthorities(source);
        return new JwtAuthenticationToken(source, roles);
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        if(jwt.getClaim("realm_access")!=null){
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            ObjectMapper mapper = new ObjectMapper();
            List<String> keycloakRoles = mapper.convertValue(realmAccess.get("roles"), List.class);
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (String role : keycloakRoles) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
            return authorities;
        }
        return new ArrayList<>();
    }
}
