package com.example.sergiogeek7.appiris.utils;

import android.util.Log;

import com.example.sergiogeek7.appiris.firemodel.DetectionModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by sergiogeek7 on 30/01/18.
 */

public class Callback {

    public static ValueEventListener valueEventListener(CB<DatabaseError, DataSnapshot> cb){
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
               cb.call(null, snapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                cb.call(databaseError, null);
            }
        };
    }

    public static ChildEventListener childEventListener(CB<DatabaseError, DataSnapshot> cb){
        return new ChildEventListener (){

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
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
}
