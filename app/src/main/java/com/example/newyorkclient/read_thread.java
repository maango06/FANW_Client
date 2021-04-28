package com.example.newyorkclient;

import android.content.Context;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class read_thread extends Thread{

    String line;
    BufferedReader reader;
    queue_manager que;

    read_thread(BufferedReader _reader, queue_manager _q) {
        this.reader = _reader;
        this.que = _q;
    }

    @Override
    public void run() {

        while(true) {
            try {
                line = reader.readLine();
                if(line == null)
                    break;
                que.insert(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}