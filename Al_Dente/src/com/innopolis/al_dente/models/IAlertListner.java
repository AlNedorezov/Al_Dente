package com.innopolis.al_dente.models;


import javafx.scene.control.Tab;

public interface IAlertListner {

    void onConfirm(Tab tab);

    void onCancel(Tab tab);
}
