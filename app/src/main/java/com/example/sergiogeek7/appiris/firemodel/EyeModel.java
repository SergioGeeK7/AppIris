package com.example.sergiogeek7.appiris.firemodel;

/**
 * Created by sergiogeek7 on 25/01/18.
 */

public class EyeModel {

    private String description;
    private String filename;

    public EyeModel(String description, String path) {
        this.description = description;
        this.filename = path.substring(path.lastIndexOf("/") + 1);
    }

    public EyeModel() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
