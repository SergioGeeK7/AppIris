package com.example.sergiogeek7.appiris;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by sergiogeek7 on 9/02/18.
 */

    public class CloudMessagingIDService extends FirebaseInstanceIdService{

    private final static String TAG = CloudMessagingIDService.class.getName();
    public static String refreshedToken;

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
