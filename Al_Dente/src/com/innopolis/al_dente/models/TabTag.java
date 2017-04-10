package com.innopolis.al_dente.models;


public class TabTag {

    private boolean wasSaved;

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean wasSaved() {
        return wasSaved;
    }

    public void setWasSaved(boolean wasSaved) {
        this.wasSaved = wasSaved;
    }
}
