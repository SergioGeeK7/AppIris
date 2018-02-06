package com.example.sergiogeek7.appiris.firemodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sergiogeek7 on 25/01/18.
 */

public class DetectionModel implements Parcelable {

    private EyeModel left;
    private EyeModel right;
    private String label;
    private Date date;
    private String userUId;
    private String fullName;
    @Exclude
    private String key;

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public String getSupplements() {
        return supplements;
    }

    public void setSupplements(String supplements) {
        this.supplements = supplements;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    private String supplements;
    private String recommendations;

    protected DetectionModel(Parcel in) {
        left = in.readParcelable(EyeModel.class.getClassLoader());
        right = in.readParcelable(EyeModel.class.getClassLoader());
        label = in.readString();
        date = new Date(in.readLong());
        userUId = in.readString();
        fullName = in.readString();
        isInProcess = in.readInt() != 0;
        key = in.readString();
        supplements = in.readString();
        recommendations = in.readString();
    }

    @Exclude
    public static final Creator<DetectionModel> CREATOR = new Creator<DetectionModel>() {
        @Override
        public DetectionModel createFromParcel(Parcel in) {
            return new DetectionModel(in);
        }

        @Override
        public DetectionModel[] newArray(int size) {
            return new DetectionModel[size];
        }
    };

    @Exclude
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(left, i);
        parcel.writeParcelable(right, i);
        parcel.writeString(label);
        parcel.writeLong(date.getTime());
        parcel.writeString(userUId);
        parcel.writeString(fullName);
        parcel.writeInt(isInProcess ? 1:0);
        parcel.writeString(key);
        parcel.writeString(supplements);
        parcel.writeString(recommendations);
    }

    public boolean getIsInProcess() {
        return isInProcess;
    }

    public void setIsInProcess(boolean inProcess) {
        isInProcess = inProcess;
    }

    private boolean isInProcess;

    public String getUserUId() {
        return userUId;
    }

    public void setUserUId(String userUId) {
        this.userUId = userUId;
    }


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

    public DetectionModel(EyeModel left, EyeModel right, Date date, String userUId, String fullName) {
        this.left = left;
        this.right = right;
        this.date = date;
        this.userUId = userUId;
        this.fullName = fullName;
    }

    public DetectionModel(){

    }

    @Exclude
    public String fullNameCamelcase (){
        String[] strArray = this.getFullName().split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap).append(" ");
        }
        return builder.toString();
    }
    @Exclude
    public String dateString (){
        return new SimpleDateFormat("MMM dd, yyyy, hh:mm:ss aa")
                .format(this.getDate());
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String userName) {
        this.fullName = userName;
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

    @Override
    @Exclude
    public int describeContents() {
        return 0;
    }


}
