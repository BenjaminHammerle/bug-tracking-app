package com.mci.swe.base.ui.view;

import com.mci.swe.models.TodoModel;
import com.mci.swe.models.BenutzerModel;
import com.mci.swe.services.TodoService;
import com.mci.swe.services.BenutzerService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route(value = "todo-liste", layout = MainLayout.class)
@PageTitle("Benutzerverwaltung")
@RolesAllowed("ADMIN")
public class TodoListeView extends Main {

    // Such- und Filter-Controls
    private final TextField searchField      = new TextField("Suche...");
    private final Button    searchButton     = new Button("Filtern");
    private final Button    resetButton      = new Button("Reset");
    private final ComboBox<String>      statusFilter   = new ComboBox<>("Status");
    private final ComboBox<String>      prioFilter     = new ComboBox<>("Priorität");
    private final ComboBox<BenutzerModel> ownerFilter    = new ComboBox<>("Ersteller");
    private final ComboBox<BenutzerModel> assigneeFilter = new ComboBox<>("Assignee");
    private final TextField             firmaFilter    = new TextField("Firma");

    // Haupt-Grid und Services
    private final Grid<TodoModel> todoGrid         = new Grid<>(TodoModel.class, false);
    private final TodoService     todoService      = new TodoService();
    private final BenutzerService benutzerService  = new BenutzerService();

    public TodoListeView() {
        setSizeFull();

        // --- Filter-Daten laden ---
        statusFilter.setItems("OPEN","IN_PROGRESS","RESOLVED","CLOSED");
        prioFilter.setItems("LOW","MEDIUM","HIGH");
        List<BenutzerModel> users = benutzerService.getAllUsers();
        ownerFilter.setItems(users);
        ownerFilter.setItemLabelGenerator(u -> u.getVorname() + " " + u.getNachname());
        assigneeFilter.setItems(users);
        assigneeFilter.setItemLabelGenerator(u -> u.getVorname() + " " + u.getNachname());
        firmaFilter.setPlaceholder("Firma...");

        // Vollständige Breite der Controls
        statusFilter.setWidthFull();
        prioFilter.setWidthFull();
        ownerFilter.setWidthFull();
        assigneeFilter.setWidthFull();
        firmaFilter.setWidthFull();
        searchField.setWidthFull();

        // Buttons konfigurieren
        searchButton.setWidth("50%");
        resetButton.setWidth("50%");
        searchButton.addClickListener(e -> applyFilters());
        resetButton.addClickListener(e -> resetFilters());
        HorizontalLayout buttons = new HorizontalLayout(searchButton, resetButton);
        buttons.setWidthFull();

        // --- Linkes Panel (Filter) im Card-Look ---
        VerticalLayout leftPanel = new VerticalLayout(
            statusFilter,
            prioFilter,
            ownerFilter,
            assigneeFilter,
            firmaFilter,
            searchField,
            buttons
        );
        leftPanel.setWidth("30%");
        leftPanel.setSpacing(true);
        leftPanel.getElement().getStyle()
            .set("background",      "var(--lumo-base-color)")
            .set("box-shadow",      "var(--lumo-box-shadow-s)")
            .set("border-radius",   "8px")
            .set("padding",         "1rem")
            .set("margin",          "0.5rem");

        // --- Grid Spalten ---
        todoGrid.addColumn(TodoModel::getId)
                .setHeader("Nummer")
                .setAutoWidth(true);

        todoGrid.addColumn(TodoModel::getTitel)
                .setHeader("Titel")
                .setAutoWidth(true);

        todoGrid.addColumn(TodoModel::getFirma)
                .setHeader("Firma")
                .setAutoWidth(true);

        // Priorität als farbige Badge
        todoGrid.addComponentColumn(todo -> {
            Span badge = new Span(todo.getPrio());
            badge.getElement().getThemeList().add("badge");
            switch (todo.getPrio()) {
                case "LOW":    badge.getElement().getThemeList().add("contrast"); break;
                case "MEDIUM": badge.getElement().getThemeList().add("primary");  break;
                case "HIGH":   badge.getElement().getThemeList().add("error");    break;
            }
            return badge;
        }).setHeader("Priorität")
          .setAutoWidth(true);

        // Status als farbige Badge
        todoGrid.addComponentColumn(todo -> {
            Span badge = new Span(todo.getStatus());
            badge.getElement().getThemeList().add("badge");
            switch (todo.getStatus()) {
                case "OPEN":        badge.getElement().getThemeList().add("contrast"); break;
                case "IN_PROGRESS": badge.getElement().getThemeList().add("primary");  break;
                case "RESOLVED":    badge.getElement().getThemeList().add("success");  break;
                case "CLOSED":      badge.getElement().getThemeList().add("error");    break;
            }
            return badge;
        }).setHeader("Status")
          .setAutoWidth(true);

        todoGrid.addColumn(todo ->
            todo.getErstellt_am()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        ).setHeader("Erstellt am")
         .setAutoWidth(true);

        // Gitter konfigurieren
        todoGrid.setItems(todoService.findAll());
        todoGrid.addItemDoubleClickListener(event ->
            getUI().ifPresent(ui ->
                ui.navigate("todo-bearbeiten/" + event.getItem().getId())
            )
        );
        todoGrid.setSizeFull();
        todoGrid.addThemeVariants(
            GridVariant.LUMO_ROW_STRIPES,
            GridVariant.LUMO_WRAP_CELL_CONTENT
        );
        todoGrid.getElement().getStyle()
            .set("background",    "var(--lumo-contrast-5pct)")
            .set("border-radius", "8px");

        // --- Rechtes Panel (Grid) im Card-Look ---
        VerticalLayout rightPanel = new VerticalLayout(todoGrid);
        rightPanel.setSizeFull();
        rightPanel.getElement().getStyle()
            .set("background", "#f5fafd")
            .set("padding",    "0.5rem")
            .set("border-radius","8px");

        // --- Zusammensetzen ---
        HorizontalLayout mainLayout = new HorizontalLayout(leftPanel, rightPanel);
        mainLayout.setSizeFull();
        mainLayout.setFlexGrow(1, leftPanel);
        mainLayout.setFlexGrow(2, rightPanel);
        add(mainLayout);
    }

    private void applyFilters() {
        String status     = statusFilter.getValue();
        String prio       = prioFilter.getValue();
        Integer ownerId   = ownerFilter.getValue()   != null ? ownerFilter.getValue().getId() : null;
        Integer assigneeId= assigneeFilter.getValue()!= null ? assigneeFilter.getValue().getId(): null;
        String firma      = firmaFilter.getValue();
        String search     = searchField.getValue();

        todoGrid.setItems(
            todoService.findFiltered(status, prio, ownerId, assigneeId, firma, search)
        );
    }

    private void resetFilters() {
        statusFilter.clear();
        prioFilter.clear();
        ownerFilter.clear();
        assigneeFilter.clear();
        firmaFilter.clear();
        searchField.clear();
        todoGrid.setItems(todoService.findAll());
    }
}
