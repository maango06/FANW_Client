package com.example.newyorkclient;

import android.content.Context;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class read_thread extends Thread{

    String line;
    Socket socket;
    TextView pannel;
    Context mContext;

    read_thread(Socket socket, TextView _pannel, Context _context) {
        this.socket = socket;
        this.pannel = _pannel;
        mContext = _context;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "EUC_KR"));

            while(true) {
                System.out.println("read waiting...");
                line = reader.readLine();
                System.out.println("something come!");
                if (line == null) {
                    break;
                }
                System.out.println(line);
                String history = pannel.getText().toString();
                history += line + "\n";
                final String fhistory = history;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)mContext).runOnUiThread(new Runnable() {
                            public void run() {
                                pannel.setText(fhistory);
                            }
                        });
                    }
                }).start();
                System.out.println("finished!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}