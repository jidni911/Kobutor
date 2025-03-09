package com.jidnivai.kobutor.service;

import android.content.Context;
import android.content.SharedPreferences;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class NetworkHelper {
    private static NetworkHelper instance;
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;

    private NetworkHelper(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        sharedPreferences = context.getSharedPreferences("kobutor", Context.MODE_PRIVATE);
    }

    public static synchronized NetworkHelper getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkHelper(context);
        }
        return instance;
    }


    public void addRequestQueue(JsonObjectRequest jsonObjectRequest) {
        requestQueue.add(jsonObjectRequest);
    }

    public Map<String, String> getAuthHeaders() {
        Map<String, String> headers = new HashMap<>();
        String token = sharedPreferences.getString("token", null);
        if (token != null) {
            headers.put("Authorization", "Bearer " + token);
        }
        return headers;
    }
}
