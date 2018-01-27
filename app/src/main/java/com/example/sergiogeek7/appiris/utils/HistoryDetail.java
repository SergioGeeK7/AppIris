package com.example.sergiogeek7.appiris.utils;

/**
 * Created by sergiogeek7 on 26/01/18.
 */

public class HistoryDetail {

    private String label;
    private String date;
    private boolean onProcess;

    public HistoryDetail(String label, String date, String onProcess) {
        this.label = label;
        this.date = date;
        //this.onProcess = onProcess;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isOnProcess() {
        return onProcess;
    }

    public void setOnProcess(boolean onProcess) {
        this.onProcess = onProcess;
    }
}
