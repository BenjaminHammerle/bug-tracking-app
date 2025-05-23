package com.mci.swe.base.ui.view;

import com.mci.swe.models.BenutzerModel;
import com.mci.swe.services.BenutzerService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import com.vaadin.flow.component.notification.Notification;

import java.util.List;

@Route(value = "todo-benutzer", layout = MainLayout.class)
@PageTitle("Benutzerverwaltung")
@RolesAllowed("ADMIN")
public class TodoBenutzerView extends Main {

    private final TextField     nameField        = new TextField("Nachname");
    private final TextField     vornameField     = new TextField("Vorname");
    private final EmailField    emailField       = new EmailField("E-Mail");
    private final PasswordField passwordField    = new PasswordField("Passwort");
    private final ComboBox<String> companyField   = new ComboBox<>("Firma");

    private final Checkbox adminCheckbox        = new Checkbox("Admin");
    private final Checkbox mitarbeiterCheckbox  = new Checkbox("Mitarbeiter");
    private final Checkbox kundeCheckbox        = new Checkbox("Kunde");

    private final Button addButton     = new Button("Hinzufügen");
    private final Button editButton    = new Button("Bearbeiten");
    private final Button deleteButton  = new Button("Löschen");
    private final Button returnButton  = new Button("Zur Übersicht");

    private final Grid<BenutzerModel> userGrid = new Grid<>(BenutzerModel.class, false);
    private final BenutzerService benutzerService = new BenutzerService();

    private BenutzerModel selectedUser;

    public TodoBenutzerView() {
        setSizeFull();
        configureForm();
        configureGrid();
        layoutComponents();
        bindEvents();
        refreshGrid();
    }

    private void configureForm() {
        companyField.setItems("Firma A", "Firma B", "Firma C");
        nameField.setWidthFull();
        vornameField.setWidthFull();
        emailField.setWidthFull();
        passwordField.setWidthFull();
        companyField.setWidthFull();

        // Buttons
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        returnButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
    }

    private void configureGrid() {
        userGrid.addColumn(BenutzerModel::getNachname).setHeader("Nachname");
        userGrid.addColumn(BenutzerModel::getVorname).setHeader("Vorname");
        userGrid.addColumn(BenutzerModel::getFirma).setHeader("Firma");
        userGrid.addColumn(BenutzerModel::getEmail).setHeader("E-Mail");
        userGrid.setSizeFull();
        userGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);
        userGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
    }

    private void layoutComponents() {
        // FormContainer mit Pastellhintergrund
        Div formContainer = new Div();
        formContainer.getStyle()
            .set("background-color", "var(--lumo-base-color)")
            .set("padding", "1rem")
            .set("border-radius", "8px")
            .set("box-shadow", "0 2px 6px rgba(0,0,0,0.1)");

        FormLayout form = new FormLayout();
        form.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("500px", 2)
        );
        form.add(nameField, vornameField, emailField, passwordField, companyField);

        // Checkbox-Gruppe
        HorizontalLayout roles = new HorizontalLayout(adminCheckbox, mitarbeiterCheckbox, kundeCheckbox);
        roles.setSpacing(true);

        // Button-Bar
        HorizontalLayout buttons = new HorizontalLayout(addButton, editButton, deleteButton);
        buttons.setSpacing(true);
        buttons.setPadding(true);

        formContainer.add(new H2("Benutzer anlegen / bearbeiten"), form, roles, buttons, returnButton);
        formContainer.setWidth("350px");

        // GridContainer
        VerticalLayout gridContainer = new VerticalLayout(userGrid);
        gridContainer.setSizeFull();
        gridContainer.getStyle()
            .set("background-color", "var(--lumo-contrast-5pct)")
            .set("padding", "0.5rem")
            .set("border-radius", "8px");

        // Hauptlayout
        HorizontalLayout main = new HorizontalLayout(formContainer, gridContainer);
        main.setSizeFull();
        main.setFlexGrow(1, gridContainer);
        main.setFlexGrow(0, formContainer);
        main.setPadding(true);
        main.setSpacing(true);

        add(main);
    }

    private void bindEvents() {
        addButton.addClickListener(e -> {
            if (validateForm()) {
                int a = adminCheckbox.getValue() ? 1 : 0;
                int m = mitarbeiterCheckbox.getValue() ? 1 : 0;
                int k = kundeCheckbox.getValue() ? 1 : 0;
                BenutzerModel u = new BenutzerModel(
                    nameField.getValue(),
                    vornameField.getValue(),
                    emailField.getValue(),
                    passwordField.getValue(),
                    companyField.getValue(),
                    a, m, k
                );
                benutzerService.addUser(u);
                clearForm();
                refreshGrid();
                Notification.show("Benutzer hinzugefügt", 2000, Notification.Position.TOP_CENTER);
            }
        });

        editButton.addClickListener(e -> {
            if (selectedUser != null && validateForm()) {
                int a = adminCheckbox.getValue() ? 1 : 0;
                int m = mitarbeiterCheckbox.getValue() ? 1 : 0;
                int k = kundeCheckbox.getValue() ? 1 : 0;
                BenutzerModel u = new BenutzerModel(
                    selectedUser.getId(),
                    nameField.getValue(),
                    vornameField.getValue(),
                    emailField.getValue(),
                    passwordField.getValue(),
                    companyField.getValue(),
                    a, m, k
                );
                benutzerService.editUser(u);
                clearForm();
                refreshGrid();
                Notification.show("Benutzer aktualisiert", 2000, Notification.Position.TOP_CENTER);
            }
        });

        deleteButton.addClickListener(e -> {
            if (selectedUser != null) {
                benutzerService.deleteUser(selectedUser);
                clearForm();
                refreshGrid();
                Notification.show("Benutzer gelöscht", 2000, Notification.Position.TOP_CENTER);
            }
        });

        returnButton.addClickListener(e -> UI.getCurrent().navigate(""));

        userGrid.asSingleSelect().addValueChangeListener(e -> {
            selectedUser = e.getValue();
            if (selectedUser != null) {
                nameField.setValue(selectedUser.getNachname());
                vornameField.setValue(selectedUser.getVorname());
                emailField.setValue(selectedUser.getEmail());
                passwordField.setValue(""); // Passwort nicht anzeigen
                companyField.setValue(selectedUser.getFirma());
                adminCheckbox.setValue(selectedUser.getIstAdmin() == 1);
                mitarbeiterCheckbox.setValue(selectedUser.getIstMitarbeiter() == 1);
                kundeCheckbox.setValue(selectedUser.getIstKunde() == 1);
            }
        });
    }

    private boolean validateForm() {
        if (nameField.isEmpty() || vornameField.isEmpty() ||
            emailField.isEmpty() || companyField.isEmpty()) {
            Notification.show("Bitte alle Pflichtfelder ausfüllen.", 2000, Notification.Position.TOP_CENTER);
            return false;
        }
        return true;
    }

    private void clearForm() {
        nameField.clear();
        vornameField.clear();
        emailField.clear();
        passwordField.clear();
        companyField.clear();
        adminCheckbox.clear();
        mitarbeiterCheckbox.clear();
        kundeCheckbox.clear();
        selectedUser = null;
    }

    private void refreshGrid() {
        List<BenutzerModel> users = benutzerService.getAllUsers();
        userGrid.setItems(users);
    }
}
