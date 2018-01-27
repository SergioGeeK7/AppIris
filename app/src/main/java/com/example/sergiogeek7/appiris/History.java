package com.example.sergiogeek7.appiris;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.sergiogeek7.appiris.firemodel.DetectionModel;
import com.example.sergiogeek7.appiris.utils.Country;
import com.example.sergiogeek7.appiris.utils.HistoryDetail;
import com.example.sergiogeek7.appiris.utils.RecyclerTouchListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {

    private List<DetectionModel> detections = new ArrayList<>();
    private RecyclerView recyclerView;
    private HistoryAdapter mAdapter;

    private final String TAG = History.class.getName();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference refDetections = database.getReference("detections").child(user.getUid());

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new HistoryAdapter(detections);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerTouchListener(getApplicationContext(), recyclerView,
                        new History.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                DetectionModel detection = detections.get(position);
                Toast.makeText(getApplicationContext(),
                        detection.getDate() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        refDetections.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    DetectionModel detection = postSnapshot.getValue(DetectionModel.class);
                    detections.add(detection);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
    }

}
