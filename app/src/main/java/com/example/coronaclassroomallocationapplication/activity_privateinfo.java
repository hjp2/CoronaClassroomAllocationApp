package com.example.coronaclassroomallocationapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.firebase.auth.FirebaseAuth;

public class activity_privateinfo extends AppCompatActivity {

    Button bt_changeinfo; //회원정보 변경
    Button bt_logout; //로그아웃
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privateinfo);

       //Layout layout = (Layout)findViewById();
        //Button butt = (Button)findViewById(R.)

        mAuth = FirebaseAuth.getInstance();
        bt_changeinfo = (Button)findViewById(R.id.info3); //회원정보 변경
        bt_logout = (Button)findViewById(R.id.info4); //로그아웃

        //고지훈 추가(회원 정보 변경) -> 문제시 이야기해주세요.
        bt_changeinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_privateinfo.this, activity_secondprivateinfo.class);
                startActivity(intent);
                finish();
            }
        });

        //고지훈 추가(로그아웃) -> 문제시 이야기해주세요.
        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getInstance().signOut(); //구글 계정에서 로그아웃
                finishAffinity();
            }
        });
    }
}
