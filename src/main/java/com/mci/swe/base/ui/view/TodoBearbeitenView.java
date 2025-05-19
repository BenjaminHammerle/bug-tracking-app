package com.mci.swe.base.ui.view;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "todo-bearbeiten", layout = MainLayout.class)
@PageTitle("Todo bearbeiten")
@PermitAll
public class TodoBearbeitenView extends Main {
       public TodoBearbeitenView() {
        add(new H2("Bearbeiten View"));
    }
}
