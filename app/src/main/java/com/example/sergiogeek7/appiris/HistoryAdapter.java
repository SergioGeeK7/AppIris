package com.example.sergiogeek7.appiris;

/**
 * Created by sergiogeek7 on 26/01/18.
 */
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sergiogeek7.appiris.firemodel.DetectionModel;
import com.example.sergiogeek7.appiris.utils.HistoryDetail;

import java.text.SimpleDateFormat;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private List<DetectionModel> detections;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView label, date, current_state;

        public MyViewHolder(View view) {
            super(view);
            label =  view.findViewById(R.id.label);
            date =  view.findViewById(R.id.date);
            current_state =  view.findViewById(R.id.current_state);
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
        //holder.year.setText(movie.getYear());
    }

    @Override
    public int getItemCount() {
        return detections.size();
    }
}