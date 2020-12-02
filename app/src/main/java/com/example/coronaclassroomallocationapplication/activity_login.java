package com.example.coronaclassroomallocationapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class activity_login extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private TextInputEditText input_id; //입력받은 아이디
    private TextInputEditText input_pw; //입력받은 비밀번호

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        input_id = findViewById(R.id.input_id); //아이디를 찾는 과정
        input_pw = findViewById(R.id.input_pw); //비밀번호를 찾는 과정

        findViewById(R.id.bt_join).setOnClickListener(this); //회원가입 버튼을 클릭했을때 이벤트
        findViewById(R.id.bt_login).setOnClickListener(this); //로그인 버튼을 클릭했을떄 이벤트
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            Toast.makeText(activity_login.this, "자동 로그인: "+user.getUid(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(activity_login.this, activity_coummunity.class));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_join: //회원가입 버튼을 클릭했을 경우
                startActivity(new Intent(activity_login.this, activity_join.class));
                break;
            case R.id.bt_login: //로그인 버튼을 클릭했을 경우
                mAuth.signInWithEmailAndPassword(input_id.getText().toString(), input_pw.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if(user != null) {
                                        Toast.makeText(activity_login.this, "로그인 성공: " + user.getUid(), Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(activity_login.this, activity_coummunity.class));
                                    }
                                } else {
                                    Toast.makeText(activity_login.this, "로그인 실패",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
        }
    }

//    public static String[] userinfo;
//
//    private DatabaseReference mDatabase;
//
//    //일반 로그인 관련
//    Button bt_login; //로그인 버튼
//    Button bt_join; //회원가입 버튼
//    Button bt_findpw; //비밀번호 찾기 버튼
//
//    TextInputEditText input_id; //입력받은 아이디
//    TextInputEditText input_pw; //입력받은 비밀번호
//
//
//    boolean flag = false; //로그인 성공 여부를 판단하기 위한 변수
//
//    String id = ""; //임시 아이디(DB이전)
//    String pw = ""; //임시 비밀번호(DB이전)
//
//    //구글 로그인 관련
//    private FirebaseAuth mAuth = null;
//    private GoogleSignInClient mGoogleSignInClient;
//    private static final int RC_SIGN_IN = 9001;
//    private SignInButton signInButton;
//
//    private void init(){ //초기화 함수
//        bt_login = (Button)findViewById(R.id.bt_login);
//        bt_join = (Button)findViewById(R.id.bt_join);
//        bt_findpw = (Button)findViewById(R.id.bt_findpw);
//        mDatabase = FirebaseDatabase.getInstance().getReference(); //firebase정의
//    }
//
//
//    private boolean loginFlag() { //로그인 성공 여부를 판단하기 위한 메서드
//        input_id = (TextInputEditText)findViewById(R.id.input_id);
//        input_pw = (TextInputEditText)findViewById(R.id.input_pw);
//
//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.e("getFirebaseDatabase", "key: " + dataSnapshot.getChildrenCount());
//                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//                    String key = postSnapshot.getKey();
//                    if(key.equals(input_id.getText().toString())) {
//                        userInfo get = postSnapshot.getValue(userInfo.class);
//                        String[] info = {get.id, get.pw, get.name, get.email, get.phonenum};
//                        userinfo = info;
//
//                        id = info[0];
//                        pw = info[1];
//
//                        Log.d("getFirebaseDatabase", "key: " + key);
//                        Log.d("getFirebaseDatabase", "info: " + info[0] + info[1] + info[2] + info[3] + info[4]);
//                        break;
//                    }else{
//                        System.out.println("There is no data...");
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
//
//        if (id.equals(input_id.getText().toString()) && !id.equals("")) {
//            System.out.println("아이디 성공");
//            if (pw.equals(input_pw.getText().toString()) && !pw.equals("")) {
//                System.out.println("비밀번호 성공");
//                flag = true;
//                return flag;
//            } else {
//                System.out.println("비밀번호 오류");
//                flag = false;
//                return flag;
//            }
//        } else {
//            System.out.println("아이디 오류");
//            flag = false;
//            return flag;
//        }
//    }
//
//    //구글 로그인
//    private void signIn() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseAuthWithGoogle(account);
//            } catch (ApiException e) {
//            }
//        }
//    }
//
//    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            updateUI(null);
//                        }
//                    }
//                });
//    }
//
//    private void updateUI(FirebaseUser user) { //update ui code here
//        if (user != null) {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        this.init(); //초기화 함수 호출
//
//        //로그인 버튼 클릭 이벤트 정의
//        bt_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(loginFlag() == true) {
//                    Intent intent = new Intent(activity_login.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }else{
//                    Toast.makeText(activity_login.this, "아이디 또는 비밀번호 오류",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        //회원가입 버튼 클릭 이벤트 정의
//        bt_join.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(activity_login.this, activity_join.class);
//                startActivity(intent);
//            }
//        });
//
//        //구글 로그인
//        signInButton = findViewById(R.id.signInButton);
//        mAuth = FirebaseAuth.getInstance(); //FirebaseAuth를 사용하기 위해 인스턴스를 받아와야함
//
//        if (mAuth.getCurrentUser() != null) {
//            Intent intent = new Intent(getApplication(), MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        signInButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                signIn();
//            }
//        });
//    }
}