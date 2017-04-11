package com.innopolis.al_dente.models;


import javafx.scene.control.Tab;
/*
*<p>Связывает controller and view - нужен для отлавливания событий нажатия на кнопки в messagebox</p>
 */
public interface IAlertListner {

    /*
    <p>Нажал на кнопку ок в msg</p>
     */
    void onConfirm(Tab tab);

    /*
    <p>Нажал на кнопку cancel в msg</p>
     */
    void onCancel(Tab tab);
}
