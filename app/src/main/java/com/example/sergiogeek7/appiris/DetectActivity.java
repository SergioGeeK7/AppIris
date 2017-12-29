package com.example.sergiogeek7.appiris;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.sergiogeek7.appiris.opencv.DetectShapes;
import com.example.sergiogeek7.appiris.opencv.Shape;
import com.example.sergiogeek7.appiris.opencv.ShapesDetected;
import com.example.sergiogeek7.appiris.utils.BitmapUtils;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetectActivity extends AppCompatActivity implements InteractiveEyeVIew.EyeViewBindings {

    @BindView(R.id.eye_view)
    InteractiveEyeVIew eye_view;
    @BindView(R.id.left_image)
    Button left_image;
    @BindView(R.id.right_image)
    Button right_image;

    public static String SHAPE_PARCELABLE = "SHAPE_PARCELABLE";
    public static String EYE_SIDE = "EYE_SIDE";
    public static String EYE_PARCELABLE = "EYE_PARCELABLE";

    private int eyeSide = 0;
    private List<Eye> eyes;
    private List<ShapesDetected> shapesDetected = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.eyes = getIntent().getParcelableArrayListExtra(ViewImage.EYE_PARCELABLE);
        setContentView(R.layout.activity_detect);
        ButterKnife.bind(this);
    }

    public void detect(){
        for (Eye eye: eyes) {
            ShapesDetected shapes = new DetectShapes(
                    BitmapUtils.resamplePic(this, eye.getCroped().getAbsoletePath())
                ).detect();
            shapesDetected.add(shapes);
        }
        changeEyeView(right_image.isEnabled() ? ImageFilters.LEFT_EYE : ImageFilters.RIGHT_EYE);
    }

    public void changeEyeView(int eyeSide){
        this.eyeSide = eyeSide;
        ShapesDetected shapes = this.shapesDetected.get(eyeSide);
        eye_view.updateView(shapes.shapes, shapes.original);
    }

    public void onLeftImage(View view){
        view.setEnabled(false);
        right_image.setEnabled(true);
        changeEyeView(ImageFilters.LEFT_EYE);
    }

    public void onRightImage(View view){
        view.setEnabled(false);
        left_image.setEnabled(true);
        changeEyeView(ImageFilters.RIGHT_EYE);
    }

    @Override
    public void onShapeClick(Shape shape) {
        Intent intent = new Intent(this, ShapeDescriptionActivity.class);
        intent.putExtra(SHAPE_PARCELABLE, shape);
        intent.putExtra(EYE_SIDE,eyeSide);
        intent.putExtra(EYE_PARCELABLE, eyes.get(this.eyeSide).getCroped());
        startActivity(intent);
    }

    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                    detect();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

}
