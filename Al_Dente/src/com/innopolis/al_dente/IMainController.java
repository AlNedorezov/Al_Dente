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

    /*
 <p>Создание или изменение временного файла</p>
  */
    void fillTemporaryFile(String header, String path, String content);

    /*
 <p>Удаление временнго файла</p>
  */
    void removeTemporaryFile(String header, String path);

}
