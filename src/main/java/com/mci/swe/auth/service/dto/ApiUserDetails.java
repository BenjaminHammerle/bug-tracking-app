package com.mci.swe.auth.service.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

public class ApiUserDetails implements UserDetails {
    private final String username;
    private final String password;
    private final Long   userId;
    private final Collection<? extends GrantedAuthority> authorities;

    public ApiUserDetails(String username, String password, Long userId,
                          Collection<? extends GrantedAuthority> authorities) {
        this.username    = username;
        this.password    = password;
        this.userId      = userId;
        this.authorities = authorities;
    }

    public Long getUserId() {
        return userId;
    }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override public String getPassword() {
        return password;
    }
    @Override public String getUsername() {
        return username;
    }
    @Override public boolean isAccountNonExpired() {
        return true;
    }
    @Override public boolean isAccountNonLocked() {
        return true;
    }
    @Override public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override public boolean isEnabled() {
        return true;
    }
}
