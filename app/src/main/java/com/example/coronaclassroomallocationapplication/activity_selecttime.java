package com.example.coronaclassroomallocationapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activity_selecttime extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private TextView title;
    private ListView listView;
    private ListViewAdapter adapter;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecttime);

        TextView state;

        title = (TextView) findViewById(R.id.TextView_title);
        state = (TextView) findViewById(R.id.TextView_state);

        final String building = getIntent().getStringExtra("building");
        final String floor = getIntent().getStringExtra("floor");
        final String sclass = getIntent().getStringExtra("sclass");
        final String sdate = getIntent().getStringExtra("sdate");


        title.setText(sclass + "호 " + sdate);

        adapter = new ListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
        final String times[] = new String[]{"09:00 ~ 10:00","10:00 ~ 11:00","11:00 ~ 11:00","12:00 ~ 13:00","13:00 ~ 14:00","14:00 ~ 15:00","15:00 ~ 16:00", "16:00 ~ 17:00"};
        final int timecount[] = {0,0,0,0,0,0,0,0};


        // 첫 번째 아이템 추가.
        //adapter.addItem("08:00 ~ 09:00","13 / 15", "예약하기");
        // 두 번째 아이템 추가.
        mStore.collection("classroom/"+building+"/층/"+floor+"/강의실/"+sclass+"/예약")
                //.orderBy("층", Query.Direction.ASCENDING)
                .whereEqualTo("날짜", sdate)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        if (queryDocumentSnapshots != null) {
                            adapter.clearItem(); //리스트가 갱신되는게 아니라 화면에 쌓이기 때문에 정리를 해주어야한다.
                            //ids.clear();
                            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                                Map<String, Object> shot = snap.getData();
                                String rtime = String.valueOf(shot.get("시간"));
                                System.out.println(rtime);

                                if(rtime.equals(times[0])) timecount[0]++;
                                if(rtime.equals(times[1])) timecount[1]++;
                                if(rtime.equals(times[2])) timecount[2]++;
                                if(rtime.equals(times[3])) timecount[3]++;
                                if(rtime.equals(times[4])) timecount[4]++;
                                if(rtime.equals(times[5])) timecount[5]++;
                                if(rtime.equals(times[6])) timecount[6]++;
                                if(rtime.equals(times[7])) timecount[7]++;


                            }
                            for(int j = 0;j<times.length;j++){
                                adapter.addItem(times[j], Integer.toString(timecount[j]), "예약하기");
                                System.out.println("test");
                                System.out.println(timecount[j]);

                            }

                            adapter.notifyDataSetChanged();
                            listView.setSelection(adapter.getCount() - 1);


                        }
                    }
                });








        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Toast.makeText(getApplicationContext(), "선택됨",
                        Toast.LENGTH_SHORT).show();

                //Intent intent = new Intent(getApplicationContext(), activity_selectclass.class);
                //intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                //intent.putExtra("building", building);
                //intent.putExtra("floor", Array.get(arg2));
                //startActivity(intent);

                if(mAuth.getCurrentUser() != null){
                    String rId = mStore.collection("classroom/"+building+"/층/"+floor+"/강의실/"+sclass+"/예약").document().getId();
                    Map<String, Object> data = new HashMap<>();
                    data.put("날짜",sdate);
                    data.put("예약자", mAuth.getCurrentUser().getUid());
                    data.put("시간","10:00 ~ 11:00");
                    mStore.collection("classroom/"+building+"/층/"+floor+"/강의실/"+sclass+"/예약").document(rId).set(data, SetOptions.merge());
                }

            }
        });


    }

}

