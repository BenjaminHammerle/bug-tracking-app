package com.mci.swe.base.ui.view;

import com.mci.swe.auth.service.dto.ApiUserDetails;
import com.mci.swe.models.BenutzerModel;
import com.mci.swe.models.TodoModel;
import com.mci.swe.services.BenutzerService;
import com.mci.swe.services.TodoService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard")
@PermitAll
public class MainView extends VerticalLayout {

    private final BenutzerService benutzerService;
    private final TodoService     todoService;

    public MainView(BenutzerService benutzerService,
                    TodoService todoService) {
        this.benutzerService = benutzerService;
        this.todoService     = todoService;

        setSizeFull();
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        buildDashboard();
    }

    private void buildDashboard() {
        // (1) Principal aus Spring Security holen
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // (2) Auf ApiUserDetails casten
        Long userId = null;
        if (auth != null && auth.getPrincipal() instanceof ApiUserDetails principal) {
            userId = principal.getUserId();
        }

        // (3) BenutzerModel per ID laden
        Optional<BenutzerModel> optUser = Optional.empty();
        if (userId != null) {
            optUser = benutzerService.findById(userId);
        }

        // (4) Begrüßung
        String greeting = optUser
            .map(u -> "Willkommen, " + u.getVorname() + " " + u.getNachname() + "!")
            .orElse("Willkommen!");
        H1 hero = new H1(greeting);
        hero.setWidthFull();
        hero.getStyle()
            .set("font-size", "2.5rem")
            .set("margin", "2rem 0 1rem 0")
            .set("color", "#0277bd")
            .set("text-align", "center");
        add(hero);

        int id = optUser.map(BenutzerModel::getId).orElse(-1);
        List<TodoModel> meineTickets = 
            todoService.findFiltered(null, null, id, null, null, null);
        List<TodoModel> zugewiesen   = 
            todoService.findFiltered(null, null, null, id, null, null);

        VerticalLayout card1 = createTodoCard("Meine Tickets", meineTickets);
        VerticalLayout card2 = createTodoCard("Mir zugewiesen",   zugewiesen);

        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setSizeFull();
        wrapper.getStyle().set("padding", "0 1rem 2rem 1rem");
        wrapper.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        FlexLayout cards = new FlexLayout(card1, card2);
        cards.setSizeFull();
        cards.setFlexWrap(FlexWrap.WRAP);
        cards.setJustifyContentMode(FlexComponent.JustifyContentMode.AROUND);
        cards.getStyle()
             .set("gap", "1rem")
             .set("max-width", "1200px")
             .set("margin", "0 auto");

        wrapper.add(cards);
        add(wrapper);
    }

    private VerticalLayout createTodoCard(String title, List<TodoModel> items) {
        VerticalLayout card = new VerticalLayout();
        card.getStyle()
            .set("background-color", "#e3f2fd")
            .set("padding", "1rem")
            .set("border-radius", "8px")
            .set("box-shadow", "0 4px 8px rgba(0,0,0,0.05)")
            .set("flex", "1 1 350px")
            .set("min-width", "280px");

        H3 header = new H3(title);
        header.getStyle()
            .set("margin", "0 0 0.75rem 0")
            .set("color", "#01579b");

        Grid<TodoModel> grid = new Grid<>(TodoModel.class, false);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        grid.addColumn(TodoModel::getId)
            .setHeader("ID")
            .setAutoWidth(true);

        grid.addColumn(TodoModel::getTitel)
            .setHeader("Titel")
            .setFlexGrow(1);

        // Priorität als Badge
        grid.addComponentColumn(t -> createPrioBadge(t.getPrio()))
            .setHeader("Priorität")
            .setAutoWidth(true);

        // Status als Badge
        grid.addComponentColumn(t -> createStatusBadge(t.getStatus()))
            .setHeader("Status")
            .setAutoWidth(true);

        grid.addColumn(t -> t.getErstellt_am().format(fmt))
            .setHeader("Erstellt am")
            .setAutoWidth(true);

        grid.setItems(items);
        grid.setHeight("250px");
        grid.addThemeVariants(
            GridVariant.LUMO_ROW_STRIPES,
            GridVariant.LUMO_COLUMN_BORDERS
        );

        grid.asSingleSelect().addValueChangeListener(ev -> {
            if (ev.getValue() != null) {
                UI.getCurrent().navigate("todo-bearbeiten/" + ev.getValue().getId());
            }
        });

        card.add(header, grid);
        return card;
    }

    // Hilfs‐Methoden für farbige Badges
    private Span createStatusBadge(String status) {
        Span badge = new Span(status);
        badge.getElement().getThemeList().add("badge");
        switch (status) {
            case "OPEN":        badge.getElement().getThemeList().add("contrast"); break;
            case "IN_PROGRESS": badge.getElement().getThemeList().add("primary");  break;
            case "RESOLVED":    badge.getElement().getThemeList().add("success");  break;
            case "CLOSED":      badge.getElement().getThemeList().add("error");    break;
        }
        return badge;
    }

    private Span createPrioBadge(String prio) {
        Span badge = new Span(prio);
        badge.getElement().getThemeList().add("badge");
        switch (prio) {
            case "LOW":    badge.getElement().getThemeList().add("success");  break;
            case "MEDIUM": badge.getElement().getThemeList().add("contrast"); break;
            case "HIGH":   badge.getElement().getThemeList().add("error");    break;
        }
        return badge;
    }
}
