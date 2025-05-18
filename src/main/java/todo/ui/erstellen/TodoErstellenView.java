package todo.ui.erstellen;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("todo/erstellen")
@PageTitle("Todo erstellen")

public class TodoErstellenView  extends Main {
       public TodoErstellenView() {
        add(new H2("First View"));
    }
}
