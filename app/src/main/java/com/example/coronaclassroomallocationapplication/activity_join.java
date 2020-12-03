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
    private TextInputEditText user_email; //사용자 이메일
    private TextInputEditText user_phonenum; //사용자 휴대폰 번호

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        user_id = findViewById(R.id.input_id);
        user_pw = findViewById(R.id.input_pw);
        user_name = findViewById(R.id.input_name);
        user_email = findViewById(R.id.input_email);
        user_phonenum = findViewById(R.id.input_phonenum);

        findViewById(R.id.joinButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
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
                                userMap.put(FirebaseID.phonenum, user_phonenum.getText().toString());
                                mStore.collection(FirebaseID.user).document(user.getUid()).set(userMap, SetOptions.merge()); //SetOptions.merge는 덮어쓰기 효과
                                Toast.makeText(activity_join.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                finish(); //회원가입 종료
                            }
                        } else { //회원가입 실패 시 발생
                            Toast.makeText(activity_join.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private Boolean validataId(){
        String val =  user_id.getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if(val.isEmpty()){
            user_id.setError("아이디 항목을 입력해주세요.");
            return false;
        }
        else if(val.length()>=15){
            user_id.setError("아이디가 너무 깁니다.");
            return false;
        }
        else if(!val.matches(noWhiteSpace)){
            user_id.setError("4자 이상 20자 이하의 아이디를 입력해주세요.");
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
        String val =  user_email.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()){
            user_email.setError("이메일 항목을 입력해주세요.");
            return false;
        }else if(!val.matches(emailPattern)){
            user_email.setError("이메일 형식을 지켜주세요.");
            return false;
        }
        else{
            user_email.setError(null);
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



//    public Boolean checkId() {
//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.e("getFirebaseDatabase", "key: " + dataSnapshot.getChildrenCount());
//                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//                    String key = postSnapshot.getKey();
//                    if(key.equals(user_id.getText().toString())) {
//                        userInfo get = postSnapshot.getValue(userInfo.class);
//                        String[] info = {get.id, get.pw, get.name, get.email, get.phonenum};
//                        flag = false;
//
//                        Log.d("getFirebaseDatabase", "key: " + key);
//                        Log.d("getFirebaseDatabase", "info: " + info[0] + info[1] + info[2] + info[3] + info[4]);
//                        break;
//                    }else{
//                        System.out.println("There is no data...");
//                        flag = true;
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w("getFirebaseDatabase","loadPost:onCancelled", databaseError.toException());
//            }
//        };
//        Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("member").orderByChild("id");
//        sortbyAge.addListenerForSingleValueEvent(postListener);
//        return flag;
//    }
//
//    public void saveInfo(View view){
//        if(!validataId() || !validataPw() || !validataName() || !validataEmail() || !validataPhonenum() ){
//            Toast.makeText(activity_join.this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if(!checkId()){
//            Toast.makeText(activity_join.this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        //EditText에 있는 글씨 얻어오기
//        String id_data =  user_id.getText().toString();
//        String pw_data =  user_pw.getText().toString();
//        String name_data =  user_name.getText().toString();
//        String email_data =  user_email.getText().toString();
//        String phonenum_data =  user_phonenum.getText().toString();
//
//        //FireBase 실시간 DB관리 객체 얻어오기
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//
//        //저장시킬 노드 참조 객체 가져오기
//        DatabaseReference rootRef = firebaseDatabase.getReference(); //()안에 아무것도 작성하지 않을 경우 최상위 노드
//
//        Map<String, Object> childUpdates = new HashMap<>();
//        Map<String, Object> postValues = null;
//
//        userInfo post = new userInfo(id_data, pw_data, name_data, email_data, phonenum_data);
//        postValues = post.toMap();
//
//        childUpdates.put("/member/"+ id_data , postValues);
//        rootRef.updateChildren(childUpdates);
//
//        Toast.makeText(activity_join.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
//
//        finish();
//    }
}