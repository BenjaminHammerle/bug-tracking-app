package todo.ui.liste;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("liste")
@PageTitle("Overview")

public class TodoListeView extends Main {
       public TodoListeView() {
        add(new H2("Übersicht View"));
    }
}