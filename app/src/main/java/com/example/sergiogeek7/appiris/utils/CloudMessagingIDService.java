package com.example.sergiogeek7.appiris.utils;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;



    public class CloudMessagingIDService extends FirebaseInstanceIdService{

    private final static String TAG = CloudMessagingIDService.class.getName();
    public static String refreshedToken;

        /**
         * Actualiza el token para enviar notificaciones
         */
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);
        refreshedToken = token;
    }
}
