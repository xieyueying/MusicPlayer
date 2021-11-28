package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    List<Map<String, String>> items = new ArrayList<Map<String, String>>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.init:
            {
                init();
                setListView();
                Toast.makeText(MainActivity.this,"刷新完成！",Toast.LENGTH_LONG).show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
        setListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Intent intent = new Intent(MainActivity.this,MusicPlay.class);
                    intent.putExtra("list", (Serializable) items);
                    intent.putExtra("i",i);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    public void setListView(){
        items = MusicUtil.getMusicMaps(MusicUtil.getMp3Infos(this));
        SimpleAdapter adapter = new SimpleAdapter(this, items,
                R.layout.music_item, new String[]{"title", "duration", "artist"}, new int[]{R.id.title,
                R.id.duration, R.id.artist});
        listView.setAdapter(adapter);
    }
    public void init(){
        String path = "sdcard/Music";
        MediaScannerConnection.scanFile(this,new String[] {path},null,

                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {

                    }

                });
    }
}