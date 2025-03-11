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
        return new Chat(chatObject);
    }

    public static Message parseMessage(JSONObject messageObject) throws JSONException {
        return new Message(messageObject);
    }

    public static User parseUser(JSONObject userObject) throws JSONException {
        return new User(userObject);
    }

    public static List<User> parseUserList(JSONArray userArray) throws JSONException {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < userArray.length(); i++) {
            users.add(parseUser(userArray.getJSONObject(i)));
        }
        return users;
    }
}
