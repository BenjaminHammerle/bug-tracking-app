package com.mci.swe.base.ui.view;

import com.mci.swe.models.TodoModel;
import com.mci.swe.models.TodoSchrittModel;
import com.mci.swe.services.SchrittService;
import com.mci.swe.services.TodoService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Route(value = "todo-bearbeiten", layout = MainLayout.class)
@PageTitle("Todo bearbeiten")
@PermitAll
public class TodoBearbeitenView extends Main implements HasUrlParameter<String> {

    public TodoModel ticket;
    public String para;
    private final List<TodoSchrittModel> steps = new ArrayList<>();
    private final Grid<TodoSchrittModel> stepGrid = new Grid<>(TodoSchrittModel.class);
    private final TodoService todoService = new TodoService(); 
    private final SchrittService schrittService = new SchrittService(); 
    private final ComboBox<String> statusFeld = new ComboBox<>("Status");


    @Override
    public void setParameter(BeforeEvent event, String parameter) {
      para = parameter;
      //ticket = loadTicket(parameter);
    }
     
    public TodoBearbeitenView (){
        ticket = loadTicket(para);
        statusFeld.setItems("OPEN", "IN_PROGRESS", "RESOLVED", "CLOSED");
        setSizeFull();

        // Linke Seite: Ticketdetails (readonly)
        VerticalLayout left = new VerticalLayout();
        left.setWidth("400px");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        left.add(new Label("ID: " + ticket.id));
        left.add(new Label("Titel: " + ticket.titel));
        left.add(new Label("Kunde: " + ticket.firma));
        left.add(new Label("Beschreibung: " + ticket.beschreibung));
        left.add(new Label("Priorität: " + ticket.prio));
        left.add(new Label("Status: " + ticket.status));
        left.add(new Label("Erstellt am: " + ticket.erstellt_am.format(formatter)));
        left.add(new Label("Nachname: " + ticket.nachname));

        // Rechte Seite: Schritteliste + Formular
        VerticalLayout right = new VerticalLayout();
        right.setWidthFull();
        right.setHeightFull();

        stepGrid.setColumns("id", "kommentar", "status_change", "erstelltAm");
        stepGrid.setItems(steps);
        stepGrid.setHeight("300px");

        TextField assigneeNameField = new TextField("Assignee Name");
        assigneeNameField.setWidthFull();
        statusFeld.setWidthFull();
       
        TextArea kommentarField = new TextArea("Kommentar");
        kommentarField.setMaxHeight("300px");
        kommentarField.setWidthFull();

        Button addButton = new Button("Schritt hinzufügen", e -> {
            
            TodoSchrittModel step = new TodoSchrittModel();
            step.assignee_id = 26; // Beispielwert
            step.kommentar = kommentarField.getValue();
            step.status_change = statusFeld.getValue();
            schrittService.addSchritt(step,para);

            steps.add(step);
            stepGrid.setItems(steps);

            assigneeNameField.clear();
            statusFeld.clear();
            kommentarField.clear();
        });

        right.add(stepGrid, assigneeNameField, statusFeld, kommentarField, addButton);

                HorizontalLayout mainLayout = new HorizontalLayout(left, right);
        mainLayout.setSizeFull();
        add(mainLayout);
       
        }// Reihenfolge geändert
    

    // Dummy-Daten für Demo
    private TodoModel loadTicket(String id) {
        //Ticket holen
       return todoService.findById(id);
    }

    private List<TodoSchrittModel> loadSteps() {
        //Ticketschritte holen
       
        return null;
    }
       

}
