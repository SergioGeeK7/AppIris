package com.example.sergiogeek7.appiris;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    public void onResume()
    {
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
                    take_photo.setText(R.string.take_photo_left);
                    take_photo.setEnabled(true);
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
    protected static final int REQUEST_STORAGE_PERMISSION = 1;
    protected static final int FIRST_LEFT_EYE = 0;
    protected static final int CAPTURE_IRIS_DONE = 2;
    protected static final String FILE_PROVIDER_AUTHORITY = "com.example.irisfileprovider";
    protected static final String EYE_PARCELABLE = "com.example.sergiogeek7.appiris.Eye";
    private static final String TAG = ViewImage.class.getName();
    private ArrayList<Eye> eyes = new ArrayList<>();
    @BindView(R.id.take_photo)
    Button take_photo;
    @BindView(R.id.img)
    ImageView img;

    private void launchCamera() {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
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
                                    original = BitmapUtils.createTempImageFile(ViewImage.this);
                                    croped = BitmapUtils.createTempImageFile(ViewImage.this);
                                } catch (IOException ex) {
                                    // Error occurred while creating the File
                                    ex.printStackTrace();
                                }
                                if (original != null && croped != null) {

                                    original_eye.setAbsoletePath(original.getAbsolutePath());
                                    original_eye.setUri(FileProvider.getUriForFile(ViewImage.this,
                                            FILE_PROVIDER_AUTHORITY,
                                            original));
                                    croped_eye.setAbsoletePath(croped.getAbsolutePath());
                                    croped_eye.setUri(Uri.fromFile(croped));
                                    eyes.add(new Eye(original_eye, croped_eye));
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, original_eye.getUri());
                                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        ButterKnife.bind(this);
    }

    protected void launchFilterActivity (){
        Intent intent = new Intent(this, ImageFilters.class);
        intent.putParcelableArrayListExtra(EYE_PARCELABLE, eyes);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri temp = eyes.get(eyes.size() - 1).getOriginal().getUri();
            eyes.get(eyes.size() - 1).getOriginal().setUri(UCrop.getOutput(data));
            eyes.get(eyes.size() - 1).getFilter().setUri(temp);
            if(eyes.size() == CAPTURE_IRIS_DONE){
                launchFilterActivity();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            isBlurImage(eyes.get(eyes.size() - 1).getOriginal().getUri());
            // due to async code I can't follow the flow here TODO: refactor
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.e(TAG, cropError.getMessage());
        } else {
             // Otherwise, delete the temporary image file
             Eye eye = eyes.get(eyes.size() - 1);
             BitmapUtils.deleteImageFile(this, eye.getOriginal().getAbsoletePath());
             BitmapUtils.deleteImageFile(this, eye.getFilter().getAbsoletePath());
        }
    }

    private void isBlurImage(final Uri uri){
        new BitmapAsyncTask(this, new BitmapAsyncTask.BitmapAsync() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap) {
                if(!DetectBlur.isBlur(bitmap)){
                    cropImage();
                    nextUIEye();
                }else{
                    showMessage(R.string.blur_photo, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    });
                }
            }
        }).execute(uri);
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
        img.setImageResource(R.drawable.ic_wink_emoticon_square_right);
    }

    public void takePhoto(View view){
        launchCamera();
    }

    private void cropImage() {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        Eye eye = eyes.get(eyes.size() - 1);
        try {
            UCrop.of(eye.getOriginal().getUri(), eye.getFilter().getUri())
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
