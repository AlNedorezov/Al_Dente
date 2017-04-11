package com.innopolis.al_dente;


import com.innopolis.al_dente.models.IAlertListner;
import com.innopolis.al_dente.models.TabTag;
import com.sun.javafx.font.freetype.HBGlyphLayout;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;

import java.util.Optional;

public class MainView {

    private static final String DEFAULT_NAME = "Untitled";
    private static final String UNSAVED_STATE = "(*)";
    private static final int MAX_TABS_COUNT = 20;
    final KeyCombination keyCloseTabCombination = new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN);
    final KeyCombination keyNewTabCombination = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
    private static IAlertListner alertListner;

    private static MainView instance;
    private int TEXT_AREA_INDEX = 0;

    private static  Parent parent;

    private MainView(){}

    public static MainView getInstance(Parent p, IAlertListner listner){

        if (instance == null){

            instance = new MainView();
        }

        parent = p;
        alertListner = listner;

        return instance;
    }

    public void initializeTabPane(Scene scene){

        TabPane tabPane = (TabPane) parent.lookup("#tabPane");
        tabPane.setPrefWidth(scene.getWidth());
        tabPane.setPrefHeight(scene.getHeight());


        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {

                setCurrentTab(newTab);
            }
        });

        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>()  {

            @Override
            public void handle(KeyEvent e) {

                if (keyNewTabCombination.match(e)) {

                    createNewTab(null, null);
                    TabTag item = new TabTag();
                    updateCurrentTab(item);
                    e.consume();
                }
            }
        });
    }

    public void updateCurrentTab(TabTag item){

        Tab tab = getCurrentTab();
        tab.setUserData(item);
    }

    public void updateCurrentTabHeader(String header){

        Tab tab = getCurrentTab();

        Label label = (Label) tab.getGraphic();

        label.setText(header);
    }

    public void updateCurrentTabSaveState(boolean setUnsavedState){

        Tab tab = getCurrentTab();

        Label label = (Label) tab.getGraphic();

        String header = label.getText();

        if (setUnsavedState){

            if ( !header.contains(UNSAVED_STATE)){

                header += UNSAVED_STATE;
            }
        }
        else {

            if ( header.contains(UNSAVED_STATE)){

                header = header.replace(UNSAVED_STATE, "");
            }
        }

        label.setText(header);
    }

    public boolean isTabWasChanged(Tab tab){

        Object object = tab.getUserData();

        Label label = (Label) tab.getGraphic();

        String header = label.getText();

        HBox hbox = (HBox) tab.getContent();

        TextArea textArea = (TextArea) hbox.getChildren().get(TEXT_AREA_INDEX);

        String content =  textArea.getText();

        if (object != null && object instanceof TabTag){

            TabTag item = (TabTag) object;

            if (!item.wasSaved() && content != null && !content.isEmpty()){

                return true;
            }
            else if (item.wasSaved() && header.contains(UNSAVED_STATE)) {

                return true;
            }
            else {

                return false;
            }
        }
        else {

            return false;
        }
    }

    public String getCurrentTabContent(){

        Tab tab = getCurrentTab();

        HBox hbox = (HBox) tab.getContent();

        TextArea textArea = (TextArea) hbox.getChildren().get(TEXT_AREA_INDEX);

        String content =  textArea.getText();

        return content;
    }

    public TabTag getCurrentTabTag(){

        Tab tab = getCurrentTab();

        Object obj = tab.getUserData();

        if (obj instanceof TabTag){

            TabTag item = (TabTag) obj;

            return item;
        }

        return null;
    }

    public Tab getCurrentTab(){

        TabPane tabPane = (TabPane) parent.lookup("#tabPane");

        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();

        Tab tab = selectionModel.getSelectedItem();

        return tab;
    }

    private void setCurrentTab(Tab tab ){

        TabPane tabPane = (TabPane) parent.lookup("#tabPane");

        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();

        selectionModel.select(tab);
    }

    public void createNewTab(String header,  String content){

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

        final HBox hbox = new HBox();
        final Label label = new Label(header);

        TextArea textArea = new TextArea(content);
        textArea.prefWidthProperty().bind(tabPane.widthProperty());
        textArea.prefHeightProperty().bind(tabPane.heightProperty());

        hbox.getChildren().add(textArea);
        hbox.setAlignment(Pos.CENTER);

        Tab tab = new Tab();
        tab.setId(String.valueOf(count));
        tab.setGraphic(label);
        tab.setContent(hbox);
        tab.setClosable(true);

        tabPane.getTabs().add(tab);

        setCurrentTab(tab);

        setLabelListners(label, tabPane, tab);

        setHBoxListners(hbox, tabPane, tab);

        setTextAreaListner(textArea, tabPane, tab);
    }

    private void createAlertConfirm(Tab tab){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Save Changes?");
        alert.setContentText("This file was modified. Save changes?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){

            alertListner.onConfirm(tab);

        } else {

            alertListner.onCancel(tab);
        }
    }

    public void closeTab(Tab tab) {

        TabPane tabPane = (TabPane) parent.lookup("#tabPane");
        tabPane.getTabs().remove(tab);
    }

    private void checkTabChangeState(Tab tab){

        if (isTabWasChanged(tab)){

            createAlertConfirm(tab);
        }
        else {

            closeTab(tab);
        }
    }


    public boolean hasTabs(){

        TabPane tabPane = (TabPane) parent.lookup("#tabPane");

        if (tabPane.getTabs() == null || tabPane.getTabs().size() == 0 ){

            return false;
        }
        else {

            return true;
        }
    }
    //--LISTNERS

    private void setTextAreaListner(TextArea textArea, TabPane tabPane, Tab tab){

        textArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {

                TabTag item =  getCurrentTabTag();

                if (item != null) {

                    String newText = textArea.getText();
                    String oldText = item.getContent();

                    boolean isTabSaved = item.wasSaved();

                    if (isTabSaved) {

                        if (!newText.equals(oldText)) {

                            updateCurrentTabSaveState(true);
                        } else {

                            updateCurrentTabSaveState(false);
                        }
                    }
                }
            }
        });
    }

    private void setHBoxListners(HBox hbox, TabPane tabPane, Tab tab){

        hbox.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>()  {

            @Override
            public void handle(KeyEvent e) {

                if (keyCloseTabCombination.match(e)) {

                    checkTabChangeState(tab);
                    e.consume();
                }
            }
        });
    }

    private void setLabelListners(Label label, TabPane tabPane, Tab tab){

        label.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.getButton() == MouseButton.MIDDLE) {

                    checkTabChangeState(tab);
                }
            }
        });
    }
}
