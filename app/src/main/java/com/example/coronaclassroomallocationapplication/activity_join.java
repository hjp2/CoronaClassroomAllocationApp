package com.example.coronaclassroomallocationapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

//어플리케이션의 회원가입 화면
public class activity_join extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); //인스턴스 mAuth를 생성하고, 초기화
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance(); //인스턴스 mStore를 생성하고, 초기화

    private TextInputEditText user_id; //사용자 아이디
    private TextInputEditText user_pw; //사용자 비밀번호
    private TextInputEditText user_name; //사용자 이름
    private TextInputEditText user_address; //사용자 주소
    private TextInputEditText user_phonenum; //사용자 휴대폰 번호

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        //각각 xml파일에 지정된 아이디 속성을 찾는 과정
        user_id = findViewById(R.id.input_id);
        user_pw = findViewById(R.id.input_pw);
        user_name = findViewById(R.id.input_name);
        user_address = findViewById(R.id.input_address);
        user_phonenum = findViewById(R.id.input_phonenum);

        //Click 이벤트 처리
        findViewById(R.id.joinButton).setOnClickListener(this);
        findViewById(R.id.join_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); //현재 실행중인 화면을 종료한다.
            }
        });
    }

    //joinButton을 눌렀을 경우, 실행되는 메소드
    @Override
    public void onClick(View v) {
        if (!validataId() || !validataPw() || !validataName() || !validataAddress() || !validataPhonenum()) {
            return; //아이디, 패스워드, 이름, 이메일, 휴대폰번호의 입력문을 검증하는 메소드
        } else {
            //이메일주소와 비밀번호를 가져와 유효성을 검사한 후 신규사용자를 만든다.
            mAuth.createUserWithEmailAndPassword(user_id.getText().toString(), user_pw.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) { //로그인을 성공하게 될 경우
                                FirebaseUser user = mAuth.getCurrentUser(); //현재 접속중인 유저의 정보를 저장
                                if (user != null) { //유저가 null이 아닐경우
                                    //userMap에 Uid, email, password, name, address, phonenum을 저장한다.
                                    Map<String, Object> userMap = new HashMap<>();
                                    userMap.put(FirebaseID.documentId, user.getUid());
                                    userMap.put(FirebaseID.email, user_id.getText().toString());
                                    userMap.put(FirebaseID.password, user_pw.getText().toString());
                                    userMap.put(FirebaseID.name, user_name.getText().toString());
                                    userMap.put(FirebaseID.address, user_address.getText().toString());
                                    userMap.put(FirebaseID.phonenum, user_phonenum.getText().toString());
                                    //FireStore에 해당 유저에 Uid부분에 userMap을 저장, SetOptions.merge는 덮어쓰기 효과
                                    mStore.collection(FirebaseID.user).document(user.getUid()).set(userMap, SetOptions.merge());
                                    //회원가입을 성공하게 될 경우, 토스트 메시지
                                    Toast.makeText(activity_join.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                    finish(); //회원가입 종료
                                }
                            } else { //회원가입 실패하게 될 경우, 토스트 메시지
                                Toast.makeText(activity_join.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    //아이디 검증 메서드
    private Boolean validataId(){
        String val =  user_id.getText().toString();
        //정규식을 사용하여, 아이디를 검증
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()){ //항목이 비어있을 경우
            user_id.setError("아이디 항목을 입력해주세요."); //에러를 통해 사용자에게 알려준다.
            return false; //에러가 있을 경우 false 반환
        }else if(!val.matches(emailPattern)){
            user_id.setError("이메일 형식을 지켜주세요.");
            return false;
        }
        else{
            user_id.setError(null);
            return true; //모든 조건이 충족될 경우 True 반환
        }
    }
    // 이름 검증 과정
    private Boolean validataName(){
        String val =  user_name.getText().toString();

        if(val.isEmpty()){ //항목이 비어있을 경우
            user_name.setError("이름 항목을 입력해주세요.");
            return false;
        }
        else{
            user_name.setError(null);
            return true;
        }
    }

    //비밀번호 검증 과정
    private Boolean validataPw(){
        String val =  user_pw.getText().toString();
        String passwordval = ".{4,}"; //정규식

        if(val.isEmpty()){ //항목이 비어있을 경우
            user_pw.setError("비밀번호 항목을 입력해주세요.");
            return false;
        }else if(!val.matches(passwordval)){ //정규식에 부합하지 않을 경우
            user_pw.setError("비밀번호를 4자 이상 입력해주세요.");
            return false;
        }
        else{ //그외
            user_pw.setError(null);
            return true;
        }
    }

    //주소 검증 과정
    private Boolean validataAddress(){
        String val =  user_address.getText().toString();
        if(val.isEmpty()) { //항목이 비어있을 경우
            user_address.setError("주소 항목을 입력해주세요.");
            return false;
        }
        else{ //그외
            user_address.setError(null);
            return true;
        }
    }

    //휴대폰 번호 검증 과정
    private Boolean validataPhonenum(){
        String val =  user_phonenum.getText().toString();

        if(val.isEmpty()){ //항목이 비어있을 경우
            user_phonenum.setError("휴대폰 번호 항목을 입력해주세요.");
            return false;
        }
        else{//그외
            user_phonenum.setError(null);
            return true;
        }
    }
}