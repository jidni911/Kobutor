package com.jidnivai.kobutor.service;

import android.os.Build;

import com.jidnivai.kobutor.models.Chat;
import com.jidnivai.kobutor.models.Image;
import com.jidnivai.kobutor.models.Message;
import com.jidnivai.kobutor.models.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageServiceHelper {

    public static Chat parseChat(JSONObject chatObject) throws JSONException {
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

        chat.setMembers(MessageServiceHelper.parseUserList(membersArray));

        // Parse requestedMembers list
        JSONArray requestedMembersArray = chatObject.getJSONArray("requestedMembers");
        List<User> requestedMembers = MessageServiceHelper.parseUserList(requestedMembersArray);
        chat.setRequestedMembers(requestedMembers);

        // Parse creator
        if (!chatObject.isNull("creator")) {
            JSONObject creatorObj = chatObject.getJSONObject("creator");

            chat.setCreator(MessageServiceHelper.parseUser(creatorObj));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chat.setUpdatedAt(LocalDateTime.parse(chatObject.getString("updatedAt"))); // Convert if necessary
            chat.setCreatedAt(LocalDateTime.parse(chatObject.getString("createdAt")));
        }


        return chat;
    }

    public static Message parseMessage(JSONObject messageObject) throws JSONException {
        Message message = new Message();
        message.setId(messageObject.getLong("id"));
        message.setMessage(messageObject.getString("message"));
        message.setRead(messageObject.getBoolean("read"));
        message.setDeleted(messageObject.getBoolean("deleted"));
        message.setSent(messageObject.getBoolean("sent"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            message.setUpdatedAt(LocalDateTime.parse(messageObject.getString("updatedAt")));
            message.setCreatedAt(LocalDateTime.parse(messageObject.getString("createdAt")));
        }
        message.setSender(new User(messageObject.getJSONObject("sender")));
        return message;
    }

    public static User parseUser(JSONObject userObject) throws JSONException {
        User user = new User();
        user.setId(userObject.getLong("id"));
        user.setUsername(userObject.getString("username"));
        user.setFullName(userObject.getString("fullName"));
        return user;
    }

    public static List<User> parseUserList(JSONArray userArray) throws JSONException {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < userArray.length(); i++) {
            users.add(parseUser(userArray.getJSONObject(i)));
        }
        return users;
    }
}
