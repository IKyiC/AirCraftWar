package com.example.aircraftwar2024.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aircraftwar2024.R;
import com.example.aircraftwar2024.game.BaseGame;
import com.example.aircraftwar2024.game.MediumGame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private static int opScore = 0;
    private static boolean gameOverFlag = false;

    private static final String TAG = "OnlineActivity";
    private Socket socket;
    private PrintWriter writer;
    private Handler handler;
    private Handler scoreUpdateHandler;
    BaseGame game;
    public static int screenWidth,screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getScreenHW();

        //等待匹配提示框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("匹配中，请等待......");
        AlertDialog dialog = builder.create();

        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1 && msg.obj.equals("Over")) {
                    setover(true);

                    Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                    intent.putExtra("myScore", game.getScore());
                    intent.putExtra("opScore", opScore);
                    startActivity(intent);
                }
                else if(msg.what == 1 && msg.obj.equals("Start")) {
                    game = new MediumGame(MainActivity.this, handler);
                    setContentView(game);
                    dialog.dismiss();
                    Thread scoreToServer = new Thread() {
                        @Override
                        public void run() {
                            while(!game.isGameOverFlag()) {
                                writer.println(game.getScore());
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            writer.println("End");
                            runOnUiThread(() -> {
                                setContentView(R.layout.activity_end);
                                TextView myText = findViewById(R.id.my_score);
                                TextView opText = findViewById(R.id.your_score);
                                myText.setText(""+game.getScore());
                                scoreUpdateHandler = new Handler(Looper.getMainLooper());
                                Runnable scoreUpdater = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (opText != null) {
                                            opText.setText("" + opScore);
                                        }
                                        scoreUpdateHandler.postDelayed(this, 100);
                                    }
                                };
                                scoreUpdateHandler.post(scoreUpdater);

                            });
                        }
                    };

                    scoreToServer.start();
                }
                else {
                    if((String)msg.obj != null) {
                        try {
                            opScore = Integer.parseInt((String) msg.obj);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        Button btn1 = (Button) findViewById(R.id.offline_btn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OfflineActivity.class);
                BaseGame.gameType = "Offline";
                startActivity(intent);
            }
        });

        Button btn2 = (Button) findViewById(R.id.online_btn);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseGame.gameType = "Online";
                dialog.show();
                //创建子线程并连接服务器
                NetConn netConn = new NetConn(handler);
                netConn.start();
            }
        });

        Button btn3 = (Button) findViewById(R.id.radio_on);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { BaseGame.isMusicOn = true;
            }
        });

        Button btn4 = (Button) findViewById(R.id.radio_off);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseGame.isMusicOn = false;
            }
        });

    }


    public static int GetOpScore() {
        return opScore;
    }
    private void setover(boolean gameOverFlag) {
        MainActivity.gameOverFlag = gameOverFlag;
    }
    public static boolean overflag() {return gameOverFlag; }
    public void getScreenHW(){
        DisplayMetrics dm = new DisplayMetrics();
        getDisplay().getRealMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    protected class NetConn extends Thread {
        private BufferedReader in;
        private Handler toClientHandler;
        public NetConn(Handler myHandler) {toClientHandler = myHandler;}

        @Override
        public void run() {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress
                        ("10.0.2.2",9999),5000);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
                writer = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream(),"utf-8")),true);
                Thread receiveServerMsg = new Thread() {
                    @Override
                    public void run() {
                        String fromServer = null;
                        try {
                            while((fromServer = in.readLine()) != null) {
                                Message msgFromServer = new Message();
                                msgFromServer.what = 1;
                                msgFromServer.obj = fromServer;
                                toClientHandler.sendMessage(msgFromServer);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
                receiveServerMsg.start();
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}