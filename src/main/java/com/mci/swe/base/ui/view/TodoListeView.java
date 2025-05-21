package com.mci.swe.base.ui.view;

import com.mci.swe.models.TodoModel;
import com.mci.swe.models.BenutzerModel;
import com.mci.swe.services.TodoService;
import com.mci.swe.services.BenutzerService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Route(value = "todo-liste", layout = MainLayout.class)
@PageTitle("Overview")
@PermitAll
public class TodoListeView extends Main {
    private final TextField searchField = new TextField("Suche...");
    private final Button searchButton = new Button("Filtern");
    private final Button resetButton = new Button("Reset");

    // Filterkomponenten
    private final ComboBox<String> statusFilter = new ComboBox<>("Status");
    private final ComboBox<String> prioFilter = new ComboBox<>("Priorität");
    private final ComboBox<BenutzerModel> ownerFilter = new ComboBox<>("Ersteller");
    private final ComboBox<BenutzerModel> assigneeFilter = new ComboBox<>("Assignee");
    private final TextField firmaFilter = new TextField("Firma");

    private final Grid<TodoModel> todoGrid = new Grid<>(TodoModel.class, false);
    private final TodoService todoService = new TodoService();
    private final BenutzerService benutzerService = new BenutzerService();

    public TodoListeView() {
        setSizeFull();

        VerticalLayout leftPanel = buildLeftPanel();
        VerticalLayout rightPanel = buildRightPanel();

        HorizontalLayout mainLayout = new HorizontalLayout(leftPanel, rightPanel);
        mainLayout.setSizeFull();
        mainLayout.setFlexGrow(1, leftPanel);
        mainLayout.setFlexGrow(2, rightPanel);
        add(mainLayout);
    }

    private VerticalLayout buildLeftPanel() {
        // Daten für Filter laden
        statusFilter.setItems("OPEN","IN_PROGRESS","RESOLVED","CLOSED");
        prioFilter.setItems("LOW","MEDIUM","HIGH");
        List<BenutzerModel> users = benutzerService.getAllUsers();
        ownerFilter.setItems(users);
        ownerFilter.setItemLabelGenerator(u -> u.getVorname() + " " + u.getNachname());
        assigneeFilter.setItems(users);
        assigneeFilter.setItemLabelGenerator(u -> u.getVorname() + " " + u.getNachname());
        firmaFilter.setPlaceholder("Firma...");
    
        // **Hier setzen wir alle Controls auf 100% ihrer Container-Breite**
        statusFilter.setWidthFull();
        prioFilter.setWidthFull();
        ownerFilter.setWidthFull();
        assigneeFilter.setWidthFull();
        firmaFilter.setWidthFull();
        searchField.setWidthFull();
    
        // Buttons in einem eigenen Layout
        Button resetButton = new Button("Reset", e -> {
            statusFilter.clear();
            prioFilter.clear();
            ownerFilter.clear();
            assigneeFilter.clear();
            firmaFilter.clear();
            searchField.clear();
            todoGrid.setItems(todoService.findAll());
        });
        // Breite Buttons halbieren
        searchButton.setWidth("50%");
        resetButton.setWidth("50%");
        HorizontalLayout buttons = new HorizontalLayout(searchButton, resetButton);
        buttons.setWidthFull();
    
        // Linkes Panel: flexibles Layout
        VerticalLayout layout = new VerticalLayout(
            statusFilter,
            prioFilter,
            ownerFilter,
            assigneeFilter,
            firmaFilter,
            searchField,
            buttons
        );
        
        layout.setWidth("30%");
        layout.setSpacing(true);
    
        // Filter-Action
        searchButton.addClickListener(e -> applyFilters());
    
        return layout;
    }

    private void applyFilters() {
        String status = statusFilter.getValue();
        String prio = prioFilter.getValue();
        Integer ownerId = ownerFilter.getValue() != null ? ownerFilter.getValue().getId() : null;
        Integer assigneeId = assigneeFilter.getValue() != null ? assigneeFilter.getValue().getId() : null;
        String firma = firmaFilter.getValue();
        String search = searchField.getValue();

        todoGrid.setItems(todoService.findFiltered(status, prio, ownerId, assigneeId, firma, search));
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

    private VerticalLayout buildRightPanel() {
        todoGrid.addColumn(TodoModel::getId).setHeader("Nummer");
        todoGrid.addColumn(TodoModel::getTitel).setHeader("Titel");
        todoGrid.addColumn(TodoModel::getFirma).setHeader("Firma");
        todoGrid.addColumn(TodoModel::getPrio).setHeader("Priorität");
        todoGrid.addColumn(TodoModel::getStatus).setHeader("Status");
        todoGrid.addColumn(todo -> todo.getErstellt_am()
            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
            .setHeader("Erstellt am");

        todoGrid.setItems(todoService.findAll());
        todoGrid.addItemDoubleClickListener(event -> {
            TodoModel selected = event.getItem();
            getUI().ifPresent(ui -> ui.navigate("todo-bearbeiten/" + selected.getId()));
        });
        todoGrid.setSizeFull();
        return new VerticalLayout(todoGrid);
    }
}
