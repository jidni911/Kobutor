package com.jidnivai.kobutor.activities.auth;

// RegisterActivity.java
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.jidnivai.kobutor.R;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtFullName, edtUsername, edtEmail, edtPassword, edtRetypePassword, edtDOB, edtPhoneNumber, edtAddress;
    private TextView tvErrorMessage;
    private RadioGroup radioGroupGender;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize UI elements
        edtFullName = findViewById(R.id.edtFullName);
        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtRetypePassword = findViewById(R.id.edtRetypePassword);
        edtDOB = findViewById(R.id.edtDOB);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtAddress = findViewById(R.id.edtAddress);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        btnRegister = findViewById(R.id.btnRegister);

        // Hide error message by default
        tvErrorMessage.setVisibility(View.GONE);

        // Register Button Click Listener
        btnRegister.setOnClickListener(v -> handleRegister());
    }

    private void handleRegister() {
        // Get values from the form fields
        String fullName = edtFullName.getText().toString().trim();
        String username = edtUsername.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String retypePassword = edtRetypePassword.getText().toString().trim();
        String gender = getSelectedGender();
        String dob = edtDOB.getText().toString().trim();
        String phoneNumber = edtPhoneNumber.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();

        // Validation logic
        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(username) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(retypePassword) || TextUtils.isEmpty(gender) ||
                TextUtils.isEmpty(dob) || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(address)) {
            tvErrorMessage.setVisibility(View.VISIBLE);
            tvErrorMessage.setText("All fields are required.");
            return;
        }

        if (username.length() < 5) {
            tvErrorMessage.setVisibility(View.VISIBLE);
            tvErrorMessage.setText("Username must be at least 5 characters.");
            return;
        }

        if (!isValidEmail(email)) {
            tvErrorMessage.setVisibility(View.VISIBLE);
            tvErrorMessage.setText("Email should be valid.");
            return;
        }

        if (password.length() < 6) {
            tvErrorMessage.setVisibility(View.VISIBLE);
            tvErrorMessage.setText("Password must be at least 6 characters.");
            return;
        }

        if (!password.equals(retypePassword)) {
            tvErrorMessage.setVisibility(View.VISIBLE);
            tvErrorMessage.setText("Passwords do not match.");
            return;
        }

        if (phoneNumber.length() < 10) {
            tvErrorMessage.setVisibility(View.VISIBLE);
            tvErrorMessage.setText("Phone number must be at least 10 digits.");
            return;
        }

        if (address.length() < 10) {
            tvErrorMessage.setVisibility(View.VISIBLE);
            tvErrorMessage.setText("Address must be at least 10 characters.");
            return;
        }

        // Hide error message on successful validation
        tvErrorMessage.setVisibility(View.GONE);

        // Handle successful registration (e.g., call backend API)
        showRegistrationSuccess();
    }

    private boolean isValidEmail(String email) {
        // Simple email regex validation
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private String getSelectedGender() {
        int selectedId = radioGroupGender.getCheckedRadioButtonId();

        if (selectedId == R.id.radioMale) {
            return "Male";
        } else if (selectedId == R.id.radioFemale) {
            return "Female";
        } else if (selectedId == R.id.radioOther) {
            return "Other";
        } else {
            return "";
        }
    }



    private void showRegistrationSuccess() {
        // Here, you could redirect the user to another activity, show a success message, etc.
    }
}
