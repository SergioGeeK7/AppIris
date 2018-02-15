package com.example.sergiogeek7.appiris;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sergiogeek7.appiris.utils.Callback;
import com.example.sergiogeek7.appiris.utils.Gender;
import com.example.sergiogeek7.appiris.utils.UserApp;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainScreen extends AppCompatActivity {

    private FirebaseUser user;
    @BindView(R.id.avatar) ImageView avatar;
    @BindView(R.id.name) TextView displayName;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final String TAG = MainScreen.class.getName();
    Gender gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        ButterKnife.bind(this);
        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");
        Gender gender = (Gender) getIntent().getSerializableExtra(Gender.class.getName());
        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null && CloudMessagingIDService.refreshedToken != null){
            saveMessagingToken();
        }

        if(gender != null){
            updateUI(gender, null);
            return;
        }
        getUserGender();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, FirebaseInstanceId.getInstance().getToken());
    }

    void saveMessagingToken(){
        Callback.taskManager(this, database
                .getReference("users")
                .child(user.getUid())
                .child("messagingToken")
                .setValue(CloudMessagingIDService.refreshedToken));
    }

    private void getUserGender(){
        DatabaseReference ref_user = database.getReference("users")
                                            .child(user.getUid());

        ref_user.addListenerForSingleValueEvent(
                Callback.valueEventListener(
                        (err, data) -> {
                            if(err != null){
                                Log.e(TAG, err.getMessage());
                                return;
                            }
                            UserApp userApp = data.getValue(UserApp.class);
                            updateUI(userApp.getGender().equals("man") ? Gender.MAN : Gender.WOMAN,
                                    userApp.getFullName());
                }, MainScreen.this));
    }

    private void updateUI(Gender gender, String name){
        this.gender = gender;
        avatar.setImageResource(gender == Gender.MAN ? R.drawable.male : R.drawable.female);
        displayName.setText(name == null ? getString(R.string.default_name) : name);
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


    public void goAbout(View v){
        startActivity(new Intent(this, about.class));
    }


    public void goCaptureActivity(View v){
        Intent intent = new Intent(this, ViewImage.class);
        intent.putExtra(Gender.class.getName(), this.gender);
        startActivity(intent);
    }
}
