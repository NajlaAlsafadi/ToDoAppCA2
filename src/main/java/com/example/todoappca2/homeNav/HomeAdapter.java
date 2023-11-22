package com.example.todoappca2.homeNav;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoappca2.R;
import com.example.todoappca2.Task;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private ArrayList<Task> tasks;

    public HomeAdapter(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.tagTextView.setText(task.getTag());
        holder.descriptionTextView.setText(task.getDescription());
        holder.deadlineTextView.setText(task.getDeadline());
        holder.statusTextView.setText(task.getStatus());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tagTextView;
        public TextView descriptionTextView;
        public TextView deadlineTextView;
        public TextView statusTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tagTextView = itemView.findViewById(R.id.tagTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            deadlineTextView = itemView.findViewById(R.id.deadlineTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
        }
    }
}