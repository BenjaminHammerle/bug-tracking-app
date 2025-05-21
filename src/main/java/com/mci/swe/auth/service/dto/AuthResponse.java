package com.mci.swe.auth.service.dto;

public class AuthResponse {
    private boolean auth;
    private Long    userId;

    // Für Jackson
    public AuthResponse() {}

    public boolean isAuth() {
        return auth;
    }
    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
