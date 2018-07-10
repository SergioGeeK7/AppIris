package com.example.sergiogeek7.appiris.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.example.sergiogeek7.appiris.BuildConfig;
import com.example.sergiogeek7.appiris.HistoryDoctor;
import com.example.sergiogeek7.appiris.MainScreen;
import com.example.sergiogeek7.appiris.R;
import com.example.sergiogeek7.appiris.RegisterForm;
import com.example.sergiogeek7.appiris.RegiterOptionalActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sergiogeek7 on 16/05/18.
 */

public class RegisterFlow {

    private final static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static final int RC_SIGN_IN = 123;

    public static void success(Activity activity){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }
        DatabaseReference ref_user = database.getReference("users/" + user.getUid());
        ref_user.addListenerForSingleValueEvent(
                Callback.valueEventListener(
                        (err, data1) -> {
                            if(err != null){
                                return;
                            }
                            Class next =    !data1.exists() ? RegisterForm.class: MainScreen.class;
                            Intent intent = new Intent(activity, next);
                            activity.startActivity(intent);
                        }, activity));
    }

    public static void showRegisterActivity(Activity activity){
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build());
            activity.startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                            .setAvailableProviders(providers)
                            .setLogo(R.drawable.logo_sin_fondo)
                            .build(),
                    RC_SIGN_IN);
    }
}
