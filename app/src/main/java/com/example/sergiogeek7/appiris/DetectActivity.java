package com.example.sergiogeek7.appiris;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.sergiogeek7.appiris.firemodel.DetectionModel;
import com.example.sergiogeek7.appiris.firemodel.EyeModel;
import com.example.sergiogeek7.appiris.firemodel.FolderModel;
import com.example.sergiogeek7.appiris.firemodel.MedicalHistoryForm;
import com.example.sergiogeek7.appiris.opencv.DetectShapes;
import com.example.sergiogeek7.appiris.opencv.Shape;
import com.example.sergiogeek7.appiris.opencv.ShapesDetected;
import com.example.sergiogeek7.appiris.utils.BitmapUtils;
import com.example.sergiogeek7.appiris.utils.Callback;
import com.example.sergiogeek7.appiris.utils.Gender;
import com.example.sergiogeek7.appiris.utils.UserApp;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetectActivity extends AppCompatActivity
        implements InteractiveEyeVIew.EyeViewBindings {

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
                    if(shapesDetected.size() > 1){
                        changeEyeView(right_image.isEnabled() ? ImageFilters.LEFT_EYE : ImageFilters.RIGHT_EYE);
                        return;
                    }
                    new DownloadFilesTask().execute();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @BindView(R.id.eye_view)
    InteractiveEyeVIew eye_view;
    @BindView(R.id.left_image)
    Button left_image;
    @BindView(R.id.right_image)
    Button right_image;


    String detectionKey;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference("detections");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public static String prevRefImage = "";
    private static String TAG = DetectActivity.class.getName();
    public static String SHAPE_PARCELABLE = "SHAPE_PARCELABLE";
    public static String EYE_SIDE = "EYE_SIDE";
    public static String EYE_PARCELABLE = "EYE_PARCELABLE";
    private int eyeSide = 0;
    private List<Eye> eyes;
    private List<ShapesDetected> shapesDetected = new ArrayList<>();
    private Uri shareFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.eyes = getIntent().getParcelableArrayListExtra(ViewImage.EYE_PARCELABLE);
        setContentView(R.layout.activity_detect);
        ButterKnife.bind(this);
    }

    public void analyzeWithExpert(View v){
        Intent intent = new Intent(this, FormMedicalHistory.class);
        intent.putExtra(MedicalHistoryForm.class.getName(), detectionKey);
        startActivity(intent);
    }

    public void uploadImages(){

        String pathLeft = eyes.get(ImageFilters.LEFT_EYE).getOriginal().getAbsoletePath();
        if(prevRefImage.equals(pathLeft)){
            Log.e(TAG, "skip upload");
            return;
        }else{
            prevRefImage = pathLeft;
        }
        String pathRight = eyes.get(ImageFilters.RIGHT_EYE).getOriginal().getAbsoletePath();

        saveToFirebase(pathLeft,
                snapshotRight -> saveToFirebase(pathRight,
                    snapshotLeft -> getUserName(Callback.valueEventListener((err, data) -> {

                        if(err != null){
                            Log.e(TAG, err.getMessage());
                            return;
                        }

                        String name = data.getValue(String.class);
                        EyeModel left = new EyeModel("", pathLeft);
                        EyeModel right = new EyeModel("", pathRight);
                        DetectionModel detectionModel = new DetectionModel(left, right, new Date(),
                                    user.getUid(), name.toLowerCase());

                        DatabaseReference detection = database.getReference("detections")
                                .push();
                        detection.setValue(detectionModel);
                        detectionKey = detection.getKey();
                    }))));
    }

    private void getUserName(ValueEventListener vl){
        DatabaseReference ref_user = database.getReference("users")
                .child(user.getUid()).child("fullName");
        ref_user.addListenerForSingleValueEvent(vl);
    }

    private void saveToFirebase(String path, OnSuccessListener callback){

        String fileName = path.substring(path.lastIndexOf("/") + 1);

        UploadTask uploadTaskLeft = storageRef.child(fileName)
                .putBytes(BitmapUtils.compressImageToByte(path));
        uploadTaskLeft.addOnFailureListener(exception -> Log.e(TAG, exception.getMessage()))
                        .addOnSuccessListener(callback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.share){
            share();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void share(){
        if(this.shareFilePath == null){
            File temp = BitmapUtils.createTempImageFile(this);
            this.shareFilePath = FileProvider
                                    .getUriForFile(this, ViewImage.PROVIDER_AUTHORITY, temp);
        }
        this.eye_view.saveView(this.shareFilePath);
        BitmapUtils.shareImage(this, this.shareFilePath);
    }

    public void changeEyeView(int eyeSide){
        this.eyeSide = eyeSide;
        ShapesDetected shapes = this.shapesDetected.get(eyeSide);
        eye_view.updateView(shapes.shapes, shapes.original);
    }

    public void onLeftImage(View view){
        view.setEnabled(false);
        right_image.setEnabled(true);
        changeEyeView(ImageFilters.LEFT_EYE);
    }

    public void onRightImage(View view){
        view.setEnabled(false);
        left_image.setEnabled(true);
        changeEyeView(ImageFilters.RIGHT_EYE);
    }

    @Override
    public void onShapeClick(Shape shape) {
        Intent intent = new Intent(this, ShapeDescriptionActivity.class);
        intent.putExtra(SHAPE_PARCELABLE, shape);
        intent.putExtra(EYE_SIDE,eyeSide);
        intent.putExtra(EYE_PARCELABLE, eyes.get(this.eyeSide).getOriginal());
        startActivity(intent);
    }
    private class DownloadFilesTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... params) {
            try {
                for (Eye eye: eyes) {

                        Bitmap bitmap = Picasso.with(DetectActivity.this)
                                .load(eye.getOriginal().getUri())
                                //.resize(400, 400)
                                .get();
                        ShapesDetected shapes = new DetectShapes(bitmap).detect();
                        shapesDetected.add(shapes);
                }
                uploadImages();
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
            return "";
        }

        protected void onProgressUpdate() {
            // TODO: make a progress bar
        }

        protected void onPostExecute(String result) {
            changeEyeView(right_image.isEnabled() ? ImageFilters.LEFT_EYE : ImageFilters.RIGHT_EYE);
        }
    }

}
