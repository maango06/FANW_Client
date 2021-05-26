package com.example.newyorkclient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MadeRoom extends AppCompatActivity {
    TextView[] players = new TextView[6];
    ImageView[] thumbs = new ImageView[6];
    TextView codename;
    TextView chat_log;

    Button sending_button;
    EditText sending_text;

    Button cancel;
    Button start;

    int player_num = 0;
    boolean master = false;
    String room_code = null;
    MadeRoom_thread maderoom_thread = null;
    MadeRoom_handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_made_room);
        players[0] = findViewById(R.id.player1);
        thumbs[0] = findViewById(R.id.image1);
        players[1] = findViewById(R.id.player2);
        thumbs[1] = findViewById(R.id.image1);
        players[2] = findViewById(R.id.player3);
        thumbs[2] = findViewById(R.id.image1);
        players[3] = findViewById(R.id.player4);
        thumbs[3] = findViewById(R.id.image1);
        players[4] = findViewById(R.id.player5);
        thumbs[4] = findViewById(R.id.image1);
        players[5] = findViewById(R.id.player6);
        thumbs[5] = findViewById(R.id.image1);
        codename = findViewById(R.id.Codename);
        cancel = findViewById(R.id.cancel);
        start = findViewById(R.id.start);
        sending_button = findViewById(R.id.sending_button);
        sending_text = findViewById(R.id.sending_text);
        chat_log = findViewById(R.id.chat_log);

        cancel.setEnabled(false);
        start.setEnabled(false);

        Intent now_intent = getIntent();
        room_code = now_intent.getStringExtra("room_code");
        master = now_intent.getBooleanExtra("master", false);

        String temp = "방코드 : " + room_code;
        codename.setText(temp);

        new send_thread("room_info").start();

        handler = new MadeRoom_handler();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CountDownTimer(4999, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        cancel.setEnabled(true);
                        String a = String.valueOf(millisUntilFinished/1000);
                        start.setText(a);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancel();
                                start.setText("시작하기");
                                cancel.setEnabled(false);
                            }
                        });
                    }
                    @Override
                    public void onFinish() {
                        Intent intent3 = new Intent(MadeRoom.this, MainGame.class);
                        startActivity(intent3);
                        start.setText("시작하기");
                    }
                }.start();
            }
        });

        sending_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = sending_text.getText().toString();
                sending_text.setText("");
                new send_thread("message|" + text).start();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        maderoom_thread.set_stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        maderoom_thread = new MadeRoom_thread(handler);
        maderoom_thread.start();
    }

    class MadeRoom_handler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            String line = msg.getData().getString("value");
            String[] info = line.split("\\|");
            switch(info[0]) {
                case "message":
                    String str = chat_log.getText() + info[1] + " : " + info[2] + "\n";
                    chat_log.setText(str);
                    break;
                case "room_info":
                    for(int i = 1; i < info.length; ++i) {
                        String[] temp = info[i].split("::");
                        Log.v("room_info", temp[0]);
                        Log.v("room_info", temp[1]);
                        players[player_num].setText(temp[0]);
                        Glide.with(MadeRoom.this).load(temp[1]).into(thumbs[player_num]);
                        ++player_num;

                    }
            }
        }
    }
}