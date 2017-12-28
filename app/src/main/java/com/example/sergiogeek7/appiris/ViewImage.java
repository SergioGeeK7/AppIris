package com.example.sergiogeek7.appiris;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.audiofx.LoudnessEnhancer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sergiogeek7.appiris.utils.BitmapUtils;
import com.yalantis.ucrop.UCrop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ViewImage extends AppCompatActivity {

    protected static final int REQUEST_IMAGE_CAPTURE = 1;
    protected static final int REQUEST_STORAGE_PERMISSION = 1;
    protected static final int FIRST_LEFT_EYE = 0;
    protected static final int CAPTURE_IRIS_DONE = 2;
    protected static final String FILE_PROVIDER_AUTHORITY = "com.example.irisfileprovider";
    protected static final String EYE_PARCELABLE = "com.example.sergiogeek7.appiris.Eye";
    private ImageView img;
    private ArrayList<Eye> eyes = new ArrayList<>();


    protected void launchCamera() {

        // Check for the external storage permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            // Create the capture image intent
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the temporary File where the photo should go
                File original = null;
                File croped = null;
                EyeFile original_eye = new EyeFile();
                EyeFile croped_eye = new EyeFile();

                try {
                    original = BitmapUtils.createTempImageFile(this);
                    croped = BitmapUtils.createTempImageFile(this);

                } catch (IOException ex) {
                    // Error occurred while creating the File
                    ex.printStackTrace();
                }
                if (original != null && croped != null) {

                    original_eye.setAbsoletePath(original.getAbsolutePath());
                    original_eye.setUri(FileProvider.getUriForFile(this,
                            FILE_PROVIDER_AUTHORITY,
                            original));
                    croped_eye.setAbsoletePath(croped.getAbsolutePath());
                    croped_eye.setUri(Uri.fromFile(croped));
                    eyes.add(new Eye(original_eye, croped_eye));
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, original_eye.getUri());
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            } else {
                // If you do not have permission, request it
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_STORAGE_PERMISSION);
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        if(eyes.size() == FIRST_LEFT_EYE){
            launchCamera();
        }
        // Intent intent = getIntent();
        // Uri pathBitmap = Uri.parse(intent.getStringExtra(MainActivity.EXTRA_MESSAGE));
    }

    @Override
    protected void onResume() {
        super.onResume();
       // if (eyes.size() == CAPTURE_IRIS_DONE){
            //Toast.makeText(this, "Finished" , Toast.LENGTH_SHORT);
            //ImageView img = findViewById(R.id.img);
            //Bitmap bitbit = BitmapUtils.resamplePic(this, eyes.peek().getOriginal().getAbsoletePath());
            //img.setImageBitmap(bitbit);
       // }
    }

    protected void launchFilterActivity (){
        Intent intent = new Intent(this, ImageFilters.class);
        intent.putParcelableArrayListExtra(EYE_PARCELABLE, eyes);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            eyes.get(eyes.size() - 1).getCroped().setUri(UCrop.getOutput(data));
            if(eyes.size() != CAPTURE_IRIS_DONE){
                launchCamera();
            }else{
                launchFilterActivity();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            cropImage();
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.e("e", cropError.getMessage());
        } else {
             // Otherwise, delete the temporary image file
             Eye eye = eyes.get(eyes.size() - 1);
             BitmapUtils.deleteImageFile(this, eye.getOriginal().getAbsoletePath());
             BitmapUtils.deleteImageFile(this, eye.getCroped().getAbsoletePath());
        }
    }

    private void cropImage() {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        Eye eye = eyes.get(eyes.size() - 1);
        try {
            UCrop.of(eye.getOriginal().getUri(), eye.getCroped().getUri())
                    .withOptions(options)
                    .withAspectRatio(1, 1)
                    // .withMaxResultSize(100, 100)
                    .start(this);
        }catch (Exception ex){
           // .v("v",ex.getMessage());
        }
        //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }


}
