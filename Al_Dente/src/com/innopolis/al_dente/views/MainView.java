package com.innopolis.al_dente.views;


import com.innopolis.al_dente.IMainController;
import com.innopolis.al_dente.models.TabTag;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sample.App;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainView {
//временный файл
    // перекинуть в контроллер
    //сделать панель для поиска и заменны ( и для коунтов)
    private static final String DEFAULT_NAME = "Untitled";

    private static final String UNSAVED_STATE = "(*)";
    private static final int MAX_TABS_COUNT = 20;
    final KeyCombination keyCloseTabCombination = new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN);
    final KeyCombination keyNewTabCombination = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
    private static IMainController iMainController;

    private static MainView instance;
    private int TEXT_AREA_INDEX = 0;

    private static final String TAB_PANE_ID = "#tabPane";
    private static final String VB_MAIN_CONTAINER_ID = "#vb_main_container";
    private static final String LABEL_MATCHED_COUNT_ID = "#search_count";

    private static  Parent parent;

    private MainView(){}

    public static MainView getInstance(Parent p, IMainController listner){

        if (instance == null){

            instance = new MainView();
        }

        parent = p;
        if (listner != null) {

            iMainController = listner;
        }

        return instance;
    }

    /*
    * <p>Инициализация панели вкладок. Нужно сдлать один раз при загрузке программы</p>
     */
    public void initializeTabPane(Scene scene){

        TabPane tabPane = (TabPane) parent.lookup(TAB_PANE_ID);
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

                    //модель не нужна перенести в контроллер
                    iMainController.createNewTab(null, null);
                    TabTag item = new TabTag();
                    updateCurrentTab(item);
                    e.consume();
                }
            }
        });

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ESCAPE)  {

                   hideSearchReplaceDialog();
                }
            }
        });
    }


    /*
    * <p>Обновляет тэг, привязанный к текущей открытой вкладке</p>
    * @param item тэг, информацию в котором нужно обновить
     */
    public void updateCurrentTab(TabTag item){

        Tab tab = getCurrentTab();
        tab.setUserData(item);
    }


    /*
    * <p>Так как часто используемый вынес в отдельный метод. У текущей вкладки просто обновляет заголовок</p>
    * @param header новый заголовок
     */
    public void updateCurrentTabHeader(String header){

        Tab tab = getCurrentTab();

        Label label = (Label) tab.getGraphic();

        label.setText(header);
    }


    /*
    * <p>Так как часто используемый вынес в отдельный метод. У текущей вкладки просто обновляет состояние - внесли ли несохраннёные изменения или нет</p>
    * @param setUnsavedState были ли внесены в текстовое поле несохраннёные изменения
     */
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


    /*
    * <p>Проверяет были ли внесены в текущую вкладку какие либо изменения. Работает для уже сохранённых и ещё не сохранённых файлов</p>
    * @param tab вкладка, которую нужно проверить
     */
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


    /*
    * <p>Так как часто используемый вынес в отдельный метод. У текущей вкладки просто получаем содержимое текстового поля</p>
     */
    public String getCurrentTabContent(){

        Tab tab = getCurrentTab();

        HBox hbox = (HBox) tab.getContent();

        TextArea textArea = (TextArea) hbox.getChildren().get(TEXT_AREA_INDEX);

        String content =  textArea.getText();

        return content;
    }


    /*
    * <p>Так как часто используемый вынес в отдельный метод. У текущей вкладки просто получаем непосредственно само текстовое поле</p>
     */
    public TextArea getCurrentTabTextArea(){

        Tab tab = getCurrentTab();

        HBox hbox = (HBox) tab.getContent();

        TextArea textArea = (TextArea) hbox.getChildren().get(TEXT_AREA_INDEX);

        return textArea;
    }


    /*
    * <p>У текущей вкладки получаем тэг</p>
    *  @return тэг текущей вкладки
     */
    public TabTag getCurrentTabTag(){

        Tab tab = getCurrentTab();

        Object obj = tab.getUserData();

        if (obj instanceof TabTag){

            TabTag item = (TabTag) obj;

            return item;
        }

        return null;
    }


    /*
    * <p>Получение текущей вкладки</p>
    *  @return текущая влкадка
     */
    public Tab getCurrentTab(){

        TabPane tabPane = (TabPane) parent.lookup(TAB_PANE_ID);

        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();

        Tab tab = selectionModel.getSelectedItem();

        return tab;
    }


    /*
    * <p>Установка текущей влкадки</p>
    * @param tab текущая вкладка
     */
    public void setCurrentTab(Tab tab ){

        TabPane tabPane = (TabPane) parent.lookup(TAB_PANE_ID);

        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();

        selectionModel.select(tab);
    }


    /*
    * <p>Получение вкладки, которая содержит в себе содержиое файла</p>
    * @param path путь к файлу
    * @return вкладка которая соответствует условию или null
     */
    public Tab getTabByFilePath(String path){

        TabPane tabPane = (TabPane) parent.lookup(TAB_PANE_ID);

        for (Tab tab: tabPane.getTabs()) {

            Object obj = tab.getUserData();

            if (obj != null && obj instanceof TabTag) {

                TabTag item = (TabTag) obj;

                if (item.getPath() != null && item.getPath().equals(path)) {

                    return tab;
                }
            }
        }
            return null;
    }


    /*
    * <p>Создание новой вкладки</p>
    * @param header заголовок
    * @param content содержимое
    * <p>Если пользователь просто создаёт вкладку а не открывает какой то файл, то входные параметры null</p>
     */
    public void createNewTab(String header,  String content){

        TabPane tabPane = (TabPane) parent.lookup(TAB_PANE_ID);
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
        textArea.setStyle("-fx-highlight-fill: lightgray; -fx-highlight-text-fill: blue; -fx-font-size: 15px;");

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

        clearSearchIndexes();
    }


    /*
    * <p>Создаёт messagebox который показывается если в файл внесены несохранённые изменения</p>
    * @param tab вкладка, которую нужно закрыть
     */
    private void createAlertConfirm(Tab tab){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Save Changes?");
        alert.setContentText("This file was modified. Save changes?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){

            iMainController.onConfirm(tab);

        } else {

            iMainController.onCancel(tab);
        }
    }


    /*
    * <p>Закрытие вкладки</p>
    * @param tab вкладка, которую нужно закрыть
     */
    public void closeTab(Tab tab) {

        TabPane tabPane = (TabPane) parent.lookup(TAB_PANE_ID);
        tabPane.getTabs().remove(tab);
        clearSearchIndexes();
    }


    /*
    * <p>Проверка были ли внесены во вкладку несохранённые изменения.</p>
    * <p>Если да, то открывается диалоговое окно. Если нет но влкдака просто закрывается</p>
    * @param tab вкладка, которую нужно закрыть
     */
    private void checkTabChangeState(Tab tab){

        if (isTabWasChanged(tab)){

            setCurrentTab(tab);
            createAlertConfirm(tab);
        }
        else {

            iMainController.closeTab(tab);
        }
    }


    /*
    * <p>Проверка имеет ли сейчас панель вкладок хотя бы одну открытую вкладку</p>
    * @return есть ли открытые вкладки
     */
    public boolean hasTabs(){

        TabPane tabPane = (TabPane) parent.lookup(TAB_PANE_ID);

        if (tabPane.getTabs() == null || tabPane.getTabs().size() == 0 ){

            return false;
        }
        else {

            return true;
        }
    }
    //--LISTNERS

    /*
    * <p>Обработчик событий - текстовое поле.</p>
    * <p>Здесь происходит процесс отслеживания изменений в текстовом поле</p>
     */
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
                            iMainController.fillTemporaryFile(item.getHeader(), item.getPath(), newText);
                        } else {

                            updateCurrentTabSaveState(false);
                            iMainController.removeTemporaryFile(item.getHeader(), item.getPath());
                        }
                    }
                }
            }
        });
    }


    /*
    * <p>Обработчик событий нажатия клавиш</p>
    * <p>Закрытие текущей влкадки</p>
     */
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


    /*
    * <p>Обработчик событий - заголовок вкладки</p>
    * <p>Нажатие колёсиком мыши на заголовок вкладки - закрытие.</p>
     */
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

    /*
   * <p>Создаёт тектсовую форму для поиска текста</p>
    */
    public void createSearchDialog(){

        clearSearchIndexes();

        VBox mainContainer  = (VBox) parent.lookup(VB_MAIN_CONTAINER_ID);

        VBox fieldContainer = new VBox(); //find and replace containers

        HBox searchContainer = new HBox(); //only searchTextChange view

        Label labelSearch = new Label("Find");
        TextField tvSearch = new TextField();
        Button btnSearch = new Button("Find");

        tvSearch.setPrefWidth(App.WIDTH - labelSearch.getWidth() - btnSearch.getWidth() - 120);

        searchContainer.getChildren().addAll(labelSearch, tvSearch, btnSearch);

        HBox.setMargin(labelSearch, new Insets(0, 10, 0, 10));
        HBox.setMargin(btnSearch, new Insets(0, 10, 0, 10));

        fieldContainer.getChildren().add(searchContainer);
        mainContainer.getChildren().add(0, fieldContainer);

        tvSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {

               searchTextChange(newValue);
            }
        });

        tvSearch.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER)  {

                    String text = tvSearch.getText();
                    findNextString(text);
                }
            }
        });

        btnSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                String text = tvSearch.getText();
                findNextString(text);
            }
        });
    }

    private void findNextString(String text) {

        if (position == count) {

            clearSearchIndexes();
        }
        else {

            start += text.length();
        }

        searchTextEnter(text);
    }

    /*
  * <p>Вспомогательые индексты для поиска</p>
   */
    private int start = -1;
    private int position = 0;
    private int count = 0;

    /*
   * <p>Работает когда содержимое тектсоовго поля меняется</p>
   * <p>Показывает только текущее слово</p>
    */
    public void searchTextChange(String text){

        TextArea textArea = getCurrentTabTextArea();

        String content = textArea.getText();

        start = content.toLowerCase().indexOf(text.toLowerCase());

        if (start < 0) {

            textArea.selectRange(0,0);

            displayUnableToFindText(text);

            count = 0;
            position = 0;

            return;
        }

        textArea.selectRange(start, start + text.length());

        Pattern p = Pattern.compile(text, Pattern.UNICODE_CASE|Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(content);
        count = 0;
        while(m.find()) { count++; }

        position = 1;

        displayMatchedCount(position, count);
    }

    /*
   * <p>Работает когда в тектсовом поле наживаем enter</p>
   * <p>Показывает при каждом нажатии следующее совпадение</p>
    */
    public void searchTextEnter(String text){

        TextArea textArea = getCurrentTabTextArea();

        String content = textArea.getText();

        if (start < 0){

            start = content.toLowerCase().indexOf(text.toLowerCase());
            position++;
        }
        else {

            start = content.toLowerCase().indexOf(text.toLowerCase(), start);
            position++;
        }

        if (start < 0) {

            textArea.selectRange(0,0);

            displayUnableToFindText(text);

            clearSearchIndexes();

            return;
        }

        textArea.selectRange(start, start + text.length());

        Pattern p = Pattern.compile(text, Pattern.UNICODE_CASE|Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(content);
        count = 0;
        while(m.find()) { count++; }

        displayMatchedCount(position, count);
    }

    /*
  * <p>Информация о том что такое слово не наййдено</p>
   */
    private void displayUnableToFindText(String text) {

        Label labelCount  = (Label) parent.lookup(LABEL_MATCHED_COUNT_ID);
        labelCount.setText("Unable to find " + text);
    }

    /*
  * <p>Отображает информацию о текущей позиции найденного слова и о количестве найденных слов</p>
   */
    private void displayMatchedCount(int position, int count){

        Label labelCount  = (Label) parent.lookup(LABEL_MATCHED_COUNT_ID);
        labelCount.setText(String.format("%d of %d matches", position, count));
    }
    /*
      * <p>Очищает индексы для поиска</p>
       */
    private void clearSearchIndexes() {

        start = 0;
        position = 0;
        count = 0;
    }

    /*
 * <p>Создаём диалог поиска-замены</p>
  */
    public void createReplaceDialog(){

        clearSearchIndexes();

        VBox mainContainer  = (VBox) parent.lookup(VB_MAIN_CONTAINER_ID);

        VBox fieldContainer = new VBox(); //find and replace containers

        HBox searchContainer = new HBox(); //only searchTextChange view

        Label labelSearch = new Label("Find");
        TextField tvSearch = new TextField();
        tvSearch.setPrefWidth(App.WIDTH - labelSearch.getWidth() - 60);

        searchContainer.getChildren().addAll(labelSearch, tvSearch);

        HBox.setMargin(labelSearch, new Insets(0, 15, 0, 15));

        //
        HBox replaceContainer = new HBox(); //only searchTextChange view

        Label labelReplace= new Label("Replace");
        TextField tvReplace = new TextField();
        tvReplace.setPrefWidth(App.WIDTH - labelReplace.getWidth() - 60);

        replaceContainer.getChildren().addAll(labelReplace, tvReplace);

        HBox.setMargin(labelReplace, new Insets(0, 5, 0, 5));
        //

        VBox.setMargin(searchContainer, new Insets(10, 0, 0, 0));
        VBox.setMargin(replaceContainer, new Insets(10, 0, 0, 0));

        fieldContainer.getChildren().addAll(searchContainer, replaceContainer);
        mainContainer.getChildren().add(0, fieldContainer);

        tvSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {

                searchTextChange(newValue);
            }
        });

        tvSearch.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER)  {

                    String text = tvSearch.getText();

                    if (position == count) {

                        clearSearchIndexes();
                    }
                    else {

                        start += text.length();
                    }

                    searchTextEnter(text);
                }
            }
        });

        tvReplace.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER)  {

                    replaceText(getCurrentTabTextArea(), tvSearch.getText(), tvReplace.getText());
                }
            }
        });
    }

    /*
 * <p>Заменяем текст в текущей textArea</p>
  */
    private void replaceText(TextArea textArea, String oldText, String newText) {

        String content = textArea.getText();

        Pattern p = Pattern.compile(oldText, Pattern.UNICODE_CASE|Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(content);
        count = 0;
        while(m.find()) { count++; }

        content = content.replace(oldText, newText);
        textArea.setText(content);

        displayReplaceCount(count);
    }


    /*
 * <p>Отображение сколько замен быо сделано</p>
  */
    private void displayReplaceCount(int count) {

        Label labelCount  = (Label) parent.lookup(LABEL_MATCHED_COUNT_ID);
        labelCount.setText(String.format("Made %d replace", count));
    }


    /*
 * <p>Здесь прячутся все поля поиска и замены и обнуляются все счётчики</p>
 * <p>Например когда открываем поиск - замена а у нас до этого был поиск. Или когда открываем новую вкладку</p>
  */
    public void hideSearchReplaceDialog(){

        VBox mainContainer  = (VBox) parent.lookup(VB_MAIN_CONTAINER_ID);

        if (mainContainer.getChildren().size() > 1){

            mainContainer.getChildren().remove(0);
        }

        Label labelCount  = (Label) parent.lookup(LABEL_MATCHED_COUNT_ID);
        labelCount.setText("");

        clearSearchIndexes();

    }
}
