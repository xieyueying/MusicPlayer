package com.example.musicplayer;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MusicUtil {
    public static List<MusicInfo> getMp3Infos(Context context) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        List<MusicInfo> musicInfos = new ArrayList<MusicInfo>();
        int m = cursor.getCount();
        for (int i = 0; i < m; i++) {
            cursor.moveToNext();
            MusicInfo musicInfo = new MusicInfo();
            int titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int durationIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int isMusicIndex = cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC);
            int pathIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            String title = cursor.getString(titleIndex); // 音乐标题
            String artist = cursor.getString(artistIndex); // 艺术家
            long duration = cursor.getLong(durationIndex); // 时长
            String path = cursor.getString(pathIndex);//路劲
            int isMusic = cursor.getInt(isMusicIndex); // 是否为音乐
            if (isMusic != 0 && duration>120000) { // 只把音乐添加到集合当中
                musicInfo.setTitle(title);
                musicInfo.setArtist(artist);
                musicInfo.setDuration(duration);
                musicInfo.setPath(path);
                musicInfos.add(musicInfo);
            }
        }
        return musicInfos;
    }
    public static List<Map<String, String>> getMusicMaps(
            List<MusicInfo> mp3Infos) {
        List<Map<String, String>> mp3list = new ArrayList<Map<String, String>>();
        for (Iterator iterator = mp3Infos.iterator(); iterator.hasNext();) {
            MusicInfo musicInfo = (MusicInfo) iterator.next();
            Map<String, String> map = new HashMap<String, String>();
            map.put("title", musicInfo.getTitle());
            map.put("artist", musicInfo.getArtist());
            map.put("duration", formatTime(musicInfo.getDuration()));
            map.put("path",musicInfo.getPath());
            mp3list.add(map);
        }
        return mp3list;
    }
    public static String formatTime(long time) {
        // TODO Auto-generated method stub
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";
        if (min.length() < 2) {
            min = "0" + time / (1000 * 60) + "";
        } else {
            min = time / (1000 * 60) + "";
        }
        if (sec.length() == 4) {
            sec = "0" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 3) {
            sec = "00" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 2) {
            sec = "000" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 1) {
            sec = "0000" + (time % (1000 * 60)) + "";
        }
        return min + ":" + sec.trim().substring(0, 2);
    }
}
