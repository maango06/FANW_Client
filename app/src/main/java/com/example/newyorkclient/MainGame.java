package com.example.newyorkclient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainGame extends AppCompatActivity {

    MyPaintView view;
    int tColor,n=0;
    String main_topic, small_topic;
    TextView who, timer_view;
    Button check, btn, btn2, btn3;
    AlertDialog theme;
    User_info[] user_info = new User_info[6];
    Lock_queue draw_queue;
    Lock_queue_thread queue_thread;

    int player_num;

    MainGame_thread maingame_thread = null;
    MainGame_handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        who = findViewById(R.id.who);
        check = findViewById(R.id.check);
        timer_view = findViewById(R.id.timer);

        player_num = getIntent().getIntExtra("player_num", 0);
        for(int i = 0; i < player_num; ++i) {
            user_info[i] = new User_info();
            user_info[i] = (User_info) getIntent().getSerializableExtra("player" + Integer.toString(i));
        }

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_popup();
            }
        });

        handler = new MainGame_handler();

        draw_queue = new Lock_queue();

        new send_thread("topic").start();

        //아래 코드들은 전부 그림 그리기 관련
        view = new MyPaintView(this);
        view.get_queue(draw_queue);

        LinearLayout container = (LinearLayout)findViewById(R.id.container);
        Resources res = getResources();


        LinearLayout.LayoutParams params;
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        container.addView(view, params);



        btn=findViewById(R.id.colorPickerButton);
        btn2=findViewById(R.id.thickPickerButton);
        btn3 = findViewById(R.id.clear);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        maingame_thread.set_stop();
        queue_thread.set_stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        queue_thread = new Lock_queue_thread(draw_queue);
        queue_thread.start();
        maingame_thread = new MainGame_thread(handler);
        maingame_thread.start();
    }

    private void show() {
        final EditText editText=new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);


        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("AlertDialog Title");
        builder.setMessage("굵기 입력(초기값은 5입니다)");
        builder.setView(editText);
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int width = Integer.parseInt(editText.getText().toString());
                        view.setStrokeWidth(width);
                        String msg = "thickness|" + Integer.toString(width);
                        draw_queue.push(msg);
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();

    }

    private void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, tColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                Toast.makeText(getApplicationContext(),""+tColor,Toast.LENGTH_LONG).show();
                view.setColor(color);
                String msg = "color|" + Integer.toString(color);
                draw_queue.push(msg);
            }
        });
        colorPicker.show();
    }

    private void clear(){
        view.clear();
        String msg = "clear";
        draw_queue.push(msg);
    }

    public void set_popup() {
        if(!small_topic.equals("false")){
            AlertDialog.Builder ad = new AlertDialog.Builder(MainGame.this);
            ad.setIcon(R.mipmap.fake_artist);
            ad.setTitle("당신은 예술가 입니다!");
            ad.setMessage("대주제 : " + main_topic + "\n" + "소주제 : " + small_topic);
            ad.setPositiveButton("확인", null);
            theme = ad.show();
            TextView mess = (TextView)theme.findViewById(android.R.id.message);
            mess.setGravity(Gravity.CENTER);
            theme.show();
        }
        else{
            AlertDialog.Builder ad = new AlertDialog.Builder(MainGame.this);
            ad.setIcon(R.mipmap.fake_artist);
            ad.setTitle("당신은 가짜 예술가 입니다!");
            ad.setMessage("대주제 : " + main_topic + "\n" + "소주제 : ???");
            ad.setPositiveButton("확인", null);
            theme = ad.show();
            TextView mess = (TextView)theme.findViewById(android.R.id.message);
            mess.setGravity(Gravity.CENTER);
            theme.show();
        }
    }

    public void set_touch(boolean _value) {
        view.set_can_touch(_value);
        (MainGame.this).runOnUiThread(new Runnable() {
            public void run() {
                btn.setEnabled(_value);
                btn2.setEnabled(_value);
                btn3.setEnabled(_value);
            }
        });
    }

    class MainGame_handler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            String line = msg.getData().getString("value");
            String[] info = line.split("\\|");
            switch(info[0]) {
                case "clear":
                    view.clear();
                    break;
                case "topic":
                    main_topic = info[1];
                    small_topic = info[2];
                    set_popup();
                    break;
                case "thickness":
                    view.setStrokeWidth(Integer.parseInt(info[1]));
                    break;
                case "color":
                    view.setColor(Integer.parseInt(info[1]));
                    break;
                case "draw_up":
                case"draw_down":
                case"draw_move":
                    view.draw_something(line);
                    break;
                case "now_turn":
                    view.clear();
                    String temp_msg = "차례 : " + info[1];
                    new game_timer(5).start();
                    who.setText(temp_msg);
                    set_touch(false);
                    break;
                case "your_turn":
                    set_touch(true);
                    break;
                case "vote":
                    final int[] select_index = new int[1];
                    final ArrayList<String> player = new ArrayList<String>();
                    for(int i = 0; i < player_num; ++i) {
                        player.add(user_info[i].nick_name);
                    }
                    AlertDialog.Builder vote = new AlertDialog.Builder(MainGame.this);
                    //vote.setIcon(R.mipmap.fake_artist);
                    vote.setTitle("가짜 예술가를 선택하세요").setSingleChoiceItems(player.toArray(new String[player.size()]), 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            select_index[0] = which;
                        }
                    }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new send_thread("vote|"+Integer.toString(select_index[0])).start();
                        }
                    });

                    vote.create();
                    vote.show();
                    break;
                case "game_result":
                    if(info[1].equals(info[2])) {
                        AlertDialog.Builder real = new AlertDialog.Builder(MainGame.this);
                        LayoutInflater factory = LayoutInflater.from(MainGame.this);
                        final View viewr = factory.inflate(R.layout.real, null);
                        real.setView(viewr);
                        real.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int sumthin) {
                                finish();
                            }
                        });
                        AlertDialog dialog = real.show();
                        TextView temp = dialog.findViewById(R.id.real_result_text);
                        String temp2_msg = info[1] + " 이 뽑혔습니다.\n" + info[1] + " 은(는) 가짜예술가였습니다!\n진짜예술가들의 승리!";
                        temp.setText(temp2_msg);
                        dialog.show();
                    }
                    else {
                        AlertDialog.Builder fake = new AlertDialog.Builder(MainGame.this);
                        LayoutInflater factory2 = LayoutInflater.from(MainGame.this);
                        final View viewr2 = factory2.inflate(R.layout.fake, null);
                        fake.setView(viewr2);
                        fake.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int sumthin) {
                                finish();
                            }
                        });
                        AlertDialog dialog = fake.show();
                        TextView temp = dialog.findViewById(R.id.fake_result_text);
                        String temp2_msg = info[1] + " 이 뽑혔습니다.\n" + info[1] + " 은(는) 가짜예술가 아닙니다!\n가짜예술가의 승리!";
                        temp.setText(temp2_msg);
                        dialog.show();
                    }
                    break;
            }
        }
    }

    class Lock_queue_thread extends Thread {
        Lock_queue lock_queue;
        boolean stop = false;
        Lock_queue_thread(Lock_queue _var) {
            this.lock_queue = _var;
        }
        public void set_stop() {
            this.stop = true;
        }
        @Override
        public void run() {
            String line;
            while(true) {
                if(this.stop) break;
                line = lock_queue.pop();
                if(line == null) {
                    Thread.yield();
                    continue;
                }
                send_thread temp = new send_thread(line);
                temp.start();
                try {
                    temp.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    class game_timer extends Thread {
        int sec;
        game_timer(int _sec) {
            this.sec = _sec;
        }
        @Override
        public void run() {
            long before = System.currentTimeMillis();
            long next_before = before + 1000;
            long after = before + sec * 1000;

            while(true) {
                if(System.currentTimeMillis() < next_before)
                    Thread.yield();
                else {
                    before += 1000;
                    next_before += 1000;
                    --sec;
                    (MainGame.this).runOnUiThread(new Runnable() {
                        public void run() {
                            timer_view.setText(Integer.toString(sec) + " 초");
                        }
                    });

                }
                 if (before >= after) {
                     break;
                 }
            }
            set_touch(false);
            draw_queue.clear();
        }
    }
}