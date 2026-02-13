package com.example.aircraftwar2024.Music;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.aircraftwar2024.R;


public class MyMediaPlayer {
    private MediaPlayer bgmMediaPlayer;
    private MediaPlayer bossBgmMediaPlayer;

    public MyMediaPlayer(Context context) {
        //初始化
        if(bgmMediaPlayer == null) {
            bgmMediaPlayer = MediaPlayer.create(context, R.raw.bgm);
            bgmMediaPlayer.setLooping(true);
        }

        if(bossBgmMediaPlayer == null) {
            bossBgmMediaPlayer = MediaPlayer.create(context, R.raw.bgm_boss);
            bossBgmMediaPlayer.setLooping(true);
        }
    }

    public void playBgm() {
        bgmMediaPlayer.start();
    }

    public void pauseBgm() {
        bgmMediaPlayer.pause();
    }

    public void playBossBgm() {
        bossBgmMediaPlayer.start();
    }

    public void pauseBossBgm() {
        bossBgmMediaPlayer.pause();
    }

    public void stopBgm() {
        if(bgmMediaPlayer.isPlaying()) {
            bgmMediaPlayer.stop();
        }
        bgmMediaPlayer.release();
        bgmMediaPlayer = null;
    }

    public void stopBossBgm() {
        if(bossBgmMediaPlayer.isPlaying()) {
            bossBgmMediaPlayer.stop();
        }
        bossBgmMediaPlayer.release();
        bossBgmMediaPlayer = null;
    }
}
