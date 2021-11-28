package com.example.musicplayer;

import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlay extends AppCompatActivity {
    ImageButton last;
    ImageButton next;
    ImageButton play;
    Button playStyle;
    TextView seekbar_start;
    TextView seekbar_end;
    TextView musicTitle;
    SeekBar seekbar;
    MediaPlayer mediaPlayer = new MediaPlayer();
    ServiceConnection serviceConnection;
    MusicService musicService;
    boolean isSeekBarChanging = false;
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musicplayer_main);
        initView();
        initMusic();
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                musicService = ((MusicService.LocalBinder) iBinder).getService();
                mediaPlayer = musicService.getMediaPlayer();
                seekBar();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        Intent intent = new Intent(MusicPlay.this, MusicService.class);
        bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (getDrawableId(play)){
                    case R.drawable.play:
                    {
                        musicService.playMusic();
                        play.setImageResource(R.drawable.pause);
                        play.setTag(R.drawable.pause);
                        break;
                    }
                    case R.drawable.pause:{
                        musicService.pauseMusic();
                        play.setImageResource(R.drawable.play);
                        play.setTag(R.drawable.play);
                        break;
                    }
                }

            }
        });
        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i==0) {
                    i = list.size() - 1;
                }
                else i = i-1;
                playMusic();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i==list.size()-1)
                    i = 0;
                else i = i+1;
                playMusic();
            }
        });
        playStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playStyle.showContextMenu();
            }
        });
        playStyle.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(0,0,0,"单曲循环");
                contextMenu.add(0,1,0,"随机播放");
                contextMenu.add(0,2,0,"顺序播放");
            }
        });

    }
    private void seekBar() {
        Timer timer = new Timer();
        seekbar.setMax(mediaPlayer.getDuration());
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isSeekBarChanging) {
                    seekbar.setProgress(mediaPlayer.getCurrentPosition());
                }
            }
        }, 0, 100);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                switch ((String)playStyle.getTag()){
                    case "singleLoop"://单曲循环
                        playMusic();
                        break;
                    case "shuffle"://随机播放
                        Random random = new Random();
                        i = random.nextInt(list.size());
                        playMusic();
                        break;
                    case "sequentially"://顺序播放
                        if(i==list.size()-1)
                            i = 0;
                        else i = i+1;
                        playMusic();
                        break;
                }
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int position = mediaPlayer.getCurrentPosition();
                seekbar_end.setText(calculateTime(mediaPlayer.getDuration()/1000));
                seekbar_start.setText(calculateTime(position / 1000));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeekBarChanging = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeekBarChanging = false;
                musicService.touchPlay(seekBar.getProgress());
            }
        });

    }

    public void initMusic() {
        Intent intent = getIntent();
        list = (List<Map<String, String>>) intent.getSerializableExtra("list");
        i = intent.getIntExtra("i", 0);
        musicTitle.setText(list.get(i).get("title"));
        Intent intent1 = new Intent(MusicPlay.this, MusicService.class);
        intent1.putExtra("path", list.get(i).get("path"));
        startService(intent1);
        play.setImageResource(R.drawable.pause);
        play.setTag(R.drawable.pause);
    }

    public String calculateTime(int time) {
        int minute;
        int second;
        if (time > 60) {
            minute = time / 60;
            second = time % 60;
            //分钟再0~9
            if (minute >= 0 && minute < 10) {
                //判断秒
                if (second >= 0 && second < 10) {
                    return "0" + minute + ":" + "0" + second;
                } else {
                    return "0" + minute + ":" + second;
                }
            } else {
                //分钟大于10再判断秒
                if (second >= 0 && second < 10) {
                    return minute + ":" + "0" + second;
                } else {
                    return minute + ":" + second;
                }
            }
        } else if (time < 60) {
            second = time;
            if (second >= 0 && second < 10) {
                return "00:" + "0" + second;
            } else {
                return "00:" + second;
            }
        }
        return null;
    }
    private void initView(){
        last = (ImageButton) findViewById(R.id.lastplay);
        play = (ImageButton) findViewById(R.id.play);
        next = (ImageButton) findViewById(R.id.next);
        playStyle = (Button) findViewById(R.id.playStyle);
        playStyle.setTag("sequentially");
        playStyle.setText("顺序播放");
        musicTitle = (TextView) findViewById(R.id.musicTitle);
        musicTitle.setSelected(true);
        seekbar_start = (TextView) findViewById(R.id.seekbar_start);
        seekbar_end = (TextView) findViewById(R.id.seekbar_end);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
    }
    private int getDrawableId(ImageButton imageButton) {
        return (Integer) imageButton.getTag();
    }
    private void playMusic(){
        musicService.initPlayMusic(list.get(i).get("path"));
        musicTitle.setText(list.get(i).get("title"));
        mediaPlayer = musicService.getMediaPlayer();
        seekbar.setProgress(mediaPlayer.getCurrentPosition());
        seekbar.setMax(mediaPlayer.getDuration());
        play.setImageResource(R.drawable.pause);
        play.setTag(R.drawable.pause);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 0:
                playStyle.setTag("singleLoop");
                playStyle.setText("循环播放");
                break;
            case 1:
                playStyle.setTag("shuffle");
                playStyle.setText("随机播放");
                break;
            case 2:
                playStyle.setTag("sequentially");
                playStyle.setText("顺序播放");
                break;
        }
        return super.onContextItemSelected(item);
    }
}
