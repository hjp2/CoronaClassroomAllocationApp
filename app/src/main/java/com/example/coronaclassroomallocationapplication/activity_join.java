package com.example.coronaclassroomallocationapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class activity_join extends AppCompatActivity implements View.OnClickListener {
    public boolean flag = false;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private TextInputEditText user_id; //사용자 아이디
    private TextInputEditText user_pw; //사용자 비밀번호
    private TextInputEditText user_name; //사용자 이름
    private TextInputEditText user_address; //사용자 주소
    private TextInputEditText user_phonenum; //사용자 휴대폰 번호

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        user_id = findViewById(R.id.input_id);
        user_pw = findViewById(R.id.input_pw);
        user_name = findViewById(R.id.input_name);
        user_address = findViewById(R.id.input_address);
        user_phonenum = findViewById(R.id.input_phonenum);

        findViewById(R.id.joinButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!validataId() || !validataPw() || !validataName() || !validataEmail() || !validataPhonenum()) {
            return;
        } else {
            mAuth.createUserWithEmailAndPassword(user_id.getText().toString(), user_pw.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    Map<String, Object> userMap = new HashMap<>();
                                    userMap.put(FirebaseID.documentId, user.getUid());
                                    userMap.put(FirebaseID.email, user_id.getText().toString());
                                    userMap.put(FirebaseID.password, user_pw.getText().toString());
                                    userMap.put(FirebaseID.name, user_name.getText().toString());
                                    userMap.put(FirebaseID.address, user_address.getText().toString());
                                    userMap.put(FirebaseID.phonenum, user_phonenum.getText().toString());
                                    mStore.collection(FirebaseID.user).document(user.getUid()).set(userMap, SetOptions.merge()); //SetOptions.merge는 덮어쓰기 효과
                                    Toast.makeText(activity_join.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                    finish(); //회원가입 종료
                                }
                            } else { //회원가입 실패 시 발생
                                Toast.makeText(activity_join.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private Boolean validataId(){
        String val =  user_id.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()){
            user_id.setError("아이디 항목을 입력해주세요.");
            return false;
        }else if(!val.matches(emailPattern)){
            user_id.setError("이메일 형식을 지켜주세요.");
            return false;
        }
        else{
            user_id.setError(null);
            return true;
        }
    }
    private Boolean validataName(){
        String val =  user_name.getText().toString();

        if(val.isEmpty()){
            user_name.setError("이름 항목을 입력해주세요.");
            return false;
        }
        else{
            user_name.setError(null);
            return true;
        }
    }
    private Boolean validataPw(){
        String val =  user_pw.getText().toString();
        String passwordval = ".{4,}";

        if(val.isEmpty()){
            user_pw.setError("비밀번호 항목을 입력해주세요.");
            return false;
        }else if(!val.matches(passwordval)){
            user_pw.setError("비밀번호를 4자 이상 입력해주세요.");
            return false;
        }
        else{
            user_pw.setError(null);
            return true;
        }
    }
    private Boolean validataEmail(){
        String val =  user_address.getText().toString();
        if(val.isEmpty()) {
            user_address.setError("주소 항목을 입력해주세요.");
            return false;
        }
        else{
            user_address.setError(null);
            return true;
        }
    }
    private Boolean validataPhonenum(){
        String val =  user_phonenum.getText().toString();

        if(val.isEmpty()){
            user_phonenum.setError("휴대폰 번호 항목을 입력해주세요.");
            return false;
        }
        else{
            user_phonenum.setError(null);
            return true;
        }
    }
}