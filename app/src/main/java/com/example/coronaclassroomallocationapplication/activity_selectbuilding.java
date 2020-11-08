package com.example.coronaclassroomallocationapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class activity_selectbuilding extends AppCompatActivity {

    TextView title;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectbuilding);
        //주석

        listView=(ListView)findViewById(R.id.listview);
        title = (TextView)findViewById(R.id.TextView_title);

        title.setText("건물선택");


        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("대학본관");
        arrayList.add("법정관");
        arrayList.add("상경관");
        arrayList.add("국제관");
        arrayList.add("제1인문관");
        arrayList.add("제2인문관");
        arrayList.add("상영관");





        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);



    }
}