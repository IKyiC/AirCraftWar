package com.example.aircraftwar2024.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aircraftwar2024.DAO.Score;
import com.example.aircraftwar2024.game.BaseGame;
import com.example.aircraftwar2024.game.EasyGame;
import com.example.aircraftwar2024.game.HardGame;
import com.example.aircraftwar2024.game.MediumGame;


public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";
    private int gameType=0;
    public static int screenWidth,screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Handler handler = new Handler(Looper.getMainLooper()){
            public void handleMessage(Message msg){
                if(msg.what == 1) {
                    Intent intent = new Intent(GameActivity.this, RankActivity.class);
                    Score score = (Score) msg.obj;
                    intent.putExtra("user_score", score.GetScore());
                    intent.putExtra("user_date", score.GetDate());
                    startActivity(intent);
                }
            }
        } ;
        if(getIntent() != null){
            gameType = getIntent().getIntExtra("gameType",1);
        }
        //获取游戏难度
        gameType = getIntent().getIntExtra("gameType", 1);
        BaseGame baseGameView = null;
        if(gameType == 1) {
            baseGameView = new EasyGame(this, handler);
        }
        else if(gameType == 2) {
            baseGameView = new MediumGame(this, handler);
        }
        else if(gameType == 3) {
            baseGameView = new HardGame(this, handler);
        }

        setContentView(baseGameView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}