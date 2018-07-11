package com.example.sergiogeek7.appiris.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;

import com.example.sergiogeek7.appiris.R;

/**
 * Message
 *
 * Wrapper para mostrar mensajes en la aplicacion
 */

public class Message {

    public static void show(String message, DialogInterface.OnClickListener callback, Context c){
        new AlertDialog.Builder(c)
                .setTitle(R.string.app_name)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, callback)
                .show();
    }
}
