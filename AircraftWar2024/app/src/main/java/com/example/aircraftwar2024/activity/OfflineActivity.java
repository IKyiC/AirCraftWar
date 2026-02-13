package com.example.aircraftwar2024.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.aircraftwar2024.R;
import com.example.aircraftwar2024.game.BaseGame;

public class OfflineActivity extends AppCompatActivity {
    public static int gameType = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        Button easy = (Button) findViewById(R.id.easy_btn);
        Intent intent_game = new Intent(OfflineActivity.this, GameActivity.class);
        easy.setOnClickListener(view -> {
            gameType = 1;
            intent_game.putExtra("gameType", gameType);
            startActivity(intent_game);
        });

        Button medium = (Button) findViewById(R.id.medium_btn);
        medium.setOnClickListener(view -> {
            gameType = 2;
            intent_game.putExtra("gameType", gameType);
            startActivity(intent_game);
        });

        Button hard = (Button) findViewById(R.id.hard_btn);
        hard.setOnClickListener(view -> {
            gameType = 3;
            intent_game.putExtra("gameType", gameType);
            startActivity(intent_game);
        });

    }

}