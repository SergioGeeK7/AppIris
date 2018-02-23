package com.example.sergiogeek7.appiris;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sergiogeek7.appiris.components.ButtonIris;
import com.example.sergiogeek7.appiris.firemodel.DetectionModel;
import com.example.sergiogeek7.appiris.firemodel.EyeModel;
import com.example.sergiogeek7.appiris.utils.Callback;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowIris extends AppCompatActivity {

    @BindView(R.id.photo_view)
    PhotoView photo_view;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.right_image)
    ButtonIris right_image;
    @BindView(R.id.left_image)
    ButtonIris left_image;
    DetectionModel detection;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference detectionsRef = database.getReference("detections");
    private boolean doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_iris);
        ButterKnife.bind(this);
        detection = getIntent().getParcelableExtra(HistoryDoctor.DETECTION);
        this.doctor = getIntent().getBooleanExtra(History.ISDOCTOR, true);
        if(!this.doctor){
            description.setKeyListener(null);
            description.setFocusable(false);
            description.setCursorVisible(false);
        }
        description.setText(detection.getDescription());
        updateUI(detection.getLeft());
    }

    public void setLeftEye(View v){
        v.setEnabled(false);
        right_image.setEnabled(true);
        updateUI(detection.getLeft());
    }

    public void setRightEye(View v){
        v.setEnabled(false);
        left_image.setEnabled(true);
        updateUI(detection.getRight());
    }

    public void end(View v){
        if(!this.doctor){
            finish();
            return;
        }
        detection.setDescription(description.getText().toString());
        Callback.taskManager(this, detectionsRef.child(detection.getKey())
                     .child("description")
                     .setValue(detection.getDescription()));
        Intent result = new Intent();
        result.putExtra(HistoryDoctor.DETECTION, detection);
        setResult(RESULT_OK, result);
        finish();
    }

    void updateUI(EyeModel eye){

        if(eye.bitmap != null){
            photo_view.setImageBitmap(eye.bitmap);
            return;
        }
        getImage(eye.getFilename(), Callback.onSuccessListenerByte(bytes -> {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        eye.bitmap = bitmap;
                        photo_view.setImageBitmap(bitmap);
        }, ShowIris.this));
    }

    void getImage(String path, OnSuccessListener<byte[]> callback){
        StorageReference storageRef = storage.getReference("detections").child(path);
        final long ONE_MEGABYTE = 1024 * 1024;
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(callback)
                  .addOnFailureListener(exception -> Log.e(ShowIris.class.getName(), exception.getMessage()));
    }

}
