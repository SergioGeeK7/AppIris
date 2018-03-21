package com.example.sergiogeek7.appiris;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.sergiogeek7.appiris.utils.Callback;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegiterOptionalActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private final String TAG = MainActivity.class.getName();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiter_optional);
        ButterKnife.bind(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            goToMainScreen();
        }
    }

    private void goToMainScreen(){
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference ref_user = database.getReference("users/" + user.getUid());
                ref_user.addListenerForSingleValueEvent(
                        Callback.valueEventListener(
                                (err, data1) -> {
                                    if(err != null){
                                        Log.e(TAG, err.getMessage());
                                        return;
                                    }
                                    Class next = !data1.exists() ? RegisterForm.class:
                                                    data1.hasChild("doctor") ?
                                                    HistoryDoctor.class : MainScreen.class;
                                    Intent intent = new Intent(this, next);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                        }, RegiterOptionalActivity.this));
            } else {
                Log.e(TAG, "nop");
            }
        }
    }

    public void register(View view){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.ic_logo_sin_fondo)
                        .build(),
                RC_SIGN_IN);
    }

    public void skipRegister(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
