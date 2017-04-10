package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

import java.io.File;

public class Controller {

    private static final String MENU_NEW_FILE = "menu_new_file";
    private static final String SAVE_FILE = "save_file";
    private static final String OPEN_FILE = "open_file";

    public void handleAboutAction(ActionEvent event) {

        MainView view = MainView.getInstance();
        FileHelper fileHelper = FileHelper.getInstance();

        Object obj = event.getSource();

        if (obj instanceof MenuItem){

            MenuItem menuItem = (MenuItem) obj;
            String id = menuItem.getId();

            switch (id){

                case MENU_NEW_FILE :{

                    view.createNewTab(Main.getParent(), null, null);
                }break;
                case OPEN_FILE :{

                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Open text file");

                    String lastPath = Main.getLastPath();

                    if (lastPath == null || lastPath.isEmpty()) {

                        fileChooser.setInitialDirectory(
                                new File(System.getProperty("user.home"))
                        );
                    }
                    else {

                        fileChooser.setInitialDirectory( new File(lastPath) );
                    }

                    File file = fileChooser.showOpenDialog(Main.getPrimaryStage());

                    String header = file.getName();

                    Main.setLastPath(file.getParent());

                    String content = fileHelper.getFileContent(file.getAbsolutePath());

                    view.createNewTab(Main.getParent(), header, content);



                }break;
                case SAVE_FILE :{

                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save text file");

                    String lastPath = Main.getLastPath();

                    if (lastPath == null || lastPath.isEmpty()) {

                        fileChooser.setInitialDirectory(
                                new File(System.getProperty("user.home"))
                        );
                    }
                    else {

                        fileChooser.setInitialDirectory( new File(lastPath) );
                    }

                    File file = fileChooser.showSaveDialog(Main.getPrimaryStage());

                    String content = view.getCurentTab(Main.getParent());

                    fileHelper.createNewFile(file.getAbsolutePath(), content);

                }break;
            }
        }
    }
}
