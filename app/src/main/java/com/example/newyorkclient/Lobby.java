package com.example.newyorkclient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        Intent intent = getIntent();
        N = findViewById(R.id.Nick);
        Nick = findViewById(R.id.NickName);

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
                Intent intent2 = new Intent(Lobby.this, MainActivity.class);
                intent2.putExtra("nick", RealNickName);
                intent2.putExtra("user_id", player_id);
                intent2.putExtra("thumb_nail", thumb_nail);

                startActivity(intent2);
            }
        });
    }
}