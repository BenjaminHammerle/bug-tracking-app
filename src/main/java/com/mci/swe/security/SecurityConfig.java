package com.mci.swe.security;

import java.util.Collections;


import com.mci.swe.auth.ui.LoginView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.vaadin.flow.spring.security.VaadinWebSecurity;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    private static class SimpleInMemoryUserDetailsManager extends InMemoryUserDetailsManager {
        public SimpleInMemoryUserDetailsManager() {
            createUser(new User("user",
                    "{noop}user",
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
            ));
            createUser(new User("admin",
                    "{noop}admin",
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))
            ));
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Erlaube Zugriff auf statische Inhalte wie Bilder
        http.authorizeHttpRequests(requests -> requests.requestMatchers("/images/**").permitAll());

        // Konfiguriere Login-Seite über Pfad, nicht Klasse (vermeidet Paket-Abhängigkeit!)
        super.configure(http);


        setLoginView(http, LoginView.class); //setLoginView(http, "/login");
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        return new SimpleInMemoryUserDetailsManager();
    }
}



