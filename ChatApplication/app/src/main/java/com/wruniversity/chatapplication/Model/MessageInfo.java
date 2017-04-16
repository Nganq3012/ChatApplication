package com.wruniversity.chatapplication.Model;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

/** Created by laptop88 on 2/25/2017. */

public class MessageInfo {
    private  String convEmoji;
    private String conv;
    private String UserSend;
    private String sender;
    private boolean isSelf;
    private boolean isOnline;
    private String date;
    private String gender;
    private String photoUrl;
    private String receive,email2user;
    private Uri pathImage;
    private String URLYTB;
    private String address;
    public MessageInfo() {
    }

    public MessageInfo(String address,String URLYTB, String gender,String conv,String convEmoji, String photoUrl, String sender, String receive, boolean isSelf, boolean isOnline, String date, String UserSend,String email2user) {
        this.address=address;
        this.URLYTB=URLYTB;
        this.gender=gender;
        this.conv = conv;
        this.convEmoji=convEmoji;
        this.photoUrl = photoUrl;
        this.sender = sender;
        this.receive = receive;
        this.isSelf = isSelf;
        this.isOnline = isOnline;
        this.date = date;
        this.UserSend = UserSend;
        this.email2user = email2user;
    }


    public boolean isSent() {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        return user.getEmail().contains(sender);
    }

    public String getConvEmoji() {
        return convEmoji;
    }

    public void setConvEmoji(String convEmoji) {
        this.convEmoji = convEmoji;
    }

    public String getConv() {
        return conv;
    }

    public void setConv(String conv) {
        this.conv = conv;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getReceive() {
        return receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }

    public String getEmail2user() {
        return email2user;
    }

    public void setEmail2user(String email2user) {
        this.email2user = email2user;
    }

    public Uri getPathImage() {
        return pathImage;
    }

    public void setPathImage(Uri pathImage) {
        this.pathImage = pathImage;
    }

    public String getUserSend() {
        return UserSend;
    }

    public void setUserSend(String userSend) {
        UserSend = userSend;
    }

    public String getURLYYB() {
        return URLYTB;
    }

    public void setURLYYB(String URLYYB) {
        this.URLYTB = URLYYB;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
