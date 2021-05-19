package com.example.newyorkclient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Queue;

public class Lobby_thread extends Thread{

    Handler handler = null;
    boolean stop = false;

    Lobby_thread(Handler _handler) {
        this.handler = _handler;
    }

    public void set_stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        socket_queue que = GlobalApplication.getGlobalApplicationContext().getQue();
        String line = null;
        while(true) {
            if(this.stop) break;
            line = que.pop();
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
                        String str = "make_room/" + info[2];
                        bundle.putString("value", str);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                    break;
                case "nickname":
                    if(info[1].equals("E")) {
                        Log.e("nickname", "fail set nickname");
                    } else {
                        Message msg = handler.obtainMessage();
                        Bundle bundle = new Bundle();
                        String str = info[0] + "/" + info[2];
                        bundle.putString("value", str);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                    break;
                case "enter_room":
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    String str = line;
                    bundle.putString("value", str);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                    break;
                default:
                    Log.e("Lobby", "unknown" + line);
            }
        }
    }
}
