package com.example.newyorkclient;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class send_thread extends Thread{
    String msg;
    static Lock lock = new ReentrantLock();
    static Socket socket = GlobalApplication.getGlobalApplicationContext().getSocket();
    static BufferedWriter writer;

    static {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "EUC-KR"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    send_thread(String _msg) {
        this.msg = _msg;
    }

    @Override
    public void run() {
        Log.v("send_thread", "ID is : " + this.getId());
        lock.lock();
        try {
            writer.write(msg);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}