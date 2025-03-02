package com.jidnivai.kobutor.activities.auth;

// ForgotPasswordActivity.java
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.jidnivai.kobutor.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText edtUsername, edtEmail;
    private TextView tvErrorMessage;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize UI elements
        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Hide error message by default
        tvErrorMessage.setVisibility(View.GONE);

        // Button click listener
        btnSubmit.setOnClickListener(v -> handleForgotPassword());
    }

    private void handleForgotPassword() {
        String username = edtUsername.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();

        // Validate the input fields
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email)) {
            tvErrorMessage.setVisibility(View.VISIBLE);
            tvErrorMessage.setText("Both fields are required.");
            return;
        }

        // Validate the email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tvErrorMessage.setVisibility(View.VISIBLE);
            tvErrorMessage.setText("Please enter a valid email address.");
            return;
        }

        // Hide error message if validation passes
        tvErrorMessage.setVisibility(View.GONE);

        // Here, send the request to the backend to handle password reset.
        // For now, just show a success message (you can add backend logic later)
        // You could also open a confirmation dialog or redirect to another activity
        showSuccessMessage();
    }

    private void showSuccessMessage() {
        // Example: Show success message and clear fields
        edtUsername.setText("");
        edtEmail.setText("");
        tvErrorMessage.setVisibility(View.GONE);
        // You can redirect to another activity, show a dialog, or something else here
    }
}
