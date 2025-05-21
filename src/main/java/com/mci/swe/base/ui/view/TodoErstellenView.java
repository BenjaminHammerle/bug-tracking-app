package com.mci.swe.base.ui.view;

import com.mci.swe.models.TodoModel;
import com.mci.swe.services.TodoService;
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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "todo-erstellen", layout = MainLayout.class)
@PageTitle("Todo erstellen")
@PermitAll
public class TodoErstellenView extends Main {

    private final TodoService todoService = new TodoService();

    private final TextField titleField = new TextField("Titel");
    private final TextArea descriptionField = new TextArea("Beschreibung");
    private final ComboBox<String> prioField = new ComboBox<>("Priorität");

    private final Button saveButton   = new Button("Speichern");
    private final Button cancelButton = new Button("Abbrechen");

    public TodoErstellenView() {
        setSizeFull();
        configureFields();
        configureButtons();
        buildLayout();
    }

    private void configureFields() {
        prioField.setItems("LOW", "MEDIUM", "HIGH");
        titleField.setWidthFull();
        descriptionField.setWidthFull();
        descriptionField.setHeight("150px");
        prioField.setWidthFull();
    }

    private void configureButtons() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

        saveButton.setWidth("50%");
        cancelButton.setWidth("50%");

        saveButton.addClickListener(e -> onSave());
        cancelButton.addClickListener(e -> UI.getCurrent().navigate("todo-liste"));
    }

    private void buildLayout() {
        // Überschrift
        H3 header = new H3("Neues Todo erstellen");

        // FormLayout für die Eingabefelder
        FormLayout form = new FormLayout();
        form.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("600px", 2)
        );
        form.add(titleField, descriptionField, prioField);
        form.setWidth("400px");

        // Button-Leiste
        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        buttons.setWidth("400px");
        buttons.setSpacing(true);

        // Container mit Pastell-Hintergrund
        Div card = new Div();
        card.getStyle()
            .set("background-color", "#e0f7fa")
            .set("padding", "1rem")
            .set("border-radius", "8px")
            .set("box-shadow", "0 2px 6px rgba(0,0,0,0.1)");
        card.add(header, form, buttons);

        // Zentrierter Außen-Container
        VerticalLayout outer = new VerticalLayout(card);
        outer.setSizeFull();
        outer.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        outer.setAlignItems(FlexComponent.Alignment.CENTER);

        add(outer);
    }

    private void onSave() {
        String titel = titleField.getValue().trim();
        String beschreibung = descriptionField.getValue().trim();
        String prio = prioField.getValue();

        if (titel.isEmpty()) {
            Notification.show("Bitte Titel eingeben.", 2000, Notification.Position.MIDDLE);
            return;
        }
        if (prio == null) {
            Notification.show("Bitte Priorität auswählen.", 2000, Notification.Position.MIDDLE);
            return;
        }

        TodoModel todo = new TodoModel();
        todo.setTitel(titel);
        todo.setBeschreibung(beschreibung);
        todo.setPrio(prio);

        todoService.addTodo(todo);
        Notification.show("Todo angelegt!", 2000, Notification.Position.TOP_CENTER);

        // Formular zurücksetzen
        titleField.clear();
        descriptionField.clear();
        prioField.clear();
    }
}
