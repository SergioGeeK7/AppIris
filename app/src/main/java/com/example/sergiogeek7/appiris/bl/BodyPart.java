package com.example.sergiogeek7.appiris.bl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Parte del cuerpo ej: corazon, columna, espalda
 */

public class BodyPart implements Parcelable{

    public String name;
    public String description;
    public int id;
    public boolean selected = false;

    public BodyPart (String name, String description, int id){
        this.name = name;
        this.description = description;
        this.id = id;
    }

    protected BodyPart(Parcel in) {
        name = in.readString();
        description = in.readString();
        id = in.readInt();
        selected = in.readInt() == 1;
    }

    public static final Creator<BodyPart> CREATOR = new Creator<BodyPart>() {
        @Override
        public BodyPart createFromParcel(Parcel in) {
            return new BodyPart(in);
        }

        @Override
        public BodyPart[] newArray(int size) {
            return new BodyPart[size];
        }
    };

    @Override
    public String toString() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeInt(id);
        parcel.writeInt(selected ? 1: 0);
    }
}
