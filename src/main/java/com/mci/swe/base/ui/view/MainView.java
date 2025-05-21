package com.mci.swe.base.ui.view;

import com.mci.swe.base.ui.component.ViewToolbar;

import com.mci.swe.security.SecurityService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * This view shows up when a user navigates to the root ('/') of the application.
 */

@Route(value = "", layout = MainLayout.class)
@PageTitle("Main")
@PermitAll
public class MainView extends VerticalLayout {

    public MainView(SecurityService securityService) {
        UserDetails user = securityService.getAuthenticatedUser();
        add(new H1("Willkommen " + (user != null ? user.getUsername() : "Gast")));
    }
}