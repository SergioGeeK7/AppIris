package com.example.sergiogeek7.appiris;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sergiogeek7.appiris.utils.Callback;
import com.example.sergiogeek7.appiris.utils.Gender;
import com.example.sergiogeek7.appiris.utils.GlobalState;
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

import java.sql.SQLOutput;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainScreen extends AppCompatActivity {

    @BindView(R.id.avatar) ImageView avatar;
    @BindView(R.id.name) TextView displayName;
    @BindView(R.id.capture_row)
    LinearLayout captureRow;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final String TAG = MainScreen.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        ButterKnife.bind(this);
        Gender gender = (Gender) getIntent().getSerializableExtra(Gender.class.getName());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            getUserApp(user.getUid());
            if(CloudMessagingIDService.refreshedToken != null){
                saveMessagingToken(user.getUid());
            }
            //captureRow.setBackgroundResource(0);
        }else{
            captureRow.setBackgroundResource(R.drawable.border_sea);
            updateUI(gender, getString(R.string.default_name));
        }
    }

    void saveMessagingToken(String Uid){
        Callback.taskManager(this, database
                .getReference("users")
                .child(Uid)
                .child("messagingToken")
                .setValue(CloudMessagingIDService.refreshedToken));
    }

    private void getUserApp(String Uid){
        database.getReference("users")
                .child(Uid)
                .addListenerForSingleValueEvent(
                Callback.valueEventListener(
                        (err, data) -> {
                            if(err != null){
                                Log.e(TAG, err.getMessage());
                                return;
                            }
                            UserApp userApp = data.getValue(UserApp.class);
                            if(userApp == null){
                                logout(null);
                                return;
                            }
                            if(userApp.isDoctor()){
                                Intent intent = new Intent(this, HistoryDoctor.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                return;
                            }
                            Gender gender = userApp.getGender().equals("m") ? Gender.MAN : Gender.WOMAN;
                            updateUI(gender, userApp.getFullName());
                }, this));
    }

    private void updateUI(Gender gender, String name){
        saveGender(gender);
        avatar.setImageResource(gender == Gender.MAN ? R.drawable.male : R.drawable.female);
        displayName.setText(name);
    }

    void saveGender(Gender gender){
        GlobalState gs = (GlobalState) getApplication();
        gs.gender = gender;
    }

    public void logout(View view){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    Intent intent = new Intent(this, RegiterOptionalActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    this.finish();
                });
    }

    public void inviteFriend(View v){
        Intent intent2 = new Intent(); intent2.setAction(Intent.ACTION_SEND);
        intent2.setType("text/plain");
        intent2.putExtra(Intent.EXTRA_TEXT, getString(R.string.invite_message) );
        startActivity(Intent.createChooser(intent2, getString(R.string.app_name)));
    }

    public void goHistory(View v){
        startActivity(new Intent(this, History.class));
    }

    public void goAbout(View v){
        startActivity(new Intent(this, about.class));
    }

    public void goCaptureActivity(View v){
        Intent intent = new Intent(this, ViewImage.class);
        startActivity(intent);
    }

    public void goToSettings(View v){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}
