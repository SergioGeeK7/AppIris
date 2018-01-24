package com.example.sergiogeek7.appiris;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import butterknife.BindView;
import butterknife.ButterKnife;

public class TermsAndConditions extends AppCompatActivity {

    //@BindView(R.id.decline_terms) RadioButton decline_terms_button;
    @BindView(R.id.accept_terms) RadioButton accept_terms_button;
    @BindView(R.id.radio_group) RadioGroup radio_group;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        ButterKnife.bind(this);
    }

    public void forward (View view){
        int id = radio_group.getCheckedRadioButtonId();
        if(id == accept_terms_button.getId()){
            Intent intent = new Intent(this, RegiterOptionalActivity.class);
            startActivity(intent);
        }else{
            finish();
        }
    }
}
