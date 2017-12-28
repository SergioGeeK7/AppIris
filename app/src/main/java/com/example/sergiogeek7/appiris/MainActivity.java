package com.example.sergiogeek7.appiris;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.sergiogeek7.appiris.opencv.DetectShapes;
import com.example.sergiogeek7.appiris.opencv.Shape;
import com.example.sergiogeek7.appiris.opencv.ShapesDetected;
import com.example.sergiogeek7.appiris.utils.BitmapUtils;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    public final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public final int ISMAN = 1;


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                    detectForms2();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };


    @BindView(R.id.img_result)
    ImageView img_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    public void onGenderClick(View view){
        Log.v(String.valueOf(Log.VERBOSE), String.valueOf(view.getId()));
        String name = view.getId() == ISMAN ? "man" : "woman";
        Intent intent = new Intent(this, ViewImage.class);
        intent.putExtra(EXTRA_MESSAGE, name);
        startActivity(intent);
    }

    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void detectForms2(){
        Bitmap bitmap = BitmapUtils.getBitmapFromAssets(this,
                "9e122257-a861-41d5-af7e-a8a61149b32c.jpeg",
                300,
                300);
        ShapesDetected shapes = new DetectShapes(bitmap).detect();
        
        img_result.setImageBitmap(shapes.original);
    }

}
