package com.mci.swe.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    private static final String SESSION_ATTR_USER_ID = "USER_ID";
    private static final String LOGOUT_SUCCESS_URL   = "/";

    /**
     * Speichert die User-ID in die Vaadin-Session – **nur** wenn eine existiert.
     */
    public void setLoggedInUserId(Long userId) {
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            session.setAttribute(SESSION_ATTR_USER_ID, userId);
        }
        // andernfalls ignorieren – wird später per getLoggedInUserId() nachgeladen.
    }

    /**
     * Liest die User-ID aus der Vaadin-Session. Kann null sein, wenn noch nicht gesetzt.
     */
    public Long getLoggedInUserId() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            Object val = session.getAttribute(SESSION_ATTR_USER_ID);
            if (val instanceof Long) {
                return (Long) val;
            }
        }
        return null;
    }

    /**
     * Holt das aktuell authentifizierte UserDetails-Objekt (oder null).
     */
    public UserDetails getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = (auth != null ? auth.getPrincipal() : null);
        return (principal instanceof UserDetails) ? (UserDetails) principal : null;
    }

    /**
     * Führt den Logout durch und leitet in Vaadin auf "/" weiter.
     */
    public void logout() {
        // Spring-Security-Kontext räumen
        new SecurityContextLogoutHandler()
            .logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(),
                null, null
            );

        // Nur wenn wir wirklich in einem UI-Thread sind, redirecten
        UI ui = UI.getCurrent();
        if (ui != null) {
            ui.getPage().setLocation(LOGOUT_SUCCESS_URL);
        }
    }
}
