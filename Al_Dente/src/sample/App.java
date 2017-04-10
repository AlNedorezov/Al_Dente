package sample;

import com.innopolis.al_dente.MainView;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

public class App extends Application {

    private static final String FILE_NAME = "index.fxml";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 700;
    private static final String TITLE = "Al Dente";
    public static final String DEFAULT_PATH = "user.home";

    private static Parent parent;
    private static Stage PrimaryStage;
    private static String LastPath = null;


    @Override
    public void start(Stage primaryStage) throws Exception{

        parent = FXMLLoader.load(getClass().getResource(FILE_NAME));

        PrimaryStage = primaryStage;

        Scene scene = new Scene(parent, WIDTH, HEIGHT);

       /* scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if (mouseEvent.getButton() == MouseButton.MIDDLE){

                    System.out.println();
                }

                System.out.println();
            }
        });*/

        MainView.getInstance().initializeTabPane(parent, scene);

        primaryStage.setTitle(TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static Parent getParent(){

        return parent;
    }

    public static String getLastPath() {
        return LastPath;
    }

    public static void setLastPath(String lastPath) {
        LastPath = lastPath;
    }

    public static Stage getPrimaryStage(){

        return PrimaryStage;
    }

    public static void main(String[] args) {


        launch(args);
    }
}
