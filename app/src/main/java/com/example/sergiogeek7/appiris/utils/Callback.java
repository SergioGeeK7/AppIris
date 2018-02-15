package com.example.sergiogeek7.appiris.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.sergiogeek7.appiris.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;

/**
 * Created by sergiogeek7 on 30/01/18.
 */

public class Callback {

    public static ValueEventListener valueEventListener(CB<DatabaseError, DataSnapshot> cb, Context c){
        ProgressDialog p = new ProgressDialog(c);
        p.setMessage(c.getString(R.string.loading));
        p.show();
        p.setTitle(c.getString(R.string.loading));
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
               p.dismiss();
               cb.call(null, snapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                cb.call(databaseError, null);
            }
        };
    }


    public static void taskManager(Context c, Task task){
        ProgressDialog p = new ProgressDialog(c);
        p.setMessage(c.getString(R.string.loading));
        p.show();
        p.setTitle(c.getString(R.string.loading));
        task.addOnCompleteListener(task1 -> {
             p.dismiss();
        });
    }

    public static OnSuccessListener<byte[]> onSuccessListenerByte (CB2<byte[]> cb, Context c){
        ProgressDialog p = new ProgressDialog(c);
        p.setMessage(c.getString(R.string.loading));
        p.show();
        p.setTitle(c.getString(R.string.loading));
        return bytes -> {
            p.dismiss();
            cb.call(bytes);
        };
    }

    public static OnSuccessListener<UploadTask.TaskSnapshot> onSuccessListener (String message,
            CB2<UploadTask.TaskSnapshot> cb, Context c){
        ProgressDialog p = new ProgressDialog(c);
        p.setMessage(message);
        p.setTitle(message);
        p.show();
        return o -> {
            p.dismiss();
            cb.call(o);
        };
    }

    public static ChildEventListener childEventListener(CB<DatabaseError, DataSnapshot> cb, Context c){
        ProgressDialog p = new ProgressDialog(c);
        p.setMessage(c.getString(R.string.loading));
        p.show();
        p.setTitle(c.getString(R.string.loading));
        return new ChildEventListener (){

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                p.dismiss();
                cb.call(null, dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                cb.call(databaseError, null);
            }
        };
    }

    public interface CB <F, S>{
        void call(F err,S data);
    }

    public interface CB2 <R>{
        void call(R data);
    }
}
