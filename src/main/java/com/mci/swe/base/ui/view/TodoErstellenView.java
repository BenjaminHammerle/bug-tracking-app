package com.mci.swe.base.ui.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.annotation.security.PermitAll;
import com.mci.swe.models.TodoModel;
import com.mci.swe.services.TodoService;
import com.vaadin.flow.component.combobox.ComboBox;

@Route(value = "todo-erstellen", layout = MainLayout.class)
@PageTitle("Todo erstellen")
@PermitAll
public class TodoErstellenView  extends Main {
    private final TodoService todoService = new TodoService();
    
    private final TextField titleField = new TextField("Titel");
    private final TextArea textField = new TextArea("Text");
    private final ComboBox<String> prio = new ComboBox<>("Priorität");

    private final Button saveButton = new Button("Todo erstellen");
    private final Button returnButton = new Button("Zurück");
    
    
       public TodoErstellenView() {
        setSizeFull();
         prio.setItems("LOW", "MEDIUM", "HIGH");

    // Überschrift
    H3 header = new H3("Neues Todo erstellen");


    // TextArea für Beschreibung
    textField.setWidth("400px");
    textField.setHeight("150px");
    
    
    prio.setWidth("400px");
    prio.setHeight("150px");

    // Optional: Felder in fester Breite
    titleField.setWidth("400px");
    saveButton.setWidth("400px");
    
    // Optional: Felder in fester Breite
    titleField.setWidth("400px");
    returnButton.setWidth("400px");

    // Layout mit vertikaler Ausrichtung
    VerticalLayout formLayout = new VerticalLayout();
    formLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
    formLayout.setSpacing(true);
    formLayout.add(header, titleField, textField, prio, saveButton, returnButton);

    // Gesamtes Layout zentrieren
    VerticalLayout outerLayout = new VerticalLayout(formLayout);
    outerLayout.setSizeFull();
    outerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    outerLayout.setAlignItems(FlexComponent.Alignment.CENTER);

    add(outerLayout);

    saveButton.addClickListener(e -> saveTodo());
    returnButton.addClickListener(e -> backToOverview());

 
    }
       
        private void saveTodo() {
        String title = titleField.getValue();
        String text = textField.getValue();
        String prioritaet = prio.getValue();

        if (title == null || title.isBlank()) {
            Notification.show("Titel darf nicht leer sein", 3000, Notification.Position.MIDDLE);
            return;
        }

        LocalDateTime createdAt = LocalDateTime.now();

        TodoModel todo = new TodoModel();
        todo.titel = title;
        todo.beschreibung = text;
        todo.id = 26;
        todo.prio = prioritaet;
        // TODO: Hier speichern (z.B. DB, Service, Liste...)
        todoService.addTodo(todo);

        // Formular zurücksetzen
        titleField.clear();
        textField.clear();
    }
        
    private void backToOverview(){
        UI.getCurrent().navigate("todo-liste"); 
    }
}
