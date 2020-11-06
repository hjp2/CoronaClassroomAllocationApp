package com.example.coronaclassroomallocationapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;
import android.view.Display;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class activity_intro extends AppCompatActivity {
    ImageView iv_intro;
    ImageView iv_logo;
    TextView tv_logo_name;
    TextView tv_logo_desc;
    Button bt_start;

    double screen_width, screen_height;
    float intro_width, intro_height;

    //인트로 화면입니다. 고지훈 수정중
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
        iv_intro.setScaleX(1.5f);
        iv_intro.setScaleY(1.5f);

        intro_height = iv_intro.getMaxHeight();
        intro_width = iv_intro.getMaxWidth();
        layout.addView(iv_intro);

        ObjectAnimator intro_translateX = ObjectAnimator.ofFloat(iv_intro,"translationX", 220f, -180f);
        ObjectAnimator intro_translateY = ObjectAnimator.ofFloat(iv_intro,"translationY", -50f, -50f);

        intro_translateX.setDuration(4000);
        intro_translateY.setDuration(4000);

        intro_translateX.start();
        intro_translateY.start();

        //로고 이미지 가져오기
        iv_logo = new ImageView(this);
        iv_logo.setImageResource(R.drawable.intro_logo);
        iv_logo.setScaleX(0.28f);
        iv_logo.setScaleY(0.28f);
        iv_logo.measure(iv_logo.getMeasuredWidth(), iv_logo.getMeasuredHeight());
        layout.addView(iv_logo);

        //로고 이름 및 로고 부연 설명 텍스트
        tv_logo_name = new TextView(this);
        tv_logo_desc = new TextView(this);
        tv_logo_name.setText(R.string.logo_name);
        tv_logo_desc.setText(R.string.logo_desc);
        tv_logo_name.setTextSize(30f);

        layout.addView(tv_logo_name);
        layout.addView(tv_logo_desc);


        //로고 위치 조정 코드
        iv_logo.setY((float)screen_height - 3500f);

        tv_logo_name.setX((float)screen_width - 930f);
        tv_logo_name.setY((float)screen_height - 1900f);
        tv_logo_desc.setX((float)screen_width - 980f);
        tv_logo_desc.setY((float)screen_height - 1800f);

        System.out.println("와이드:"+ screen_width);
    }
}