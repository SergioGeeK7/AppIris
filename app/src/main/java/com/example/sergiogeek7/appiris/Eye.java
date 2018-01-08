package com.example.sergiogeek7.appiris;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sergiogeek7 on 23/12/17.
 */

public class Eye implements Parcelable {

    private EyeFile original;
    private EyeFile filter;

    public Eye(EyeFile original, EyeFile filter){
        this.original = original;
        this.filter = filter;
    }

    protected Eye(Parcel in) {
        original = in.readParcelable(EyeFile.class.getClassLoader());
        filter = in.readParcelable(EyeFile.class.getClassLoader());
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

    public EyeFile getFilter() {
        return filter;
    }

    public void setFilter(EyeFile filter) {
        this.filter = filter;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(original, i);
        parcel.writeParcelable(filter, i);
    }
}
