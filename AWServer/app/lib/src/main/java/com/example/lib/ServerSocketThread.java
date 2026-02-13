package com.example.lib;


import com.sun.tools.javac.Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

public class ServerSocketThread extends Thread{
    private BufferedReader in;
    private PrintWriter myw;
    private PrintWriter yourw;
    private Socket mySocket;
    private Socket yourSocket;
    public ServerSocketThread(Socket socket1, Socket socket2){
        this.mySocket = socket1;
        this.yourSocket = socket2;
    }

    @Override
    public void run(){
        System.out.println("wait client message " );
        try {
            in = new BufferedReader(new InputStreamReader(mySocket.getInputStream(),"UTF-8"));
            myw = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(mySocket.getOutputStream(), "UTF-8")), true);
            yourw = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(yourSocket.getOutputStream(), "UTF-8")), true);

            //两个客户端连接成功则启动游戏,这个不需要接受客户端的信息
            if(MyClass.socketList.size() == 2) {
                myw.println("Start");
                System.out.println("Start Game!");
            }

            String content;

            while ((content = in.readLine()) != null) {
                //4.和客户端通信
                //当有玩家死亡
                if(content.equals("End")) {
                    //游戏结束对单个客户端进行处理
                    MyClass.socketList.remove(mySocket);

                    if(MyClass.socketList.size() == 0) {
                        //双方都死亡直接跳出循环结束整个游戏
                        break;
                    }
                }
                else {
                    //传送对手的分数
                    yourw.println(content);
                }
            }

            //没有用户则结束整个游戏
            if(MyClass.socketList.size() == 0) {
                myw.println("Over");
                System.out.println("Game Over!");
                yourw.println("Over");
                System.out.println("Game Over!");

                mySocket.shutdownInput();
                mySocket.shutdownOutput();
                mySocket.close();   //关闭socket连接
                yourSocket.shutdownInput();
                yourSocket.shutdownOutput();
                yourSocket.close();   //关闭socket连接
            }

        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }
}
