package com.jidnivai.kobutor.service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jidnivai.kobutor.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.Consumer;

public class AuthService {

    Context context;

    public AuthService(Context context) {
        this.context = context;
    }

    public void login(String username, String password, Consumer<JSONObject> onSuccess, Consumer<String> onError) {
        String url = context.getString(R.string.api_url) + "/auth/login";

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                onSuccess::accept
                ,
                error -> {
                    onError.accept(error.getMessage());
                }
        ) {
            @Override
            public byte[] getBody() {
                JSONObject body = new JSONObject();
                try {
                    body.put("username", username);
                    body.put("password", password);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                return body.toString().getBytes();
            }
        };

        queue.add(request);

    }

    public void signup(
            String fullName,
            String username,
            String email,
            String password,
            String retypedPassword,
            String gender,
            String dob,
            String phoneNumber,
            String address,
            Consumer<String> onSuccess,
            Consumer<String> onError
    ) {
        String url = context.getString(R.string.api_url) + "/auth/signup";
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject body = new JSONObject();
        try {
            body.put("fullName", fullName);
            body.put("username", username);
            body.put("email", email);
            body.put("password", password);
            body.put("retypePassword", retypedPassword);
            body.put("gender", gender);
            body.put("dob", dob);
            body.put("phoneNumber", phoneNumber);
            body.put("address", address);
        } catch (JSONException e) {
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body,
                response -> {
                    onSuccess.accept(response.toString());
                },
                error -> {
                    onError.accept(error.getMessage());
                }) {
            @Override
            public byte[] getBody() {
                return body.toString().getBytes();
            }
        };
        queue.add(request);
    }

    public void echo(Runnable onSuccess, Runnable onError) {
        String url = context.getString(R.string.api_url) + "/auth/echo";

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    onSuccess.run();
                },
                error -> {
                    onError.run();
                }
        ){};

        queue.add(request);
    }
}
