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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MessageService {
    Context context;
    String url ="";
    public MessageService(Context context){
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
                            chat.setGroup(chatObject.getBoolean("group"));

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
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                if (token != null) {
                    headers.put("Authorization", "Bearer " + token); // Add token to header
                }
                return headers;
            }
        };;

        Volley.newRequestQueue(context).add(request);
    }



    public void loadChat(Long chatId, Consumer<Chat> onSuccess, Consumer<VolleyError> onError) {

    }


    public void createChat(List<User> users){

    }

    public void sendMessage(Chat chat, Message message){

    }


}
