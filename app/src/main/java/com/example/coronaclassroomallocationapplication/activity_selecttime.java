package com.example.coronaclassroomallocationapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class activity_selecttime extends AppCompatActivity {
    TextView title;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecttime);
        //주석


        ListViewAdapter adapter;
        ListView listview ;
        TextView state;

        title = (TextView)findViewById(R.id.TextView_title);


        state = (TextView)findViewById(R.id.TextView_state);
        title.setText("915호 12월 12일");



        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);

        // 첫 번째 아이템 추가.
        adapter.addItem("08:00 ~ 09:00","13 / 15", "예약하기");
        // 두 번째 아이템 추가.
        adapter.addItem("time2","people2", "state2");
        // 세 번째 아이템 추가.
        adapter.addItem("time3","people3", "state3");
        adapter.addItem("time4","people4", "state4");

        adapter.addItem("time5","people5", "state5");

        adapter.addItem("time6","people6", "state6");


    }


}