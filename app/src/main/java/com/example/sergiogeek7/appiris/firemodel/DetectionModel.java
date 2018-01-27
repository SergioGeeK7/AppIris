package com.example.sergiogeek7.appiris.firemodel;

import java.util.Date;

/**
 * Created by sergiogeek7 on 25/01/18.
 */

public class DetectionModel {

    private EyeModel left;
    private EyeModel right;
    private String label;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public DetectionModel(EyeModel left, EyeModel right, Date date) {
        this.left = left;
        this.right = right;
        this.date = date;
    }

    public DetectionModel(){

    }

    public EyeModel getLeft() {
        return left;
    }

    public void setLeft(EyeModel left) {
        this.left = left;
    }

    public EyeModel getRight() {
        return right;
    }

    public void setRight(EyeModel right) {
        this.right = right;
    }
}
