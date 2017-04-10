package com.innopolis.al_dente;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import sample.App;

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

                    view.createNewTab(App.getParent(), null, null);
                }break;
                case OPEN_FILE :{

                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Open text file");

                    String lastPath = App.getLastPath();

                    if (lastPath == null || lastPath.isEmpty()) {

                        fileChooser.setInitialDirectory(
                                new File(System.getProperty(App.DEFAULT_PATH))
                        );
                    }
                    else {

                        fileChooser.setInitialDirectory( new File(lastPath) );
                    }

                    File file = fileChooser.showOpenDialog(App.getPrimaryStage());

                    String header = file.getName();

                    App.setLastPath(file.getParent());

                    String content = fileHelper.getFileContent(file.getAbsolutePath());

                    view.createNewTab(App.getParent(), header, content);



                }break;
                case SAVE_FILE :{

                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save text file");

                    String lastPath = App.getLastPath();

                    if (lastPath == null || lastPath.isEmpty()) {

                        fileChooser.setInitialDirectory(
                                new File(System.getProperty(App.DEFAULT_PATH))
                        );
                    }
                    else {

                        fileChooser.setInitialDirectory( new File(lastPath) );
                    }

                    File file = fileChooser.showSaveDialog(App.getPrimaryStage());

                    String header = file.getName();

                    view.updateCurrentTabHeader(App.getParent(), header);

                    App.setLastPath(file.getParent());

                    String content = view.getCurrentTabContent(App.getParent());

                    fileHelper.createNewFile(file.getAbsolutePath(), content);

                }break;
            }
        }
    }
}
