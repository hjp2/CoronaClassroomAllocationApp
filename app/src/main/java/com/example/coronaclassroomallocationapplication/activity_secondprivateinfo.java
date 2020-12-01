package com.example.coronaclassroomallocationapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class activity_secondprivateinfo extends AppCompatActivity {
    Button bt_deleteuser; //회원 탈퇴
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privateinfo2);

        mAuth = FirebaseAuth.getInstance();
        bt_deleteuser = (Button)findViewById(R.id.info4); //회원 탈퇴

        //고지훈 추가(회원 탈퇴) -> 문제시 이야기해주세요.
        bt_deleteuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getCurrentUser().delete();
                finishAffinity();
            }
        });
    }
}
