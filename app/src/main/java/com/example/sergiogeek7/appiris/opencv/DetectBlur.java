package com.example.sergiogeek7.appiris.opencv;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.imgproc.Imgproc;

/**
 * Created by sergiogeek7 on 4/01/18.
 */

public class DetectBlur {

    private final static String TAG = DetectBlur.class.getName();

    public static boolean isBlur (Bitmap bitmap){

        double THRESHOLD = 90;
        Mat image = new Mat();
        Utils.bitmapToMat(bitmap, image);
        Mat destination = new Mat();
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Laplacian(image, destination, CvType.CV_64F);
        MatOfDouble median = new MatOfDouble();
        MatOfDouble std= new MatOfDouble();
        Core.meanStdDev(destination, median , std);
        double blur = Math.pow(std.get(0,0)[0], 2);
        Log.e(TAG, blur + "");
        return Math.pow(std.get(0,0)[0], 2) < THRESHOLD;
    }
}
