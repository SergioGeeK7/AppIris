package com.example.sergiogeek7.appiris.activities;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.sergiogeek7.appiris.R;
import com.example.sergiogeek7.appiris.bl.Gender;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Pantalla principal
 *
 * pantalla para escoger genero
 */


public class ChooseSex extends AppCompatActivity {

    private final String TAG = MainScreen.class.getName();
    @BindView(R.id.man) ImageView manView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    public void onGenderClick(View view){
        Log.e(TAG, manView.getId() + " " + view.getId() + " " + R.id.man);
        Gender gender = view.getId() == R.id.man ? Gender.MAN: Gender.WOMAN;
        Intent intent = new Intent(this, MainScreen.class);
        intent.putExtra(Gender.class.getName(), gender);
        startActivity(intent);
    }



}
