package com.example.sergiogeek7.appiris;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sergiogeek7.appiris.databinding.ActivityFormMedicalHistoryBinding;
import com.example.sergiogeek7.appiris.firemodel.MedicalHistoryForm;
import com.example.sergiogeek7.appiris.utils.Callback;
import com.example.sergiogeek7.appiris.utils.Gender;
import com.example.sergiogeek7.appiris.utils.GlobalState;
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
    Gender gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_medical_history);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        historyKey = getIntent().getStringExtra(MedicalHistoryForm.class.getName());
        this.gender = ((GlobalState)getApplication()).gender;
        if(historyKey != null){
            getHistory();
        }else{
            getLatestHistory();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    void getHistory(){
        medicalHistory.child(historyKey)
                       .addListenerForSingleValueEvent(
                               Callback.valueEventListener((err, data) ->
                                       bindData(err, data, true), FormMedicalHistory.this));
    }

    void getLatestHistory(){
        medicalHistory.orderByChild("userUId")
                        .equalTo(user.getUid())
                        .limitToLast(1)
                        .addListenerForSingleValueEvent(
                                Callback.valueEventListener((err, data) ->
                                        bindData(err, data, false), FormMedicalHistory.this));
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
        binding.setGender(gender);
        setTabs();
    }


    void setTabs(){
        steps.add((View)findViewById(R.id.step1));
        steps.add((View)findViewById(R.id.step2));
        steps.add((View)findViewById(R.id.step3));
        steps.add((View)findViewById(R.id.step4));
        steps.add((View)findViewById(R.id.step5));
    }

    public void forward(View v){
        if(!validate(steps.get(currentStep))){
            return;
        }
        if(currentStep == steps.size() - 1) {
            end();
            return;
        }
        steps.get(currentStep).setVisibility(View.GONE);
        steps.get(++currentStep).setVisibility(View.VISIBLE);
        if(currentStep == steps.size() - 1) {
            ((Button)v).setText(getString(R.string.finish));
        }
    }

    boolean validate(View view){
        String message = "";
        if(view.getId() == R.id.step2){
            if(!mh.isBeef() && !mh.isChicken() && !mh.isPork()){
                message += getString(R.string.missing_aliment)  + "\n";
            }
            if (!mh.isSalty() && !mh.isSweet() && !mh.isCold() && !mh.isHot()){
                message +=  getString(R.string.missing_state) + "\n";
            }
            if(mh.getBreakfastAliments() == null || mh.getBreakfastAliments().isEmpty()){
                message += getString(R.string.missing_breakfast_description);
            }
        }
        if(view.getId() == R.id.step3){
            if(mh.getLunchAliments() == null || mh.getLunchAliments().isEmpty()){
                message += getString(R.string.missing_lunch_description) + "\n";
            }
            if(mh.getDinnerAliments() == null || mh.getDinnerAliments().isEmpty()){
                message += getString(R.string.missing_dinner_description);
            }
        }
        if(view.getId() == R.id.step5){
            if(this.gender == Gender.WOMAN && !mh.isFewPeriod() && !mh.isDailyPeriod() && !mh.isMonthlyPeriod()
                    && !mh.isColic()){
                message +=  getString(R.string.missing_period);
            }
        }
        if(message.isEmpty()){
            return true;
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return false;
    }

    void end(){
        Intent result = new Intent();
        result.putExtra(MedicalHistoryForm.class.getName(), mh);
        setResult(RESULT_OK, result);
        finish();
    }
}
