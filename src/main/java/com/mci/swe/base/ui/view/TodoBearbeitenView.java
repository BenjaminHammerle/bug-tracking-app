package com.mci.swe.base.ui.view;

import com.mci.swe.models.TodoModel;
import com.mci.swe.models.TodoSchrittModel;
import com.mci.swe.models.BenutzerModel;
import com.mci.swe.services.SchrittService;
import com.mci.swe.services.TodoService;
import com.mci.swe.services.BenutzerService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Route(value = "todo-bearbeiten", layout = MainLayout.class)
@PageTitle("Todo bearbeiten")
@PermitAll
public class TodoBearbeitenView extends Main implements HasUrlParameter<String> {

    private final TodoService todoService = new TodoService();
    private final SchrittService schrittService = new SchrittService();
    private final BenutzerService benutzerService = new BenutzerService();

    private TodoModel ticket;
    private String ticketId;
    private final List<TodoSchrittModel> steps = new ArrayList<>();

    // UI-Komponenten
    private final FormLayout                ticketForm     = new FormLayout();
    private final Grid<TodoSchrittModel>    stepListGrid   = new Grid<>(TodoSchrittModel.class, false);
    private final FormLayout                stepDetailForm = new FormLayout();
    private final ComboBox<BenutzerModel>   assigneeCombo  = new ComboBox<>("Assignee");
    private final ComboBox<String>          statusField    = new ComboBox<>("Status");
    private final TextArea                  commentField   = new TextArea("Kommentar");

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        ticketId = parameter;
        ticket    = todoService.findById(parameter);
        steps.clear();
        steps.addAll(schrittService.getSchritte(parameter));
        buildLayout();
    }

    private void buildLayout() {
        removeAll();
        setSizeFull();

        // --- Ticket-Details links ---
        buildTicketForm();
        ticketForm.getElement().getStyle()
            .set("background-color", "#e0f7fa")  // hellblau
            .set("padding", "1rem")
            .set("border-radius", "8px");
        ticketForm.setWidth("35%");

        // --- Schritt-Liste & Detail rechts ---
        buildStepListGrid();
        buildStepDetailForm();

        VerticalLayout newStepLayout = buildNewStepForm();
        newStepLayout.getElement().getStyle()
            .set("background-color", "var(--lumo-contrast-5pct)")  // hellgrau
            .set("padding", "1rem")
            .set("border-radius", "8px");

        // SplitLayout: links Ticket, rechts Liste/Detail + Neu-Form
        SplitLayout mainSplit = new SplitLayout();
        mainSplit.setSizeFull();
        mainSplit.addToPrimary(ticketForm);

        VerticalLayout right = new VerticalLayout();
        right.setSizeFull();
        right.setPadding(false);
        right.setSpacing(false);

        SplitLayout rightSplit = new SplitLayout();
        rightSplit.setSizeFull();
        rightSplit.addToPrimary(stepListGrid);
        rightSplit.addToSecondary(stepDetailForm);
        rightSplit.setSplitterPosition(7);

        right.add(rightSplit, newStepLayout);
        right.expand(rightSplit);

        mainSplit.addToSecondary(right);
        mainSplit.setSplitterPosition(35);

        add(mainSplit);
    }

    private VerticalLayout buildNewStepForm() {
        // ComboBox & TextArea konfigurieren
        statusField.setItems("OPEN","IN_PROGRESS","RESOLVED","CLOSED");
        statusField.setWidthFull();
        List<BenutzerModel> users = benutzerService.getAllUsers();
        assigneeCombo.setItems(users);
        assigneeCombo.setItemLabelGenerator(u -> u.getVorname() + " " + u.getNachname());
        assigneeCombo.setWidthFull();
        commentField.setWidthFull();
        commentField.setHeight("120px");

        // Button mit Theme‐Variants
        Button add = new Button("Neuen Schritt anlegen", ev -> {
            TodoSchrittModel st = new TodoSchrittModel();
            BenutzerModel b = assigneeCombo.getValue();
            if (b != null) {
                st.setAssignee_id(b.getId());
                st.setAssignee_name(b.getVorname() + " " + b.getNachname());
            }
            st.setStatus_change(statusField.getValue());
            st.setKommentar(commentField.getValue());
            schrittService.addSchritt(st, ticketId);

            // Liste neu laden
            steps.clear();
            steps.addAll(schrittService.getSchritte(ticketId));
            stepListGrid.getDataProvider().refreshAll();

            // Formular zurücksetzen
            commentField.clear();
            assigneeCombo.clear();
            statusField.clear();
        });
        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        // Layout zusammenbauen
        VerticalLayout form = new VerticalLayout(
            new Span("Neuen Schritt anlegen"),
            assigneeCombo,
            statusField,
            commentField,
            add
        );
        form.setPadding(true);
        form.setSpacing(true);
        form.setWidthFull();
        return form;
    }

    private void buildTicketForm() {
        ticketForm.removeAll();
        ticketForm.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0",    1),
            new FormLayout.ResponsiveStep("600px", 2)
        );
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ticketForm.addFormItem(new H2("Ticket #" + ticket.getId()), " ");
        ticketForm.addFormItem(new Div(new Span(ticket.getTitel())),          "Titel");
        ticketForm.addFormItem(new Div(new Span(ticket.getFirma())),          "Firma");
        ticketForm.addFormItem(new Div(new Span(ticket.getBeschreibung())),   "Beschreibung");
        // Prio als Badge
        ticketForm.addFormItem(new Div(createPrioBadge(ticket.getPrio())),    "Priorität");
        // Status als Badge
        ticketForm.addFormItem(new Div(createStatusBadge(ticket.getStatus())), "Status");
        ticketForm.addFormItem(
            new Div(new Span(ticket.getErstellt_am().format(fmt))),
            "Erstellt am"
        );
    }

    private void buildStepListGrid() {
        stepListGrid.removeAllColumns();
        // Nur ID
        stepListGrid.addColumn(TodoSchrittModel::getId)
            .setHeader("ID")
            .setAutoWidth(true);
        stepListGrid.setItems(steps);
        stepListGrid.setHeightFull();

        stepListGrid.asSingleSelect().addValueChangeListener(ev -> {
            TodoSchrittModel selected = ev.getValue();
            if (selected != null) {
                showStepDetails(selected);
            } else {
                stepDetailForm.removeAll();
            }
        });
    }

    private void buildStepDetailForm() {
        stepDetailForm.removeAll();
        stepDetailForm.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0",    1),
            new FormLayout.ResponsiveStep("600px", 2)
        );
        // wird über showStepDetails() befüllt
    }

    private void showStepDetails(TodoSchrittModel step) {
        stepDetailForm.removeAll();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        stepDetailForm.addFormItem(new H2("Schritt #" + step.getId()), " ");
        stepDetailForm.addFormItem(
            new Div(new Span(step.getAssignee_name())),
            "Assignee"
        );
        stepDetailForm.addFormItem(
            new Div(new Span(step.getKommentar())),
            "Kommentar"
        );
        // Status als Badge
        stepDetailForm.addFormItem(
            new Div(createStatusBadge(step.getStatus_change())),
            "Status"
        );
        stepDetailForm.addFormItem(
            new Div(new Span(step.getErstelltAm().format(fmt))),
            "Erstellt am"
        );
    }

    // Hilfs‐Methoden für farbige Badges
    private Span createStatusBadge(String status) {
        Span badge = new Span(status);
        badge.getElement().getThemeList().add("badge");
        switch (status) {
            case "OPEN":        badge.getElement().getThemeList().add("contrast"); break;
            case "IN_PROGRESS": badge.getElement().getThemeList().add("primary");  break;
            case "RESOLVED":    badge.getElement().getThemeList().add("success");  break;
            case "CLOSED":      badge.getElement().getThemeList().add("error");    break;
        }
        return badge;
    }

    private Span createPrioBadge(String prio) {
        Span badge = new Span(prio);
        badge.getElement().getThemeList().add("badge");
        switch (prio) {
            case "LOW":    badge.addClassName("badge-success");  break;
            case "MEDIUM": badge.addClassName("badge-contrast"); break;
            case "HIGH":   badge.addClassName("badge-error");    break;
        }
        return badge;
    }
}
