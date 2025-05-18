package todo.ui.benutzer;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("benutzer")
@PageTitle("Benutzer")

public class TodoBenutzerView extends Main {
       public TodoBenutzerView() {
        add(new H2("Benutzer View"));
    }
}