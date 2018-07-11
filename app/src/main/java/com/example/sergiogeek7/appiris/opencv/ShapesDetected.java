package com.example.sergiogeek7.appiris.opencv;

import android.graphics.Bitmap;
import java.util.List;

/**
 * ShapesDetected
 *
 * Structura de formas detectadas en el iris
 */

public class ShapesDetected {

    public List<Shape> shapes;
    public Bitmap original;
    public Bitmap scheme;

    public ShapesDetected (List<Shape> shapes, Bitmap original){
        this.shapes = shapes;
        this.original = original;
    }
}
