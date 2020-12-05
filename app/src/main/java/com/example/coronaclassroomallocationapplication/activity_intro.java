package com.example.coronaclassroomallocationapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

//어플리케이션의 인트로 화면
public class activity_intro extends AppCompatActivity {
    private static int SPLASH_SCREEN = 4000; //애니메이션이 진행되는 시간 4초
    Animation topAnim, bottomAnim; //화면의 상단 그림 애니메이션과 하단 텍스트 애니메이션을 담당하는 변수
    ImageView image; //이미지뷰의 변수
    TextView logo, slogan; //텍스트뷰의 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //휴대폰 Status bar를 제거하기위해 FULLSCREEN을 사용
        setContentView(R.layout.activity_intro);

        //애니메이션 => 애니메이션 폴더에 존재하는 속성을 찾아 사용할 수 있도록 지정
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //각각의 변수에서 xml에 지정된 아이디를 찾는 과정
        image = findViewById(R.id.imageView8);
        logo = findViewById(R.id.logo);
        slogan = findViewById(R.id.slogan);

        //애니메이션 효과 지정
        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() { //지연 처리를 하기 위해 핸들러 사용
                Intent intent = new Intent(activity_intro.this, activity_login.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);
    }
}