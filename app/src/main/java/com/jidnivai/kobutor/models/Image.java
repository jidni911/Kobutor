package com.jidnivai.kobutor.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Image implements Serializable {

    private Long id;

    private String path;

    private String url;

    private String name;

    protected byte[] imageBytes;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image() {
    }

    public Image(JSONObject jsonObject) throws JSONException {
            if (jsonObject==null) return;
            if(jsonObject.has("id") && !jsonObject.isNull("id")) this.id = jsonObject.getLong("id");
            if (jsonObject.has("path") && !jsonObject.isNull("path") && jsonObject.get("path") instanceof String) this.path = jsonObject.getString("path");
            if (jsonObject.has("url") && !jsonObject.isNull("url") && jsonObject.get("url") instanceof String) this.url = jsonObject.getString("url");
            if (jsonObject.has("name") && !jsonObject.isNull("name") && jsonObject.get("name") instanceof String) this.name = jsonObject.getString("name");
            if (jsonObject.has("imageBytes") && !jsonObject.isNull("imageBytes") && jsonObject.get("imageBytes") instanceof String) this.imageBytes = jsonObject.getString("imageBytes").getBytes();
            if (jsonObject.has("description") && !jsonObject.isNull("description") && jsonObject.get("description") instanceof String) this.description = jsonObject.getString("description");

    }
}
