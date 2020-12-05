package com.example.coronaclassroomallocationapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

//어플리케이션의 내정보변경 화면
public class activity_secondprivateinfo extends AppCompatActivity {
    private Button bt_deleteuser; //회원 탈퇴 버튼
    private Button bt_changeuser; //회원정보변경 버튼
    private TextInputEditText info_pw; //패스워드
    private TextInputEditText info_name; //이름
    private TextInputEditText info_address; //주소
    private TextInputEditText info_phonenum; //휴대폰 번호
    private Button bt_mypost; //내가 작성한글
    private TextView user_info_id; //사용자의 아이디
    private TextView user_info_name; //사용자의 이름
    private FirebaseAuth mAuth; //FirebaseAuth의 인스턴스 변수
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance(); //FireStore의 인스턴스 변수 생성,초기화
    private ImageView privateinfo_back_button; //뒤로가기 버튼


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privateinfo2);

        //각 뷰에 맞는 아이디를 저장
        mAuth = FirebaseAuth.getInstance();
        bt_deleteuser = (Button)findViewById(R.id.delete_user); //회원 탈퇴
        info_pw = findViewById(R.id.info_input_pw);
        info_name = findViewById(R.id.info_input_name);
        info_address = findViewById(R.id.info_input_address);
        info_phonenum = findViewById(R.id.info_input_phonenum);
        user_info_id = findViewById(R.id.user_info_id);
        user_info_name = findViewById(R.id.user_info_name);
        bt_changeuser = findViewById(R.id.info_change);
        bt_mypost=findViewById(R.id.info_write);
        privateinfo_back_button = findViewById(R.id.privateinfo_back_button);

        //현재 사용자를 요청하는 코드
        String user = mAuth.getInstance().getUid(); //현재 사용자의 Uid값을 저장
        mStore.collection(FirebaseID.user).document(user) //FireStore에 user의 값과 일치하는 정보를 찾음
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) { //찾기가 성공했다면 조건문 내부 실행
                            if (task.getResult().exists()) { //문서가 없을경우 처리방법
                                if (task.getResult() != null) {
                                    Map<String, Object> snap = task.getResult().getData(); //키와 값으로 이루어진 Map변수 선언
                                    String id = String.valueOf(snap.get(FirebaseID.email)); //아이디 삽입
                                    String pw = String.valueOf(snap.get(FirebaseID.password)); //비밀번호 삽입
                                    String name = String.valueOf(snap.get(FirebaseID.name)); //이름 삽입
                                    String address = String.valueOf(snap.get(FirebaseID.address)); //주소 삽입
                                    String phonenum = String.valueOf(snap.get(FirebaseID.phonenum)); //휴대폰 번호 삽입
                                    info_pw.setText(pw); //비밀번호 필드에 저장
                                    user_info_id.setText(id); //아이디 필드에 저장
                                    info_name.setText(name); //사용자 이름 필드에 저장
                                    user_info_name.setText(name); //사용자 이름 필드에 저장
                                    info_address.setText(address); //사용자 주소 필드에 저장
                                    info_phonenum.setText(phonenum); //사용자 휴대폰번호 필드에 저장
                                }
                            }
                        }
                    }
                });

        bt_changeuser.setOnClickListener(new View.OnClickListener() {
            String user = mAuth.getInstance().getUid(); //현재 로그인된 사용자의 Uid값을 저장
            @Override
            public void onClick(View v) { //클릭 이벤트
                mStore.collection(FirebaseID.user).document(user)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser(); //정보를 업데이트 하기위해 현재 사용자의 정보를 저장
                                    if (user != null) {
                                        Map<String, Object> userMap = new HashMap<>(); //사용자 정보를 저장하기 위해  Map변수 생성
                                        userMap.put(FirebaseID.password, info_pw.getText().toString()); //비밀번호 삽입
                                        userMap.put(FirebaseID.name, info_name.getText().toString()); //이름 삽입
                                        userMap.put(FirebaseID.address, info_address.getText().toString()); //주소 삽입
                                        userMap.put(FirebaseID.phonenum, info_phonenum.getText().toString()); //휴대폰 번호 삽입
                                        mStore.collection(FirebaseID.user).document(user.getUid()).set(userMap, SetOptions.merge()); //SetOptions.merge는 덮어쓰기 효과
                                        //정보가 저장이 완료되었을 경우, 토스트메시지 출력
                                        Toast.makeText(activity_secondprivateinfo.this, "정보 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });

        //회원 탈퇴 버튼 이벤트
        bt_deleteuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getCurrentUser().delete(); //현재 사용자의 정보를 삭제
                Toast.makeText(activity_secondprivateinfo.this, "그동안 이용해주셔서 감사합니다.", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        });

        //내글보기 버튼 이벤트트
       bt_mypost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_secondprivateinfo.this, activity_mypost.class);
                startActivity(intent);
                finish();
            }
        });
        //뒤로가기 버튼 이벤트
        privateinfo_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_secondprivateinfo.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });;
    }
}
