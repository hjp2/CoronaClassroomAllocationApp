package com.example.coronaclassroomallocationapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Text;

import java.util.Map;

//어플리케이션의 메인화면
public class MainActivity extends AppCompatActivity {
    CardView sub_title1; //강의실 예약
    CardView sub_title2; //커뮤니티
    CardView sub_title3; //동의모바일
    CardView sub_title4; //공지사항
    CardView sub_title5; //중앙도서관
    CardView sub_title6; //내정보변경
    TextView myname; //이름
    Button logout_button; //로그아웃 버튼
    private FirebaseAuth mAuth;
    Intent intent;
    private long backKeyPressedTime = 0;
    private Toast toast;
    private String id;
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();


    private void init(){ //초기화 메소드 => 각각의 View의 Id를 찾아 저장한다.
        sub_title1 = (CardView)findViewById(R.id.sub_title1);
        sub_title2 = (CardView)findViewById(R.id.sub_title2);
        sub_title3 = (CardView)findViewById(R.id.sub_title3);
        sub_title4 = (CardView)findViewById(R.id.sub_title4);
        sub_title5 = (CardView)findViewById(R.id.sub_title5);
        sub_title6 = (CardView)findViewById(R.id.sub_title6);
        logout_button = findViewById(R.id.logout_button);
        myname = (TextView)findViewById(R.id.myname);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.init();

        //강의실 버튼을 클릭했을 경우
        sub_title1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //건물 선택화면으로 이동
                intent = new Intent(MainActivity.this, activity_selectbuilding.class);
                startActivity(intent);
            }
        });

        //커뮤니티 버튼을 클릭했을 경우
        sub_title2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //커뮤니티 화면으로 이동
                intent = new Intent(MainActivity.this, activity_coummunity.class);
                startActivity(intent);
            }
        });

        //동의모바일 버튼을 클릭했을 경우
        sub_title3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //동의모바일 화면으로 이동
                intent = getPackageManager().getLaunchIntentForPackage("kr.ac.deu.mobileid");
                startActivity(intent);
                finish();
            }
        });

        //공지사항 버튼을 클릭했을 경우
        sub_title4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //동의대학교 공지사항 화면으로 이동
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.deu.ac.kr/www/board/3"));
                startActivity(intent);
                finish();
            }
        });

        //중앙도서관 버튼을 클릭했을 경우
        sub_title5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //동의대학교 중앙도서관 화면으로 이동
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://lib.deu.ac.kr/lib/SlimaPlus.csp"));
                startActivity(intent);
                finish();
            }
        });

//        sub_title6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                intent = new Intent(MainActivity.this, activity_secondprivateinfo.class);
//                startActivity(intent);
//            }
//        });

        //내정보변경 버튼을 클릭했을 경우
        sub_title6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //내정보변경 화면으로 이동
                intent = new Intent(MainActivity.this, activity_secondprivateinfo.class);
                startActivity(intent);
            }
        });

        //로그아웃 버튼을 클릭했을 경우
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getInstance().signOut(); //계정에서 로그아웃
                intent = new Intent(MainActivity.this, activity_login.class);
                startActivity(intent);
            }
        });

        //메인화면의 이름 속성을 현재 사용자의 이름으로 변경하기 위한 코드
        String user = mAuth.getInstance().getUid();
        //mStore에 저장되어있는 현재 유저의 정보를 받아 그중 name속성을 메인화면 페이지에 적용
        mStore.collection(FirebaseID.user).document(user)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) { //문서가 없을경우 처리방법
                                if (task.getResult() != null) {
                                    Map<String, Object> snap = task.getResult().getData();
                                    String name = String.valueOf(snap.get(FirebaseID.name));
                                    myname.setText(name);
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
            toast.cancel();
            toast = Toast.makeText(this,"이용해 주셔서 감사합니다.",Toast.LENGTH_LONG);
            toast.show();
        }
    }
}