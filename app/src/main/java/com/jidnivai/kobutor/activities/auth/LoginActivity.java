package com.jidnivai.kobutor.activities.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.jidnivai.kobutor.R;
import com.jidnivai.kobutor.activities.messaging.HomeActivity;
import com.jidnivai.kobutor.service.AuthService;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {


    EditText usernameEditText, passwordEditText;
    Button loginButton;
    TextView forgotPasswordTextView, signUpTextView;

    AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        authService = new AuthService(this);
        setContentView(R.layout.activity_login);

        // Get the intent that started this activity


        // Initialize views
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        forgotPasswordTextView = findViewById(R.id.forgot_password);
        signUpTextView = findViewById(R.id.sign_up);
        boolean isSet = false;
        Intent i = getIntent();
        if (i != null) {
            // Get the username and password from the intent
            String username = i.getStringExtra("username");
            String password = i.getStringExtra("password");
            if (username != null && password != null) {
                // Set the username and password in the EditText fields
                usernameEditText.setText(username);
                passwordEditText.setText(password);
                isSet = true;
            }
        }
        if(!isSet){
            SharedPreferences sharedPreferences = getSharedPreferences("kobutor", MODE_PRIVATE);
            String username = sharedPreferences.getString("login_username", "");
            String password = sharedPreferences.getString("login_password", "");
            usernameEditText.setText(username);
            passwordEditText.setText(password);
        }


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
            finish(); // Close the LoginActivity
        });

        signUpTextView.setOnClickListener(v -> {
            // Navigate to Sign Up Activity
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish(); // Close the LoginActivity
        });
    }

    private void loginUser(String username, String password) {
        loginButton.setClickable(false);
        loginButton.setBackgroundColor(getResources().getColor(R. color. colorPrimaryDarkDark));
        authService.login(username, password, (object)->{
            System.out.println(object);

            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
            loginButton.setClickable(true);
            loginButton.setBackgroundColor(getResources().getColor(R. color. colorPrimaryDark));
            //TODO save token to shared preferences
            SharedPreferences.Editor editor = getSharedPreferences("kobutor", MODE_PRIVATE).edit();
            try {
                editor.putString("login_username",username);
                editor.putString("login_password", password);
                JSONObject user = object.getJSONObject("user");

                editor.putString("token", object.getString("jwtToken"));
                editor.putLong("id", user.getLong("id"));
                editor.putString("username", user.getString("username"));

                editor.putString("fullName", user.getString("fullName"));
                editor.putString("email", user.getString("email"));
                editor.putString("gender", user.getString("gender"));
                editor.putString("dob", user.getString("dob"));
                editor.putString("phoneNumber", user.getString("phoneNumber"));
                editor.putString("address", user.getString("address"));
                editor.putString("roles",user.getString("roles"));
//                editor.putString("profilePicture", object.getString("profilePicture"));
                editor.putBoolean("isLoggedIn", true);

            } catch (JSONException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            editor.apply();

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        },m->{
            loginButton.setBackgroundColor(getResources().getColor(R. color. colorPrimaryDark));
            Toast.makeText(LoginActivity.this, "Login failed "+m, Toast.LENGTH_SHORT).show();
            loginButton.setClickable(true);
        });

        // For now, just show a toast (you can replace this with real authentication logic)
//        Toast.makeText(this, "Logged in as: " + username + " with password: " + password,  Toast.LENGTH_SHORT).show();



        return;
        // Navigate to MainActivity (or home screen)
//        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//        startActivity(intent);
//        finish(); // Close the LoginActivity
    }
}