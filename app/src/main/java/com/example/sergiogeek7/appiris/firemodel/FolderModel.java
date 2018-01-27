package com.example.sergiogeek7.appiris.firemodel;

/**
 * Created by sergiogeek7 on 25/01/18.
 */

public class FolderModel {

    private DetectionModel detectionModel;
    private String label;

    public FolderModel() {
    }

    public FolderModel(DetectionModel detectionModel, String label) {
        this.detectionModel = detectionModel;
        this.label = label;
    }

    public DetectionModel getDetectionModel() {
        return detectionModel;
    }

    public void setDetectionModel(DetectionModel detectionModel) {
        this.detectionModel = detectionModel;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
