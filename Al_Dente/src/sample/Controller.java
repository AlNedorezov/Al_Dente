package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;

public class Controller {
    public void handleAboutAction(ActionEvent event) {

        Object obj = event.getSource();

        if (obj instanceof MenuItem){

            MenuItem menuItem = (MenuItem) obj;
            String id = menuItem.getId();
            System.out.println();
        }
    }
}
