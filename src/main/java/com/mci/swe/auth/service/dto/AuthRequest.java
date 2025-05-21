// src/main/java/com/mci/swe/auth/service/dto/AuthRequest.java
package com.mci.swe.auth.service.dto;

public class AuthRequest {
    private String email;
    private String passwort;

    public AuthRequest() { }

    public AuthRequest(String email, String passwort) {
        this.email = email;
        this.passwort = passwort;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswort() {
        return passwort;
    }
    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }
}
