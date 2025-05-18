package com.mci.swe.base.ui.view;

import com.mci.swe.base.ui.component.ViewToolbar;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

/**
 * This view shows up when a user navigates to the root ('/') of the application.
 */

@Route(value = "", layout = MainLayout.class)
@PermitAll
public final class MainView extends Main {

    // TODO Replace with your own main view.

    public MainView() {
        addClassName(LumoUtility.Padding.MEDIUM);
        add(new ViewToolbar("Main"));
        add(new Div("Please select a view from the menu on the left."));
        add(new Div("Here will grow a wonderful app!!!!!!!!!"));
        add(new Div("Here will grow a wonderful app!!!!!!!!!"));
        add(new Div("Here will grow a wonderful app!!!!!!!!!!!!!!!"));
    }

    /**
     * Navigates to the main view.
     */
    public static void showMainView() {
        UI.getCurrent().navigate(MainView.class);
    }
}
