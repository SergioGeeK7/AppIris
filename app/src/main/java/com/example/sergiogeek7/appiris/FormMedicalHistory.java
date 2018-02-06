package com.example.sergiogeek7.appiris;


import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.sergiogeek7.appiris.databinding.ActivityFormMedicalHistoryBinding;
import com.example.sergiogeek7.appiris.firemodel.MedicalHistoryForm;
import com.example.sergiogeek7.appiris.utils.Callback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;

public class FormMedicalHistory extends AppCompatActivity {

    private String TAG = FormMedicalHistory.class.getName();
    List<View> steps = new ArrayList<>();
    private int currentStep = 0;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final DatabaseReference medicalHistory = database.getReference("medicalHistory");
    String historyKey;
    MedicalHistoryForm mh;
    boolean review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_medical_history);
        review = getIntent().getBooleanExtra(AnalysisRequest.REVIEW_MODE,
                                                    false);
        historyKey = getIntent().getStringExtra(MedicalHistoryForm.class.getName());
        if(historyKey == null){
            Log.e(TAG, "is null wey");
        }
        if(review){
            getHistory(review);
        }else{
            getLatestHistory(review);
        }
    }

    void getHistory(boolean review){
        medicalHistory.child(historyKey)
                       .addListenerForSingleValueEvent(
                               Callback.valueEventListener((err, data) ->
                                       bindData(err, data, review)));
    }

    void getLatestHistory(boolean review){
        medicalHistory.orderByChild("userUId")
                        .equalTo(user.getUid())
                        .limitToLast(1)
                        .addListenerForSingleValueEvent(
                                Callback.valueEventListener((err, data) ->
                                        bindData(err, data, review)));
    }

    void bindData(DatabaseError err, DataSnapshot data, boolean reviewMode){
        if(err != null){
            Log.e(TAG, err.getMessage());
            return;
        }
        ActivityFormMedicalHistoryBinding binding =
                DataBindingUtil.setContentView(this,
                        R.layout.activity_form_medical_history);
        if(data.exists()){

            if(!data.getKey().equals("medicalHistory")){
                mh = data.getValue(MedicalHistoryForm.class);
            }else{
                mh = data.getChildren()
                        .iterator()
                        .next()
                        .getValue((MedicalHistoryForm.class));
            }

        }else{
            mh = new MedicalHistoryForm();
        }
        binding.setForm(mh);
        binding.setReviewMode(reviewMode);
        setTabs();
    }

    void saveHistory(){
        mh.setUserUId(user.getUid());
        medicalHistory.child(historyKey).setValue(mh);
        database.getReference("detections").child(historyKey).child("isInProcess").setValue(true);
    }

    void setTabs(){
        steps.add((View)findViewById(R.id.step1));
        steps.add((View)findViewById(R.id.step2));
        steps.add((View)findViewById(R.id.step3));
        steps.add((View)findViewById(R.id.step4));
        steps.add((View)findViewById(R.id.step5));
    }

    public void forward(View v){

        if(currentStep == steps.size() - 1) {
            if(this.review){
                saveHistory();
            }
            finish();
            return;
        }
        steps.get(currentStep).setVisibility(View.GONE);
        steps.get(++currentStep).setVisibility(View.VISIBLE);
        if(currentStep == steps.size() - 1) {
            ((Button)v).setText(getString(R.string.finish));
        }
    }
}
