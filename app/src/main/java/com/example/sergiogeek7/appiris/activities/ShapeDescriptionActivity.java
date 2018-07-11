package com.example.sergiogeek7.appiris.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sergiogeek7.appiris.R;
import com.example.sergiogeek7.appiris.bl.BodyPart;
import com.example.sergiogeek7.appiris.bl.BodySector;
import com.example.sergiogeek7.appiris.components.GridButtons;
import com.example.sergiogeek7.appiris.bl.Psicosomaticas;
import com.example.sergiogeek7.appiris.opencv.Shape;
import com.example.sergiogeek7.appiris.utils.BitmapUtils;
import com.example.sergiogeek7.appiris.utils.EyeFile;
import com.example.sergiogeek7.appiris.bl.Gender;
import com.example.sergiogeek7.appiris.utils.GlobalState;
import com.example.sergiogeek7.appiris.utils.Message;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.sergiogeek7.appiris.activities.ImageFilters.LEFT_EYE;

/**
 * Descripcion de la forma
 *
 * pantalla para describir los sintomas de la forma detectada (esta pantalla se quito
 * pero se quizo dejar por si cambian de opinion)
 */

public class ShapeDescriptionActivity extends AppCompatActivity{

    ArrayList<BodyPart> bodyParts;
    Psicosomaticas psicosomaticas;
    private static String TAG = ShapeDescriptionActivity.class.getName();

    int eyeSide;
    private EyeFile eye;
    private Uri shareFilePath;
    private Shape shape;
    private BodySector bodySector;
    private Bitmap sector;

    @BindView(R.id.image_preview)
    ImageView imagePreview;
    @BindView(R.id.scrollView)
    HorizontalScrollView scrollView;
    @BindView(R.id.diagnosis)
    TextView txtDiagnosis;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape_description);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getBoolean(getString(R.string.show_tour_key), true) && DetectActivity.onceTourDescription){
            Message.show(getString(R.string.tour_organs_description), null, this);
            DetectActivity.onceTourDescription = false;
        }


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


        if(paramMenuItem.getItemId() == android.R.id.home){
            onBackPressed();
        }

        if (paramMenuItem.getItemId() == R.id.share)
        {
            share();
            return true;
        }
        if(paramMenuItem.getItemId() == R.id.go_to_main_screen){
            startActivity(new Intent(this, MainScreen.class));
        }
        return super.onOptionsItemSelected(paramMenuItem);
    }

    public void share(){
        if (this.shareFilePath == null) {
            this.shareFilePath =
                    FileProvider.getUriForFile(this, "com.app.irisfileprovider",
                            BitmapUtils.createTempImageFile(this));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.share));
        builder.setPositiveButton(getString(R.string.only_image),
                (dialog, id) -> {
                    Bitmap bitmap = BitmapUtils.addWaterMark(sector,getString(R.string.app_name));
                    BitmapUtils.saveBitmap(this, bitmap , shareFilePath);
                    BitmapUtils.shareImage(this, shareFilePath);
                    dialog.cancel();
                });
        builder.setNeutralButton(getString(R.string.with_scheme),
                (dialog, id) -> {
                    Bitmap bitmap = BitmapUtils.addWaterMark(sector, getString(R.string.app_name));
                    Canvas canvas = new Canvas(bitmap);
                    Bitmap drawable  = BitmapUtils.getResizedBitmap(BitmapUtils.drawableToBitmap(
                            getResources().getDrawable(bodySector.drawableResource)), bitmap.getWidth(), bitmap.getHeight());
                    canvas.drawBitmap(drawable, 0,0,null);
                    BitmapUtils.saveBitmap(this, bitmap , shareFilePath);
                    BitmapUtils.shareImage(this, shareFilePath, txtDiagnosis.getText().toString());
                    dialog.cancel();
                });
        builder.create().show();
    }

    Bitmap overlayBitmap(Bitmap b1, Bitmap drawable){
        Bitmap bmOverlay = Bitmap.createBitmap(b1.getWidth(), b1.getHeight(), b1.getConfig());
        Bitmap b2 = BitmapUtils.getResizedBitmap(drawable, b1.getWidth(), b1.getHeight());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(b1, new Matrix(), null);
        canvas.drawBitmap(b2, new Matrix(), null);
        return bmOverlay;
    }

    public Bitmap crop(Bitmap bitmap, double[] scale){
        // x- width , y -> height
        Bitmap newBitmap = Bitmap.createBitmap(bitmap,
                (int)(scale[0] * bitmap.getWidth()),
                (int)(scale[1] * bitmap.getHeight()),
                (int)(scale[2] * bitmap.getWidth()),
                (int)(scale[3] * bitmap.getHeight()));
        return newBitmap;
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
            Bitmap drawable  = BitmapUtils.drawableToBitmap(
                    getResources().getDrawable(bodySector.drawableResource));
            sector = crop(bitmap, scale);
            imagePreview.setImageBitmap(overlayBitmap(sector, drawable));
        }
    }
}