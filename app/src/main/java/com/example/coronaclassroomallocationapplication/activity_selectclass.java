package com.example.coronaclassroomallocationapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class activity_selectclass extends AppCompatActivity {
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private FirebaseDatabase mDatabase;

    private TextView title;
    private ListView listView;
    private CalendarView calview;
    private ArrayAdapter<String> adapter;
    private List<String> Array = new ArrayList<String>();
    private String sdate;
    private ArrayList<String> max;
    private String currentlevel;
    private String[] levelinfo= new String[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectclass);
        //주석
        final String building = getIntent().getStringExtra("building");
        final String floor = getIntent().getStringExtra("floor");
        SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-M-d");
        Date time = new Date();
        sdate = format1.format(time);
        System.out.println(sdate);

        ImageView backbutton = findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });




        listView = findViewById(R.id.listview);
        TextView title;
        title = findViewById(R.id.TextView_title);
        calview = findViewById(R.id.calendarView);
        calview.setDate(System.currentTimeMillis(), false, true);

        calview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                int rmonth = month + 1;
                sdate = new String(year + "-" + rmonth + "-" + dayOfMonth);
                System.out.println(sdate);
            }
        });


        title.setText(building + " " + floor);

        adapter = new ArrayAdapter<String>(this, android.R.layout. simple_list_item_1, new ArrayList<String>());
        max = new ArrayList<String>();
        listView.setAdapter(adapter);




        DocumentReference docRef = mStore.collection("corona").document("level");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        currentlevel = document.getString("current");
                        levelinfo[0] = document.getString(currentlevel);

                        System.out.println(currentlevel);
                        System.out.println(levelinfo);

                    }

                    levelinfo[0] = document.getString(currentlevel);
                }

            }
        });


        mStore.collection("classroom/"+building+"/층/"+floor+"/강의실")
                //.orderBy("층", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {

                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        if (queryDocumentSnapshots != null) {
                            Array.clear(); //리스트가 갱신되는게 아니라 화면에 쌓이기 때문에 정리를 해주어야한다.
                            //ids.clear();
                            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                                Map<String, Object> shot = snap.getData();
                                //String floor = String.valueOf(shot.get("층"));
                                String info = String.valueOf(shot.get("종류"));
                                String temp = String.valueOf(shot.get("최대인원"));
                                max.add(temp);
                                String sclass = snap.getId();
                                //System.out.println(floor);

                                //ids.add(id);
                                Array.add(sclass);
                                adapter.add(sclass+" - "+info + "\n적정인원: "+ Integer.parseInt(temp)/2);
                            }
                            adapter.notifyDataSetChanged();
                            listView.setSelection(adapter.getCount() - 1);
                            listView.smoothScrollToPosition(0);

                        }
                    }
                });




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //Toast.makeText(getApplicationContext(), (String) Array.get(arg2),
                //        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), activity_selecttime.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("building", building);
                intent.putExtra("floor", floor);
                intent.putExtra("sdate", sdate);
                intent.putExtra("max", Integer.toString(Integer.parseInt(max.get(arg2))/2));
                intent.putExtra("sclass", (String) Array.get(arg2));
                startActivity(intent);
            }
        });


    }
}