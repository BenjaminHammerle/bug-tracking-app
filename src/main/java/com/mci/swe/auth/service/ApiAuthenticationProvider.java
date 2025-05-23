// src/main/java/com/mci/swe/auth/service/ApiAuthenticationProvider.java
package com.mci.swe.auth.service;

import com.mci.swe.auth.service.dto.ApiUserDetails;
import com.mci.swe.auth.service.dto.AuthRequest;
import com.mci.swe.auth.service.dto.AuthResponse;
import com.mci.swe.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class ApiAuthenticationProvider implements AuthenticationProvider {

    private final RestTemplate restTemplate;

    @Autowired
    @Lazy
    private SecurityService securityService;

    @Autowired
    public ApiAuthenticationProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String email  = authentication.getName();
        String rawPwd = authentication.getCredentials().toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        AuthRequest authRequest = new AuthRequest(email, rawPwd);
        HttpEntity<AuthRequest> entity = new HttpEntity<>(authRequest, headers);

        // API-Call
        ResponseEntity<AuthResponse> resp;
        try {
            resp = restTemplate.exchange(
                "https://nx0u5kutgk.execute-api.eu-central-1.amazonaws.com/PROD/Auth",
                HttpMethod.GET,
                entity,
                AuthResponse.class
            );
        } catch (HttpClientErrorException.Unauthorized e) {
            // 401 von der API → BadCredentials für Vaadin
            throw new BadCredentialsException("Ungültige Zugangsdaten");
        }

        // auth=false oder kein Body → Credentials ungültig
        if (resp.getStatusCode() != HttpStatus.OK
            || resp.getBody() == null
            || !resp.getBody().isAuth()) {
            throw new BadCredentialsException("Ungültige Zugangsdaten");
        }

        // Body auslesen
        AuthResponse body = resp.getBody();
        Long userId = body.getUserId();
        securityService.setLoggedInUserId(userId);

        // Authorities dynamisch bauen
        List<GrantedAuthority> authorities = new ArrayList<>();
        // Jeder eingeloggte User erhält ROLE_USER
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        // Falls isAdmin == 1, auch ROLE_ADMIN
        if (body.getIsAdmin() == 1) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        // Principal mit Rollen
        ApiUserDetails principal = new ApiUserDetails(
            email, rawPwd, userId, authorities
        );

        // Authentication-Token zurückgeben
        return new UsernamePasswordAuthenticationToken(
            principal, rawPwd, authorities
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class
                     .isAssignableFrom(authentication);
    }
}
