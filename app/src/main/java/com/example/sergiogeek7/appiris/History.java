package com.example.sergiogeek7.appiris;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sergiogeek7.appiris.firemodel.DetectionModel;
import com.example.sergiogeek7.appiris.utils.Callback;
import com.example.sergiogeek7.appiris.utils.Country;
import com.example.sergiogeek7.appiris.utils.HistoryDetail;
import com.example.sergiogeek7.appiris.utils.RecyclerTouchListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {

    private List<DetectionModel> detections = new ArrayList<>();
    private RecyclerView recyclerView;
    private HistoryAdapter mAdapter;
    public static final String ISDOCTOR = "ISDOCTOR";
    private final String TAG = History.class.getName();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Query refDetections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView = findViewById(R.id.recycler_view);
        if(user == null){
            Toast.makeText(this, getString(R.string.must_register_first), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        refDetections = database.getReference("detections")
                                .orderByChild("userUId")
                                .equalTo(user.getUid());
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
                Intent intent = new Intent(History.this, AddResultsDoctor.class);
                intent.putExtra(HistoryDoctor.DETECTION, detection);
                intent.putExtra(ISDOCTOR, false);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                view.findViewById(R.id.date).setVisibility(View.GONE);
                TextView label = view.findViewById(R.id.label);
                label.setVisibility(View.GONE);
                EditText editLabel = view.findViewById(R.id.edit_label);
                editLabel.setText(label.getText());
                editLabel.setVisibility(View.VISIBLE);
                editLabel.requestFocus();
            }
        }));

        refDetections.addValueEventListener(Callback.valueEventListener((err, data) -> {

            if(err != null){
                Log.e(TAG, err.getMessage());
                return;
            }
            detections.clear();
            for (DataSnapshot postSnapshot: data.getChildren()) {
                DetectionModel detection = postSnapshot.getValue(DetectionModel.class);
                detection.setKey(postSnapshot.getKey());
                detections.add(detection);
            }
            mAdapter.notifyDataSetChanged();
        }, History.this));
    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }
}
