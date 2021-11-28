package com.example.musicplayer;

import android.media.MediaPlayer;
import android.provider.MediaStore;

public class MusicInfo {
    private String title;
    private long duration;
    private String artist;
    private String path;
    public MusicInfo(){
        super();
    }
    public MusicInfo(String title,long duration,String artist,String path){
        this.artist = artist;
        this.duration = duration;
        this.title = title;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
