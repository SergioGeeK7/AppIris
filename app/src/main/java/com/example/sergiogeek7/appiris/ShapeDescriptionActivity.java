package com.example.sergiogeek7.appiris;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sergiogeek7.appiris.opencv.Psicosomaticas;
import com.example.sergiogeek7.appiris.opencv.Shape;
import com.example.sergiogeek7.appiris.opencv.ShapeContext;
import com.example.sergiogeek7.appiris.utils.BitmapUtils;

import org.opencv.core.Point;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShapeDescriptionActivity extends AppCompatActivity {


    Shape shape;
    Psicosomaticas psicosomaticas = new Psicosomaticas();
    int eyeSide;
    EyeFile eye;

    @BindView(R.id.description_img)
    ImageView imagePreview;

    @BindView(R.id.description_text)
    TextView description_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape_description);
        ButterKnife.bind(this);
        this.shape = getIntent().getParcelableExtra(DetectActivity.SHAPE_PARCELABLE);
        this.eyeSide = getIntent().getIntExtra(DetectActivity.EYE_SIDE, 0);
        this.eye = getIntent().getParcelableExtra(DetectActivity.EYE_PARCELABLE);
        this.imagePreview.setImageBitmap(cutBitmap(shape, eye.getAbsoletePath()));
        description_text.setText(psicosomaticas.getBodyPart(shape, 0));
    }

    private Bitmap cutBitmap (Shape shape, String imgPath){

        Bitmap btm = BitmapUtils.resamplePic(imagePreview.getContext(), imgPath);
        final int col = btm.getWidth();
        final int row = btm.getHeight();
        Point point = shape.getCoordinates(new ShapeContext() {
            @Override
            public int getColumn() {
                return col;
            }
            @Override
            public int getRow() {
                return row;
            }
        });
        //canvas.scale(scaleFactor, scaleFactor);
        return Bitmap.createBitmap(btm,(int) point.x - 50 ,(int) point.y - 50, 100, 100);
    }
}
