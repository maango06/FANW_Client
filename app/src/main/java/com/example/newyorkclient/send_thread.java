package com.example.newyorkclient;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class send_thread extends Thread{
    String msg;
    BufferedWriter writer;
    static Lock lock = new ReentrantLock();

    send_thread(BufferedWriter _writer, String _msg) {
        this.msg = _msg;
        this.writer = _writer;
    }

    @Override
    public void run() {
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