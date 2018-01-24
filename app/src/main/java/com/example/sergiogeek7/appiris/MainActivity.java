package com.example.sergiogeek7.appiris;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sergiogeek7.appiris.utils.Country;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public final int ISMASCULINE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Choose authentication providers
        setContentView(R.layout.activity_main);
    }

    public void onGenderClick(View view){
        Log.v(String.valueOf(Log.VERBOSE), String.valueOf(view.getId()));
        String name = view.getId() == ISMASCULINE ? "man" : "woman";
        Intent intent = new Intent(this, ViewImage.class);
        intent.putExtra(EXTRA_MESSAGE, name);
        startActivity(intent);
    }



}
