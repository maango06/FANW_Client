package com.example.newyorkclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class MadeRoom extends AppCompatActivity {
    User_info[] user_info = new User_info[6];
    TextView codename;
    TextView chat_log;

    ImageView[] image_list = new ImageView[6];
    TextView[] text_list = new TextView[6];

    Button sending_button;
    EditText sending_text;

    Button start;

    private long backKeyPressedTime = 0;
    private Toast toast;

    int player_num = 0;
    boolean master = false;
    String room_code = null;
    String my_id;
    MadeRoom_thread maderoom_thread = null;
    MadeRoom_handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_made_room);
        for(int i = 0; i < 6; ++i) {
            user_info[i] = new User_info();
            user_info[i].player_id = "";
        }
        text_list[0] = findViewById(R.id.player1);
        image_list[0] = findViewById(R.id.image1);
        text_list[1] = findViewById(R.id.player2);
        image_list[1] = findViewById(R.id.image2);
        text_list[2] = findViewById(R.id.player3);
        image_list[2] = findViewById(R.id.image3);
        text_list[3] = findViewById(R.id.player4);
        image_list[3] = findViewById(R.id.image4);
        text_list[4] = findViewById(R.id.player5);
        image_list[4] = findViewById(R.id.image5);
        text_list[5] = findViewById(R.id.player6);
        image_list[5] = findViewById(R.id.image6);

        codename = findViewById(R.id.Codename);
        start = findViewById(R.id.start);
        sending_button = findViewById(R.id.sending_button);
        sending_text = findViewById(R.id.sending_text);
        chat_log = findViewById(R.id.chat_log);

        Intent now_intent = getIntent();
        room_code = now_intent.getStringExtra("room_code");
        master = now_intent.getBooleanExtra("master", false);
        my_id = now_intent.getStringExtra("player_id");

        if(!master)
            start.setEnabled(false);

        String temp = "방코드 : " + room_code;
        codename.setText(temp);

        new send_thread("room_info").start();

        handler = new MadeRoom_handler();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new send_thread("game_start").start();
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
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 1000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "한번 더 누르시면 방을 나갑니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 1000) {
            new send_thread("quit").start();
            finish();
            toast.cancel();
        }
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
                        user_info[player_num].nick_name = temp[0];
                        text_list[player_num].setText(temp[0]);
                        user_info[player_num].player_id = temp[1];
                        user_info[player_num].thumb_nail = temp[2];
                        Glide.with(MadeRoom.this).load(temp[2]).into(image_list[player_num]);
                        ++player_num;
                    }
                    break;
                case "enter_room":
                    String[] temp = info[1].split("::");
                    user_info[player_num].nick_name = temp[0];
                    text_list[player_num].setText(temp[0]);
                    user_info[player_num].player_id = temp[1];
                    user_info[player_num].thumb_nail = temp[2];
                    Glide.with(MadeRoom.this).load(temp[2]).into(image_list[player_num]);
                    ++player_num;
                    break;
                case "quit_room":

                    int num = 0;
                    for(;num<6;++num) {
                        if(user_info[num].player_id.equals(info[1]))
                            break;
                    }
                    --player_num;

                    for(;num<player_num;++num) {
                        user_info[num].thumb_nail = user_info[num+1].thumb_nail;
                        user_info[num].player_id = user_info[num+1].player_id;
                        user_info[num].nick_name = user_info[num+1].nick_name;
                        text_list[num].setText(user_info[num].nick_name);
                        Glide.with(MadeRoom.this).load(user_info[num].thumb_nail).into(image_list[num]);
                    }

                    user_info[player_num].thumb_nail = "";
                    user_info[player_num].player_id = "";
                    user_info[player_num].player_id = "";
                    text_list[player_num].setText("");
                    image_list[player_num].setImageResource(R.drawable.kakao_default_profile_image);

                    if(user_info[0].player_id.equals(my_id)) {
                        master = true;
                        start.setEnabled(true);
                    }
                    break;
                case "game_start":
                    Intent intent3 = new Intent(MadeRoom.this, MainGame.class);
                    for(int i = 0; i < player_num; ++i)
                        intent3.putExtra("player" + Integer.toString(i), user_info[i]);
                    intent3.putExtra("player_num", player_num);
                    startActivity(intent3);
                    break;
            }
        }
    }
}