package com.mci.swe.base.ui.view;

import com.mci.swe.models.TodoModel;
import com.mci.swe.models.BenutzerModel;
import com.mci.swe.services.TodoService;
import com.mci.swe.services.BenutzerService;
import com.mci.swe.security.SecurityService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard")
@PermitAll
public class MainView extends Main {

    private final SecurityService securityService;
    private final BenutzerService benutzerService = new BenutzerService();
    private final TodoService todoService = new TodoService();

    public MainView(SecurityService securityService) {
        this.securityService = securityService;
        setSizeFull();
        buildDashboard();
    }

    private void buildDashboard() {
        // --- Hero-Bereich mit starker Begrüßung ---
        UserDetails user = securityService.getAuthenticatedUser();
        String email = user != null ? user.getUsername() : "";
        Optional<BenutzerModel> optUser = benutzerService.getAllUsers().stream()
            .filter(u -> u.getEmail().equalsIgnoreCase(email))
            .findFirst();

        String greeting = optUser
            .map(u -> "Willkommen, " + u.getVorname() + " " + u.getNachname() + "!")
            .orElse("Willkommen!");

        H1 hero = new H1(greeting);
        hero.getStyle()
            .set("font-size", "2.5rem")
            .set("margin", "1rem 0")
            .set("color", "#0277bd");   // dunkles Blau
        add(hero);

        // --- Zwei Karten: „Meine Tickets“ und „Mir zugewiesen“ ---
        int userId = optUser.map(BenutzerModel::getId).orElse(-1);
        List<TodoModel> meineTickets = todoService.findFiltered(null, null, userId, null, null, null);
        List<TodoModel> zugewiesen   = todoService.findFiltered(null, null, null, userId, null, null);

        VerticalLayout card1 = createTodoCard("Meine Tickets", meineTickets);
        VerticalLayout card2 = createTodoCard("Mir zugewiesen", zugewiesen);

        // --- Responsive Flex-Container ---
        FlexLayout cards = new FlexLayout(card1, card2);
        cards.setSizeFull();
        cards.setFlexWrap(FlexWrap.WRAP);
        cards.getStyle()
            .set("gap", "1rem")
            .set("padding-bottom", "2rem");
        add(cards);
    }

    private VerticalLayout createTodoCard(String title, List<TodoModel> items) {
        // Karte
        VerticalLayout card = new VerticalLayout();
        card.getStyle()
            .set("background-color", "#e3f2fd")
            .set("padding", "1rem")
            .set("border-radius", "8px")
            .set("box-shadow", "0 4px 8px rgba(0,0,0,0.05)")
            .set("flex", "1 1 350px")  // erlaubt, ab 350px zu umbrechen
            .set("min-width", "280px"); 

        // Titel
        H3 header = new H3(title);
        header.getStyle()
            .set("margin", "0 0 0.75rem 0")
            .set("color", "#01579b");

        // Grid
        Grid<TodoModel> grid = new Grid<>(TodoModel.class, false);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        grid.addColumn(TodoModel::getId).setHeader("ID").setAutoWidth(true);
        grid.addColumn(TodoModel::getTitel).setHeader("Titel").setFlexGrow(1);
        grid.addColumn(TodoModel::getPrio).setHeader("Priorität").setAutoWidth(true);
        grid.addColumn(TodoModel::getStatus).setHeader("Status").setAutoWidth(true);
        grid.addColumn(t -> t.getErstellt_am().format(fmt))
            .setHeader("Erstellt am").setAutoWidth(true);

        grid.setItems(items);
        grid.setHeight("250px");
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS);

        // Klick in Detail
        grid.asSingleSelect().addValueChangeListener(ev -> {
            if (ev.getValue() != null) {
                UI.getCurrent().navigate("todo-bearbeiten/" + ev.getValue().getId());
            }
        });

        card.add(header, grid);
        return card;
    }
}
