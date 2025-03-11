package com.jidnivai.kobutor.service;


import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jidnivai.kobutor.R;
import com.jidnivai.kobutor.models.Chat;
import com.jidnivai.kobutor.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class UserService {
    Context context;
    String baseUrl = "";
    NetworkHelper networkHelper;
    public UserService(Context context) {
        this.context = context;
        baseUrl = context.getString(R.string.api_url) + "/user";
        networkHelper = NetworkHelper.getInstance(context);
    }

    public void getUserById(Long id, Consumer<User> onSuccess, Consumer<VolleyError> onError) {
        String url = baseUrl + "/" + id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        User user = new User(response);
                        onSuccess.accept(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        onError.accept(new VolleyError("JSON Parsing Error", e));
                    }
                },
                error -> onError.accept(error)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return networkHelper.getAuthHeaders();
            }
        };
        networkHelper.addRequestQueue(request);
    }

    public void changeProfilePicture(Uri imageUri) {
        String url = baseUrl + "/changeProfilePicture";  // Update with your Spring Boot API URL


    }


}
