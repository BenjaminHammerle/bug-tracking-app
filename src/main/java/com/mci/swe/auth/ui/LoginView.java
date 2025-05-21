package com.mci.swe.auth.ui;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Login | Bug Tracker")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm loginForm = new LoginForm();

    public LoginView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Der LoginForm schickt per default POST an /login
        loginForm.setAction("login");
        loginForm.setForgotPasswordButtonVisible(false);

        add(new H1("Login"), loginForm);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Zeige Fehlermeldung, wenn "?error" in der URL
        if (event.getLocation()
                 .getQueryParameters()
                 .getParameters()
                 .containsKey("error")) {
            loginForm.setError(true);
        }
    }
}
