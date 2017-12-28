package com.example.sergiogeek7.appiris;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public final int ISMAN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onGenderClick(View view){
        Log.v(String.valueOf(Log.VERBOSE), String.valueOf(view.getId()));
        String name = view.getId() == ISMAN ? "man" : "woman";
        Intent intent = new Intent(this, ViewImage.class);
        intent.putExtra(EXTRA_MESSAGE, name);
        startActivity(intent);
    }

}
