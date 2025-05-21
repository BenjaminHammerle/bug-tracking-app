package com.mci.swe.security;

import com.mci.swe.auth.service.ApiAuthenticationProvider;
import com.mci.swe.auth.ui.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends VaadinWebSecurity {

    private final ApiAuthenticationProvider apiAuthProvider;

    public SecurityConfig(ApiAuthenticationProvider apiAuthProvider) {
        this.apiAuthProvider = apiAuthProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 1) Deinen Custom-Provider registrieren
        http.authenticationProvider(apiAuthProvider);

        // 2) Vaadin-spezifische Defaults (CSRF, Static Resources, Login) setzen
        super.configure(http);

        // 3) Dein LoginView verknüpfen
        setLoginView(http, LoginView.class);

        // Hinweis: Wenn Du weitere Endpoints freigeben willst,
        //       kannst Du das entweder per @PermitAll-Annotation am Controller
        //       oder in einer weiteren configure-Phase machen.
    }
}
