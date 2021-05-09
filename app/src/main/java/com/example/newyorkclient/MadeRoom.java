package com.example.newyorkclient;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MadeRoom extends AppCompatActivity {
    TextView player1;
    TextView player2;
    TextView player3;
    TextView player4;
    TextView player5;
    TextView player6;
    TextView codename;

    Button cancel;
    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_made_room);
        player1 = findViewById(R.id.player1);
        player2 = findViewById(R.id.player2);
        player3 = findViewById(R.id.player3);
        player4 = findViewById(R.id.player4);
        player5 = findViewById(R.id.player5);
        player6 = findViewById(R.id.player6);
        codename = findViewById(R.id.Codename);
        cancel = findViewById(R.id.cancel);
        start = (Button) findViewById(R.id.start);

        cancel.setEnabled(false);
        //start.setEnabled(false);

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
                    }
                }.start();
            }
        });


    }
}