package com.example.sergiogeek7.appiris;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sergiogeek7.appiris.utils.Gender;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainScreen extends AppCompatActivity {

    private FirebaseUser user;
    @BindView(R.id.avatar) ImageView avatar;
    @BindView(R.id.name) TextView displayName;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final String TAG = MainScreen.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        ButterKnife.bind(this);
        Gender gender = (Gender) getIntent().getSerializableExtra(Gender.class.getName());
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(gender != null){
            updateUI(gender);
            return;
        }
        getUserGender();
    }

    private void getUserGender(){
        DatabaseReference ref_user = database.getReference("users")
                                            .child(user.getUid())
                                            .child("gender");

        ref_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String gender = snapshot.getValue(String.class);
                updateUI(gender.equals("man") ? Gender.MAN : Gender.WOMAN);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
    }

    private void updateUI(Gender gender){
        avatar.setImageResource(gender == Gender.MAN ? R.drawable.male : R.drawable.female);
        displayName.setText(user == null ? getString(R.string.default_name) : user.getDisplayName());
    }

    public void logout(View view){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    Intent intent = new Intent(this, RegiterOptionalActivity.class);
                    startActivity(intent);
                });
    }

    public void goHistory(View v){
        startActivity(new Intent(this, History.class));
    }


    public void goCaptureActivity(View v){
        startActivity(new Intent(this, ViewImage.class));
    }
}
