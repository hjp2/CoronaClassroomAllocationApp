package com.example.coronaclassroomallocationapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private String max;

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

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        listView.setAdapter(adapter);

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
                                max = String.valueOf(shot.get("최대인원"));
                                String sclass = snap.getId();
                                //System.out.println(floor);

                                //ids.add(id);
                                Array.add(sclass);
                                adapter.add(sclass+" - "+info + "       최대인원: "+max);
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
                intent.putExtra("max", max);
                intent.putExtra("sclass", (String) Array.get(arg2));
                startActivity(intent);
            }
        });


    }
}