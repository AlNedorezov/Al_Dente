package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static final String FILE_NAME = "sample.fxml";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 700;
    private static final String TITLE = "Al Dente";


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource(FILE_NAME));

        Scene scene = new Scene(root, WIDTH, HEIGHT);

        primaryStage.setTitle(TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {


        launch(args);
    }
}
