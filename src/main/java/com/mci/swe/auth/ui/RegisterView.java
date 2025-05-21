package com.mci.swe.auth.ui;


import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import com.vaadin.flow.component.UI;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.List;

@Route("register")
@PageTitle("Registrieren | Bug Tracker")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {

    private final InMemoryUserDetailsManager userDetailsManager;

    public RegisterView(InMemoryUserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;

        // Formfelder
        TextField vorname = new TextField("Vorname");
        TextField nachname = new TextField("Nachname");
        EmailField email = new EmailField("E-Mail");
        PasswordField passwort = new PasswordField("Passwort");
        TextField firma = new TextField("Firma");

        Checkbox istAdmin = new Checkbox("Admin");
        Checkbox istMitarbeiter = new Checkbox("Mitarbeiter");
        Checkbox istKunde = new Checkbox("Kunde");

        Button registerButton = new Button("Registrieren");

        registerButton.addClickListener(e -> {
            String userEmail = email.getValue();
            String rawPassword = passwort.getValue();

            if (userDetailsManager.userExists(userEmail)) {
                Notification.show("Benutzer mit dieser E-Mail existiert bereits.");
                return;
            }

            // Rollen bestimmen
            List<String> rollen = new ArrayList<>();
            if (istAdmin.getValue()) rollen.add("ADMIN");
            if (istMitarbeiter.getValue()) rollen.add("MITARBEITER");
            if (istKunde.getValue()) rollen.add("KUNDE");

            if (rollen.isEmpty()) {
                Notification.show("Bitte mindestens eine Rolle auswählen.");
                return;
            }

            UserDetails newUser = User.withUsername(userEmail)
                    .password("{noop}" + rawPassword) // {noop} = Klartext (später ersetzen!)
                    .roles(rollen.toArray(new String[0]))
                    .build();

            userDetailsManager.createUser(newUser);

            // Automatisch einloggen
            Authentication auth = new UsernamePasswordAuthenticationToken(newUser, rawPassword, newUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);

            Notification.show("Registrierung erfolgreich. Willkommen, " + vorname.getValue() + "!");

            UI.getCurrent().navigate("login"); // Zur Startseite weiterleiten
        });

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(
                new H1("Benutzerregistrierung"),
                vorname,
                nachname,
                email,
                passwort,
                firma,
                new Text("Rollen:"),
                istAdmin, istMitarbeiter, istKunde,
                registerButton
        );
    }
}
