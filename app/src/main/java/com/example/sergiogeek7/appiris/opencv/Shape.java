package com.example.sergiogeek7.appiris.opencv;

import android.util.Log;

/**
 * Created by sergiogeek7 on 27/12/17.
 */

public class Shape {

    private double originColumn;
    private double originRow;
    private double column;
    private double row;

    public Shape(double column, double row, double originColumn, double originRow){
        this.column = column;
        this.row = row;
        this.originColumn = originColumn;
        this.originRow = originRow;
    }

    public double getColumn(){
        return this.column;
    }

    public double getRow(){
        return this.row;
    }

    public double getAngle (){
        double co = Math.abs(this.getRow() - originRow);
        double ca = Math.abs(this.getColumn() - originColumn);
        Log.e("ee", this.getRow() + " " + this.getColumn());

        int quadrant = 0;
        if (this.getColumn() == originColumn) {
            if (this.getRow() < originRow)
                return 90.0;
            else
                return 270.0;
        }
        if (this.getColumn() < originColumn){
            if (this.getRow() < originRow){
                quadrant = -180;
            }else if (this.getRow() > originRow || this.getRow() == originRow){
                quadrant = 180;
            }
        }else if (this.getColumn() > originColumn && this.getRow() > originRow){
            quadrant = -360;
        }
        return Math.abs(Math.atan(co / ca) * 180 / Math.PI + quadrant);
    }

}
