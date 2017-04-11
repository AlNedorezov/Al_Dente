package com.innopolis.al_dente.models;


public class TabTag {

    private boolean wasSaved;

    private String path;

    private String header;

    private String content;

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

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
