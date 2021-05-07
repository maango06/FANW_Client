package com.example.newyorkclient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Queue;

public class Lobby_thread extends Thread{

    Socket_service socket_service;
    Handler handler = null;
    boolean stop = false;

    Lobby_thread(Socket_service _socket_service, Handler _handler) {
        this.socket_service = _socket_service;
        this.handler = _handler;
    }

    public void set_stop() {
        this.stop = true;
        while(stop) { }
    }

    @Override
    public void run() {
        Queue<String> que = socket_service.getQue();
        String line = null;
        while(true) {
            if(stop) {
                stop = false;
                break;
            }
            line = que.poll();
            if(line == null) {
                Thread.yield();
                continue;
            }
            Log.v("Lobby_thread", line);

            String[] info = line.split("/");
            switch(info[0]) {
                case "make_room":
                    if(info[1].equals("E")) {
                        Log.e("make_room", "fail makeing room");

                    } else {
                        Message msg = handler.obtainMessage();
                        Bundle bundle = new Bundle();
                        String str = "room_code/" + info[2].toString();
                        bundle.putString("value", str);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                    break;
                default:
                    Log.e("Lobby", "unknown" + line);
            }
        }
    }
}
