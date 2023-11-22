package com.example.todoappca2.homeNav;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoappca2.R;
import com.example.todoappca2.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements ItemTouchHelperListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter HomeAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Task> myDataset = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {

            String userId = currentUser.getUid();
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();


            database.child("users").child(userId).child("tasks")
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    myDataset.clear();
                    for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                        Task task = taskSnapshot.getValue(Task.class);
                        myDataset.add(task);
                    }
                    HomeAdapter = new HomeAdapter(myDataset);
                    recyclerView.setAdapter(HomeAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        return view;
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_task, null);
        builder.setView(dialogView);

        final EditText tagEditText = dialogView.findViewById(R.id.tagEditText);
        final EditText descriptionEditText = dialogView.findViewById(R.id.descriptionEditText);
        final DatePicker deadlineDatePicker = dialogView.findViewById(R.id.deadlineDatePicker);
        final Spinner statusSpinner = dialogView.findViewById(R.id.statusSpinner);

        builder.setPositiveButton("Add", (dialog, which) -> {
            int day = deadlineDatePicker.getDayOfMonth();
            int month = deadlineDatePicker.getMonth();
            int year = deadlineDatePicker.getYear();
            String tag = tagEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String deadline = day + "/" + (month + 1) + "/" + year;
            String status = statusSpinner.getSelectedItem().toString();
            addTask(tag, description, deadline, status);
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addTask(String tag, String description, String deadline, String status) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();


            String taskId = database.child("users").child(userId).child("tasks")
                    .push().getKey();
            Task task = new Task(tag, description, deadline, status);
            task.setId(taskId);

            database.child("users").child(userId).child("tasks").child(taskId)
                    .setValue(task);
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.LEFT) {
            showUpdateDialog(viewHolder.getAdapterPosition());
        }
    }
    @Override
    public void onUpdate(int position) {
        showUpdateDialog(position);
    }
    @Override
    public void onDelete(int position) {
        showDeleteDialog(position);
    }
    private void showUpdateDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_task, null);
        builder.setView(dialogView);

        Task task = myDataset.get(position);
        final Spinner statusSpinner = dialogView.findViewById(R.id.statusSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.status_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        int positionOfStatus = adapter.getPosition(task.getStatus());
        statusSpinner.setSelection(positionOfStatus);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String status = statusSpinner.getSelectedItem().toString();
            updateTask(position, status);
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                HomeAdapter.notifyItemChanged(position);
            }
        });

        dialog.show();
    }

    private void updateTask(int position, String status) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            try {
                String userId = currentUser.getUid();
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();

                Task task = myDataset.get(position);
                task.setStatus(status);

                String taskId = task.getId();
                Log.d("TaskIdDebug", "Task ID before update: " + taskId);
                database.child("users").child(userId).child("tasks")
                        .child(taskId).child("status").setValue(status);
                myDataset.set(position, task);
                HomeAdapter.notifyItemChanged(position);

                Log.d("TaskIdDebug", "Task ID after update: " + taskId);
            } catch (Exception e) {
                Log.e("UpdateTaskError", "Error updating task", e);
            }
        }
    }
    private void showDeleteDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this task?");

        builder.setPositiveButton("Delete", (dialog, which) -> {
            deleteTask(position);
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                HomeAdapter.notifyItemChanged(position);
            }
        });
        dialog.show();
    }

    private void deleteTask(int position) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            try {
                String userId = currentUser.getUid();
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();

                Task task = myDataset.get(position);
                String taskId = task.getId();

                database.child("users").child(userId).child("tasks")
                        .child(taskId).removeValue();
                myDataset.remove(position);
                HomeAdapter.notifyItemRemoved(position);
            } catch (Exception e) {
                Log.e("DeleteTaskError", "Error deleting task", e);
            }
        }
    }

}