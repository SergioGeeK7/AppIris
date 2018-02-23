package com.example.sergiogeek7.appiris.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;

import com.example.sergiogeek7.appiris.R;

/**
 * Created by sergiogeek7 on 23/02/18.
 */

public class Message {

    public static void show(String message, DialogInterface.OnClickListener callback, Context c){
        new AlertDialog.Builder(c)
                .setTitle(R.string.alert)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, callback)
                .show();
    }
}
