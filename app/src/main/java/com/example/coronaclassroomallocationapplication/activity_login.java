package com.example.coronaclassroomallocationapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
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

import org.w3c.dom.Text;

public class activity_login extends AppCompatActivity {
    //일반 로그인 관련
    Button bt_login; //로그인 버튼
    Button bt_join; //회원가입 버튼
    Button bt_findpw; //비밀번호 찾기 버튼

    TextInputEditText input_id; //입력받은 아이디
    TextInputEditText input_pw; //입력받은 비밀번호

    String id; //임시 아이디(DB이전)
    String pw; //임시 비밀번호(DB이전)

    boolean flag = false; //로그인 성공 여부를 판단하기 위한 변수

    //구글 로그인 관련
    private FirebaseAuth mAuth = null;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private SignInButton signInButton;

    private void init(){ //초기화 함수
        bt_login = (Button)findViewById(R.id.bt_login);
        bt_join = (Button)findViewById(R.id.bt_join);
        bt_findpw = (Button)findViewById(R.id.bt_findpw);

        id = "qwer";
        pw = "qwer123";
    }

    private boolean loginFlag() { //로그인 성공 여부를 판단하기 위한 메서드
        input_id = (TextInputEditText)findViewById(R.id.input_id);
        input_pw = (TextInputEditText)findViewById(R.id.input_pw);

        if (id.equals(input_id.getText().toString())) {
            System.out.println("아이디 성공");
            if (pw.equals(input_pw.getText().toString())) {
                System.out.println("비밀번호 성공");
                flag = true;
                return flag;
            } else {
                System.out.println("비밀번호 오류");
                flag = false;
                return flag;
            }
        } else {
            System.out.println("아이디 오류");
            flag = false;
            return flag;
        }
    }

    //구글 로그인
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
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
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) { //update ui code here
        if (user != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.init(); //초기화 함수 호출

        //로그인 버튼 클릭 이벤트 정의
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loginFlag() == true) {
                    Intent intent = new Intent(activity_login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(activity_login.this, "아이디 또는 비밀번호 오류",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //회원가입 버튼 클릭 이벤트 정의
        bt_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_login.this, activity_join.class);
                startActivity(intent);
            }
        });

        //구글 로그인
        signInButton = findViewById(R.id.signInButton);
        mAuth = FirebaseAuth.getInstance(); //FirebaseAuth를 사용하기 위해 인스턴스를 받아와야함

        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }
}