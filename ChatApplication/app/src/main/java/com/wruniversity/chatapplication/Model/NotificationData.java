package com.wruniversity.chatapplication.Model;

/**
 * Created by laptop88 on 3/17/2017.
 */

public class NotificationData {
    private String imageName;
    private int id;
    private String title;
    private String textMessage;
    private String sound;

    public NotificationData() {
        super();
    }
    public  NotificationData(String imageName,int id ,String tittle,String textMessage,String sound)
    {
        this.imageName=imageName;
        this.id=id;
        this.title=tittle;
        this.textMessage=textMessage;
        this.sound=sound;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }
}
