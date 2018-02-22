package com.example.sergiogeek7.appiris.appiris;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.sergiogeek7.appiris.ImageFilters;
import com.example.sergiogeek7.appiris.opencv.Shape;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergiogeek7 on 7/02/18.
 */

public class BodySector implements Parcelable {

    private int[] rightKey;
    private int[] leftKey;
    ArrayList<BodyPart> parts = new ArrayList<>();
    public int drawableResource;
    public double [] scaleRight;
    public double [] scaleLeft;

    public BodySector(int[] rightKey, int[] leftKey, int drawableResource){
        this.rightKey = rightKey;
        this.leftKey = leftKey;
        this.drawableResource = drawableResource;
    }

    protected BodySector(Parcel in) {
        rightKey = in.createIntArray();
        leftKey = in.createIntArray();
        in.readList(this.parts, BodyPart.class.getClassLoader());
        drawableResource = in.readInt();
        scaleRight = in.createDoubleArray();
        scaleLeft = in.createDoubleArray();
    }

    public static final Creator<BodySector> CREATOR = new Creator<BodySector>() {
        @Override
        public BodySector createFromParcel(Parcel in) {
            return new BodySector(in);
        }

        @Override
        public BodySector[] newArray(int size) {
            return new BodySector[size];
        }
    };

    public boolean isInRange(int index, int keySide){
        int [] keys = keySide == ImageFilters.LEFT_EYE ? leftKey : rightKey;
        for (int key: keys){
            if(key == index){
                return true;
            }
        }
        return false;
    }

    public ArrayList<BodyPart> getParts(){
        return this.parts;
    }
    public void setParts(ArrayList<BodyPart> parts){
        this.parts = parts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeIntArray(rightKey);
        parcel.writeIntArray(leftKey);
        parcel.writeList(parts);
        parcel.writeInt(drawableResource);
        parcel.writeDoubleArray(scaleRight);
        parcel.writeDoubleArray(scaleLeft);
    }
}
