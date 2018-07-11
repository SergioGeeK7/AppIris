package com.example.sergiogeek7.appiris.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.example.sergiogeek7.appiris.R;
import com.example.sergiogeek7.appiris.activities.MainScreen;
import com.example.sergiogeek7.appiris.activities.RegiterOptionalActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TermsAndConditions extends AppCompatActivity {

    @BindView(R.id.accept_terms) RadioButton accept_terms_button;
    @BindView(R.id.radio_group) RadioGroup radio_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Intent intent = new Intent(this, MainScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.activity_terms_and_conditions);
        ButterKnife.bind(this);
    }

    public void forward (View view){
        int id = radio_group.getCheckedRadioButtonId();
        if(id == accept_terms_button.getId()){
            Intent intent = new Intent(this, RegiterOptionalActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else{
            finish();
        }
    }
}
