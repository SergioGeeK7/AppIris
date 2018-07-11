package com.example.sergiogeek7.appiris.opencv;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.sergiogeek7.appiris.bl.BodyPart;

import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Forma detectada
 */

public class Shape implements Parcelable{

    private double column;
    private double row;
    private double colPercentage;
    private double rowPercentage;
    private double originColumn;
    private double originRow;
    public List<BodyPart> selectedParts = new ArrayList<>();
    public String description = "";

    /**
     * Constructor
     * @param column columna del bitmap
     * @param row   row del bitmap
     * @param context contexto de la actividad
     */
    public Shape(double column, double row, ShapeContext context){
        this.column = column;
        this.row = row;
        this.colPercentage = this.column / context.getColumn();
        this.rowPercentage = this.row / context.getRow();
        this.originColumn = context.getColumn() / 2 ;
        this.originRow = context.getRow() / 2;
    }

    public double getColumn(){
        return this.column;
    }

    public double getRow(){
        return this.row;
    }

    /**
     * Obtener angulo 360 grados de la forma detectada
     * @return
     */
    public double getAngle (){

        double co = Math.abs(this.getRow() - originRow);
        double ca = Math.abs(this.getColumn() - originColumn);

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

    /**
     * verificar si se hizo click en la forma
     * @param context contexto del bitmap
     * @param point punto con coordenadas
     * @param radius rango
     * @return
     */
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
    public void writeToParcel(Parcel parcel, int i){
        parcel.writeDouble(this.column);
        parcel.writeDouble(this.row);
        parcel.writeDouble(this.colPercentage);
        parcel.writeDouble(this.rowPercentage);
        parcel.writeDouble(this.originColumn);
        parcel.writeDouble(this.originRow);
        parcel.writeList(selectedParts);
        parcel.writeString(description);
    }

    protected Shape(Parcel in) {
        column = in.readDouble();
        row = in.readDouble();
        colPercentage = in.readDouble();
        rowPercentage = in.readDouble();
        originColumn = in.readDouble();
        originRow = in.readDouble();
        in.readList(selectedParts, BodyPart.class.getClassLoader());
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
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

}
