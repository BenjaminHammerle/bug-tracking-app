package com.mci.swe.base.ui.view;

import com.mci.swe.security.SecurityService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {

    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        setPrimarySection(Section.NAVBAR);
        createHeader();
    }

    private void createHeader() {
        // Bug-Icon + Titel
        Icon bugIcon = VaadinIcon.BUG.create();
        bugIcon.getStyle().set("color", "white");
        H1 title = new H1("BugTracker");
        title.getStyle().set("color", "white").set("margin", "0");

        // Navigation-Links
        RouterLink dashboardLink = createLink(VaadinIcon.HOME, "Dashboard", MainView.class);
        RouterLink listLink      = createLink(VaadinIcon.LIST, "Todo-Liste", TodoListeView.class);
        RouterLink createLink    = createLink(VaadinIcon.PLUS_CIRCLE, "Neues Todo", TodoErstellenView.class);
        RouterLink usersLink     = createLink(VaadinIcon.USERS, "Benutzer", TodoBenutzerView.class);

        HorizontalLayout nav = new HorizontalLayout(dashboardLink, listLink, createLink, usersLink);
        nav.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        // Logout-Button
        Button logout = new Button(new Icon(VaadinIcon.SIGN_OUT), e -> {
            securityService.logout();
            UI.getCurrent().navigate("login");
        });
        logout.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
        logout.getStyle().set("color", "white");

        // Header zusammenbauen
        HorizontalLayout header = new HorizontalLayout(bugIcon, title, nav, logout);
        header.expand(nav);
        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        header.setWidthFull();
        header.getStyle()
              .set("background-color", "#1976d2")
              .set("padding", "0.5rem 1rem")
              .set("box-shadow", "0 2px 4px rgba(0,0,0,0.2)")
              .set("gap", "1rem");

        addToNavbar(header);
    }

    private RouterLink createLink(VaadinIcon icon, String text, Class<? extends Component> target) {
        Icon i = icon.create();
        i.getStyle().set("margin-right", "0.25rem").set("color", "white");

        // "" als Text, damit target != null
        RouterLink link = new RouterLink("", target);
        // remove the default text node
        link.getElement().removeAllChildren();
        link.add(i, new Span(text));

        link.setHighlightCondition(HighlightConditions.sameLocation());
        link.getStyle()
            .set("color", "white")
            .set("text-decoration", "none")
            .set("font-weight", "500")
            .set("padding", "0 0.75rem");

        return link;
    }
}
