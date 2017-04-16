package com.wruniversity.chatapplication.Model;

/**
 * Created by laptop88 on 2/25/2017.
 */

public class UserChat {

    /**
     * The Constant EXTRA_DATA.
     */
    public static final String EXTRA_DATA = "extraID";
    public static final String EXTRA_DATA_USER = "extra_User_name";
    public static final String EXTRA_GENDER = "extraGender";
    public static final String EXTRA_ID_Vid_YOUTUBE = "extraYoutube";

    private String Username;
    private String ID;
    private String Email;
    private String gender;
    private String status;

    public UserChat() {
        this.ID = "";
        this.Username = "";
        this.Email = "";
        this.gender = "";
    }

    public UserChat(String ID, String Username, String Email, String gender,String status) {
        this.ID = ID;
        this.Username = Username;
        this.Email = Email;
        this.gender = gender;
        this.status=status;
    }


    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public boolean isFemale() {
        if(gender=="Female")
        return true;
        else
            return false;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGender() {
        return gender;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
