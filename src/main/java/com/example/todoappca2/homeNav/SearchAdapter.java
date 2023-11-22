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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private ArrayList<Task> searchResults;

    public SearchAdapter(ArrayList<Task> searchResults) {
        this.searchResults = searchResults;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Task task = searchResults.get(position);
        holder.tagTextView.setText(task.getTag());
        holder.descriptionTextView.setText(task.getDescription());
        holder.deadlineTextView.setText(task.getDeadline());
        holder.statusTextView.setText(task.getStatus());
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        public TextView tagTextView;
        public TextView descriptionTextView;
        public TextView deadlineTextView;
        public TextView statusTextView;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            tagTextView = itemView.findViewById(R.id.tagTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            deadlineTextView = itemView.findViewById(R.id.deadlineTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
        }
    }
}