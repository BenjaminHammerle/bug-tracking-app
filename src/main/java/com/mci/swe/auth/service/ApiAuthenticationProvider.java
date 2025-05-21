package com.mci.swe.auth.service;

import com.mci.swe.auth.service.dto.ApiUserDetails;
import com.mci.swe.auth.service.dto.AuthRequest;
import com.mci.swe.auth.service.dto.AuthResponse;
import com.mci.swe.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ApiAuthenticationProvider implements AuthenticationProvider {

    private final RestTemplate restTemplate;
    private final SecurityService securityService;

    @Autowired
    public ApiAuthenticationProvider(RestTemplate restTemplate,
                                     SecurityService securityService) {
        this.restTemplate    = restTemplate;
        this.securityService = securityService;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String email  = authentication.getName();
        String rawPwd = authentication.getCredentials().toString();

        // Header mit JSON-Content-Type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Body bauen
        AuthRequest authRequest = new AuthRequest(email, rawPwd);
        HttpEntity<AuthRequest> entity = new HttpEntity<>(authRequest, headers);

        // API-Aufruf
        ResponseEntity<AuthResponse> resp = restTemplate.exchange(
            "https://nx0u5kutgk.execute-api.eu-central-1.amazonaws.com/PROD/Auth",
            HttpMethod.GET,
            entity,
            AuthResponse.class
        );

        if (resp.getStatusCode() != HttpStatus.OK
            || resp.getBody() == null
            || !resp.getBody().isAuth()) {
            throw new BadCredentialsException("Ungültige Zugangsdaten");
        }

        // UserId aus der API-Response
        Long userId = resp.getBody().getUserId();

        // Optional: in der Vaadin-Session speichern, falls Du das weiterhin möchtest
        securityService.setLoggedInUserId(userId);

        // Rollen (Authorities)
        List<GrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority("ROLE_USER")
        );

        // Principal mit unserem eigenen UserDetails
        ApiUserDetails principal = new ApiUserDetails(
            email,
            rawPwd,
            userId,
            authorities
        );

        // Authentication-Token mit Principal
        return new UsernamePasswordAuthenticationToken(
            principal,
            rawPwd,
            authorities
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class
                     .isAssignableFrom(authentication);
    }
}
