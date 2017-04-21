package com.innopolis.al_dente;


import javafx.scene.control.Tab;
/*
*<p>Связывает controller and view - нужен для отлавливания событий нажатия на кнопки в messagebox</p>
 */
public interface IMainController {

    /*
    <p>Нажал на кнопку ок в msg</p>
     */
    void onConfirm(Tab tab);

    /*
    <p>Нажал на кнопку cancel в msg</p>
     */
    void onCancel(Tab tab);

    /*
   <p>Создание новой вкладки</p>
    */
    void createNewTab(String header, String content);

    /*
  <p>Закрытие вкладки</p>
   */
    void  closeTab(Tab tab);

    void fillTemporaryFile(String header, String path, String content);

    void removeTemporaryFile(String header, String path);
}
