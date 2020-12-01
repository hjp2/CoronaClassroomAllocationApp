package com.example.coronaclassroomallocationapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_join extends AppCompatActivity {
    TextInputEditText user_id; //사용자 아이디
    TextInputEditText user_pw; //사용자 비밀번호
    TextInputEditText user_name; //사용자 이름
    TextInputEditText user_email; //사용자 이메일
    TextInputEditText user_phonenum; //사용자 휴대폰 번호

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        user_id = findViewById(R.id.input_id);
        user_pw = findViewById(R.id.input_pw);
        user_name = findViewById(R.id.input_name);
        user_email = findViewById(R.id.input_email);
        user_phonenum = findViewById(R.id.input_phonenum);
    }

    public void saveInfo(View view){
        //EditText에 있는 글씨 얻어오기
        String id_data =  user_id.getText().toString();
        String pw_data =  user_pw.getText().toString();
        String name_data =  user_name.getText().toString();
        String email_data =  user_email.getText().toString();
        String phonenum_data =  user_phonenum.getText().toString();


        //FireBase 실시간 DB관리 객체 얻어오기
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        //저장시킬 노드 참조 객체 가져오기
        DatabaseReference rootRef = firebaseDatabase.getReference(); //()안에 아무것도 작성하지 않을 경우 최상위 노드

        DatabaseReference memberRef = rootRef.child("member");
        DatabaseReference itemRef = memberRef.push(); //자식 노드 생성
        itemRef.child("id").setValue(id_data);
        itemRef.child("pw").setValue(pw_data);
        itemRef.child("name").setValue(name_data);
        itemRef.child("email").setValue(email_data);
        itemRef.child("phonenum").setValue(phonenum_data);

        finish();
    }
}