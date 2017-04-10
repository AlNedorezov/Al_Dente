package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

    private static final String FILE_NAME = "sample.fxml";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 700;
    private static final String TITLE = "Al Dente";

    private static Parent parent;
    private static Stage primaryStage;


    @Override
    public void start(Stage primaryStage) throws Exception{

        parent = FXMLLoader.load(getClass().getResource(FILE_NAME));

        Scene scene = new Scene(parent, WIDTH, HEIGHT);

        TabPane tabPane = (TabPane) parent.lookup("#tabPane");
        tabPane.setPrefWidth(scene.getWidth());
        tabPane.setPrefHeight(scene.getHeight());

        primaryStage.setTitle(TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static Parent getParent(){

        return parent;
    }

    public static Stage getPrimaryStage(){

        return primaryStage;
    }

    public static void main(String[] args) {


        launch(args);
    }
}
