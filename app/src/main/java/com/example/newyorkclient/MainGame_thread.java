package com.example.newyorkclient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MainGame_thread extends Thread{
    Handler handler = null;
    boolean stop = false;

    MainGame_thread(Handler _handler) {
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
            Log.v("MainGame_thread", line);

            String[] info = line.split("\\|");
            switch(info[0]) {
                case "topic":
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("value", line);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                    break;
                default:
                    Log.e("MainGame", "unknown" + line);
            }
        }
    }
}
