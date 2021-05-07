package com.example.newyorkclient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Lobby extends AppCompatActivity {
    Button enter;
    Button making;
    Button N;
    TextView Nick;

    String nick_name;
    String player_id;
    String thumb_nail;
    String RealNickName;
    int codenumber;

    Socket_service socket_service;
    Lobby_thread lobby_thread = null;
    Lobby_handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        handler = new Lobby_handler();

        Intent intent = getIntent();
        N = findViewById(R.id.Nick);
        Nick = findViewById(R.id.NickName);

        try {
            socket_service = GlobalApplication.getSocket_service();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        lobby_thread = new Lobby_thread(socket_service, handler);
        lobby_thread.start();

        N.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(Lobby.this);
                ad.setIcon(R.mipmap.fake_artist);
                ad.setTitle("닉네임 설정");
                ad.setMessage("닉네임을 입력하세요");

                final EditText et = new EditText(Lobby.this);
                ad.setView(et);

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String result = et.getText().toString();
                        RealNickName = result;
                        Nick.setText(result);
                        dialog.dismiss();
                    }
                });
                ad.show();
            }
        });

        nick_name = intent.getExtras().getString("name");
        player_id = intent.getExtras().getString("user_id");
        thumb_nail = intent.getExtras().getString("thumb_nail");

        enter = findViewById(R.id.Enter);
        making = findViewById(R.id.RoomMaking);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(Lobby.this);
                ad.setIcon(R.mipmap.fake_artist);
                ad.setTitle("입장하기");
                ad.setMessage("입장코드를 입력하세요");

                final EditText et = new EditText(Lobby.this);
                ad.setView(et);

                ad.setPositiveButton("입장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String result = et.getText().toString();
                        codenumber = Integer.parseInt(result);
                        dialog.dismiss();
                    }
                });
                ad.show();
            }
        });

        making.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket_service.Send("login/" + player_id + "/" + nick_name);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        lobby_thread.set_stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        lobby_thread = new Lobby_thread(socket_service, handler);
    }

    class Lobby_handler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            String line = msg.getData().getString("value");
            String[] info = line.split("/");
            switch(info[0]) {
                case "room_code":

            }
        }
    }

}