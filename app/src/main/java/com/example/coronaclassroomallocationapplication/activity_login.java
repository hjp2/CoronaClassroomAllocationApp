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

//어플리케이션의 로그인, 구글로그인 화면
public class activity_login extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); //인스턴스 mAuth 선언, 초기화
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance(); //인스턴스 mStore 선언, 초기화

    private TextInputEditText input_id; //입력받은 아이디
    private TextInputEditText input_pw; //입력받은 비밀번호
    private CheckBox checkBox;//자동로그인 체크박스
    private boolean check;

    //구글 로그인을 하기위해 사용되는 변수
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001; //구글로그인 하기 위해 요청 코드

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        input_id = findViewById(R.id.input_id); //아이디를 xml에서 찾는 과정
        input_pw = findViewById(R.id.input_pw); //비밀번호를 xml에서 찾는 과정
        checkBox = findViewById(R.id.login_checkbox); //체크박스를 xml에서 찾는 과정

        findViewById(R.id.bt_join).setOnClickListener(this); //회원가입 버튼을 클릭했을때 이벤트
        findViewById(R.id.bt_login).setOnClickListener(this); //로그인 버튼을 클릭했을때 이벤트
        findViewById(R.id.signInButton).setOnClickListener(this); //구글 로그인 버튼을 클릭했을때 이벤트

        //사용자ID, 이메일 주소와 같은 프로필 정보를 요청하여, 가져온다.(구글)
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //gso에서 지정한 옵션으로 GoogleSignInClient를 빌드(구글)
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    //구글 로그인을 위한 메소드
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //액티비티 결과 처리
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) { //요청코드가 변수와 같을 경우
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
            }
        }
    }

    //구글 로그인 성공한 후, 파이어베이스를 인증하는 과정
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //사용자가 정상적으로 로그인하면 GoogleSignInAccount객체에서 토큰ID를 가져온다.
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Firebase사용자 인증 정보로 교환
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            updateUI(null);
                        }
                    }
                });
    }

    //로그인후에 파이어스토어 내 정보를 저장하고, 다음 메인 화면으로 넘기는 메소드
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            String email = user.getEmail();
            String name = user.getDisplayName();
            String phonenum = user.getPhoneNumber();

            //구글 로그인 관련 1회성성
            Map<String, Object> userMap = new HashMap<>();
            //userMap.put(FirebaseID.documentId, user.getUid());
            userMap.put(FirebaseID.email, email); //구글 이메일
            userMap.put(FirebaseID.password, null); //구글 패스워드
            userMap.put(FirebaseID.name, name); //구글 이름
            userMap.put(FirebaseID.address, null); //구글 주소(구글에서 저장을 안했기 때문에 null)
            userMap.put(FirebaseID.phonenum, phonenum); //구글 휴대폰번호(구글에서 저장을 안했기 때문에 null)
            mStore.collection(FirebaseID.user).document(user.getUid()).set(userMap, SetOptions.merge()); //SetOptions.merge는 덮어쓰기 효과

            //intent를 사용하여 메인화면으로 이동
            Intent intent = new Intent(activity_login.this, MainActivity.class);
            startActivity(intent);
            finish(); //완료되면 현재화면 종료
        }
    }

    //기본 이메일 로그인
    @Override
    protected void onStart() {
        super.onStart();
        //현재 접속된 유저 정보가 존재할 경우, 자동 로그인
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            Toast.makeText(activity_login.this, "자동 로그인: "+user.getUid(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(activity_login.this, MainActivity.class));
            finish(); //현재화면 종료
        }
    }

    //각각에 버튼을 클릭했을 때 발생하는 이   벤트
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_join: //회원가입 버튼을 클릭했을 경우
                startActivity(new Intent(activity_login.this, activity_join.class)); //회원가입 화면으로 이동
                break;
            case R.id.bt_login: //로그인 버튼을 클릭했을 경우
                if(input_id.getText().toString().equals("") || input_pw.getText().toString().equals("")){ //텍스트필드가 비어있을 경우 토스트메시지
                    Toast.makeText(activity_login.this, "아이디와 비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                }else {
                    mAuth.signInWithEmailAndPassword(input_id.getText().toString(), input_pw.getText().toString())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if(checkBox.isChecked()){ //자동로그인을 위한 체크박스
                                            checkBox.setChecked(true);
                                        }
                                        if (user != null) {
                                            //로그인이 성공하게 될 경우 토스트 메시지 및 메인 화면 이동
                                            Toast.makeText(activity_login.this, "로그인 성공: " + user.getUid(), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(activity_login.this, MainActivity.class));
                                        }
                                    } else {
                                        //로그인을 실패했을 경우 토스트메시지
                                        Toast.makeText(activity_login.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                break;
            case R.id.signInButton: //구글 로그인 버튼을 클릭했을 경우
                signIn(); //메서드호출
                break;
        }
    }
}