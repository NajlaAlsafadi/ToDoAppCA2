package com.example.todoappca2.homeNav;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoappca2.R;
import com.example.todoappca2.Task;
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


public class SearchFragment extends Fragment {

    private RecyclerView searchRecyclerView;
    private RecyclerView.Adapter searchAdapter;
    private RecyclerView.LayoutManager searchLayoutManager;
    private ArrayList<Task> searchResults = new ArrayList<>();
    private EditText tagSearchEditText;
    private EditText statusSearchEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchRecyclerView = view.findViewById(R.id.searchRecyclerView);
        tagSearchEditText = view.findViewById(R.id.tagSearchEditText);
        statusSearchEditText = view.findViewById(R.id.statusSearchEditText);

        searchLayoutManager = new LinearLayoutManager(getActivity());
        searchAdapter = new SearchAdapter(searchResults);
        searchRecyclerView.setLayoutManager(searchLayoutManager);
        searchRecyclerView.setAdapter(searchAdapter);

        tagSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterTasksByTag(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        statusSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterTasksByStatus(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        return view;

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void filterTasksByTag(String tag) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            Query query = database.child("users").child(userId).child("tasks")
                    .orderByChild("tag")
                    .startAt(tag)
                    .endAt(tag + "\uf8ff");

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    searchResults.clear();
                    for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                        Task task = taskSnapshot.getValue(Task.class);
                        searchResults.add(task);
                    }
                    searchAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("Firebase", "Error querying data: " + databaseError.getMessage());
                }
            });
        }
    }
        private void filterTasksByStatus(String status){
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();

                Query query = database.child("users").child(userId).child("tasks")
                        .orderByChild("status")
                        .startAt(status)
                        .endAt(status + "\uf8ff");


                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        searchResults.clear();
                        for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                            Task task = taskSnapshot.getValue(Task.class);
                            searchResults.add(task);
                        }
                        searchAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("Firebase", "Error querying data: " + databaseError.getMessage());
                    }
                });
            }
        }

}