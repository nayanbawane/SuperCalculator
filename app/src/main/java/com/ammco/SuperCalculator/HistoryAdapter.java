package com.ammco.SuperCalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private ArrayList<HistoryModel> historyModelArrayList;
    private Context context;

    public HistoryAdapter(ArrayList<HistoryModel> historyModelArrayList, Context context) {
        this.historyModelArrayList = historyModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_items,parent,false);
        return new HistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryModel historyData;
        historyData = historyModelArrayList.get(position);

        holder.tvResult.setText("= " + historyData.getResult());
        holder.tvEquation.setText(historyData.getEquation());

    }

    @Override
    public int getItemCount() {
        return historyModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvEquation;
        TextView tvResult;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEquation = itemView.findViewById(R.id.tvIdEquation);
            tvResult = itemView.findViewById(R.id.tvIdResults);
        }
    }
}
