package com.example.coronaclassroomallocationapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

//커뮤니티에 글을 작성하기 위한 화면
public class activity_post extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); //Firebase를 사용하기 위해 mAuth인스턴스 생성 및 초기화
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance(); //Firebase를 사용하기 위해 mStore인스턴스 생성 및 초기화
    private EditText mTitle, mContents; //글 제목, 글 내욜
    private String name; //작성자 이름

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mTitle = findViewById(R.id.post_title_edit); //View에서 Id를 찾아 지정
        mContents = findViewById(R.id.post_contents_edit); //View에서 Id를 찾아 지정

        findViewById(R.id.post_save_button).setOnClickListener(this); //글 작성 완료 버튼 이벤트

        //현재 작성한 사용자의 이름을 받아오기 위한 작업
        if(mAuth.getCurrentUser() != null){
            //FireStore에 저장된 user의 정보를 받아, 속성인 name만 추출
            mStore.collection(FirebaseID.user).document(mAuth.getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.getResult() != null){
                                name = (String) task.getResult().getData().get(FirebaseID.name);
                            }
                        }
                    });
        }

        //뒤로가기 버튼을 클릭했을 경우 이벤트
        findViewById(R.id.write_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Community화면으로 이동
                Intent intent = new Intent(activity_post.this, activity_coummunity.class);
                startActivity(intent);
                finish(); //작업을 마치면 현재 화면 종료
            }
        });
    }

    //완료 버튼 클릭 이벤트
    @Override
    public void onClick(View v) {
        if(mAuth.getCurrentUser() != null){//현재 로그인된 사용자가 null이 아닐 경우
            String postId = mStore.collection(FirebaseID.post).document().getId(); //현재 접속한 유저의 아이디를 받아온다.
            Map<String, Object> data = new HashMap<>(); //데이터를 저장하기위해 Key와 Value를 갖는 Map변수 선언
            data.put(FirebaseID.documentId, postId); //포스팅 Id
            data.put(FirebaseID.userId, mAuth.getCurrentUser().getUid()); //접속한 사용자 아이디
            data.put(FirebaseID.title,mTitle.getText().toString()); //작성글 제목
            data.put(FirebaseID.contents,mContents.getText().toString()); //작성글 내용
            data.put(FirebaseID.name, name); //작성자 이름
            data.put(FirebaseID.timestamp, FieldValue.serverTimestamp()); //작성된 글의 시간
            mStore.collection(FirebaseID.post).document(postId).set(data, SetOptions.merge());
            //FireStore에 post항목에 저장
            finish();//모든 작업이 완료될 경우, 작업 종료
        }
    }
}