package com.example.gamingstore;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamingstore.data.AppDatabase;
import com.example.gamingstore.data.User.userEntity;

public class ProfileActivity extends AppCompatActivity {
    private AppDatabase database;
    private int currentUserId;
    private TextView firstNameText, secondNameText, usernameText, emailText, phoneText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Initialize database
        database = AppDatabase.getInstance(this);
        currentUserId = getIntent().getIntExtra("USER_ID", -1);
        if (currentUserId == -1) {
            Toast.makeText(this, "Error: User not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        loadUserData();
    }

    private void initializeViews() {
        firstNameText = findViewById(R.id.firstNameText);
        secondNameText = findViewById(R.id.secondNameText);
        usernameText = findViewById(R.id.usernameText);
        emailText = findViewById(R.id.emailText);
        phoneText = findViewById(R.id.phoneText);
    }

    private void loadUserData() {
        database.userDao().getUserById(currentUserId).observe(this, user -> {
            if (user != null) {
                String[] names = user.getFullName() != null ? user.getFullName().split(" ", 2) : new String[]{"", ""};
                firstNameText.setText(names.length > 0 ? names[0] : "");
                secondNameText.setText(names.length > 1 ? names[1] : "");
                usernameText.setText(user.getUsername());
                emailText.setText(user.getEmail());
                phoneText.setText(user.getPhoneNumber());
            }
        });
    }
}