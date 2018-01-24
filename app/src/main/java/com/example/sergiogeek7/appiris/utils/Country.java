package com.example.sergiogeek7.appiris.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by sergiogeek7 on 20/01/18.
 */

public class Country {

    private String key;
    private List<String> cities;
    private HashMap<String, String> label;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public HashMap<String, String> getLabel() {
        return label;
    }

    public void setLabel(HashMap<String, String> label) {
        this.label = label;
    }

    @Override
    public String toString() {
        String key = Locale.getDefault().getLanguage();
        return this.getLabel().get(key);
    }
}
