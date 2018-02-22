package com.example.sergiogeek7.appiris;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.sergiogeek7.appiris.appiris.BodyPart;
import com.example.sergiogeek7.appiris.appiris.BodySector;
import com.example.sergiogeek7.appiris.components.GridButtons;
import com.example.sergiogeek7.appiris.opencv.Psicosomaticas;
import com.example.sergiogeek7.appiris.opencv.Shape;
import com.example.sergiogeek7.appiris.utils.BitmapUtils;
import com.example.sergiogeek7.appiris.utils.Gender;
import com.example.sergiogeek7.appiris.utils.GlobalState;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.sergiogeek7.appiris.ImageFilters.LEFT_EYE;
import static com.example.sergiogeek7.appiris.ImageFilters.RIGHT_EYE;

public class ShapeDescriptionActivity extends AppCompatActivity{

    ArrayList<BodyPart> bodyParts;
    Psicosomaticas psicosomaticas;
    private static String TAG = ShapeDescriptionActivity.class.getName();

    int eyeSide;
    private EyeFile eye;
    private Uri shareFilePath;
    private Shape shape;
    private BodySector bodySector;

    @BindView(R.id.description_img)
    DescriptionImageView imagePreview;
    @BindView(R.id.scrollView)
    HorizontalScrollView scrollView;
    @BindView(R.id.diagnosis)
    TextView txtDiagnosis;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape_description);
        ButterKnife.bind(this);
        Gender gender = ((GlobalState)getApplication()).gender;
        psicosomaticas = new Psicosomaticas(this, gender);
        this.bodyParts = getIntent().getParcelableArrayListExtra(DetectActivity.BODY_PARTS);
        this.eyeSide = getIntent().getIntExtra(DetectActivity.EYE_SIDE, 0);
        this.eye = getIntent().getParcelableExtra(DetectActivity.EYE_PARCELABLE);
        this.shape = getIntent().getParcelableExtra(DetectActivity.SHAPE_PARCELABLE);
        this.bodySector = getIntent().getParcelableExtra(DetectActivity.BODY_SECTOR);
        onChangeBodySelected(null);
        addOrgans();
        new loadBitmap().execute(eye.getAbsoletePath());
    }

    public void done(View v){
        Intent returnIntent = new Intent();
        returnIntent.putParcelableArrayListExtra(DetectActivity.BODY_PARTS, this.bodyParts);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    void onChangeBodySelected(BodyPart newPart){
        StringBuilder diagnosis = new StringBuilder(getString(R.string.diagnosis));
        for (BodyPart part: bodyParts){
            if(!part.selected){
                continue;
            }
            int indexParagraph = diagnosis.indexOf(part.description);
            if(indexParagraph == -1){
                diagnosis.append("\n").append(part.name).append(": ").append(part.description);
            }else{
                diagnosis.insert(indexParagraph - 2,", " + part.name);
            }
        }
        txtDiagnosis.setText(diagnosis);
    }

    void addOrgans (){
        GridButtons gridButtons = new GridButtons(this, bodyParts, this::onChangeBodySelected);
        RelativeLayout.LayoutParams lParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        scrollView.addView(gridButtons, 0, lParams);
    }

    public boolean onCreateOptionsMenu(Menu paramMenu){
        getMenuInflater().inflate(R.menu.menu_main, paramMenu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem paramMenuItem){
        if (paramMenuItem.getItemId() == R.id.share)
        {
            share();
            return true;
        }
        return super.onOptionsItemSelected(paramMenuItem);
    }

    public void share(){
        if (this.shareFilePath == null) {
            this.shareFilePath =
                    FileProvider.getUriForFile(this, "com.app.irisfileprovider",
                            BitmapUtils.createTempImageFile(this));
        }
        this.imagePreview.saveView(this.shareFilePath);
        BitmapUtils.shareImage(this, this.shareFilePath,
                txtDiagnosis.getText().toString());
    }

    class loadBitmap extends AsyncTask<String, Void, Bitmap>{

        protected Bitmap doInBackground(String... params){

            Bitmap bitmap = null;
            try {
                bitmap = BitmapUtils.compressImage(params[0]);
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
            return bitmap;
        }

        protected void onProgressUpdate(){
            // TODO: make a progress bar
        }

        protected void onPostExecute(Bitmap bitmap){
            double[] scale = eyeSide == LEFT_EYE ? bodySector.scaleLeft: bodySector.scaleRight;
            imagePreview.updateView(bitmap, bodySector.drawableResource, scale);
        }
    }

}
