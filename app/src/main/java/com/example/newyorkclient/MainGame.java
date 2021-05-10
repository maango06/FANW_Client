package com.example.newyorkclient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainGame extends AppCompatActivity {

    MyPaintView view;
    int tColor,n=0;
    String topic; // 최종 주제
    TextView who;
    ProgressBar timeout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        who = findViewById(R.id.who);
        timeout = findViewById(R.id.timeout);

        //주제 선정(랜덤으로 한 명한테 이 기능 부여) 및 라이어 선정
        final int[] selectedTopic = {0};
        final String[] topics = new String[]{"동물","장소","직업","먹을 것","탈 것","감정"};
        AlertDialog.Builder ad = new AlertDialog.Builder(MainGame.this);
        ad.setIcon(R.mipmap.fake_artist);
        ad.setTitle("주제 선정").setSingleChoiceItems(topics, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedTopic[0] = which;
            }
        }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                topic =  topics[selectedTopic[0]];
                //아래 if 조건 안에 라이어 조건 집어 넣기
                if(true){
                    AlertDialog.Builder choose = new AlertDialog.Builder(MainGame.this);
                    choose.setIcon(R.mipmap.fake_artist);
                    choose.setTitle("당신은 예술가 입니다!");
                    choose.setMessage("\t\t\t\t\t\t\t\t\t\t\t\t\t\t주제: "+ topic+"\n"+"\t\t\t\t\t\t\t\t\t\t\t\t제시어: "+ "answer");
                    choose.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    choose.show();
                }
                else{
                    AlertDialog.Builder choose = new AlertDialog.Builder(MainGame.this);
                    choose.setIcon(R.mipmap.fake_artist);
                    choose.setTitle("당신은 가짜 예술가입니다!");
                    choose.setMessage("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t주제: "+ topic);

                    choose.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    choose.show();
                }
            }
        });

        ad.create();
        ad.show();
        // 여기까지

        //30 초간 터치 가능하게 하는 기능 -->> 돌아가면서 그림 그릴 때 사용
        new CountDownTimer(29999, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String a = String.valueOf(30-millisUntilFinished/1000);
                int countdown = Integer.parseInt(a);
                timeout.setProgress(countdown);
            }
            @Override
            public void onFinish() {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }.start();

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

}