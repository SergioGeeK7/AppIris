package com.example.sergiogeek7.appiris;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.sergiogeek7.appiris.appiris.BodyPart;
import com.example.sergiogeek7.appiris.components.GridButtons;
import com.example.sergiogeek7.appiris.opencv.Psicosomaticas;
import com.example.sergiogeek7.appiris.opencv.Shape;
import com.example.sergiogeek7.appiris.utils.BitmapUtils;
import com.example.sergiogeek7.appiris.utils.Gender;
import com.example.sergiogeek7.appiris.utils.GlobalState;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ShapeDescriptionActivity extends AppCompatActivity{

    Shape shape;
    Psicosomaticas psicosomaticas;
    private static String TAG = ShapeDescriptionActivity.class.getName();

    int eyeSide;
    private EyeFile eye;
    private Uri shareFilePath;

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
        this.shape = getIntent().getParcelableExtra(DetectActivity.SHAPE_PARCELABLE);
        this.eyeSide = getIntent().getIntExtra(DetectActivity.EYE_SIDE, 0);
        this.eye = getIntent().getParcelableExtra(DetectActivity.EYE_PARCELABLE);
        setDescriptionText();
        addOrgans();
        new loadBitmap().execute(eye.getAbsoletePath());
    }

    public void done(View v){
        Intent returnIntent = new Intent();
        returnIntent.putExtra(DetectActivity.SHAPE_PARCELABLE, shape);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    void toggleParts(BodyPart newPart){
        BodyPart partFound = findPart(newPart);
        if(partFound != null){
            shape.selectedParts.remove(partFound);
        }else{
            shape.selectedParts.add(newPart);
        }
        setDescriptionText();
    }

    void setDescriptionText(){
        StringBuilder diagnosis = new StringBuilder(getString(R.string.diagnosis));
        for (BodyPart part: shape.selectedParts){
            int indexParagraph = diagnosis.indexOf(part.description);
            if(indexParagraph == -1){
                diagnosis.append("\n").append(part.name).append(": ").append(part.description);
            }else{
                diagnosis.insert(indexParagraph - 2,", " + part.name);
            }
        }
        shape.description = diagnosis.toString();
        txtDiagnosis.setText(diagnosis);
    }

    BodyPart findPart(BodyPart newPart){
        for (BodyPart part: shape.selectedParts){
            if(part.id == newPart.id){
                return part;
            }
        }
        return null;
    }

    void addOrgans (){
        GridButtons gridButtons = new GridButtons(this,
                psicosomaticas.getBodyPart(shape, eyeSide).getParts(), this::toggleParts);
        gridButtons.setSelectedItems(shape.selectedParts);
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
            int drawableBodyPart = psicosomaticas.getBodyPart(shape, eyeSide).drawableResource;
            imagePreview.updateView(shape, bitmap, drawableBodyPart);
        }
    }

}
