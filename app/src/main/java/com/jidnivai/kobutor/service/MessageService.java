package com.jidnivai.kobutor.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jidnivai.kobutor.R;
import com.jidnivai.kobutor.models.Chat;
import com.jidnivai.kobutor.models.Image;
import com.jidnivai.kobutor.models.Message;
import com.jidnivai.kobutor.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MessageService {
    Context context;
    String url = "";

    public MessageService(Context context) {
        this.context = context;
        url = context.getString(R.string.api_url) + "/messege";
    }

    public void loadAllChats(Consumer<List<Chat>> onSuccess, Consumer<VolleyError> onError) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("kobutor", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null); // Adjust key if different


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray chatsArray = response.getJSONArray("content"); // Extract chats list
                        List<Chat> chatList = new ArrayList<>();

                        for (int i = 0; i < chatsArray.length(); i++) {
                            JSONObject chatObject = chatsArray.getJSONObject(i);

                            Chat chat = new Chat();
                            chat.setId(chatObject.getLong("id"));
                            chat.setName(chatObject.getString("name"));
//                            chat.setGroup(chatObject.getBoolean("isGroup"));
                            chat.setLastMessage(chatObject.getString("lastMessage"));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                chat.setLastMessageTime(LocalDateTime.parse(chatObject.getString("lastMessageTime")));
                            }
                            chat.setMessegeCount(chatObject.getInt("messegeCount"));

                            if (!chatObject.isNull("groupImage")) {
                                JSONObject groupImageObj = chatObject.getJSONObject("groupImage");
                                Image groupImage = new Image(); // Assuming an Image class exists
                                groupImage.setId(groupImageObj.getLong("id"));
                                groupImage.setUrl(groupImageObj.getString("url")); // Adjust field names accordingly
                                chat.setGroupImage(groupImage);
                            }

                            // Parse members list
                            JSONArray membersArray = chatObject.getJSONArray("members");
                            List<User> members = new ArrayList<>();
                            for (int j = 0; j < membersArray.length(); j++) {
                                JSONObject userObj = membersArray.getJSONObject(j);
                                User user = new User();
                                user.setId(userObj.getLong("id"));
                                user.setUsername(userObj.getString("username"));
                                user.setFullName(userObj.getString("fullName"));
                                members.add(user);
                            }
                            chat.setMembers(members);

                            // Parse requestedMembers list
                            JSONArray requestedMembersArray = chatObject.getJSONArray("requestedMembers");
                            List<User> requestedMembers = new ArrayList<>();
                            for (int j = 0; j < requestedMembersArray.length(); j++) {
                                JSONObject userObj = requestedMembersArray.getJSONObject(j);
                                User user = new User();
                                user.setId(userObj.getLong("id"));
                                user.setUsername(userObj.getString("username"));
                                user.setFullName(userObj.getString("fullName"));
                                requestedMembers.add(user);
                            }
                            chat.setRequestedMembers(requestedMembers);

                            // Parse creator
                            if (!chatObject.isNull("creator")) {
                                JSONObject creatorObj = chatObject.getJSONObject("creator");
                                User creator = new User();
                                creator.setId(creatorObj.getLong("id"));
                                creator.setUsername(creatorObj.getString("username"));
                                chat.setCreator(creator);
                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                chat.setUpdatedAt(LocalDateTime.parse(chatObject.getString("updatedAt"))); // Convert if necessary
                                chat.setCreatedAt(LocalDateTime.parse(chatObject.getString("createdAt")));
                            }

                            chatList.add(chat);
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                if (token != null) {
                    headers.put("Authorization", "Bearer " + token); // Add token to header
                }
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(request);
    }


    public void loadChat(Long chatId, Consumer<List<Message>> onSuccess, Consumer<VolleyError> onError) {
        String url = context.getString(R.string.api_url) + "/messege/chat/" + chatId;

        SharedPreferences sharedPreferences = context.getSharedPreferences("kobutor", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null); // Adjust key if different


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray messagesArray = response.getJSONArray("content");
                        List<Message> messages = new ArrayList<>();
                        for (int i = 0; i < messagesArray.length(); i++) {
                            JSONObject messageObject = messagesArray.getJSONObject(i);
                            Message message = new Message();
                            message.setId(messageObject.getLong("id"));
                            message.setMessage(messageObject.getString("message"));
                            message.setRead(messageObject.getBoolean("read"));
                            message.setDeleted(messageObject.getBoolean("deleted"));
                            message.setSent(messageObject.getBoolean("sent"));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                message.setCreatedAt(LocalDateTime.parse(messageObject.getString("createdAt")));
                                message.setUpdatedAt(LocalDateTime.parse(messageObject.getString("updatedAt")));
                            }
                            JSONObject senderObject = messageObject.getJSONObject("sender");
                            User sender = new User();
                            sender.setId(senderObject.getLong("id"));
                            sender.setUsername(senderObject.getString("username"));
                            sender.setFullName(senderObject.getString("fullName"));
                            message.setSender(sender);
                            messages.add(message);
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
                Map<String, String> headers = new HashMap<>();
                if (token != null) {
                    headers.put("Authorization", "Bearer " + token); // Add token to header
                }
                return headers;
            }
        };
        Volley.newRequestQueue(context).add(request);

    }


    public void createChat(String name, long id, Consumer<Chat> onSuccess, Consumer<VolleyError> onError) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("kobutor", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        String api = context.getString(R.string.api_url) + "/messege/newChat?name=" + name + "&ids=" + id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, api, null,
                response -> {
                    try {
                        Chat chat = new Chat();
                        chat.setId(response.getLong("id"));
                        chat.setName(response.getString("name"));
//                            chat.setGroup(chatObject.getBoolean("isGroup"));
                        chat.setLastMessage(response.getString("lastMessage"));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            chat.setLastMessageTime(LocalDateTime.parse(response.getString("lastMessageTime")));
                        }

                        if (!response.isNull("groupImage")) {
                            JSONObject groupImageObj = response.getJSONObject("groupImage");
                            Image groupImage = new Image(); // Assuming an Image class exists
                            groupImage.setId(groupImageObj.getLong("id"));
                            groupImage.setUrl(groupImageObj.getString("url")); // Adjust field names accordingly
                            chat.setGroupImage(groupImage);
                        }

                        // Parse members list
                        JSONArray membersArray = response.getJSONArray("members");
                        List<User> members = new ArrayList<>();
                        for (int j = 0; j < membersArray.length(); j++) {
                            JSONObject userObj = membersArray.getJSONObject(j);
                            User user = new User();
                            user.setId(userObj.getLong("id"));
                            user.setUsername(userObj.getString("username"));
                            user.setFullName(userObj.getString("fullName"));
                            members.add(user);
                        }
                        chat.setMembers(members);

                        // Parse requestedMembers list
                        JSONArray requestedMembersArray = response.getJSONArray("requestedMembers");
                        List<User> requestedMembers = new ArrayList<>();
                        for (int j = 0; j < requestedMembersArray.length(); j++) {
                            JSONObject userObj = requestedMembersArray.getJSONObject(j);
                            User user = new User();
                            user.setId(userObj.getLong("id"));
                            user.setUsername(userObj.getString("username"));
                            user.setFullName(userObj.getString("fullName"));
                            requestedMembers.add(user);
                        }
                        chat.setRequestedMembers(requestedMembers);

                        // Parse creator
                        if (!response.isNull("creator")) {
                            JSONObject creatorObj = response.getJSONObject("creator");
                            User creator = new User();
                            creator.setId(creatorObj.getLong("id"));
                            creator.setUsername(creatorObj.getString("username"));
                            chat.setCreator(creator);
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            chat.setUpdatedAt(LocalDateTime.parse(response.getString("updatedAt"))); // Convert if necessary
                            chat.setCreatedAt(LocalDateTime.parse(response.getString("createdAt")));
                        }
                        onSuccess.accept(chat);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        onError.accept(new VolleyError("JSON Parsing Error", e));
                    }
                },
                error -> onError.accept(error)) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                if (token != null) {
                    headers.put("Authorization", "Bearer " + token); // Add token to header
                }
                return headers;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

    public void sendMessage(String message, Long id, Consumer<Message> onSuccess, Consumer<VolleyError> onError) {
        String url = context.getString(R.string.api_url) + "/messege";
        SharedPreferences sharedPreferences = context.getSharedPreferences("kobutor", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null); // Adjust key if different

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                response -> {
                    try{
                        Message newMessage = new Message();
                        newMessage.setId(response.getLong("id"));
                        newMessage.setMessage(response.getString("message"));
                        newMessage.setRead(response.getBoolean("read"));
                        newMessage.setDeleted(response.getBoolean("deleted"));
                        newMessage.setSent(response.getBoolean("sent"));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            newMessage.setCreatedAt(LocalDateTime.parse(response.getString("createdAt")));
                            newMessage.setUpdatedAt(LocalDateTime.parse(response.getString("updatedAt")));
                        }
                        JSONObject senderObject = response.getJSONObject("sender");
                        User sender = new User();
                        sender.setId(senderObject.getLong("id"));
                        sender.setUsername(senderObject.getString("username"));
                        sender.setFullName(senderObject.getString("fullName"));
                        newMessage.setSender(sender);
                        onSuccess.accept(newMessage);
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
                Map<String, String> headers = new HashMap<>();
                if (token != null) {
                    headers.put("Authorization", "Bearer " + token); // Add token to header
                }
                return headers;
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
        Volley.newRequestQueue(context).add(request);

    }

    public void searchContact(String query, Consumer<List<User>> onSuccess, Consumer<VolleyError> onError) {
        String url = context.getString(R.string.api_url) + "/messege/newChatSuggestions/" + query;
        SharedPreferences sharedPreferences = context.getSharedPreferences("kobutor", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null); // Adjust key if different

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray usersArray = response.getJSONArray("content");
                        List<User> users = new ArrayList<>();
                        for (int i = 0; i < usersArray.length(); i++) {
                            User user = new User();
                            JSONObject userObject = usersArray.getJSONObject(i);
                            user.setId(userObject.getLong("id"));
                            user.setUsername(userObject.getString("username"));
                            user.setFullName(userObject.getString("fullName"));
                            user.setEmail(userObject.getString("email"));
                            users.add(user);
                        }
                        onSuccess.accept(users);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        onError.accept(new VolleyError("JSON Parsing Error", e));
                    }
                },
                error -> onError.accept(error)) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                if (token != null) {
                    headers.put("Authorization", "Bearer " + token); // Add token to header
                }
                return headers;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }
}


