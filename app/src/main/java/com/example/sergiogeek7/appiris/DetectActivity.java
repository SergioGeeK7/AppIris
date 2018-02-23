package com.example.sergiogeek7.appiris;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.example.sergiogeek7.appiris.appiris.BodyPart;
import com.example.sergiogeek7.appiris.appiris.BodySector;
import com.example.sergiogeek7.appiris.firemodel.DetectionModel;
import com.example.sergiogeek7.appiris.firemodel.EyeModel;
import com.example.sergiogeek7.appiris.opencv.DetectShapes;
import com.example.sergiogeek7.appiris.opencv.Psicosomaticas;
import com.example.sergiogeek7.appiris.opencv.Shape;
import com.example.sergiogeek7.appiris.opencv.ShapesDetected;
import com.example.sergiogeek7.appiris.utils.BitmapUtils;
import com.example.sergiogeek7.appiris.utils.Callback;
import com.example.sergiogeek7.appiris.utils.Gender;
import com.example.sergiogeek7.appiris.utils.GlobalState;
import com.example.sergiogeek7.appiris.utils.Message;
import com.example.sergiogeek7.appiris.utils.UserApp;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import static com.example.sergiogeek7.appiris.ImageFilters.LEFT_EYE;

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
                        changeEyeView(right_image.isEnabled() ? LEFT_EYE : ImageFilters.RIGHT_EYE);
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
    @BindView(R.id.save_btn)
    Button save_btn;

    private String detectionKey;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference("detections");
    DatabaseReference detectionRef = database.getReference("detections");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final static String TAG = DetectActivity.class.getName();
    public final static String BODY_PARTS = "BODY_PARTS";
    public final static String EYE_SIDE = "EYE_SIDE";
    public final static String EYE_PARCELABLE = "EYE_PARCELABLE";
    public final static String SHAPE_PARCELABLE = "SHAPE_PARCELABLE";
    public final static String DRAWABLE_RESOURCE = "DRAWABLE_RESOURCE";
    public final static int SHAPE_DESCRIPTION_ACTIVITY = 1;
    public final static String BODY_SECTOR = "BODY_SECTOR";
    private BodySector latestBodySector;
    private int eyeSide = 0;
    private List<Eye> eyes;
    private List<ShapesDetected> shapesDetected = new ArrayList<>();
    private Uri shareFilePath;
    private int sharedCount = 0;
    private Psicosomaticas psicosomaticas;
    private Gender gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.eyes = getIntent().getParcelableArrayListExtra(ViewImage.EYE_PARCELABLE);
        if(this.detectionKey == null){
            this.detectionKey = getIntent().getStringExtra(DetectionModel.class.getName());
        }
        gender = ((GlobalState)getApplication()).gender;
        psicosomaticas = new Psicosomaticas(this, gender);
        setContentView(R.layout.activity_detect);
        ButterKnife.bind(this);
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getBoolean(getString(R.string.show_tour_key), true)){
            Message.show(getString(R.string.tour_detect_activity), null, this);
        }
    }

    public void uploadImages(Bitmap left, Bitmap right){

        saveToFirebase(left,
                Callback.onSuccessListener(getString(R.string.analyzing), leftSnap ->
                        saveToFirebase(right,
                            Callback.onSuccessListener(getString(R.string.analyzing), rightSnap ->
                                            getUserApp(
                                    Callback.valueEventListener((err, data) -> {

                                            if(err != null){
                                                Log.e(TAG, err.getMessage());
                                                return;
                                            }
                                            UserApp userapp = data.getValue(UserApp.class);
                                            EyeModel leftEye = new EyeModel("",
                                                    leftSnap.getMetadata().getName());
                                            EyeModel rightEye = new EyeModel("",
                                                    rightSnap.getMetadata().getName());
                                            DetectionModel detectionModel
                                                    = new DetectionModel(leftEye, rightEye,
                                                            new Date(), user.getUid(),
                                                    userapp.getFullName().toLowerCase()
                                                    , userapp.getMessagingToken());

                                            DatabaseReference detection = detectionKey == null ?
                                                    detectionRef.push() : detectionRef.child(detectionKey);

                                            Callback.taskManager(this,
                                                    detection.setValue(detectionModel));
                                            detectionKey = detection.getKey();
                                    }, this) )
                        , this)),this));
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
        if(id == R.id.go_to_main_screen){
            startActivity(new Intent(this, MainScreen.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void share(){
        if(this.shareFilePath == null){
            File temp = BitmapUtils.createTempImageFile(this);
            this.shareFilePath = FileProvider
                                    .getUriForFile(this, ViewImage.PROVIDER_AUTHORITY, temp);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.share));
        builder.setPositiveButton(getString(R.string.only_image),
                (dialog, id) -> {
                    Bitmap bitmap = BitmapUtils.addWaterMark(shapesDetected.get(eyeSide).original,getString(R.string.app_name));
                    BitmapUtils.saveBitmap(DetectActivity.this,bitmap , shareFilePath);
                    BitmapUtils.shareImage(DetectActivity.this, shareFilePath);
                    dialog.cancel();
                });
        builder.setNeutralButton(getString(R.string.with_scheme),
                (dialog, id) -> {
                    Bitmap bitmap = BitmapUtils.addWaterMark(shapesDetected.get(eyeSide).original, getString(R.string.app_name));
                    Canvas canvas = new Canvas(bitmap);
                    canvas.drawBitmap(shapesDetected.get(eyeSide).scheme, 0,0, null);
                    BitmapUtils.saveBitmap(DetectActivity.this, bitmap , shareFilePath);
                    BitmapUtils.shareImage(DetectActivity.this, shareFilePath);
                    dialog.cancel();
                });
        builder.create().show();
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
        eye_view.updateView(shapes.shapes, shapes.original, shapes.scheme);
    }

    int getCurrentDrawable(int eyeSide){
        return eyeSide == LEFT_EYE ?
                gender == Gender.MAN ? R.drawable.ic_esquema__ohl: R.drawable.ic_esquema__oml:
                gender == Gender.WOMAN ? R.drawable.ic_esquema__omr : R.drawable.ic_esquema__ohr;
    }

    public void onLeftImage(View view){
        view.setEnabled(false);
        right_image.setEnabled(true);
        changeEyeView(LEFT_EYE);
    }

    public void onRightImage(View view){
        view.setEnabled(false);
        left_image.setEnabled(true);
        changeEyeView(ImageFilters.RIGHT_EYE);
    }

    @Override
    public void onShapeClick(Shape shape) {
        this.latestBodySector = psicosomaticas.getBodySector(shape, eyeSide);
        Intent intent = new Intent(this, ShapeDescriptionActivity.class);
        intent.putParcelableArrayListExtra(BODY_PARTS, this.latestBodySector.getParts());
        intent.putExtra(BODY_SECTOR, this.latestBodySector);
        intent.putExtra(DRAWABLE_RESOURCE, this.latestBodySector.drawableResource);
        intent.putExtra(EYE_SIDE, eyeSide);
        intent.putExtra(SHAPE_PARCELABLE, shape);
        intent.putExtra(EYE_PARCELABLE, eyes.get(this.eyeSide).getOriginal());
        startActivityForResult(intent, SHAPE_DESCRIPTION_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SHAPE_DESCRIPTION_ACTIVITY) {
            if(resultCode == RESULT_OK){
                ArrayList<BodyPart> bodyParts = data.getParcelableArrayListExtra(BODY_PARTS);
                latestBodySector.setParts(bodyParts);
                if(user != null && detectionKey != null){
                    saveEyesDescription();
                }
            }
            // (resultCode == RESULT_CANCELED)// Write your code if there's no result
        }
    }


    public void save(View v){
        uploadImages(shapesDetected.get(LEFT_EYE).original,
                shapesDetected.get(ImageFilters.RIGHT_EYE).original);
        v.setEnabled(false);
    }

    void saveEyesDescription(){
        String eyeNode = eyeSide == LEFT_EYE ? "left" : "right";
        StringBuilder description = new StringBuilder();
        List<String> organs = new ArrayList<>();

        for (BodySector bodySector: this.psicosomaticas.getAll()){
            for(BodyPart part: bodySector.getParts()){
                if(!part.selected){
                    continue;
                }
                int indexParagraph = description.indexOf(part.description);
                if(indexParagraph == -1){
                    description.append("\n").append(part.name).append(": ").append(part.description);
                }else{
                    description.insert(indexParagraph - 2,", " + part.name);
                }
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

        ProgressDialog p = new ProgressDialog(DetectActivity.this);

        protected String doInBackground(Void... params) {
            try {
                for (Eye eye: eyes) {

                        Bitmap bitmap = Picasso.with(DetectActivity.this)
                                .load(eye.getOriginal().getUri())
                                //.resize(400, 400)
                                .get();
                        ShapesDetected shapes = new DetectShapes(bitmap).detect();
                        Bitmap schemeOriginal = BitmapUtils.drawableToBitmap(
                                getResources().getDrawable(getCurrentDrawable(shapesDetected.size())));
                        shapes.scheme = BitmapUtils.getResizedBitmap(schemeOriginal,
                                             eye_view.getWidth(), eye_view.getHeight());
                        shapes.original = BitmapUtils.getResizedBitmap(shapes.original,
                                eye_view.getWidth(), eye_view.getHeight());
                        shapesDetected.add(shapes);
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
            return "";
        }

        @Override
        protected void onPreExecute() {
            p.setMessage(getString(R.string.analyzing));
            p.show();
            p.setTitle(getString(R.string.analyzing));
        }

        protected void onPostExecute(String result) {
            save_btn.setEnabled(true);
            changeEyeView(right_image.isEnabled() ? LEFT_EYE : ImageFilters.RIGHT_EYE);
            p.dismiss();
        }
    }

}
