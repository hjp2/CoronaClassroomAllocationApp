package com.example.coronaclassroomallocationapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    CardView sub_title1; //강의실 예약
    CardView sub_title2; //커뮤니티
    CardView sub_title3; //동의모바일
    CardView sub_title4; //공지사항
    CardView sub_title5; //중앙도서관
    CardView sub_title6; //내정보변경
    Intent intent;
    private long backKeyPressedTime = 0;
    private Toast toast;

    private void init(){
        sub_title1 = (CardView)findViewById(R.id.sub_title1);
        sub_title2 = (CardView)findViewById(R.id.sub_title2);
        sub_title3 = (CardView)findViewById(R.id.sub_title3);
        sub_title4 = (CardView)findViewById(R.id.sub_title4);
        sub_title5 = (CardView)findViewById(R.id.sub_title5);
        sub_title6 = (CardView)findViewById(R.id.sub_title6);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.init();

        sub_title1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, activity_selectbuilding.class);
                startActivity(intent);
            }
        });

        sub_title2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, activity_coummunity.class);
                startActivity(intent);
            }
        });

        sub_title3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = getPackageManager().getLaunchIntentForPackage("kr.ac.deu.mobileid");
                startActivity(intent);
                finish();
            }
        });

        sub_title4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.deu.ac.kr/www/board/3"));
                startActivity(intent);
                finish();
            }
        });

        sub_title5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://lib.deu.ac.kr/lib/SlimaPlus.csp"));
                startActivity(intent);
                finish();
            }
        });

        sub_title6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, activity_privateinfo.class);
                startActivity(intent);
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

//    @Override
//    public void onClick(View v){
//        switch (v.getId()){
//            case R.id.sub_title1:
//                Intent intent = new Intent(MainActivity.this, activity_selectbuilding.class);
//                startActivity(intent);
//                finish();
//                break;
//            case R.id.sub_title2:
//                break;
//            case R.id.sub_title3:
//                Intent intent3 = getPackageManager().getLaunchIntentForPackage("kr.ac.deu.mobileid");
//                startActivity(intent3);
//                finish();
//                break;
//            case R.id.sub_title4:
//                Intent intent4 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.deu.ac.kr/www/board/3"));
//                startActivity(intent4);
//                finish();
//                break;
//            case R.id.sub_title5:
//                Intent intent5 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://lib.deu.ac.kr/lib/SlimaPlus.csp"));
//                startActivity(intent5);
//                finish();
//                break;
//            case R.id.sub_title6:
//                Intent intent6 = new Intent(MainActivity.this, activity_privateinfo.class);
//                startActivity(intent6);
//                finish();
//                break;
//        }
//    }
}