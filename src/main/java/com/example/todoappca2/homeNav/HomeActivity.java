package com.example.todoappca2.homeNav;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.todoappca2.R;
import com.example.todoappca2.intro.MainActivity;
import com.example.todoappca2.intro.SignUpActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private DatabaseReference userRef;
    private ImageView navHeaderImage;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.nav_view);
        if (savedInstanceState == null) {
            Fragment fragment = new HomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_content, fragment)
                    .commit();
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Fragment fragment = null;
                if (id == R.id.nav_home) {
                    fragment = new HomeFragment();
                } else if (id == R.id.nav_profile) {
                    fragment = new ProfileFragment();
                } else if (id == R.id.nav_search) {
                    fragment = new SearchFragment();
                } else if (id == R.id.nav_logout) {
                    auth.signOut();
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }

                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_content, fragment)
                            .commit();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        user = auth.getCurrentUser();
        if (user != null) {

            userRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        String name = task.getResult().child("name").getValue(String.class);
                        String phone = task.getResult().child("phone").getValue(String.class);


                        TextView navHeaderName = navigationView.getHeaderView(0).findViewById(R.id.nav_header_name);
                        TextView navHeaderPhone = navigationView.getHeaderView(0).findViewById(R.id.nav_header_phone);
                        navHeaderImage = navigationView.getHeaderView(0).findViewById(R.id.nav_header_image);

                        navHeaderName.setText("Welcome, " + name + "!");
                        navHeaderPhone.setText("Phone: " + phone);
                        loadProfileImage();
                    }
                }
            });
        }
    }

    private void loadProfileImage() {

        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("profile_images/" + user.getUid() + ".jpg");

        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(navHeaderImage);
        }).addOnFailureListener(exception -> {

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}