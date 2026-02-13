package com.example.aircraftwar2024.Music;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import com.example.aircraftwar2024.R;

import java.util.HashMap;

public class MySoundPool {
    private SoundPool soundPool;
    private AudioAttributes audioAttributes = null;
    private HashMap<Integer, Integer> soundPoolHashMap = new HashMap<Integer, Integer>();

    public MySoundPool(Context context) {
        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(audioAttributes)
                .build();

        soundPoolHashMap.put(1, soundPool.load(context, R.raw.bomb_explosion, 1));
        soundPoolHashMap.put(2, soundPool.load(context, R.raw.bullet_hit, 1));
        soundPoolHashMap.put(3, soundPool.load(context, R.raw.game_over, 1));
        soundPoolHashMap.put(4, soundPool.load(context, R.raw.get_supply, 1));
    }

    public void explosionSoundPool() {
        soundPool.play(soundPoolHashMap.get(1), 1, 1, 0, 0, 1.2f);
    }

    public void hitSoundPool() {
        soundPool.play(soundPoolHashMap.get(2), 1, 1, 0, 0, 1.2f);
    }

    public void overSoundPool() {
        soundPool.play(soundPoolHashMap.get(3), 1, 1, 0, 0, 1.2f);
    }

    public void supplySoundPool() {
        soundPool.play(soundPoolHashMap.get(4), 1, 1, 0, 0, 1.2f);
    }
}
