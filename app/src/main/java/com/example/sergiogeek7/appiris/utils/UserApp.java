package com.example.sergiogeek7.appiris.utils;

/**
 * Created by sergiogeek7 on 22/01/18.
 */

public class UserApp {
    private String size;
    private String  weigh;
    private String gender;
    private String birth_date;
    private String country;
    private String city;
    private String fullName;
    private String messagingToken;

    public boolean isDoctor() {
        return doctor;
    }

    public void setDoctor(boolean doctor) {
        this.doctor = doctor;
    }

    private boolean doctor;

    public String getMessagingToken() {
        return messagingToken;
    }

    public void setMessagingToken(String messagingToken) {
        this.messagingToken = messagingToken;
    }

    public UserApp(){

    }

    public UserApp(String size, String weigh, String gender,
                   String birth_date, String country, String city,
                   String fullName) {
        this.size = size;
        this.weigh = weigh;
        this.gender = gender;
        this.birth_date = birth_date;
        this.country = country;
        this.city = city;
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getWeigh() {
        return weigh;
    }

    public void setWeigh(String weigh) {
        this.weigh = weigh;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
