package com.example.sergiogeek7.appiris;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sergiogeek7.appiris.opencv.Psicosomaticas;
import com.example.sergiogeek7.appiris.opencv.Shape;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShapeDescriptionActivity extends AppCompatActivity {


    Shape shape;
    Psicosomaticas psicosomaticas = new Psicosomaticas();

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
        description_text.setText(psicosomaticas.getBodyPart(shape, 0));
    }


}
