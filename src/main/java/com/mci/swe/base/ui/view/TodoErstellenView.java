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
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.userdetails.UserDetails;

@Route(value = "todo-erstellen", layout = MainLayout.class)
@PageTitle("Neues Todo erstellen")
@PermitAll
public class TodoErstellenView extends Main {

    private final SecurityService securityService;
    private final BenutzerService benutzerService = new BenutzerService();
    private final TodoService     todoService       = new TodoService();

    private final TextField        titleField       = new TextField("Titel");
    private final TextArea         descriptionField = new TextArea("Beschreibung");
    private final ComboBox<String> prioField        = new ComboBox<>("Priorität");

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
        titleField.setWidthFull();
        descriptionField.setWidthFull();
        descriptionField.setHeight("150px");
        prioField.setItems("LOW", "MEDIUM", "HIGH");
        prioField.setWidthFull();
    }

    private void configureButtons() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
        saveButton.setWidth("120px");
        cancelButton.setWidth("120px");
        saveButton.addClickListener(e -> onSave());
        cancelButton.addClickListener(e -> UI.getCurrent().navigate("/"));
    }

    private void buildLayout() {
        // Card container
        Div card = new Div();
        card.getStyle()
            .set("max-width", "600px")
            .set("margin", "auto")
            .set("padding", "2rem")
            .set("border-radius", "8px")
            .set("background-color", "#f0f9ff")
            .set("box-shadow", "0 4px 12px rgba(0,0,0,0.1)");

        // Header
        H2 header = new H2("Neues Todo erstellen");
        header.getStyle().set("margin-bottom", "1.5rem");
        card.add(header);

        // Form
        FormLayout form = new FormLayout();
        form.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("500px", 1)
        );
        form.add(titleField, descriptionField, prioField);
        form.setWidthFull();
        form.getStyle().set("column-gap", "1rem").set("row-gap", "1rem");
        card.add(form);

        // Buttons layout
        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        buttons.setWidthFull();
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttons.setSpacing(true);
        buttons.getStyle().set("margin-top", "1.5rem");
        card.add(buttons);

        // Outer layout
        VerticalLayout outer = new VerticalLayout(card);
        outer.setSizeFull();
        outer.setAlignItems(FlexComponent.Alignment.CENTER);
        outer.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
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

        TodoModel todo = new TodoModel();
        todo.setTitel(titel);
        todo.setBeschreibung(beschreibung);
        todo.setPrio(prio);

        UserDetails user = securityService.getAuthenticatedUser();
        String email = user.getUsername();
        List<BenutzerModel> allUsers = benutzerService.getAllUsers();
        Optional<BenutzerModel> found = allUsers.stream()
            .filter(u -> u.getEmail().equalsIgnoreCase(email))
            .findFirst();
        if (found.isEmpty()) {
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
        todo.setOwner_id(me.getId());

        todoService.addTodo(todo);
        Notification.show("Todo erfolgreich angelegt!", 2000, Notification.Position.TOP_CENTER);

        titleField.clear();
        descriptionField.clear();
        prioField.clear();

        UI.getCurrent().navigate("");
    }
}
