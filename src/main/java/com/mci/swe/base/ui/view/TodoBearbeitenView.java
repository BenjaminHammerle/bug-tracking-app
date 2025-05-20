package com.mci.swe.base.ui.view;

import com.mci.swe.models.TodoModel;
import com.mci.swe.models.TodoSchrittModel;
import com.mci.swe.services.TodoService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
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


    @Override
    public void setParameter(BeforeEvent event, String parameter) {
      para = parameter;
      //ticket = loadTicket(parameter);
    }
     
    public TodoBearbeitenView (){
        ticket = loadTicket(para);
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

        stepGrid.setItems(steps);
        stepGrid.setHeight("300px");
        stepGrid.addColumn(TodoSchrittModel::getErstelltAm).setHeader("Erstellt am");
        stepGrid.addColumn(TodoSchrittModel::getAssignee_name).setHeader("Bearbeiter");
        stepGrid.addColumn(TodoSchrittModel::getStatus_change).setHeader("Status");
        stepGrid.addColumn(TodoSchrittModel::getKommentar).setHeader("Kommentar");

        TextField assigneeNameField = new TextField("Assignee Name");
        TextField statusChangeField = new TextField("Statusänderung");
        TextArea kommentarField = new TextArea("Kommentar");
        kommentarField.setMaxHeight("100px");

        Button addButton = new Button("Schritt hinzufügen", e -> {
            TodoSchrittModel step = new TodoSchrittModel();
            step.id = steps.size() + 1;
            step.assignee_id = 999; // Beispielwert
            step.assignee_name = assigneeNameField.getValue();
            step.status_change = statusChangeField.getValue();
            step.kommentar = kommentarField.getValue();
            step.erstelltAm = LocalDateTime.now();

            steps.add(step);
            stepGrid.setItems(steps);

            assigneeNameField.clear();
            statusChangeField.clear();
            kommentarField.clear();
        });

        right.add(stepGrid, assigneeNameField, statusChangeField, kommentarField, addButton);

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
        TodoSchrittModel s1 = new TodoSchrittModel();
        s1.id = 1;
        s1.assignee_id = 2;
        s1.assignee_name = "Anna Admin";
        s1.kommentar = "Ticket aufgenommen.";
        s1.status_change = "OPEN";
        s1.erstelltAm = LocalDateTime.now().minusHours(5);

        TodoSchrittModel s2 = new TodoSchrittModel();
        s2.id = 2;
        s2.assignee_id = 3;
        s2.assignee_name = "Bob Bearbeiter";
        s2.kommentar = "Analyse läuft.";
        s2.status_change = "IN_PROGRESS";
        s2.erstelltAm = LocalDateTime.now().minusHours(2);

        return List.of(s1, s2);
    }
       

}
