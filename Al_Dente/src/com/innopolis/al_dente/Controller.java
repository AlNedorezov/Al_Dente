package com.innopolis.al_dente;

import com.innopolis.al_dente.models.TabTag;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import sample.App;

import java.io.File;

public class Controller {

    private static final String MENU_NEW_FILE = "menu_new_file";
    private static final String SAVE_FILE = "save_file";
    private static final String SAVE_FILE_AS = "save_file_as";
    private static final String OPEN_FILE = "open_file";
    private static final String EXIT = "exit";

    private static final String FILE_CHOOSER_OPEN_FILE = "Open text file";
    private static final String FILE_SAVE_FILE_AS = "Save File As";

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

                   openFile(view, fileHelper);
                }break;
                case SAVE_FILE :{

                    saveFile(App.getParent(), view, fileHelper);
                }break;

                case SAVE_FILE_AS :{

                    saveFileAs(App.getParent(), view, fileHelper);
                }break;

                case EXIT: {

                    closeApplication();
                }break;
            }
        }
    }

    private void openFile(MainView view, FileHelper fileHelper) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(FILE_CHOOSER_OPEN_FILE);

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

        if (file == null) { return; }

        App.setLastPath(file.getParent());

        String header = file.getName();
        String content = fileHelper.getFileContent(file.getAbsolutePath());


        TabTag item = new TabTag();
        item.setWasSaved(true);
        item.setContent(content);
        item.setPath(file.getAbsolutePath());
        item.setHeader(header);

        view.createNewTab(App.getParent(), header, content);
        view.setCurrentTabTag(App.getParent(), item);
    }

    private void closeApplication() {

        Platform.exit();
        System.exit(0);
    }

    private void saveFile(Parent parent, MainView view, FileHelper fileHelper){

        TabTag item = view.getCurrentTabTag(parent);

        if (item == null || !item.wasSaved()){

            saveFileAs(parent, view, fileHelper);

        }
        else  {

            String content = view.getCurrentTabContent(parent);

            fileHelper.updateFile(item.getPath(), content);
            item.setContent(content);

            view.setCurrentTabTag(parent, item);
            view.updateCurrentTabSaveState(parent, false);
        }
    }

    private void saveFileAs(Parent parent, MainView view, FileHelper fileHelper){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(SAVE_FILE_AS);

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

        App.setLastPath(file.getParent());

        String header = file.getName();
        String content = view.getCurrentTabContent(App.getParent());

        TabTag item = new TabTag();
        item.setWasSaved(true);
        item.setPath(file.getAbsolutePath());
        item.setHeader(header);
        item.setContent(content);

        TabTag tabTag = view.getCurrentTabTag(parent);

        if (tabTag == null || !tabTag.wasSaved()) {

            view.updateCurrentTabHeader(App.getParent(), header);
        }
        else {

            view.createNewTab(App.getParent(), header, content );
        }

        view.setCurrentTabTag(App.getParent(), item);
        fileHelper.createNewFile(file.getAbsolutePath(), content);
    }
}
