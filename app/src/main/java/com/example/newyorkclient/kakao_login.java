package com.example.newyorkclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.exception.KakaoException;

public class kakao_login extends AppCompatActivity {

    Session session;
    KakaoCallback kakao_call_back = new KakaoCallback();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kakao_login);
        session = Session.getCurrentSession();
        session.addCallback(kakao_call_back);

    }

    public class KakaoCallback implements ISessionCallback {

        // 로그인에 성공한 상태
        @Override
        public void onSessionOpened() {
            requestMe();
        }

        // 로그인에 실패한 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
        }

        // 사용자 정보 요청
        public void requestMe() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                }

                @Override
                public void onFailure(ErrorResult errorResult) {
                    Log.e("KAKAO_API", "사용자 정보 요청 실패: " + errorResult);
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    UserAccount kakaoAccount = result.getKakaoAccount();
                    Profile profile = kakaoAccount.getProfile();
                    Intent intent = new Intent(getApplicationContext(), Lobby.class);
                    intent.putExtra("name", profile.getNickname());
                    intent.putExtra("user_id", Long.toString(result.getId()));
                    System.out.println(result.getId());
                    intent.putExtra("thumb_nail", profile.getThumbnailImageUrl());

                    startActivity(intent);
                    finish();

                }
            });
        }
    }

}


