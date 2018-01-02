package com.example.sergiogeek7.appiris.opencv;

import android.graphics.Bitmap;
import android.util.Log;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sergiogeek7 on 27/12/17.
 */

public class DetectShapes {

    private Bitmap img;
    private double MARGIN = 0.8;

    public DetectShapes(Bitmap img) {
        this.img = img;
    }

    public ShapesDetected detect() {

        Mat original = new Mat();
        Mat modified = new Mat();
        Bitmap bmp32 = this.img.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, modified);
        Utils.bitmapToMat(bmp32, original);
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.cvtColor(modified, modified, Imgproc.COLOR_RGBA2GRAY);
        Imgproc.GaussianBlur(modified, modified, new org.opencv.core.Size(5, 5), 0);
        Imgproc.threshold(modified, modified, 60, 255, Imgproc.THRESH_BINARY);
        Imgproc.findContours(modified, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        List<Shape> coordinates = new ArrayList<>();
        // avoid eyelashes
        final int cols = modified.cols();
        final int rows = modified.rows();
        double xThreshold = cols * MARGIN;
        double yThreshold = rows * MARGIN;

        for (MatOfPoint point : contours) {
            double area = Imgproc.contourArea(point);
            if (area < 100 || area > 2000)
                continue;
            Point center = new Point();
            Imgproc.minEnclosingCircle(new MatOfPoint2f(point.toArray()), center, new float[1]);

            if (center.x > xThreshold || center.y > yThreshold ||
                    center.y < xThreshold * 0.1 || center.x < yThreshold * 0.1)
                continue;

//            Imgproc.drawContours(original, Collections.singletonList(point), -1,
//                    new Scalar(0, 255, 0), 2);
            coordinates.add(new Shape(center.x, center.y, new ShapeContext() {
                @Override
                public int getColumn() {
                    return cols;
                }

                @Override
                public int getRow() {
                    return rows;
                }
            }));
        }
        Bitmap bm = Bitmap.createBitmap(original.cols(), original.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(original, bm);
        return new ShapesDetected(coordinates, bm);
    }

}