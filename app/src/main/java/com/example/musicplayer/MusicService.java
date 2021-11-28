package com.example.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.util.Map;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private LocalBinder localBinder = new LocalBinder();

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public MusicService() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String path = intent.getStringExtra("path");
        try{
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (Exception e){
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    public void initPlayMusic(String path){
        try{
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void playMusic(){
        mediaPlayer.start();
    }
    public void pauseMusic(){
        mediaPlayer.pause();
    }
    public void touchPlay(int progress){
        mediaPlayer.seekTo(progress);
    }
    public class LocalBinder extends Binder{
        MusicService getService(){
            return MusicService.this;
        }
    }
}