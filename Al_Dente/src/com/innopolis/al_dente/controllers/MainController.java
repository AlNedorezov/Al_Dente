package com.innopolis.al_dente.controllers;

import com.innopolis.al_dente.FileHelper;
import com.innopolis.al_dente.IMainController;
import com.innopolis.al_dente.models.TabTag;
import com.innopolis.al_dente.views.MainView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.stage.FileChooser;
import sample.App;

import java.io.File;

public class MainController implements IMainController {

    private static final String MENU_NEW_FILE = "menu_new_file";
    private static final String SAVE_FILE = "save_file";
    private static final String SAVE_FILE_AS = "save_file_as";
    private static final String OPEN_FILE = "open_file";
    private static final String FIND = "menu_find";
    private static final String REPLACE = "menu_replace";
    private static final String EXIT = "exit";

    private static final String FILE_CHOOSER_OPEN_FILE = "Open text file";
    private static final String FILE_SAVE_FILE_AS = "Save File As";
    private MainView view;

    @FXML
    public void initialize(){

         view = MainView.getInstance(App.getParent(), this);
    }

    /*
    <p>Обработчик событий нажатия на элементы панели меню</p>
     */
    public void handleAboutAction(ActionEvent event) {

        FileHelper fileHelper = FileHelper.getInstance();

        Object obj = event.getSource();

        if (obj instanceof MenuItem){

            MenuItem menuItem = (MenuItem) obj;
            String id = menuItem.getId();

            switch (id){

                case MENU_NEW_FILE :{

                    createNewTab(null, null);
                    TabTag item = new TabTag();
                    view.updateCurrentTab(item);
                }break;
                case OPEN_FILE :{

                    openFile(view, fileHelper);
                }break;
                case SAVE_FILE :{

                    if (view.hasTabs()) {

                        saveFile(App.getParent(), view, fileHelper);
                    }
                }break;

                case SAVE_FILE_AS :{

                    if (view.hasTabs()) {

                        saveFileAs(App.getParent(), view, fileHelper);
                    }
                }break;

                case FIND:{

                    if (view.hasTabs()) {

                        view.hideSearchReplaceDialog();
                        view.createSearchDialog();
                    }
                }break;

                case REPLACE :{

                    if (view.hasTabs()) {

                        view.hideSearchReplaceDialog();
                        view.createReplaceDialog();
                    }
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

        Tab tab = view.getTabByFilePath(file.getAbsolutePath());

        if (tab != null){

            view.setCurrentTab(tab);
        }
        else {

            String header = file.getName();
            String content = fileHelper.getFileContent(file.getAbsolutePath());


            TabTag item = new TabTag();
            item.setWasSaved(true);
            item.setContent(content);
            item.setPath(file.getAbsolutePath());
            item.setHeader(header);

            createNewTab(header, content);
            view.updateCurrentTab(item);
        }
    }

    private void closeApplication() {

        Platform.exit();
        System.exit(0);
    }

    private void saveFile(Parent parent, MainView view, FileHelper fileHelper){

        TabTag item = view.getCurrentTabTag();

        if (item == null || !item.wasSaved()){

            saveFileAs(parent, view, fileHelper);

        }
        else  {

            String content = view.getCurrentTabContent();

            fileHelper.updateFile(item.getPath(), content);
            item.setContent(content);

            view.updateCurrentTab(item);
            view.updateCurrentTabSaveState(false);

            removeTemporaryFile(item.getHeader(), item.getPath());
        }
    }

    private void saveFileAs(Parent parent, MainView view, FileHelper fileHelper){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(FILE_SAVE_FILE_AS);

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

        if (file == null) { return; }

        App.setLastPath(file.getParent());

        String header = file.getName();
        String content = view.getCurrentTabContent();

        TabTag item = new TabTag();
        item.setWasSaved(true);
        item.setPath(file.getAbsolutePath());
        item.setHeader(header);
        item.setContent(content);

        TabTag tabTag = view.getCurrentTabTag();

        if (tabTag == null || !tabTag.wasSaved()) {

            view.updateCurrentTabHeader( header);
        }
        else {

            view.createNewTab(header, content );
        }

        view.updateCurrentTab(item);
        fileHelper.createNewFile(file.getAbsolutePath(), content);
    }

    @Override
    public void onConfirm(Tab tab) {

        FileHelper fileHelper = FileHelper.getInstance();

        Object obj = tab.getUserData();

        if (obj != null && obj instanceof TabTag){

            TabTag item = (TabTag) obj;

            if (item.wasSaved()){

                saveFile(App.getParent(), view, fileHelper);
            }
            else {

                saveFileAs(App.getParent(), view, fileHelper );
            }
        }
        else {

            saveFileAs(App.getParent(), view, fileHelper );
        }

        view.closeTab(tab);
    }

    @Override
    public void onCancel(Tab tab) {

        view.closeTab(tab);
        chechRemoveTemporaryFile(tab);
    }

    @Override
    public void createNewTab(String header, String content) {

        view.createNewTab(header, content);
        view.hideSearchReplaceDialog();
    }

    @Override
    public void closeTab(Tab tab) {

        view.closeTab(tab);
       chechRemoveTemporaryFile(tab);
    }

    private void chechRemoveTemporaryFile(Tab tab) {

        Object object = tab.getUserData();

        if (object != null && object instanceof TabTag) {

            TabTag item = (TabTag) object;

            if ( item.wasSaved()) {

                removeTemporaryFile(item.getHeader(), item.getPath());
            }
        }
    }

    @Override
    public void fillTemporaryFile(String header, String path, String content) {

        FileHelper fileHelper = FileHelper.getInstance();
        fileHelper.fillTemporaryFile(header, path, content);
    }

    @Override
    public void removeTemporaryFile(String header, String path) {

        FileHelper fileHelper = FileHelper.getInstance();
        fileHelper.removeTemporaryFile(header, path);
    }

}
