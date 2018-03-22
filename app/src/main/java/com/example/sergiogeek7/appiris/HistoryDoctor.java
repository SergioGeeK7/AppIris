package com.example.sergiogeek7.appiris;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sergiogeek7.appiris.firemodel.DetectionModel;
import com.example.sergiogeek7.appiris.utils.Callback;
import com.example.sergiogeek7.appiris.utils.RecyclerTouchListener;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryDoctor extends AppCompatActivity {


    @BindView(R.id.search_box)
    EditText search_box;
    private List<DetectionModel> detections = new ArrayList<>();
    private RecyclerView recyclerView;
    private HistoryDoctorAdapter mAdapter;
    private final String TAG = HistoryDoctor.class.getName();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference refDetections = database.getReference("detections");
    public static String DETECTION = "DETECTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_doctor);
        recyclerView = findViewById(R.id.recycler_view);
        ButterKnife.bind(this);
        if(user == null){
            return;
        }
        mAdapter = new HistoryDoctorAdapter(detections);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // attach listeners
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerTouchListener(getApplicationContext(), recyclerView,
                        new History.ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                DetectionModel detection = detections.get(position);
                                Intent intent =
                                        new Intent(HistoryDoctor.this,
                                                AnalysisRequest.class);
                                intent.putExtra(DETECTION, detection);
                                startActivity(intent);
                            }

                            @Override
                            public void onLongClick(View view, int position) {

                            }
                        }));

        search_box.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                getDetectionsData(search_box.getText().toString());
            }
            return false;
        });
        getDetectionsData("");
    }

    void getDetectionsData(String term){
        if(term.isEmpty()){
            refDetections.orderByChild("state")
                    .equalTo("pending")
                    .addValueEventListener(
                    Callback.valueEventListener((err, data) ->
                            updateHistoryAdapter(err, data, !term.isEmpty()), HistoryDoctor.this));
        }else{
            refDetections.orderByChild("fullName")
                    .startAt(term)
                    .endAt(term + "\uf8ff")
                    .addValueEventListener(
                            Callback.valueEventListener((err, data) ->
                                        updateHistoryAdapter(err, data, !term.isEmpty()),
                                    HistoryDoctor.this));
        }

    }

    void updateHistoryAdapter(DatabaseError err, DataSnapshot data,boolean filtering){
        if(err != null){
            Log.e(TAG, err.getMessage());
            return;
        }
        detections.clear();
        for (DataSnapshot postSnapshot: data.getChildren()) {
            DetectionModel detection = postSnapshot.getValue(DetectionModel.class);
            //if(detection.getState() != null && detection.getState().equals("pending")){
            detection.setKey(postSnapshot.getKey());
            detections.add(detection);
        }
        if(!filtering){
            Collections.reverse(detections);
        }
        mAdapter.dataChanged(filtering);
    }

    public boolean onCreateOptionsMenu(Menu paramMenu){
        getMenuInflater().inflate(R.menu.menu_doctor, paramMenu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem paramMenuItem){
        if (paramMenuItem.getItemId() == R.id.logout)
        {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(task -> {
                        Intent intent = new Intent(this, RegiterOptionalActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        this.finish();
                    });
            return true;
        }
        return super.onOptionsItemSelected(paramMenuItem);
    }
}
