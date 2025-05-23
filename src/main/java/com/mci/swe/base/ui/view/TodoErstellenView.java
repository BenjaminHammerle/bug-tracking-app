package com.mci.swe.base.ui.view;

import java.util.List;
import java.util.Optional;

import com.mci.swe.models.TodoModel;
import com.mci.swe.services.TodoService;
import com.mci.swe.services.BenutzerService;
import com.mci.swe.models.BenutzerModel;
import com.mci.swe.security.SecurityService;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.userdetails.UserDetails;

@Route(value = "todo-erstellen", layout = MainLayout.class)
@PageTitle("Todo erstellen")
@PermitAll
public class TodoErstellenView extends Main {

    private final SecurityService securityService;
    private final BenutzerService benutzerService = new BenutzerService();
    private final TodoService    todoService       = new TodoService();

    private final TextField      titleField       = new TextField("Titel");
    private final TextArea       descriptionField = new TextArea("Beschreibung");
    private final ComboBox<String> prioField      = new ComboBox<>("Priorität");

    private final Button saveButton   = new Button("Speichern");
    private final Button cancelButton = new Button("Abbrechen");

    public TodoErstellenView(SecurityService securityService) {
        this.securityService = securityService;
        setSizeFull();
        configureFields();
        configureButtons();
        buildLayout();
    }

    private void configureFields() {
        prioField.setItems("LOW", "MEDIUM", "HIGH");
        titleField.setWidth("400px");
        descriptionField.setWidth("400px");
        descriptionField.setHeight("150px");
        prioField.setWidth("400px");
    }

    private void configureButtons() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

        saveButton.setWidth("50%");
        cancelButton.setWidth("50%");

        saveButton.addClickListener(e -> onSave());
        cancelButton.addClickListener(e -> UI.getCurrent().navigate("/"));
    }

    private void buildLayout() {
        H3 header = new H3("Neues Todo erstellen");

        FormLayout form = new FormLayout();
        form.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("600px", 2)
        );
        form.add(titleField, descriptionField, prioField);
        form.setWidth("420px");

        Div buttons = new Div(saveButton, cancelButton);
        buttons.getStyle()
               .set("display", "flex")
               .set("gap", "0.5rem");
        buttons.setWidth("420px");

        Div card = new Div(header, form, buttons);
        card.getStyle()
            .set("background-color", "#e0f7fa")
            .set("padding", "1rem")
            .set("border-radius", "8px")
            .set("box-shadow", "0 2px 6px rgba(0,0,0,0.1)");

        VerticalLayout outer = new VerticalLayout(card);
        outer.setSizeFull();
        outer.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        outer.setAlignItems(FlexComponent.Alignment.CENTER);

        add(outer);
    }

    private void onSave() {
        String titel = titleField.getValue().trim();
        if (titel.isEmpty()) {
            Notification.show("Bitte Titel eingeben.", 2000, Notification.Position.MIDDLE);
            return;
        }
        String beschreibung = descriptionField.getValue().trim();
        String prio = prioField.getValue();
        if (prio == null) {
            Notification.show("Bitte Priorität auswählen.", 2000, Notification.Position.MIDDLE);
            return;
        }

        // Model füllen
        TodoModel todo = new TodoModel();
        todo.setTitel(titel);
        todo.setBeschreibung(beschreibung);
        todo.setPrio(prio);

        // current user ermitteln und owner_id setzen
        UserDetails user = securityService.getAuthenticatedUser();
        String email = user.getUsername();
        System.out.println("[DEBUG] Angemeldete E-Mail: " + email);

        List<BenutzerModel> allUsers = benutzerService.getAllUsers();
        Optional<BenutzerModel> found = allUsers.stream()
            .filter(u -> u.getEmail().equalsIgnoreCase(email))
            .findFirst();

        if (found.isEmpty()) {
            // Fallback: nur lokaler Teil vor '@'
            String prefix = email.contains("@")
                ? email.substring(0, email.indexOf("@"))
                : email;
            found = allUsers.stream()
                .filter(u -> {
                    String ue = u.getEmail();
                    if (ue == null || !ue.contains("@")) return false;
                    String uprefix = ue.substring(0, ue.indexOf("@"));
                    return uprefix.equalsIgnoreCase(prefix);
                })
                .findFirst();
        }

        BenutzerModel me = found.orElseThrow(() ->
            new IllegalStateException("Angemeldeter Benutzer nicht gefunden")
        );

        System.out.println("[DEBUG] Gefundener Benutzer: E-Mail=" + me.getEmail() +
                           " (ID=" + me.getId() + ")");

        todo.setOwner_id(me.getId());

        // abschicken
        todoService.addTodo(todo);
        Notification.show("Todo angelegt!", 2000, Notification.Position.TOP_CENTER);

        // Formular zurücksetzen
        titleField.clear();
        descriptionField.clear();
        prioField.clear();

        // navigiere zurück ins Dashboard
        UI.getCurrent().navigate("");
    }
}
