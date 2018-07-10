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

    public DetectShapes(Bitmap img) {
        this.img = img;
    }

    public ShapesDetected detect() {

        Mat original = new Mat();
        Utils.bitmapToMat(this.img, original);
        Mat modified = new Mat();
        Utils.bitmapToMat(this.img, modified);
        this.img.recycle();
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.cvtColor(modified, modified, Imgproc.COLOR_RGBA2GRAY);
        Imgproc.GaussianBlur(modified, modified, new org.opencv.core.Size(5, 5), 0);
        Imgproc.threshold(modified, modified, 60, 255, Imgproc.THRESH_BINARY);
        Imgproc.findContours(modified, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        List<Shape> coordinates = new ArrayList<>();
        // avoid eyelashes
        final int cols = modified.rows();
        final int rows = modified.cols();
        double MARGIN = 0.8;
        int MAX_AREA = 1500;
        int MIN_AREA = 100;
        int RADIOUS = 15;
        int LIMIT_SHAPES_DETECTED = 7;
        double xThreshold = cols * MARGIN;
        double yThreshold = rows * MARGIN;
        int i = 0;

        Collections.sort(contours, (p1, p2) ->
                (int)(Imgproc.contourArea(p2) - Imgproc.contourArea(p1)));

        for (MatOfPoint point : contours) {
            double area = Imgproc.contourArea(point);
            if (area < MIN_AREA || area > MAX_AREA)
                continue;
            Point center = new Point();
            Imgproc.minEnclosingCircle(new MatOfPoint2f(point.toArray()), center, new float[1]);
            Imgproc.circle(original, center, RADIOUS, new Scalar(255,255,255));

            if (center.x > xThreshold || center.y > yThreshold ||
                    center.y < xThreshold * 0.1 || center.x < yThreshold * 0.1)
                continue;
            // Log.e("coors", center.x + " y: " + center.y);
            // Imgproc.drawContours(original, Collections.singletonList(point), -1, new Scalar(0, 255, 0), 2);

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
            if(++i == LIMIT_SHAPES_DETECTED)
                break;
        }
        Bitmap bm = Bitmap.createBitmap(modified.cols(), modified.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(original, bm);
        return new ShapesDetected(coordinates, bm);
    }

}