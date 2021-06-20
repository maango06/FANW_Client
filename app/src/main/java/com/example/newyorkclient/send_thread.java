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
    int my_turn;
    static Lock lock = new ReentrantLock();
    static Socket socket = GlobalApplication.getGlobalApplicationContext().getSocket();
    static BufferedWriter writer;
    static int now_turn = 0;
    static int ticket = 0;

    static {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "EUC-KR"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    send_thread(String _msg) {

        this.msg = _msg;
        this.my_turn = ticket;
        ticket++;
    }

    @Override
    public void run() {
        while(now_turn != my_turn) {
            Thread.yield();
        }
        lock.lock();

        try {
            writer.write(msg);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Log.v("send_thread", Integer.toString(my_turn) + "nd thread : " + msg);
            now_turn++;
            lock.unlock();
        }
    }
}