package todo.ui.erstellen;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.annotation.security.PermitAll;
import todo.models.TodoModel;

@Route("todo/erstellen")
@PageTitle("Todo erstellen")
@PermitAll
public class TodoErstellenView  extends Main {
    
    private final TextField titleField = new TextField("Titel");
    private final TextArea textField = new TextArea("Text");
    private final TextField createdAtField = new TextField("Erstellt am");

    private final Button saveButton = new Button("Todo erstellen");
    private final Button returnButton = new Button("Zurück");
    
    
       public TodoErstellenView() {
        setSizeFull();

    // Überschrift
    H3 header = new H3("Neues Todo erstellen");

    // createdAt Feld (nicht editierbar, aktuelles Datum/Uhrzeit)
    createdAtField.setReadOnly(true);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    createdAtField.setValue(LocalDateTime.now().format(formatter));

    // TextArea für Beschreibung
    textField.setWidth("400px");
    textField.setHeight("150px");

    // Optional: Felder in fester Breite
    titleField.setWidth("400px");
    createdAtField.setWidth("400px");
    saveButton.setWidth("400px");
    
    // Optional: Felder in fester Breite
    titleField.setWidth("400px");
    createdAtField.setWidth("400px");
    returnButton.setWidth("400px");

    // Layout mit vertikaler Ausrichtung
    VerticalLayout formLayout = new VerticalLayout();
    formLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
    formLayout.setSpacing(true);
    formLayout.add(header, titleField, textField, createdAtField, saveButton, returnButton);

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

        if (title == null || title.isBlank()) {
            Notification.show("Titel darf nicht leer sein", 3000, Notification.Position.MIDDLE);
            return;
        }

        LocalDateTime createdAt = LocalDateTime.now();

        TodoModel todo = new TodoModel();
        todo.titel = title;
        todo.text = text;
        todo.erstelltAm = createdAt;

        // TODO: Hier speichern (z.B. DB, Service, Liste...)

        Notification.show("Todo mit ID " + 1 + " erstellt!", 3000, Notification.Position.MIDDLE);

        // Formular zurücksetzen
        titleField.clear();
        textField.clear();
    }
        
    private void backToOverview(){
        UI.getCurrent().navigate("liste"); 
    }
}
