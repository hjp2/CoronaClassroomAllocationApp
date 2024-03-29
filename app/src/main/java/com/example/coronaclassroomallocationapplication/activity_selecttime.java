package com.example.coronaclassroomallocationapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class activity_selecttime extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private TextView title;
    private ListView listView;
    private ListViewAdapter adapter;
    int i = 0;
    int timecount[] = new int[8];
    String [] docid = new String[8];

    String rtime;
    String rname;
    String userid = mAuth.getCurrentUser().getUid();



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
        final String max = getIntent().getStringExtra("max");

        ImageView backbutton = findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        title.setText(sclass + "호 " + sdate);

        adapter = new ListViewAdapter(max);

        // 리스트뷰 참조 및 Adapter달기
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);




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
                            timecount = new int[8];
                            String times[] = new String[]{"09:00 ~ 10:00","10:00 ~ 11:00","11:00 ~ 12:00","12:00 ~ 13:00","13:00 ~ 14:00","14:00 ~ 15:00","15:00 ~ 16:00", "16:00 ~ 17:00"};
                            String[] checkname = {"예약하기", "예약하기","예약하기","예약하기","예약하기","예약하기","예약하기","예약하기"};
                            //String [] docid = new String[8];



                            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                                Map<String, Object> shot = snap.getData();

                                rtime = String.valueOf(shot.get("시간"));
                                rname = String.valueOf(shot.get("예약자"));
                                String id = snap.getId();

                                for(int k = 0 ; k<times.length ;k++){
                                    if(rtime.equals(times[k])){
                                        timecount[k]++;
                                        if(rname.equals(userid)){
                                            checkname[k] = "예약완료";
                                            docid[k] = id;
                                        }
                                    }

                                }
                            }
                            for(int j = 0;j<times.length;j++){
                                adapter.addItem(times[j], Integer.toString(timecount[j]) +" / "+ max, checkname[j]);
                                System.out.println("test");
                                System.out.println(timecount[j]);

                            }

                            adapter.notifyDataSetChanged();
                            //listView.setSelection(adapter.getCount() - 1);

                        }
                    }
                });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if(mAuth.getCurrentUser() != null){
                    String rId = mStore.collection("classroom/"+building+"/층/"+floor+"/강의실/"+sclass+"/예약").document().getId();
                    Map<String, Object> data = new HashMap<>();
                    if(adapter.getItem(arg2).getState().equals("예약하기")){
                        data.put("날짜",sdate);
                        data.put("예약자", mAuth.getCurrentUser().getUid());
                        data.put("시간",adapter.getItem(arg2).getTime());
                        mStore.collection("classroom/"+building+"/층/"+floor+"/강의실/"+sclass+"/예약").document(rId).set(data, SetOptions.merge());
                    }
                    else if(adapter.getItem(arg2).getState().equals("예약완료")){
                        mStore.collection("classroom/"+building+"/층/"+floor+"/강의실/"+sclass+"/예약").document(docid[arg2]).delete();
                    }
                }
            }
        });


    }

}