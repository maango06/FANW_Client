package com.example.newyorkclient;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class read_thread extends Thread{
    @Override
    public void run() {
        Log.v("read_thread", "ID is : " + this.getId());
        Socket socket = GlobalApplication.getGlobalApplicationContext().getSocket();
        socket_queue que = GlobalApplication.getGlobalApplicationContext().getQue();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "EUC_KR"));
            while(true) {
                try {
                    String line = reader.readLine();
                    if (line == null)
                        break;
                    que.push(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
