package com.example.newyorkclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    Socket socket;
    TextView pannel;
    EditText text_pannel;
    Button send_button;

    String nick_name;
    String player_id;
    String thumb_nail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        nick_name = intent.getExtras().getString("name");
        player_id = intent.getExtras().getString("user_id");
        thumb_nail = intent.getExtras().getString("thumb_nail");

        pannel = (TextView) findViewById(R.id.chet_log);
        text_pannel = (EditText) findViewById(R.id.send_text);
        send_button = (Button) findViewById(R.id.send_button);

        String ip = "218.146.163.216";

        Connector connector = new Connector(ip, pannel);
        connector.start();

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text_pannel.getText().toString();
                text_pannel.setText("");
                new send_thread(socket, msg).start();

            }
        });

    }

    public class Connector extends Thread{

        String ip;
        TextView pannel;

        Connector(String _ip, TextView _pannel) {
            this.ip = _ip;
            this.pannel = _pannel;
        }

        @Override
        public void run() {
            try {
                System.out.println("connecting...");
                socket = new Socket(ip, 8989);
                String login_text = "login/" + player_id + "/" + nick_name;
                new send_thread(socket, login_text).start();

                read_thread read = new read_thread(socket, pannel, MainActivity.this);
                read.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}