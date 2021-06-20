package com.example.newyorkclient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MadeRoom_thread extends Thread{

    Handler handler = null;
    boolean stop = false;

    MadeRoom_thread(Handler _handler) {
        this.handler = _handler;
    }

    public void set_stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        lock_queue que = GlobalApplication.getGlobalApplicationContext().getQue();
        String line = null;
        while(true) {
            if(this.stop) break;
            line = que.pop();
            if(line == null) {
                Thread.yield();
                continue;
            }
            Log.v("MadeRoom_thread", line);

            String[] info = line.split("\\|");
            switch(info[0]) {
                case "message":
                case "room_info":
                case "enter_room":
                case "quit_room":
                case "quit_check":
                case "game_start":
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("value", line);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                    break;
                default:
                    Log.e("MadeRoom", "unknown" + line);
            }
        }
    }
}