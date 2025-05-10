package com.example.gamingstore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamingstore.data.User.UserDao;
import com.example.gamingstore.data.User.userEntity;
import com.example.gamingstore.utils.SecurityUtils;
import com.example.gamingstore.data.AppDatabase;

public class signin_pageActivity extends AppCompatActivity {

    private EditText firstNameEdittext;
    private EditText secondNameEdittext;
    private EditText emailEdittext;
    private EditText phoneNumberEdittext;
    private EditText usernameEdittext;
    private EditText passwordEdittext;
    private EditText confirmPasswordEdittext;

    private AppDatabase database;
    private UserDao UserDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_page);

        database = AppDatabase.getInstance(this);
        UserDao = database.userDao();

        firstNameEdittext = findViewById(R.id.firstName);
        secondNameEdittext = findViewById(R.id.secondName);
        emailEdittext = findViewById(R.id.email);
        phoneNumberEdittext = findViewById(R.id.phoneNumber);
        usernameEdittext = findViewById(R.id.usernameLogin);
        passwordEdittext = findViewById(R.id.passwordLogin);
        confirmPasswordEdittext = findViewById(R.id.passwordLoginSecond);

        Button signUpButton = findViewById(R.id.signUpButton);
        ImageView backButton = findViewById(R.id.myImage);

        backButton.setOnClickListener(v -> {
            startActivity(new Intent(signin_pageActivity.this, login_pageActivity.class));
            finish();
        });

        signUpButton.setOnClickListener(v -> {

            String firstName = firstNameEdittext.getText().toString().trim();
            String secondName = secondNameEdittext.getText().toString().trim();
            String email = emailEdittext.getText().toString().trim();
            String phoneNumber = phoneNumberEdittext.getText().toString().trim();
            String username = usernameEdittext.getText().toString().trim();
            String password = passwordEdittext.getText().toString().trim();
            String confirmPassword = confirmPasswordEdittext.getText().toString().trim();

            if (firstName.isEmpty() || secondName.isEmpty() || email.isEmpty() ||
                    phoneNumber.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            String hashedPassword = SecurityUtils.hashPassword(password);
            userEntity newUser = new userEntity(
                username,
                email,
                hashedPassword,
                firstName + " " + secondName,
                phoneNumber,
                ""
            );

            new Thread(() -> {
                try {
                    UserDao.insert(newUser);
                    runOnUiThread(() -> {
                        Toast.makeText(signin_pageActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(signin_pageActivity.this, login_pageActivity.class));
                        finish();
                    });
                } catch (Exception e) {
                    runOnUiThread(() ->
                            Toast.makeText(signin_pageActivity.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }).start();
        });

    }

    private boolean isValidEmail(String email) {
        return !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPhone(String phone) {
        return !phone.isEmpty() && Patterns.PHONE.matcher(phone).matches();
    }

    private boolean isValidPassword(String password, String confirmPassword) {
        if (password.length() < 6) {
            passwordEdittext.setError("Password must be at least 6 characters");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordEdittext.setError("Passwords must match");
            return false;
        }
        return true;
    }
}