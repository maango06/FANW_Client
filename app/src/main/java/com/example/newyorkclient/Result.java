package com.example.newyorkclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class Result extends AppCompatActivity {

    ImageView real;
    ImageView fake;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        real = findViewById(R.id.Real);
        fake = findViewById(R.id.Fake);
        result = findViewById(R.id.word);
        real.setEnabled(false);
        fake.setEnabled(false);
        result.setEnabled(false);
        if(true){
            //맞혔을 때(서버에서 제일 많이 뽑힌 플레이어 가져옴
            real.setEnabled(true);
            new CountDownTimer(2001, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    result.setTextColor(Color.BLUE);
                    result.setText("Real Artisen\n WIN!!\n~~~~~");
                    result.setEnabled(true);
                }
                @Override
                public void onFinish() {

                }
            }.start();
        }
        else{
            //틀렸을 때
            fake.setEnabled(true);
            new CountDownTimer(2001, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    result.setTextColor(Color.RED);
                    result.setText("Fake Artist\n WIN!!\n~~~~~");
                    result.setEnabled(true);
                }
                @Override
                public void onFinish() {

                }
            }.start();
        }
        Intent intent = new Intent(Result.this, MadeRoom.class);
        startActivity(intent);
    }
}