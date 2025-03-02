package com.jidnivai.kobutor.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.jidnivai.kobutor.R;
import com.jidnivai.kobutor.activities.messaging.HomeActivity;

public class LoginActivity extends AppCompatActivity {


    EditText usernameEditText, passwordEditText;
    Button loginButton;
    TextView forgotPasswordTextView, signUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Initialize views
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        forgotPasswordTextView = findViewById(R.id.forgot_password);
        signUpTextView = findViewById(R.id.sign_up);


        // Set onClickListener for login button
        loginButton.setOnClickListener(v -> {
            // Validate inputs
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Handle login logic (for example, call an API for authentication)
                loginUser(username, password);
            }
        });

        // Set onClickListeners for "Forgot Password" and "Sign Up" links
        forgotPasswordTextView.setOnClickListener(v -> {
            // Navigate to Forgot Password Activity
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        signUpTextView.setOnClickListener(v -> {
            // Navigate to Sign Up Activity
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser(String username, String password) {
        // For now, just show a toast (you can replace this with real authentication logic)
        Toast.makeText(this, "Logged in as: " + username + " with password: " + password,  Toast.LENGTH_SHORT).show();

        // Navigate to MainActivity (or home screen)
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish(); // Close the LoginActivity
    }
}