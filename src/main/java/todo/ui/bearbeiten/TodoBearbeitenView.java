package todo.ui.bearbeiten;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("todo/bearbeiten")
@PageTitle("Todo bearbeiten")

public class TodoBearbeitenView extends Main {
       public TodoBearbeitenView() {
        add(new H2("Bearbeiten View"));
    }
}
