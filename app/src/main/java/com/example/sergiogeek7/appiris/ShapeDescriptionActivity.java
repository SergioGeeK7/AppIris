package com.example.sergiogeek7.appiris;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.example.sergiogeek7.appiris.opencv.Psicosomaticas;
import com.example.sergiogeek7.appiris.opencv.Shape;
import com.example.sergiogeek7.appiris.utils.BitmapUtils;
import com.squareup.picasso.Picasso;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ShapeDescriptionActivity extends AppCompatActivity {


    Shape shape;
    Psicosomaticas psicosomaticas = new Psicosomaticas();
    private static String TAG = ShapeDescriptionActivity.class.getName();
    int eyeSide;
    EyeFile eye;

    @BindView(R.id.description_img)
    DescriptionImageView imagePreview;

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
        description_text.setText(psicosomaticas.getBodyPart(shape, 0));
        new loadBitmap().execute(eye.getAbsoletePath());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("log", "Destroying");
    }

    class loadBitmap extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... params) {

            Bitmap bitmap = null;
            try {
                bitmap = BitmapUtils.compressImage(params[0]);
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
            return bitmap;
        }

        protected void onProgressUpdate() {
            // TODO: make a progress bar
        }

        protected void onPostExecute(Bitmap bitmap) {
            imagePreview.updateView(shape, bitmap);
        }
    }

}
