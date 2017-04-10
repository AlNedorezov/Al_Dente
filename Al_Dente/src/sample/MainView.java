package sample;


import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

import java.util.List;

public class MainView {

    private static final String DEFAULT_NAME = "Untitled";
    private static final int MAX_TABS_COUNT = 20;

    private static MainView instance;

    private MainView(){}

    public static MainView getInstance(){

        if (instance == null){

            instance = new MainView();
        }

        return instance;
    }

    public String getCurentTab(Parent parent){

        TabPane tabPane = (TabPane) parent.lookup("#tabPane");

        String content = null;

        List<Tab> listTabs = tabPane.getTabs();

        if (listTabs.size() > 0){

            Tab tab = listTabs.get(0);

            HBox hbox = (HBox) tab.getContent();

            TextArea textArea = (TextArea) hbox.getChildren().get(0);

            content =  textArea.getText();
        }

        return content;
    }

    public void createNewTab(Parent parent, String header,  String content){

        TabPane tabPane = (TabPane) parent.lookup("#tabPane");
        int count =  tabPane.getTabs().size();


        if (count >= MAX_TABS_COUNT){

            return;
        }

        if (header == null || header.isEmpty()){

            header = DEFAULT_NAME + count;
        }

       if (content == null){

            content = "";
       }

        count++;

        HBox hbox = new HBox();

        Tab tab = new Tab();
        tab.setText(header);


        TextArea textArea = new TextArea(content);
        textArea.prefWidthProperty().bind(tabPane.widthProperty());
        textArea.prefHeightProperty().bind(tabPane.heightProperty());


        hbox.getChildren().add(textArea);
        hbox.setAlignment(Pos.CENTER);

        tab.setContent(hbox);

        tabPane.getTabs().add(tab);

        System.out.println();
    }
}
