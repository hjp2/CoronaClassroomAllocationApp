package com.example.coronaclassroomallocationapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;


public class activity_login extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private TextInputEditText input_id; //입력받은 아이디
    private TextInputEditText input_pw; //입력받은 비밀번호
    private CheckBox checkBox;//자동로그인 체크박스
    private boolean check;

    //구글 로그인 관련
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        input_id = findViewById(R.id.input_id); //아이디를 찾는 과정
        input_pw = findViewById(R.id.input_pw); //비밀번호를 찾는 과정
        checkBox = findViewById(R.id.login_checkbox);

        findViewById(R.id.bt_join).setOnClickListener(this); //회원가입 버튼을 클릭했을때 이벤트
        findViewById(R.id.bt_login).setOnClickListener(this); //로그인 버튼을 클릭했을때 이벤트
        findViewById(R.id.signInButton).setOnClickListener(this); //구글 로그인 버튼을 클릭했을때 이벤트

        //구글로그인 확인 과정
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    //구글 로그인
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            String email = user.getEmail();
            String name = user.getDisplayName();
            String phonenum = user.getPhoneNumber();

            //구글 로그인 관련 1회성성
            Map<String, Object> userMap = new HashMap<>();
            //userMap.put(FirebaseID.documentId, user.getUid());
            userMap.put(FirebaseID.email, email);
            userMap.put(FirebaseID.password, null);
            userMap.put(FirebaseID.name, name);
            userMap.put(FirebaseID.address, null);
            userMap.put(FirebaseID.phonenum, phonenum);
            mStore.collection(FirebaseID.user).document(user.getUid()).set(userMap, SetOptions.merge()); //SetOptions.merge는 덮어쓰기 효과

            Intent intent = new Intent(activity_login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //기본 이메일 로그인
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            Toast.makeText(activity_login.this, "자동 로그인: "+user.getUid(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(activity_login.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_join: //회원가입 버튼을 클릭했을 경우
                startActivity(new Intent(activity_login.this, activity_join.class));
                break;
            case R.id.bt_login: //로그인 버튼을 클릭했을 경우
                if(input_id.getText().toString().equals("") || input_pw.getText().toString().equals("")){
                    Toast.makeText(activity_login.this, "아이디와 비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                }else {
                    mAuth.signInWithEmailAndPassword(input_id.getText().toString(), input_pw.getText().toString())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if(checkBox.isChecked()){
                                            checkBox.setChecked(true);
                                        }
                                        if (user != null) {
                                            Toast.makeText(activity_login.this, "로그인 성공: " + user.getUid(), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(activity_login.this, MainActivity.class));
                                        }
                                    } else {
                                        Toast.makeText(activity_login.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                break;
            case R.id.signInButton: //구글 로그인 버튼을 클릭했을 경우
                signIn();
                break;
        }
    }
}