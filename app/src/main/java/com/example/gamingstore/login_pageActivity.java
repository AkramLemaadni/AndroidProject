package com.example.gamingstore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamingstore.data.User.UserDao;
import com.example.gamingstore.data.User.userEntity;
import com.example.gamingstore.utils.SecurityUtils;
import com.example.gamingstore.utils.SessionManager;
import com.example.gamingstore.data.AppDatabase;

public class login_pageActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button buttonLogin;
    private TextView buttonSignin;
    private AppDatabase database;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        // Initialize views
        usernameEditText = findViewById(R.id.usernameLogin);
        passwordEditText = findViewById(R.id.passwordLogin);
        buttonLogin = findViewById(R.id.loginButton);
        buttonSignin = findViewById(R.id.signinButton);

        // Initialize database
        database = AppDatabase.getInstance(this);
        userDao = database.userDao();

        // Login button click
        buttonLogin.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            String hashedPassword = SecurityUtils.hashPassword(password);
            Log.d("LOGIN_DEBUG", "Hashed password: " + hashedPassword);

            new Thread(() -> {
                userEntity user = userDao.login(username, hashedPassword);
                Log.d("LOGIN_DEBUG", "User found: " + (user != null));

                runOnUiThread(() -> {
                    if (user != null) {
                        // Create session
                        SessionManager session = new SessionManager(login_pageActivity.this);
                        session.createLoginSession(user.getUserId());
                        Log.d("LOGIN_DEBUG", "Session created for user: " + user.getUserId());

                        // Go to home page
                        Intent intent = new Intent(login_pageActivity.this, home_pageActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(login_pageActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });

        // Sign up button click
        buttonSignin.setOnClickListener(v -> {
            startActivity(new Intent(login_pageActivity.this, signin_pageActivity.class));
        });
    }
}