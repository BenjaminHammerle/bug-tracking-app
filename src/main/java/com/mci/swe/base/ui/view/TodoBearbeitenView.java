package com.mci.swe.base.ui.view;

import com.mci.swe.models.TodoModel;
import com.mci.swe.models.TodoSchrittModel;
import com.mci.swe.services.SchrittService;
import com.mci.swe.services.TodoService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Route(value = "todo-bearbeiten", layout = MainLayout.class)
@PageTitle("Todo bearbeiten")
@PermitAll
public class TodoBearbeitenView extends Main implements HasUrlParameter<String> {

    private TodoModel ticket;
    private String para;
    private final List<TodoSchrittModel> steps = new ArrayList<>();
    private final Grid<TodoSchrittModel> stepGrid = new Grid<>(TodoSchrittModel.class);
    private final TodoService todoService = new TodoService();
    private final SchrittService schrittService = new SchrittService();
    private final ComboBox<String> statusFeld = new ComboBox<>("Status");

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        this.para = parameter;
        this.ticket = loadTicket(parameter);
        // Lade Schritte frisch aus dem Service
        steps.clear();
        steps.addAll(loadSteps());
        stepGrid.setItems(steps);
        // Baue UI nach Parameter-Set
        initView();
    }

    private void initView() {
        statusFeld.setItems("OPEN", "IN_PROGRESS", "RESOLVED", "CLOSED");
        setSizeFull();

        // Linke Seite: Ticketdetails (readonly)
        VerticalLayout left = new VerticalLayout();
        left.setWidth("400px");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        left.add(new Label("ID: " + ticket.getId()));
        left.add(new Label("Titel: " + ticket.getTitel()));
        left.add(new Label("Kunde: " + ticket.getFirma()));
        left.add(new Label("Beschreibung: " + ticket.getBeschreibung()));
        left.add(new Label("Priorität: " + ticket.getPrio()));
        left.add(new Label("Status: " + ticket.getStatus()));
        left.add(new Label("Erstellt am: " + ticket.getErstellt_am().format(formatter)));
        left.add(new Label("Nachname: " + ticket.getNachname()));

        // Rechte Seite: Schritteliste + Formular
        VerticalLayout right = new VerticalLayout();
        right.setWidthFull();
        right.setHeightFull();

        stepGrid.setColumns("id", "kommentar", "status_change", "erstelltAm");
        stepGrid.setHeight("300px");

        TextField assigneeNameField = new TextField("Assignee Name");
        assigneeNameField.setWidthFull();
        statusFeld.setWidthFull();

        TextArea kommentarField = new TextArea("Kommentar");
        kommentarField.setMaxHeight("300px");
        kommentarField.setWidthFull();

        Button addButton = new Button("Schritt hinzufügen", e -> {
            TodoSchrittModel step = new TodoSchrittModel();
            step.setAssignee_id(26); // Beispielwert
            step.setKommentar(kommentarField.getValue());
            step.setStatus_change(statusFeld.getValue());
            schrittService.addSchritt(step, para);

            // Grid neu befüllen
            steps.clear();
            steps.addAll(loadSteps());
            stepGrid.setItems(steps);

            assigneeNameField.clear();
            statusFeld.clear();
            kommentarField.clear();
        });

        right.add(stepGrid, assigneeNameField, statusFeld, kommentarField, addButton);

        HorizontalLayout mainLayout = new HorizontalLayout(left, right);
        mainLayout.setSizeFull();
        removeAll();
        add(mainLayout);
    }

    private TodoModel loadTicket(String id) {
        return todoService.findById(id);
    }

    private List<TodoSchrittModel> loadSteps() {
        return schrittService.getSchritte(para);
    }
}