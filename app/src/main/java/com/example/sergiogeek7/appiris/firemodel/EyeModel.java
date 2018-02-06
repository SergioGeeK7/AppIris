package com.example.sergiogeek7.appiris.firemodel;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

/**
 * Created by sergiogeek7 on 25/01/18.
 */

public class EyeModel implements Parcelable{
    private String description;
    private String filename;
    @Exclude
    public Bitmap bitmap;

    protected EyeModel(Parcel in) {
        description = in.readString();
        filename = in.readString();
    }

    @Exclude
    public static final Creator<EyeModel> CREATOR = new Creator<EyeModel>() {
        @Override
        public EyeModel createFromParcel(Parcel in) {
            return new EyeModel(in);
        }

        @Override
        public EyeModel[] newArray(int size) {
            return new EyeModel[size];
        }
    };

    @Override
    @Exclude
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(description);
        parcel.writeString(filename);
    }

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

    @Override
    @Exclude
    public int describeContents() {
        return 0;
    }


}
