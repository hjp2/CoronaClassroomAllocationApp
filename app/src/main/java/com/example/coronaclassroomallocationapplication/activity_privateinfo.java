package com.example.coronaclassroomallocationapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.coronaclassroomallocationapplication.adapters.PostAdapter;
import com.example.coronaclassroomallocationapplication.models.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;



import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class activity_privateinfo extends AppCompatActivity {

    Button bt_changeinfo; //회원정보 변경
    Button bt_logout; //로그아웃
    Button bt_mypost; //로그아웃
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    public String myname;
    private List<Post> mDatas;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privateinfo);
        TextView textView1 = (TextView) findViewById(R.id.name);
        textView1.setText(myname);

        mAuth = FirebaseAuth.getInstance();
        bt_mypost = (Button) findViewById(R.id.info2); //내글 보기
        bt_changeinfo = (Button) findViewById(R.id.info3); //회원정보 변경
        bt_logout = (Button) findViewById(R.id.info4); //로그아웃


        //박성준 추가(내글 보기 변경) -> 문제시 이야기해주세요.
        bt_mypost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_privateinfo.this, activity_mypost.class);
                startActivity(intent);
                finish();
            }
        });

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