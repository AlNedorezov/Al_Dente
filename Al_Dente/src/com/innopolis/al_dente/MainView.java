package com.innopolis.al_dente;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;

public class MainView {

    private static final String DEFAULT_NAME = "Untitled";
    private static final int MAX_TABS_COUNT = 20;

    private static MainView instance;
    private int TEXT_AREA_INDEX = 0;

    private MainView(){}

    public static MainView getInstance(){

        if (instance == null){

            instance = new MainView();
        }

        return instance;
    }

    public void updateCurrentTabHeader(Parent parent, String header){

        Tab tab = getCurrentTab(parent);

        Label label = (Label) tab.getGraphic();

        label.setText(header);
    }

    public void initializeTabPane(Parent parent, Scene scene){

        TabPane tabPane = (TabPane) parent.lookup("#tabPane");
        tabPane.setPrefWidth(scene.getWidth());
        tabPane.setPrefHeight(scene.getHeight());


        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {

                setCurrentTab(parent, newTab);
            }
        });
    }

    public String getCurrentTabContent(Parent parent){

        Tab tab = getCurrentTab(parent);

        String content = null;

        HBox hbox = (HBox) tab.getContent();

        TextArea textArea = (TextArea) hbox.getChildren().get(TEXT_AREA_INDEX);

        content =  textArea.getText();

        return content;
    }

    private Tab getCurrentTab(Parent parent){

        TabPane tabPane = (TabPane) parent.lookup("#tabPane");

        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();

        Tab tab = selectionModel.getSelectedItem();

        return tab;
    }

    private void setCurrentTab(Parent parent, Tab tab ){

        TabPane tabPane = (TabPane) parent.lookup("#tabPane");

        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();

        selectionModel.select(tab);
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
        tab.setId(String.valueOf(count));

        final Label label = new Label(header);

        tab.setGraphic(label);


        TextArea textArea = new TextArea(content);
        textArea.prefWidthProperty().bind(tabPane.widthProperty());
        textArea.prefHeightProperty().bind(tabPane.heightProperty());


        hbox.getChildren().add(textArea);
        hbox.setAlignment(Pos.CENTER);

        tab.setContent(hbox);

        setCurrentTab(parent, tab);

        tab.setClosable(true);

        tabPane.getTabs().add(tab);

        label.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.getButton() == MouseButton.MIDDLE) {

                    System.out.println();
                }
            }
        });

    }
}
