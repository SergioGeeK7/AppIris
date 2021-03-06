package com.example.sergiogeek7.appiris;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.provider.MediaStore;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sergiogeek7.appiris.opencv.DetectBlur;
import com.example.sergiogeek7.appiris.utils.BitmapUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yalantis.ucrop.UCrop;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewImage extends AppCompatActivity{


    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    if(!load_photo.isEnabled()){
                        take_photo.setText(R.string.take_photo_left);
                        load_photo.setText(R.string.load_photo_left);
                        load_photo.setEnabled(true);
                        take_photo.setEnabled(true);
                    }
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    protected static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int SELECT_GALLERY_IMAGE = 101;
    protected static final int REQUEST_STORAGE_PERMISSION = 1;
    protected static final int FIRST_LEFT_EYE = 0;
    protected static final int CAPTURE_IRIS_DONE = 2;
    protected static final String PROVIDER_AUTHORITY = "com.example.irisfileprovider";
    protected static final String EYE_PARCELABLE = "com.example.sergiogeek7.appiris.Eye";
    private static final String TAG = ViewImage.class.getName();
    private boolean cancellable = false;
    private ArrayList<Eye> eyes = new ArrayList<>();
    @BindView(R.id.take_photo)
    Button take_photo;
    @BindView(R.id.load_photo)
    Button load_photo;
    @BindView(R.id.img)
    ImageView img;

    public void launchCamera(View view) {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, createEye());
                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Permissions are not granted!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    public void loadPhoto(View view) {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_a_picture)), SELECT_GALLERY_IMAGE);
                        } else {
                            Toast.makeText(getApplicationContext(), "Permissions are not granted!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private Uri createEye (){

        if(this.eyes.size() == CAPTURE_IRIS_DONE)
            return this.eyes.get(this.eyes.size() - 1).getFilter().getUri();

        File original = BitmapUtils.createTempImageFile(ViewImage.this);
        File filter = BitmapUtils.createTempImageFile(ViewImage.this);

        EyeFile eye_original = new EyeFile(filter.getAbsolutePath(), Uri.fromFile(filter));
        EyeFile eye_filter =
                new EyeFile (original.getAbsolutePath(),
                             FileProvider.getUriForFile(this, PROVIDER_AUTHORITY, original)
        );
        eyes.add(new Eye(eye_original, eye_filter));
        cancellable = true;
        return eye_filter.getUri();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        ButterKnife.bind(this);
    }

    protected void launchFilterActivity (){

        Intent i = new Intent(this, ImageFilters.class);
        i.putParcelableArrayListExtra(EYE_PARCELABLE, eyes);
        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            if(eyes.size() == CAPTURE_IRIS_DONE){
                launchFilterActivity();
                return;
            }
            nextUIEye();
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            isEyeBlur(eyes.get(eyes.size() - 1));
            // due to async code I can't follow the flow here TODO: refactor
        } else if (requestCode == SELECT_GALLERY_IMAGE && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            Uri uri = data.getData();
            createEye();
            cropImage(uri, eyes.get(eyes.size() - 1).getOriginal().getUri());
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Throwable cropError = UCrop.getError(data);
            Log.e(TAG, cropError.getMessage());
        } else {
             // Otherwise, delete the temporary image file
             if(cancellable){
                 Eye eye = eyes.get(eyes.size() - 1);
                 BitmapUtils.deleteImageFile(this, eye.getOriginal().getAbsoletePath());
                 BitmapUtils.deleteImageFile(this, eye.getFilter().getAbsoletePath());
                 eyes.remove(eye);
                 cancellable = false;
             }
        }
    }

    private void isEyeBlur(Eye eye){
        new BitmapAsyncTask(this, bitmap -> {
            if(!DetectBlur.isBlur(bitmap)){
                cropImage(eye.getFilter().getUri(), eye.getOriginal().getUri());
            }else{
                showMessage(R.string.blur_photo, (dialog, which)
                        -> {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, eye.getFilter().getUri());
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                });
            }
        }).execute(eye.getFilter().getUri());
    }

    private void showMessage(int resourceId, DialogInterface.OnClickListener callback){
        new AlertDialog.Builder(this)
                .setTitle(R.string.alert)
                .setMessage(resourceId)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, callback)
                .show();
    }

    private void nextUIEye(){
        take_photo.setText(R.string.take_photo_right);
        load_photo.setText(R.string.load_photo_right);
        img.setImageResource(R.drawable.ic_wink_emoticon_square_right);
        cancellable = false;
    }

    private void cropImage(Uri original, Uri dest) {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        try {
            UCrop.of(original, dest)
                    .withOptions(options)
                    .withAspectRatio(1, 1)
                    // .withMaxResultSize(100, 100)
                    .start(this);
        }catch (Exception ex){
           Log.e(TAG, ex.getMessage());
        }
        //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }

}
