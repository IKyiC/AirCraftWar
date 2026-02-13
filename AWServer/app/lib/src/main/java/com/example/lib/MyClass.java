package com.example.lib;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyClass {
    public static List<Socket> socketList = new ArrayList<>();
    public static void main(String[] args){
        new MyClass();
    }

    public MyClass(){
        try {
            // 1. 创建ServerSocket
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("--Listener Port: 9999--");
            while (true) {
                System.out.println("waiting client connect");
                //2.等待接收请求，这里接收两个客户端的请求
                // 如果队列中没有连接请求，accept()方法就会一直等待，直到接收到了连接请求才返回
                Socket client1 = serverSocket.accept();
                Socket client2 = serverSocket.accept();

                socketList.add(client1);
                socketList.add(client2);

                //3.开启两个子线程处理和两个客户端的消息传输
                new ServerSocketThread(client1, client2).start();
                new ServerSocketThread(client2, client1).start();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}