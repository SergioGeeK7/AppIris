package com.example.sergiogeek7.appiris;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sergiogeek7.appiris.components.TabIris;
import com.example.sergiogeek7.appiris.firemodel.DetectionModel;
import com.example.sergiogeek7.appiris.firemodel.EyeModel;
import com.example.sergiogeek7.appiris.firemodel.MedicalHistoryForm;
import com.example.sergiogeek7.appiris.utils.Callback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddResultsDoctor extends AppCompatActivity {

    @BindView(R.id.content_text)
    TextView content_text;
    @BindView(R.id.affected_organs_tab)
    TabIris affected_organs_tab;
    @BindView(R.id.recommendations_tab)
    TabIris recommendations_tab;
    @BindView(R.id.nutritional_supplements_tab)
    TabIris nutritional_supplements_tab;
    @BindView(R.id.right_eye)
    Button right_eye_button;
    @BindView(R.id.left_eye)
    Button left_eye_button;
    @BindView(R.id.save_btn)
    Button save_btn;
    @BindView(R.id.send_btn)
    Button send_btn;
    @BindView(R.id.done_btn)
    Button done_btn;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference detectionsRef = database.getReference("detections");
    final DatabaseReference medicalHistory = database.getReference("medicalHistory");
    private String TAG = AddResultsDoctor.class.getName();
    private EyeModel currentEye;
    private int previousTabId = R.id.affected_organs_tab;
    private boolean doctor;
    DetectionModel detection;
    private final int HISTORY_FORM = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_results_doctor);
        ButterKnife.bind(this);
        this.detection = getIntent().getParcelableExtra(HistoryDoctor.DETECTION);
        this.doctor = getIntent().getBooleanExtra(History.ISDOCTOR, true);
        currentEye = detection.getLeft();
        affected_organs_tab.setEnabled(true);
        content_text.setText(currentEye.getDescription());
        if(!doctor){
            content_text.setEnabled(false);
            save_btn.setVisibility(View.GONE);
            send_btn.setVisibility(View.VISIBLE);
            done_btn.setVisibility(View.GONE);
        }
    }

    public void send(View v){
        Intent intent = new Intent(this, FormMedicalHistory.class);
        startActivityForResult(intent, HISTORY_FORM);
    }

    public void changeEditorTab(View v){
        updateUI(previousTabId, v.getId());
        previousTabId = v.getId();
        affected_organs_tab.setEnabled(affected_organs_tab.getId() == v.getId());
        nutritional_supplements_tab.setEnabled(nutritional_supplements_tab.getId() == v.getId());
        recommendations_tab.setEnabled(recommendations_tab.getId() == v.getId());
    }

    void updateUI(int previousTabId, int nextTabId){
        saveEyeData(previousTabId);
        setEyeData(nextTabId);
    }

    void setEyeData(int tabId){
        if(tabId == affected_organs_tab.getId()){
            content_text.setText(currentEye.getDescription());
        }else if(tabId == nutritional_supplements_tab.getId()){
            content_text.setText(detection.getSupplements());
        }else if (tabId == recommendations_tab.getId()){
            content_text.setText(detection.getRecommendations());
        }
    }

    void saveEyeData(int tabId){
        if(tabId == affected_organs_tab.getId()){
            currentEye.setDescription(content_text.getText().toString());
        }else if(tabId == nutritional_supplements_tab.getId()){
            detection.setSupplements(content_text.getText().toString());
        }else if (tabId == recommendations_tab.getId()){
            detection.setRecommendations(content_text.getText().toString());
        }
    }

    public void changeEye(View v){
        saveEyeData(previousTabId);
        if(v.getId() == left_eye_button.getId()){
            currentEye = detection.getRight();
            setEyeData(previousTabId);
            right_eye_button.setVisibility(View.VISIBLE);
            left_eye_button.setVisibility(View.GONE);
        }else {
            currentEye = detection.getLeft();
            setEyeData(previousTabId);
            left_eye_button.setVisibility(View.VISIBLE);
            right_eye_button.setVisibility(View.GONE);
        }
    }

    public void goToShowIris (View v){
        Intent intent = new Intent(this, ShowIris.class);
        intent.putExtra(HistoryDoctor.DETECTION, detection);
        intent.putExtra(History.ISDOCTOR, this.doctor);
        startActivity(intent);
    }

    public void save(View view){
        saveEyeData(previousTabId);
        Callback.taskManager(this,
        detectionsRef.child(detection.getKey())
                    .setValue(detection));
        Intent returnIntent = new Intent();
        returnIntent.putExtra(HistoryDoctor.DETECTION, detection);
        setResult(AnalysisRequest.RESULT_OK, returnIntent);
    }

    public void done (View v){
        detection.setState("done");
        save(null);
        Callback.taskManager(this,
                detectionsRef
                        .child(detection.getKey())
                        .child("analyzed")
                        .setValue(new Date()));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AnalysisRequest.DETECTION_REQUEST && resultCode == RESULT_OK) {
            detection = data.getParcelableExtra(HistoryDoctor.DETECTION);
        }else if(requestCode == HISTORY_FORM && resultCode == RESULT_OK){
            MedicalHistoryForm mh = data.getParcelableExtra(MedicalHistoryForm.class.getName());
            saveHistory(mh, detection.getKey());
        }
    }

    void saveHistory(MedicalHistoryForm mh, String detectionKey){
        mh.setUserUId(user.getUid());
        Callback.taskManager(this,medicalHistory.child(detectionKey).setValue(mh));
        Callback.taskManager(this,database.getReference("detections")
                .child(detectionKey)
                .child("state")
                .setValue("pending"));
        Toast.makeText(this, getString(R.string.sent), Toast.LENGTH_LONG).show();
    }
}
