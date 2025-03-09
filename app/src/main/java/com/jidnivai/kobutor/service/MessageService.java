package com.jidnivai.kobutor.service;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jidnivai.kobutor.R;
import com.jidnivai.kobutor.models.Chat;
import com.jidnivai.kobutor.models.Message;
import com.jidnivai.kobutor.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MessageService {
    Context context;
    String baseUrl = "";
    NetworkHelper networkHelper;
    public MessageService(Context context) {
        this.context = context;
        baseUrl = context.getString(R.string.api_url) + "/messege";
        networkHelper = NetworkHelper.getInstance(context);
    }
    public void loadAllChats(Consumer<List<Chat>> onSuccess, Consumer<VolleyError> onError) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, baseUrl, null,
                response -> {
                    try {
                        JSONArray chatsArray = response.getJSONArray("content"); // Extract chats list
                        List<Chat> chatList = new ArrayList<>();
                        for (int i = 0; i < chatsArray.length(); i++) {
                            JSONObject chatObject = chatsArray.getJSONObject(i);
                            chatList.add(MessageServiceHelper.parseChat(chatObject));
                        }
                        onSuccess.accept(chatList);
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
    public void loadChat(Long chatId, Consumer<List<Message>> onSuccess, Consumer<VolleyError> onError) {
        String url = baseUrl + "/chat/" + chatId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray messagesArray = response.getJSONArray("content");
                        List<Message> messages = new ArrayList<>();
                        for (int i = 0; i < messagesArray.length(); i++) {
                            JSONObject messageObject = messagesArray.getJSONObject(i);
                            messages.add(MessageServiceHelper.parseMessage(messageObject));
                        }
                        onSuccess.accept(messages);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        onError.accept(new VolleyError("JSON Parsing Error", e));
                    }
                },
                error -> onError.accept(error)
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return networkHelper.getAuthHeaders();
            }
        };
        networkHelper.addRequestQueue(request);
    }
    public void createChat(String name, long id, Consumer<Chat> onSuccess, Consumer<VolleyError> onError) {
        String api = baseUrl + "/newChat?name=" + name + "&ids=" + id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, api, null,
                response -> {
                    try {
                        onSuccess.accept(MessageServiceHelper.parseChat(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        onError.accept(new VolleyError("JSON Parsing Error", e));
                    }
                },
                error -> onError.accept(error)) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return networkHelper.getAuthHeaders();
            }
        };
        networkHelper.addRequestQueue(request);
    }
    public void sendMessage(String message, Long id, Consumer<Message> onSuccess, Consumer<VolleyError> onError) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, baseUrl, null,
                response -> {
                    try {
                        onSuccess.accept(MessageServiceHelper.parseMessage(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        onError.accept(new VolleyError("JSON Parsing Error", e));
                    }
                },
                error -> {
                    onError.accept(error);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return networkHelper.getAuthHeaders();
            }
            @Override
            public byte[] getBody() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("messege", message);
                    jsonObject.put("chatId", id);
                    return jsonObject.toString().getBytes(StandardCharsets.UTF_8);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return super.getBody();
            }
        };
        networkHelper.addRequestQueue(request);
    }
    public void searchContact(String query, Consumer<List<User>> onSuccess, Consumer<VolleyError> onError) {
        String url = baseUrl + "/newChatSuggestions/" + query;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray usersArray = response.getJSONArray("content");
                        onSuccess.accept(MessageServiceHelper.parseUserList(usersArray));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        onError.accept(new VolleyError("JSON Parsing Error", e));
                    }
                },
                error -> onError.accept(error)) {
            @Override
            public Map<String, String> getHeaders() {
                return networkHelper.getAuthHeaders();
            }
        };
        networkHelper.addRequestQueue(request);
    }
}