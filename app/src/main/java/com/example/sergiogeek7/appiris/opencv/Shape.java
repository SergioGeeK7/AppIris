package com.example.sergiogeek7.appiris.opencv;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.opencv.core.Point;

/**
 * Created by sergiogeek7 on 27/12/17.
 */

public class Shape implements Parcelable{

    private double column;
    private double row;
    private double colPercentage;
    private double rowPercentage;
    private double originColumn;
    private double originRow;

    public Shape(double column, double row, ShapeContext context){
        this.column = column;
        this.row = row;
        this.colPercentage = this.column / context.getColumn();
        this.rowPercentage = this.row / context.getRow();
        this.originColumn = context.getColumn() / 2 ;
        this.originRow = context.getRow() / 2;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(this.column);
        parcel.writeDouble(this.row);
        parcel.writeDouble(this.colPercentage);
        parcel.writeDouble(this.rowPercentage);
        parcel.writeDouble(this.originColumn);
        parcel.writeDouble(this.originRow);
    }

    protected Shape(Parcel in) {
        column = in.readDouble();
        row = in.readDouble();
        colPercentage = in.readDouble();
        rowPercentage = in.readDouble();
        originColumn = in.readDouble();
        originRow = in.readDouble();
    }

    public static final Creator<Shape> CREATOR = new Creator<Shape>() {
        @Override
        public Shape createFromParcel(Parcel in) {
            return new Shape(in);
        }

        @Override
        public Shape[] newArray(int size) {
            return new Shape[size];
        }
    };

    public double getColumn(){
        return this.column;
    }

    public double getRow(){
        return this.row;
    }

    public double getAngle (){

        double co = Math.abs(this.getRow() - originRow);
        double ca = Math.abs(this.getColumn() - originColumn);
        //Log.e("ee", this.getRow() + " " + this.getColumn());

        int quadrant = 0;
        if (this.getColumn() == this.originColumn) {
            if (this.getRow() < this.originRow)
                return 90.0;
            else
                return 270.0;
        }
        if (this.getColumn() < this.originColumn){
            if (this.getRow() < this.originRow){
                quadrant = -180;
            }else if (this.getRow() > this.originRow || this.getRow() == this.originRow){
                quadrant = 180;
            }
        }else if (this.getColumn() > this.originColumn && this.getRow() > this.originRow){
            quadrant = -360;
        }
        return Math.abs(Math.atan(co / ca) * 180 / Math.PI + quadrant);
    }

    public boolean onClick (ShapeContext context, Point point, int radius){

        Point shape = getCoordinates(context);

        return (point.x  <= shape.x + radius && point.x >= shape.x - radius)
                && (point.y  <= shape.y + radius && point.y >= shape.y - radius);
    }

    public Point getCoordinates(ShapeContext context){
        int x = (int) (context.getColumn()  * this.colPercentage);
        int y = (int) (context.getRow()  * this.rowPercentage);
        return  new Point(x, y);
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
