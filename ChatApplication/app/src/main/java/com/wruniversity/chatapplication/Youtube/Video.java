package com.wruniversity.chatapplication.Youtube;

/**
 * Created by laptop88 on 4/8/2017.
 */
public class Video {

    public String videoId;
    public String title;
    public String url;

    public Video(String videoId, String title, String url) {
        this.title = title;
        this.videoId = videoId;
        this.url = url;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

