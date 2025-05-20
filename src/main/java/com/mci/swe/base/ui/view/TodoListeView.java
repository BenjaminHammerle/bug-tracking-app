package com.mci.swe.base.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;

import jakarta.annotation.security.PermitAll;
import com.mci.swe.models.TodoModel;
import com.mci.swe.services.TodoService;
import com.vaadin.flow.router.QueryParameters;

@Route(value = "todo-liste", layout = MainLayout.class)
@PageTitle("Overview")
@PermitAll
public class TodoListeView extends Main {
    private final TextField searchField = new TextField();
    private final Button searchButton = new Button("Suchen");

    private final Button addTodoButton = new Button("Neues Todo");
    private final Button userAdminButton = new Button("Benutzerverwaltung");
    private final Button logoutButton = new Button("Logout");

    private final Grid<TodoModel> todoGrid = new Grid<>(TodoModel.class, false);
    private final TodoService todoService = new TodoService(); 
    
        public TodoListeView() {
        setSizeFull();

        // Linke Seite
        VerticalLayout leftPanel = buildLeftPanel();

        // Rechte Seite
        VerticalLayout rightPanel = buildRightPanel();

        HorizontalLayout mainLayout = new HorizontalLayout(leftPanel, rightPanel);
        mainLayout.setSizeFull();
        mainLayout.setFlexGrow(1, leftPanel);
        mainLayout.setFlexGrow(2, rightPanel);
        
        add(mainLayout); 
    }
        
    private VerticalLayout buildLeftPanel() {
        searchField.setPlaceholder("Suchbegriff...");
        HorizontalLayout searchLayout = new HorizontalLayout(searchField, searchButton);
        searchLayout.setWidthFull();

        addTodoButton.setWidthFull();
        userAdminButton.setWidthFull();
        logoutButton.setWidthFull();

        VerticalLayout layout = new VerticalLayout(
                searchLayout,
                addTodoButton,
                userAdminButton,
                logoutButton
        );
        layout.setWidth("30%");
        layout.setSpacing(true);

        // Button-Aktionen
        addTodoButton.addClickListener(e ->  todoGrid.setItems(todoService.findAll()));
        userAdminButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("benutzer")));
        logoutButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("liste")));

        searchButton.addClickListener(e -> {
            String keyword = searchField.getValue();
            todoGrid.setItems(todoService.search(keyword));
        });

        return layout;
    }
    
     private VerticalLayout buildRightPanel() {
        todoGrid.addColumn(TodoModel::getId).setHeader("Nummer");
        todoGrid.addColumn(TodoModel::getTitel).setHeader("Titel");
        todoGrid.addColumn(TodoModel::getFirma).setHeader("Firma");
        todoGrid.addColumn(todo -> todo.getErstellt_am().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
                .setHeader("Erstellt am");
        todoGrid.setItems(todoService.findAll());

        todoGrid.addItemDoubleClickListener(event -> {
            TodoModel selected = event.getItem();
            getUI().ifPresent(ui -> ui.navigate("todo-bearbeiten/" + selected.getId()));
        });

        todoGrid.setSizeFull();

        VerticalLayout layout = new VerticalLayout(todoGrid);
        layout.setSizeFull();
        return layout;
    }

}