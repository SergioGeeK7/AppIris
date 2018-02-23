package com.example.sergiogeek7.appiris.opencv;

import android.graphics.Bitmap;
import java.util.List;

/**
 * Created by sergiogeek7 on 27/12/17.
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
