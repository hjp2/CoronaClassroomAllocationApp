package com.example.coronaclassroomallocationapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class activity_selectclass extends AppCompatActivity {

    TextView title;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectclass);
        //주석

        listView=(ListView)findViewById(R.id.listview);
        title = (TextView)findViewById(R.id.TextView_title);

        title.setText("정보공학관 9층");


        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("정보901 \n멀티미디어 실습실");
        arrayList.add("정보901 \n멀티미디어 실습실");
        arrayList.add("정보901 \n멀티미디어 실습실");
        arrayList.add("정보901 \n멀티미디어 실습실");
        arrayList.add("정보901 \n멀티미디어 실습실");
        arrayList.add("정보901 \n멀티미디어 실습실");
        arrayList.add("정보901 \n멀티미디어 실습실");
        arrayList.add("정보901 \n멀티미디어 실습실");
        arrayList.add("정보901 \n멀티미디어 실습실");
        arrayList.add("정보901 \n멀티미디어 실습실");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);



    }
}