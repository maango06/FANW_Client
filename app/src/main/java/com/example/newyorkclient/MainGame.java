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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainGame extends AppCompatActivity {

    MyPaintView view;
    int tColor,n=0;
    String main_topic, small_topic;
    String lier; //최종 범인
    TextView who;
    ProgressBar timeout;
    Button check;
    AlertDialog theme;

    MainGame_thread maingame_thread = null;
    MainGame_handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        who = findViewById(R.id.who);
        timeout = findViewById(R.id.timeout);
        check = findViewById(R.id.check);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_popup();
            }
        });

        handler = new MainGame_handler();

        new send_thread("topic").start();

//        //가짜 예술가 투표 하는 기능
//        final int[] fake = new int[1];
//        final String[] player = new String[]{"p1","p2","p3","p4","p5","p6"};
//        AlertDialog.Builder vote = new AlertDialog.Builder(MainGame.this);
//        ad.setIcon(R.mipmap.fake_artist);
//        ad.setTitle("가짜 예술가를 선택하세요").setSingleChoiceItems(topics, 0, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                fake[0] = which;
//            }
//        }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                lier =  player[fake[0]];
//                //서버로 뽑은 플레이어를 보내는 코드 넣어야 됨
//
//
//
//                Intent intent = new Intent(MainGame.this, Result.class);
//                startActivity(intent);
//            }
//        });

//        vote.create();
//        vote.show();

        //30 초간 터치 가능하게 하는 기능 -->> 돌아가면서 그림 그릴 때 사용
//        new CountDownTimer(29999, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                String a = String.valueOf(30-millisUntilFinished/1000);
//                int countdown = Integer.parseInt(a);
//                timeout.setProgress(countdown);
//            }
//            @Override
//            public void onFinish() {
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//            }
//        }.start();

        //화면 막기
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        //화면 풀기
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);



        //아래 코드들은 전부 그림 그리기 관련
        view = new MyPaintView(this);

        LinearLayout container = (LinearLayout)findViewById(R.id.container);
        Resources res = getResources();


        LinearLayout.LayoutParams params;
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        container.addView(view, params);



        Button btn=findViewById(R.id.colorPickerButton);
        Button btn2=findViewById(R.id.thickPickerButton);
        Button btn3 = findViewById(R.id.clear);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                        view.setStrokeWidth(Integer.parseInt(editText.getText().toString()));

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
            }
        });
        colorPicker.show();
    }

    private void clear(){
        int Eraser = Color.WHITE;
        view.setColor(Eraser);
        view.setStrokeWidth(100);
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

    class MainGame_handler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            String line = msg.getData().getString("value");
            String[] info = line.split("\\|");
            switch(info[0]) {
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
                    String temp_msg = "차례 : " + info[1];
                    who.setText(temp_msg);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    break;
                case "your_turn":
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    break;
            }
        }
    }

}