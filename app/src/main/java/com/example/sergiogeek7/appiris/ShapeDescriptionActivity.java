package com.example.sergiogeek7.appiris;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.example.sergiogeek7.appiris.components.GridButtons;
import com.example.sergiogeek7.appiris.opencv.Psicosomaticas;
import com.example.sergiogeek7.appiris.opencv.Shape;
import com.example.sergiogeek7.appiris.utils.BitmapUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ShapeDescriptionActivity extends AppCompatActivity {


    Shape shape;
    Psicosomaticas psicosomaticas = new Psicosomaticas();
    private static String TAG = ShapeDescriptionActivity.class.getName();
    int eyeSide;
    private EyeFile eye;
    private Uri shareFilePath;

    @BindView(R.id.description_img)
    DescriptionImageView imagePreview;
    @BindView(R.id.shape_description_layout)
    RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape_description);
        ButterKnife.bind(this);
        this.shape = getIntent().getParcelableExtra(DetectActivity.SHAPE_PARCELABLE);
        this.eyeSide = getIntent().getIntExtra(DetectActivity.EYE_SIDE, 0);
        this.eye = getIntent().getParcelableExtra(DetectActivity.EYE_PARCELABLE);
        addOrgans();
        new loadBitmap().execute(eye.getAbsoletePath());
    }

    void addOrgans (){
        GridButtons gridButtons = new GridButtons(this,
                psicosomaticas.getBodyPart(shape, 0).split(",") );
        RelativeLayout.LayoutParams lParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        lParams.addRule(RelativeLayout.BELOW, R.id.description_img);
        lParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        mainLayout.addView(gridButtons, 0, lParams);
    }

    public boolean onCreateOptionsMenu(Menu paramMenu)
    {
        getMenuInflater().inflate(R.menu.menu_main, paramMenu);
        return true;
    }

    protected void onDestroy()
    {
        super.onDestroy();
        Log.e("log", "Destroying");
    }

    public boolean onOptionsItemSelected(MenuItem paramMenuItem)
    {
        if (paramMenuItem.getItemId() == R.id.share)
        {
            share();
            return true;
        }
        return super.onOptionsItemSelected(paramMenuItem);
    }

    public void share()
    {
        if (this.shareFilePath == null) {
            this.shareFilePath =
                    FileProvider.getUriForFile(this, "com.app.irisfileprovider",
                            BitmapUtils.createTempImageFile(this));
        }
        this.imagePreview.saveView(this.shareFilePath);
        String str = this.psicosomaticas.getBodyPart(this.shape, 0);
        BitmapUtils.shareImage(this, this.shareFilePath, str);
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
