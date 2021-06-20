package com.example.newyorkclient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Lobby extends AppCompatActivity {
    Button enter;
    Button making;
    Button nickname_button;
    TextView nickname_textview;

    String nick_name;
    String player_id;
    String thumb_nail;
    Lobby_thread lobby_thread = null;
    Lobby_handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        handler = new Lobby_handler();

        Intent intent = getIntent();
        nickname_button = findViewById(R.id.Nick);
        nickname_textview = findViewById(R.id.NickName);

        nickname_button.setOnClickListener(new View.OnClickListener() {
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
                        new send_thread("nickname|" + result).start();
                        dialog.dismiss();
                    }
                });
                ad.show();
            }
        });

        nick_name = intent.getExtras().getString("name");
        player_id = intent.getExtras().getString("user_id");
        thumb_nail = intent.getExtras().getString("thumb_nail");
        new send_thread("login|" + player_id + "|" + nick_name + "|" + thumb_nail).start();

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
                        new send_thread("enter_room|" + result).start();
                        dialog.dismiss();
                    }
                });
                ad.show();
            }
        });

        making.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new send_thread("make_room").start();
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
        lobby_thread = new Lobby_thread(handler);
        lobby_thread.start();
    }

    class Lobby_handler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            String line = msg.getData().getString("value");
            String[] info = line.split("\\|");
            switch(info[0]) {
                case "make_room":
                    Intent intent = new Intent(Lobby.this, MadeRoom.class);
                    intent.putExtra("master", true);
                    intent.putExtra("room_code", info[1]);
                    startActivity(intent);
                    break;
                case "enter_room":
                    if(info[1].equals("S")) {
                        Intent intent2 = new Intent(Lobby.this, MadeRoom.class);
                        intent2.putExtra("master", false);
                        intent2.putExtra("room_code", info[2]);
                        startActivity(intent2);
                    }
                    else {
                        AlertDialog.Builder ad = new AlertDialog.Builder(Lobby.this);
                        ad.setIcon(R.mipmap.fake_artist);
                        ad.setTitle("에러");
                        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        ad.show();
                    }
                    break;
                case "nickname":
                    nick_name = info[1];
                    nickname_textview.setText(nick_name);
            }
        }
    }
}