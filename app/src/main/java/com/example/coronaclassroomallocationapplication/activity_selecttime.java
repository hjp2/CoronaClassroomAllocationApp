package com.example.coronaclassroomallocationapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class activity_selecttime extends AppCompatActivity {
    TextView title;
    ListView listView;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecttime);
        //주석

        final ListViewAdapter adapter;
        ListView listview ;
        TextView state;

        title = (TextView)findViewById(R.id.TextView_title);


        state = (TextView)findViewById(R.id.TextView_state);


        final String building = getIntent().getStringExtra("building");
        final String floor = getIntent().getStringExtra("floor");
        final String sclass = getIntent().getStringExtra("sclass");
        final String sdate = getIntent().getStringExtra("sdate");

        System.out.println(sclass);


        title.setText(sclass+"호 "+ sdate);
        initDatabase();


        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);

        // 첫 번째 아이템 추가.
        //adapter.addItem("08:00 ~ 09:00","13 / 15", "예약하기");
        // 두 번째 아이템 추가.

        mReference = mDatabase.getReference("classroom/"+building+"/"+floor+"/"+sclass+"/"+"예약"); // 변경값을 확인할 child 이름
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                String times[] = new String[]{"09:00 ~ 10:00","10:00 ~ 11:00","11:00 ~ 11:00","12:00 ~ 13:00","13:00 ~ 14:00","14:00 ~ 15:00","15:00 ~ 16:00", "16:00 ~ 17:00"};
                System.out.println(dataSnapshot.getValue().toString());

                //String data = dataSnapshot.getValue().toString();
                //JsonParser parser = new JsonParser();
                //Object ojb = parser.parse(data);
                //JSONObject jsonObject = (JSONObject)ojb;
                //System.out.println();


                int timecount[] = {0,0,0,0,0,0,0,0};
                int i = 0;

                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String rdate = dataSnapshot.child(Integer.toString(i)).child("날짜").getValue().toString();
                    String rtime = dataSnapshot.child(Integer.toString(i)).child("시간").getValue().toString();
                    String rperson = dataSnapshot.child(Integer.toString(i)).child("예약자").getValue().toString();

                    if(rdate.equals(sdate)){
                        System.out.println("same date");
                        if(rtime.equals("09:00")) timecount[0]++;
                        if(rtime.equals("10:00")) timecount[1]++;
                        if(rtime.equals("11:00")) timecount[2]++;
                        if(rtime.equals("12:00")) timecount[3]++;
                        if(rtime.equals("13:00")) timecount[4]++;
                        if(rtime.equals("14:00")) timecount[5]++;
                        if(rtime.equals("15:00")) timecount[6]++;
                        if(rtime.equals("16:00")) timecount[7]++;
                    }

                    System.out.println(rdate + rtime+ rperson);
                    System.out.println(sdate);
                    System.out.println(i);
                    i++;
                }

                for(int j=0; j<8;j++ ){
                    adapter.addItem(times[j],Integer.toString(timecount[j]), "예약하기");

                }


                adapter.notifyDataSetChanged();
                //listView.setSelection(adapter.getCount() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    private void initDatabase() {

        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReference("log");
        mReference.child("log").setValue("check");

        mChild = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mReference.addChildEventListener(mChild);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(mChild);
    }
}
