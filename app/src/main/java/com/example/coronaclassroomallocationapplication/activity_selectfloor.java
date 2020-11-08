package com.example.coronaclassroomallocationapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class activity_selectfloor extends AppCompatActivity {
    TextView title;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectfloor);
        //주석

        listView=(ListView)findViewById(R.id.listview);
        title = (TextView)findViewById(R.id.TextView_title);

        title.setText("층 선택");


        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("1층 기계자동차");
        arrayList.add("2층 식당 전산실습실");
        arrayList.add("3층 연구실");
        arrayList.add("4층 기계자동차");



        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);



    }
}