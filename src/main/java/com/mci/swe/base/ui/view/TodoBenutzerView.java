package com.mci.swe.base.ui.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.security.PermitAll;
import com.mci.swe.models.BenutzerModel;
import com.mci.swe.services.BenutzerService;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.notification.Notification;

@Route(value = "todo-benutzer", layout = MainLayout.class)
@PageTitle("Benutzer")
@PermitAll
public class TodoBenutzerView extends Main {
    private final TextField nameField = new TextField("Nachname");
    private final TextField vornameField = new TextField("Vorname");
    private final EmailField emailField = new EmailField("E-Mail");
    private final PasswordField passwordField = new PasswordField("Passwort");
    private final ComboBox<String> companyField = new ComboBox<>("Firma");

    
    Checkbox adminCheckbox = new Checkbox("Admin");
    Checkbox mitarbeiterCheckbox = new Checkbox("Mitarbeiter");
    Checkbox kundeCheckbox = new Checkbox("Kunde");
    private final Button addButton = new Button("Benutzer hinzufügen");
    private final Button editButton = new Button("Bearbeiten");
    private final Button deleteButton = new Button("Löschen");

    private final Grid<BenutzerModel> userGrid = new Grid<>(BenutzerModel.class);
    private final BenutzerService benutzerService = new BenutzerService();
    
    private final Button returnToOverview = new Button("Zurück");

    private BenutzerModel selectedUser;
    
       public TodoBenutzerView() {
        setSizeFull();
        configureForm();
        configureGrid();
        configureLayout();
        updateGrid();
        addListeners();
    }
       
       private void configureForm() {
        companyField.setItems("Firma A", "Firma B", "Firma C");

        nameField.setWidthFull();
        companyField.setWidthFull();
        emailField.setWidthFull();
        passwordField.setWidthFull();

        addButton.setWidthFull();
        editButton.setWidthFull();
        deleteButton.setWidthFull();
    }
       
       private void configureGrid() {
        userGrid.setColumns("nachname", "vorname", "firma", "email");
        userGrid.setWidthFull();
        userGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
    }
       
       
    private void configureLayout() {
        VerticalLayout formLayout = new VerticalLayout(nameField, vornameField, emailField, passwordField,companyField,
                adminCheckbox,mitarbeiterCheckbox,kundeCheckbox, addButton, editButton, deleteButton);
        formLayout.setWidth("400px");

        VerticalLayout gridLayout = new VerticalLayout(userGrid, returnToOverview);
         gridLayout.setSizeFull();
        userGrid.setSizeFull();
        returnToOverview.setWidthFull();

        HorizontalLayout mainLayout = new HorizontalLayout(formLayout, gridLayout);
        mainLayout.setSizeFull();
        add(mainLayout);
    }
    
        private void addListeners() {
        addButton.addClickListener(e -> {
          int adminInt = adminCheckbox.getValue()? 1 : 0;
          int mitarbeiterInt = mitarbeiterCheckbox.getValue()? 1 : 0;
          int kundeInt = kundeCheckbox.getValue()? 1 : 0;
         BenutzerModel user = new BenutzerModel(nameField.getValue(), vornameField.getValue(),
                    emailField.getValue(), passwordField.getValue(),companyField.getValue(), 
                 adminInt ,mitarbeiterInt, kundeInt);
            benutzerService.addUser(user);
            clearForm();
            updateGrid();
        });
        
        editButton.addClickListener(e -> {
            if (selectedUser != null) {
                selectedUser.setNachname(nameField.getValue());
                selectedUser.setVorname(vornameField.getValue());
                selectedUser.setFirma(companyField.getValue()); 
                selectedUser.setEmail(emailField.getValue());
                selectedUser.setPassword(passwordField.getValue());
                
                int adminInt = adminCheckbox.getValue()? 1 : 0;
                int mitarbeiterInt = mitarbeiterCheckbox.getValue()? 1 : 0;
                int kundeInt = kundeCheckbox.getValue()? 1 : 0;
                
                BenutzerModel user = new BenutzerModel(selectedUser.getId(),nameField.getValue(), vornameField.getValue(),
                    emailField.getValue(), passwordField.getValue(),companyField.getValue(),adminInt ,mitarbeiterInt, kundeInt);
                benutzerService.editUser(user);
                updateGrid();
                clearForm();
            }
        });
        
        deleteButton.addClickListener(e -> {
            if (selectedUser != null) {
                benutzerService.deleteUser(selectedUser);
                selectedUser = null;
                clearForm();
                updateGrid();
            }
        });
        
        returnToOverview.addClickListener(e -> {
            UI.getCurrent().navigate("liste");  
        });
        
        userGrid.asSingleSelect().addValueChangeListener(e -> {
            selectedUser = e.getValue();
            if (selectedUser != null) {
                nameField.setValue(selectedUser.getNachname());
                vornameField.setValue(selectedUser.getVorname());
                companyField.setValue(selectedUser.getFirma());
                emailField.setValue(selectedUser.getEmail());
                mitarbeiterCheckbox.setValue(false);
                adminCheckbox.setValue(false);
                kundeCheckbox.setValue(false);

                if(selectedUser.getIstAdmin()==1){
                    adminCheckbox.setValue(true);
                } else if(selectedUser.getIstMitarbeiter()==1){
                     mitarbeiterCheckbox.setValue(false);
                } else{
                     kundeCheckbox.setValue(true);
                }
            }
        });
        
        adminCheckbox.addValueChangeListener(event -> {
            if (event.getValue()) {
                mitarbeiterCheckbox.setValue(false);
                kundeCheckbox.setValue(false);
            }
        });

        mitarbeiterCheckbox.addValueChangeListener(event -> {
            if (event.getValue()) {
                adminCheckbox.setValue(false);
                kundeCheckbox.setValue(false);
            }
        });

        kundeCheckbox.addValueChangeListener(event -> {
            if (event.getValue()) {
                adminCheckbox.setValue(false);
                mitarbeiterCheckbox.setValue(false);
            }
        }); 
    }
        
    private void clearForm() {
        nameField.clear();
        vornameField.clear();
        companyField.clear();
        emailField.clear();
        passwordField.clear();
    }

    private void updateGrid() {
        userGrid.setItems(benutzerService.getAllUsers());
    }
}