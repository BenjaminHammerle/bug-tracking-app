package todo.ui.benutzer;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import todo.models.BenutzerModel;
import todo.ui.services.BenutzerService;

@Route("benutzer")
@PageTitle("Benutzer")

public class TodoBenutzerView extends Main {
    private final TextField nameField = new TextField("Name");
    private final ComboBox<String> companyField = new ComboBox<>("Firma");
    private final EmailField emailField = new EmailField("E-Mail");
    private final PasswordField passwordField = new PasswordField("Passwort");

    private final Button addButton = new Button("Benutzer hinzufügen");
    private final Button editButton = new Button("Bearbeiten");
    private final Button deleteButton = new Button("Löschen");

    private final Grid<BenutzerModel> userGrid = new Grid<>(BenutzerModel.class);
    private final List<BenutzerModel> userList = new ArrayList<>();
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
        userGrid.setColumns("nachname", "firma", "email");
        userGrid.setWidthFull();
        userGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
    }
       
       
    private void configureLayout() {
        VerticalLayout formLayout = new VerticalLayout(nameField, companyField, emailField, passwordField,
                addButton, editButton, deleteButton);
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
            BenutzerModel user = new BenutzerModel(nameField.getValue(), companyField.getValue(),
                    emailField.getValue(), passwordField.getValue());
            benutzerService.addUser(user);
            clearForm();
            updateGrid();
        });
        
        editButton.addClickListener(e -> {
            if (selectedUser != null) {
                selectedUser.setNachname(nameField.getValue());
                selectedUser.setFirma(companyField.getValue());
                selectedUser.setEmail(emailField.getValue());
                selectedUser.setPassword(passwordField.getValue());
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
                companyField.setValue(selectedUser.getFirma());
                emailField.setValue(selectedUser.getEmail());
                passwordField.setValue(selectedUser.getPassword());
            }
        });
    }
        
    private void clearForm() {
        nameField.clear();
        companyField.clear();
        emailField.clear();
        passwordField.clear();
    }

    private void updateGrid() {
        userGrid.setItems(benutzerService.getAllUsers());
    }
}