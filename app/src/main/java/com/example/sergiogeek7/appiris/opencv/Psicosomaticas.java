package com.example.sergiogeek7.appiris.opencv;

/**
 * Created by sergiogeek7 on 27/12/17.
 */

public class Psicosomaticas {

    private String [] bodyParts;
    private int SLICE = 15;

    public Psicosomaticas (String[] bodyParts){
        this.bodyParts = bodyParts;
    }

    public String getBodyPart(Shape shape){
        int index = (int) Math.floor(shape.getAngle()/SLICE);
        return bodyParts[index];
    }

}
