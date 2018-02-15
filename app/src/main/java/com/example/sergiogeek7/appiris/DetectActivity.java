package com.example.sergiogeek7.appiris;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sergiogeek7.appiris.appiris.BodyPart;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.io.ByteArrayOutputStream;
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

    private String detectionKey;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference("detections");
    DatabaseReference detectionRef = database.getReference("detections");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final static String TAG = DetectActivity.class.getName();
    public final static String SHAPE_PARCELABLE = "SHAPE_PARCELABLE";
    public final static String EYE_SIDE = "EYE_SIDE";
    public final static String EYE_PARCELABLE = "EYE_PARCELABLE";
    public final static int SHAPE_DESCRIPTION_ACTIVITY = 1;
    private Shape latestShape;
    private int eyeSide = 0;
    private List<Eye> eyes;
    private List<ShapesDetected> shapesDetected = new ArrayList<>();
    private Uri shareFilePath;
    private int sharedCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.eyes = getIntent().getParcelableArrayListExtra(ViewImage.EYE_PARCELABLE);
        setContentView(R.layout.activity_detect);
        ButterKnife.bind(this);

    }

    public void analyzeWithExpert(View v){
        if(user == null){
            Toast.makeText(this, getString(R.string.must_register_first), Toast.LENGTH_LONG)
            .show();
            return;
        }
        Intent intent = new Intent(this, FormMedicalHistory.class);
        intent.putExtra(MedicalHistoryForm.class.getName(), detectionKey);
        startActivity(intent);
    }

    public void uploadImages(Bitmap left, Bitmap right){

        Log.e(TAG,"Saving 1");
        saveToFirebase(left,
                Callback.onSuccessListener(leftSnap ->
                        saveToFirebase(right,
                            Callback.onSuccessListener(rightSnap -> getUserApp(
                                    Callback.valueEventListener((err, data) -> {

                                            if(err != null){
                                                Log.e(TAG, err.getMessage());
                                                return;
                                            }
                                            Log.e(TAG,"Saving2");
                                            UserApp userapp = data.getValue(UserApp.class);
                                            EyeModel leftEye = new EyeModel("",
                                                    leftSnap.getMetadata().getName());
                                            EyeModel rightEye = new EyeModel("",
                                                    rightSnap.getMetadata().getName());
                                            DetectionModel detectionModel
                                                    = new DetectionModel(leftEye, rightEye, new Date(),
                                                    user.getUid(),
                                                    userapp.getFullName().toLowerCase()
                                                    , userapp.getMessagingToken());

                                            DatabaseReference detection
                                                    = detectionRef.push();
                                            Callback.taskManager(this,
                                                    detection.setValue(detectionModel));
                                            detectionKey = detection.getKey();

                                    }, DetectActivity.this) )
                        , DetectActivity.this)),DetectActivity.this));
    }

    private void getUserApp(ValueEventListener vl){
        DatabaseReference ref_user = database.getReference("users")
                .child(user.getUid());
        ref_user.addListenerForSingleValueEvent(vl);
    }

    private void saveToFirebase(Bitmap bitmap, OnSuccessListener<UploadTask.TaskSnapshot> callback){

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String fileName = "JPEG_" + timeStamp + "_";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTaskLeft = storageRef.child(fileName)
                                              .putBytes(data);
        // Callback.taskManager(this, uploadTaskLeft);
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
        if(user != null && detectionKey != null){
            Callback.taskManager(this,
                    detectionRef
                            .child(detectionKey)
                            .child("sharedCount")
                            .setValue(++sharedCount));
        }
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
        this.latestShape = shape;
        Intent intent = new Intent(this, ShapeDescriptionActivity.class);
        intent.putExtra(SHAPE_PARCELABLE, shape);
        intent.putExtra(EYE_SIDE, eyeSide);
        intent.putExtra(EYE_PARCELABLE, eyes.get(this.eyeSide).getOriginal());
        startActivityForResult(intent, SHAPE_DESCRIPTION_ACTIVITY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SHAPE_DESCRIPTION_ACTIVITY) {
            if(resultCode == RESULT_OK){
                Shape shape = data.getParcelableExtra(SHAPE_PARCELABLE);
                latestShape.selectedParts = shape.selectedParts;
                if(!latestShape.description.equals(shape.description) && user != null){
                    latestShape.description = shape.description;
                    saveEyesDescription();
                }
            }
            // (resultCode == RESULT_CANCELED)//Write your code if there's no result
        }
    }

    void saveEyesDescription(){
        ShapesDetected shapes = this.shapesDetected.get(eyeSide);
        String eyeNode = eyeSide == ImageFilters.LEFT_EYE ? "left" : "right";
        StringBuilder description = new StringBuilder();
        List<String> organs = new ArrayList<>();

        for (Shape shape: shapes.shapes){
            description.append(shape.description);
            for(BodyPart part: shape.selectedParts){
                organs.add(part.name);
            }
        }
        Callback.taskManager(this,
                detectionRef.child(detectionKey)
                        .child(eyeNode)
                        .child("description")
                        .setValue(description.toString()));
        Callback.taskManager(this,
                detectionRef.child(detectionKey)
                        .child("organsList")
                        .setValue(organs));
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
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
            return "";
        }

        protected void onProgressUpdate() {
            // TODO: make a progress bar
        }

        protected void onPostExecute(String result) {
            uploadImages(shapesDetected.get(ImageFilters.LEFT_EYE).original,
                    shapesDetected.get(ImageFilters.RIGHT_EYE).original);
            changeEyeView(right_image.isEnabled() ? ImageFilters.LEFT_EYE : ImageFilters.RIGHT_EYE);
        }
    }

}
