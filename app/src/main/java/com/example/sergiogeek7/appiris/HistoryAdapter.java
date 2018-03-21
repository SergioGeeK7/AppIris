package com.example.sergiogeek7.appiris;

/**
 * Created by sergiogeek7 on 26/01/18.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sergiogeek7.appiris.firemodel.DetectionModel;
import com.example.sergiogeek7.appiris.utils.Callback;
import com.example.sergiogeek7.appiris.utils.HistoryDetail;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private List<DetectionModel> detections;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView label, date, current_state, done_label;
        public EditText edit_label;

        public MyViewHolder(View view) {
            super(view);
            label =  view.findViewById(R.id.label);
            date =  view.findViewById(R.id.date);
            current_state =  view.findViewById(R.id.current_state);
            edit_label = view.findViewById(R.id.edit_label);
            done_label = view.findViewById(R.id.done_label);
        }
    }


    public HistoryAdapter(List<DetectionModel> detections) {
        this.detections = detections;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DetectionModel history = detections.get(position);
        String date = new SimpleDateFormat("MMM dd, yyyy, hh:mm:ss aa")
                                .format(history.getDate());
        holder.label.setText(history.getLabel() != null ? history.getLabel() : date);
        holder.date.setText(date);
        if(history.getState() != null && history.getState().equals("done") ){
            holder.done_label.setVisibility(View.VISIBLE);
        }
        holder.edit_label.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                holder.date.setVisibility(View.VISIBLE);
                holder.label.setVisibility(View.VISIBLE);
                holder.edit_label.setVisibility(View.GONE);
                holder.label.setText(holder.edit_label.getText());
                saveLabel(holder.edit_label.getText().toString(), history.getKey(), holder.label.getContext());
                return true;
            }
            return false;
        });

        holder.edit_label.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                holder.date.setVisibility(View.VISIBLE);
                holder.edit_label.setVisibility(View.GONE);
            }else{
                InputMethodManager imm =
                        (InputMethodManager) holder.edit_label.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(v,0);
            }
        });
    }

    private void saveLabel(String label, String detectionKey, Context context){
        Callback.taskManager(context, database.getReference("detections")
                .child(detectionKey)
                .child("label")
                .setValue(label));
    }

//        holder.edit_label.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
//                if (event == null) {
//                    holder.date.setVisibility(View.VISIBLE);
//                    holder.label.setVisibility(View.VISIBLE);
//                    holder.edit_label.setVisibility(View.GONE);
//                    holder.label.setText(holder.edit_label.getText());
//                }
//                return true;
//            }
//        });

    @Override
    public int getItemCount() {
        return detections.size();
    }
}