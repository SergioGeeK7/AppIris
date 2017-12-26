package com.example.sergiogeek7.appiris;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sergiogeek7 on 23/12/17.
 */

public class Eye implements Parcelable {

    private EyeFile original = new EyeFile();
    private EyeFile croped = new EyeFile();

    public Eye(EyeFile original, EyeFile croped){
        this.original = original;
        this.croped = croped;
    }

    protected Eye(Parcel in) {
        original = in.readParcelable(EyeFile.class.getClassLoader());
        croped = in.readParcelable(EyeFile.class.getClassLoader());
    }

    public static final Creator<Eye> CREATOR = new Creator<Eye>() {
        @Override
        public Eye createFromParcel(Parcel in) {
            return new Eye(in);
        }

        @Override
        public Eye[] newArray(int size) {
            return new Eye[size];
        }
    };

    public EyeFile getOriginal() {
        return original;
    }

    public void setOriginal(EyeFile original) {
        this.original = original;
    }

    public EyeFile getCroped() {
        return croped;
    }

    public void setCroped(EyeFile croped) {
        this.croped = croped;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(original, i);
        parcel.writeParcelable(croped, i);
    }
}
