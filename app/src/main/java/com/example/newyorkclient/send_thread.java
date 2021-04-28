package com.example.newyorkclient;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class send_thread extends Thread{
    Socket socket;
    BufferedWriter writer;
    String msg;

    send_thread(Socket _socket, String _msg) {
        this.socket = _socket;
        this.msg = _msg;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "EUC-KR"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("sending...");

        try {
            writer.write(msg);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("sending finish");
    }



}
