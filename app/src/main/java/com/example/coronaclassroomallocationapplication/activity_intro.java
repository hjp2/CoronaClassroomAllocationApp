package com.example.coronaclassroomallocationapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

public class activity_intro extends AppCompatActivity {
    ImageView iv_intro;
    ImageView iv_logo;
    ImageView iv_logo_name;
    ImageView iv_start;

    double screen_width, screen_height;
    float intro_width, intro_height;

    //인트로 화면입니다. 고지훈 이미지 버튼 눌림 효과 어떻게 할까?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.layout);

        /* 화면 크기 값 가져오기 */
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screen_width = size.x;
        screen_height = size.y;

        //배경 이미지 가져오기
        iv_intro = new ImageView(this);
        iv_intro.setImageResource(R.drawable.intro_image);
        iv_intro.setScaleX(2.18f);
        iv_intro.setScaleY(2.18f);

        intro_height = iv_intro.getMaxHeight();
        intro_width = iv_intro.getMaxWidth();
        layout.addView(iv_intro);

        ObjectAnimator intro_translateX = ObjectAnimator.ofFloat(iv_intro,"translationX", 700f, 350f);
        ObjectAnimator intro_translateY = ObjectAnimator.ofFloat(iv_intro,"translationY", -50f, -50f);

        intro_translateX.setDuration(8000);
        intro_translateY.setDuration(8000);

        intro_translateX.start();
        intro_translateY.start();

        //로고 이미지 가져오기
        iv_logo = new ImageView(this);
        iv_logo.setImageResource(R.drawable.intro_logo);
        iv_logo.setScaleX(0.28f);
        iv_logo.setScaleY(0.28f);
        iv_logo.measure(iv_logo.getMeasuredWidth(), iv_logo.getMeasuredHeight());
        layout.addView(iv_logo);

        iv_logo_name = new ImageView(this);
        iv_logo_name.setImageResource(R.drawable.intro_logo_name);
        iv_logo_name.setScaleX(0.65f);
        iv_logo_name.setScaleY(0.65f);
        iv_logo_name.measure(iv_logo_name.getMeasuredWidth(),iv_logo_name.getMeasuredHeight());
        layout.addView(iv_logo_name);

        //로고 위치 조정 코드
        iv_logo.setY((float)screen_height - 3700f);
        iv_logo_name.setY((float)screen_height - 2200f);

        //버튼 이미지 및 배치
        iv_start = new ImageView(this);
        iv_start.setImageResource(R.drawable.intro_start_button);
        iv_start.setScaleX(0.8f);
        iv_start.setScaleY(0.8f);
        iv_start.measure(iv_start.getMeasuredWidth(), iv_start.getMeasuredHeight());
        layout.addView(iv_start);

        //버튼 이미지 조정 코드
        iv_start.setY((float)screen_height - 700f);

        //버튼 클릭 이벤트 정의
        iv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_intro.this, activity_login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}