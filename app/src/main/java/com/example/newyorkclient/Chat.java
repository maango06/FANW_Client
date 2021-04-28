package com.example.newyorkclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Chat extends AppCompatActivity {
    TextView pannel;
    EditText text_pannel;
    Button send_button;

    String nick_name;
    String player_id;
    String thumb_nail;

    Socket_service socket_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getHashKey();

        try {
            socket_service = GlobalApplication.getSocket_service();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        pannel = (TextView) findViewById(R.id.chet_log);
        text_pannel = (EditText) findViewById(R.id.send_text);
        send_button = (Button) findViewById(R.id.send_button);

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text_pannel.getText().toString();
                text_pannel.setText("");
                socket_service.Send(msg);

            }
        });

        Intent intent = getIntent();

        nick_name = intent.getExtras().getString("nick");
        player_id = intent.getExtras().getString("user_id");
        thumb_nail = intent.getExtras().getString("thumb_nail");
        String msg = "login/" + player_id + "/" + nick_name;
        socket_service.Send(msg);

    }

    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }
}