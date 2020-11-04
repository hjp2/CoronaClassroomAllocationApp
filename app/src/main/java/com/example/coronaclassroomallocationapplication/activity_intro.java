package com.example.coronaclassroomallocationapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageView;

public class activity_intro extends AppCompatActivity {
    ImageView iv_intro;

    double screen_width, screen_height;
    float intro_width, intro_height;
    float intro_center_x, intro_center_y;

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




    }
}