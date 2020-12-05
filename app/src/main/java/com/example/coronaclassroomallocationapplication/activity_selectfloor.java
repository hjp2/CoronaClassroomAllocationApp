package com.example.coronaclassroomallocationapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class activity_selectfloor extends AppCompatActivity {
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> Array = new ArrayList<String>();
    //private List<String> ids = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectfloor);
        //주석
        TextView title;
        title = (TextView) findViewById(R.id.TextView_title);
        final String building = getIntent().getStringExtra("building");
        //final String docid = getIntent().getStringExtra("docid");

        ImageView backbutton = findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        title.setText(building + " 층선택");
        listView = (ListView) findViewById(R.id.listview);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        listView.setAdapter(adapter);







        mStore.collection("classroom/"+building+"/층")
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
                                String info = String.valueOf(shot.get("층정보"));
                                String floor = snap.getId();
                                //System.out.println(floor);

                                //ids.add(id);
                                Array.add(floor);
                                adapter.add(floor+" - "+info);
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
                //Toast.makeText(getApplicationContext(), Array.get(arg2),
                //        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), activity_selectclass.class);
                intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("building", building);
                intent.putExtra("floor", Array.get(arg2));
                startActivity(intent);

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

    }
}
