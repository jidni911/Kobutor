package com.jidnivai.kobutor.models;

import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User implements Serializable {
    private Long id;
    private String fullName;
    private String username;
    private String email;


    private Gender gender;
    private LocalDate dob;
    private Set<Role> roles;

    private String phoneNumber;
    private String address;

    private Image profilePicture;

    private Image coverPicture;

    private UserStatus status = UserStatus.INACTIVE;

    private Audio profileMusic;

    private String about;
    private String website;
    private String facebook;
    private String instagram;
    private String twitter;
    private String youtube;
    private String github;
    private String linkedin;
    private String pinterest;
    private String tiktok;
    private String snapchat;
    private String telegram;
    private String whatsapp;
    private String discord;
    private String reddit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Audio getProfileMusic() {
        return profileMusic;
    }

    public void setProfileMusic(Audio profileMusic) {
        this.profileMusic = profileMusic;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Image getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Image profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Image getCoverPicture() {
        return coverPicture;
    }

    public void setCoverPicture(Image coverPicture) {
        this.coverPicture = coverPicture;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getPinterest() {
        return pinterest;
    }

    public void setPinterest(String pinterest) {
        this.pinterest = pinterest;
    }

    public String getTiktok() {
        return tiktok;
    }

    public void setTiktok(String tiktok) {
        this.tiktok = tiktok;
    }

    public String getSnapchat() {
        return snapchat;
    }

    public void setSnapchat(String snapchat) {
        this.snapchat = snapchat;
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getDiscord() {
        return discord;
    }

    public void setDiscord(String discord) {
        this.discord = discord;
    }

    public String getReddit() {
        return reddit;
    }

    public void setReddit(String reddit) {
        this.reddit = reddit;
    }

    public enum Gender {

        MALE, FEMALE, NOT_SPECIFIED
    }

    public enum UserStatus {
        INACTIVE,
        ACTIVE,
        SUSPENDED,
        DELETED
    }

    public static class Role implements Serializable {


        private String name; // Unique role name

        private List<User> users;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }
    }

    public User() {
    }
    public User(JSONObject jsonObject) throws JSONException {
        if (jsonObject == null) return;

        if (jsonObject.has("id") && !jsonObject.isNull("id"))
            this.id = jsonObject.getLong("id");

        if (jsonObject.has("fullName") && !jsonObject.isNull("fullName"))
            this.fullName = jsonObject.getString("fullName");

        if (jsonObject.has("username") && !jsonObject.isNull("username"))
            this.username = jsonObject.getString("username");

        if (jsonObject.has("email") && !jsonObject.isNull("email"))
            this.email = jsonObject.getString("email");

        if (jsonObject.has("gender") && !jsonObject.isNull("gender"))
            this.gender = Gender.valueOf(jsonObject.getString("gender").toUpperCase());

        if (jsonObject.has("dob") && !jsonObject.isNull("dob") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.dob = LocalDate.parse(jsonObject.getString("dob"));
            }

        if (jsonObject.has("phoneNumber") && !jsonObject.isNull("phoneNumber"))
            this.phoneNumber = jsonObject.getString("phoneNumber");

        if (jsonObject.has("address") && !jsonObject.isNull("address"))
            this.address = jsonObject.getString("address");

        if (jsonObject.has("status") && !jsonObject.isNull("status"))
            this.status = UserStatus.valueOf(jsonObject.getString("status").toUpperCase());

        if (jsonObject.has("about") && !jsonObject.isNull("about"))
            this.about = jsonObject.getString("about");

        if (jsonObject.has("website") && !jsonObject.isNull("website"))
            this.website = jsonObject.getString("website");

        if (jsonObject.has("facebook") && !jsonObject.isNull("facebook"))
            this.facebook = jsonObject.getString("facebook");

        if (jsonObject.has("instagram") && !jsonObject.isNull("instagram"))
            this.instagram = jsonObject.getString("instagram");

        if (jsonObject.has("twitter") && !jsonObject.isNull("twitter"))
            this.twitter = jsonObject.getString("twitter");

        if (jsonObject.has("youtube") && !jsonObject.isNull("youtube"))
            this.youtube = jsonObject.getString("youtube");

        if (jsonObject.has("github") && !jsonObject.isNull("github"))
            this.github = jsonObject.getString("github");

        if (jsonObject.has("linkedin") && !jsonObject.isNull("linkedin"))
            this.linkedin = jsonObject.getString("linkedin");

        if (jsonObject.has("pinterest") && !jsonObject.isNull("pinterest"))
            this.pinterest = jsonObject.getString("pinterest");

        if (jsonObject.has("tiktok") && !jsonObject.isNull("tiktok"))
            this.tiktok = jsonObject.getString("tiktok");

        if (jsonObject.has("snapchat") && !jsonObject.isNull("snapchat"))
            this.snapchat = jsonObject.getString("snapchat");

        if (jsonObject.has("telegram") && !jsonObject.isNull("telegram"))
            this.telegram = jsonObject.getString("telegram");

        if (jsonObject.has("whatsapp") && !jsonObject.isNull("whatsapp"))
            this.whatsapp = jsonObject.getString("whatsapp");

        if (jsonObject.has("discord") && !jsonObject.isNull("discord"))
            this.discord = jsonObject.getString("discord");

        if (jsonObject.has("reddit") && !jsonObject.isNull("reddit"))
            this.reddit = jsonObject.getString("reddit");

        // Handle profilePicture and coverPicture
        if (jsonObject.has("profilePicture") && !jsonObject.isNull("profilePicture"))
            this.profilePicture = new Image(jsonObject.getJSONObject("profilePicture"));

        if (jsonObject.has("coverPicture") && !jsonObject.isNull("coverPicture"))
            this.coverPicture = new Image(jsonObject.getJSONObject("coverPicture"));

        if (jsonObject.has("profileMusic") && !jsonObject.isNull("profileMusic"))
            this.profileMusic = new Audio(jsonObject.getJSONObject("profileMusic"));

        // Handle roles (assuming it's a JSON array)
        if (jsonObject.has("roles") && !jsonObject.isNull("roles")) {
            this.roles = new HashSet<>();
            for (int i = 0; i < jsonObject.getJSONArray("roles").length(); i++) {
                JSONObject roleObject = jsonObject.getJSONArray("roles").getJSONObject(i);
                Role role = new Role();
                role.setName(roleObject.getString("name"));
                this.roles.add(role);
            }
        }
    }

}
