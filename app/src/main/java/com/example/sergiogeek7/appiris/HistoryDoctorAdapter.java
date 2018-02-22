package com.example.sergiogeek7.appiris;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.sergiogeek7.appiris.firemodel.DetectionModel;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by sergiogeek7 on 30/01/18.
 */

public class HistoryDoctorAdapter extends RecyclerView.Adapter<HistoryDoctorAdapter.ViewHolderDoctor> {

    private List<DetectionModel> detections;
    private boolean setDivider = true;
    private boolean isFilteringData = false;

    public class ViewHolderDoctor extends RecyclerView.ViewHolder {
        public TextView label, date, current_state;
        public EditText edit_label;
        public ImageView divider;

        public ViewHolderDoctor(View view) {
            super(view);
            label =  view.findViewById(R.id.label);
            date =  view.findViewById(R.id.date);
            current_state =  view.findViewById(R.id.current_state);
            edit_label = view.findViewById(R.id.edit_label);
            divider = view.findViewById(R.id.divider);
        }
    }

    public HistoryDoctorAdapter(List<DetectionModel> detections) {
        this.detections = detections;
    }

    @Override
    public HistoryDoctorAdapter.ViewHolderDoctor onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_row, parent, false);

        return new HistoryDoctorAdapter.ViewHolderDoctor(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryDoctorAdapter.ViewHolderDoctor holder, int position) {
        DetectionModel history = detections.get(position);
        holder.label.setText(history.fullNameCamelcase());
        holder.date.setText(history.dateString());
    }

    public void dataChanged (boolean isFilteringData){
        this.setDivider = true;
        this.isFilteringData = isFilteringData;
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return detections.size();
    }
}
