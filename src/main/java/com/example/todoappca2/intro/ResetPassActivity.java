package com.example.todoappca2.intro;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todoappca2.R;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassActivity extends AppCompatActivity {

    private EditText inputEmail;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpass);

        auth = FirebaseAuth.getInstance();

        Button btnReset = findViewById(R.id.btn_resetpass);
        inputEmail = findViewById(R.id.email);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter your registered email", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(ResetPassActivity.this, "Email Sent!", Toast.LENGTH_SHORT).show();
                                inputEmail.setText("");
                            } else {
                                Toast.makeText(ResetPassActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}