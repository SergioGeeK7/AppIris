package com.example.sergiogeek7.appiris;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.example.sergiogeek7.appiris.firemodel.DetectionModel;
import com.example.sergiogeek7.appiris.firemodel.EyeModel;
import com.example.sergiogeek7.appiris.firemodel.MedicalHistoryForm;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnalysisRequest extends AppCompatActivity {

    @BindView(R.id.label)
    TextView label;
    @BindView(R.id.date)
    TextView date;
    DetectionModel detection;
    public static int DETECTION_REQUEST = 1;
    private String TAG = AnalysisRequest.class.getName();
    public static final String REVIEW_MODE = "REVIEW_MODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_request);
        ButterKnife.bind(this);
        detection = getIntent().getParcelableExtra(HistoryDoctor.DETECTION);
        updateUI();
    }

    public void goToMedicalHistory (View v){
        Intent intent = new Intent(this, FormMedicalHistory.class);
        intent.putExtra( MedicalHistoryForm.class.getName(), detection.getKey());
        startActivity(intent);
    }

    void updateUI(){
        label.setText(detection.fullNameCamelcase());
        date.setText(detection.dateString());
    }

    public void goToResults(View v){
        Intent intent = new Intent(this, AddResultsDoctor.class);
        intent.putExtra(HistoryDoctor.DETECTION, detection);
        startActivityForResult(intent, 1);
    }

    public void goToShowIris (View v){
        Intent intent = new Intent(this, ShowIris.class);
        intent.putExtra(HistoryDoctor.DETECTION, detection);
        startActivityForResult(intent, DETECTION_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == DETECTION_REQUEST && resultCode == RESULT_OK) {
            detection = data.getParcelableExtra(HistoryDoctor.DETECTION);
        }
    }
}
