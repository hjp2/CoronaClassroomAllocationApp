package com.example.coronaclassroomallocationapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coronaclassroomallocationapplication.adapters.RepostAdapter;
import com.example.coronaclassroomallocationapplication.adapters.notice1_Adapter;
import com.example.coronaclassroomallocationapplication.models.Repost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activity_notice extends AppCompatActivity {

    //
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private TextView sub_name, sub_data, review;
    private String id;
    private String check;

    //댓글 기능
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ativity_notice);

        ListView listview ;
        final notice1_Adapter adapter;
        adapter = new notice1_Adapter();

        listview = (ListView) findViewById(R.id.review_list);
        listview.setAdapter(adapter);

        ImageView main_image = (ImageView) findViewById(R.id.main_image);
        ImageView support_image = (ImageView) findViewById(R.id.support_image);
        TextView main_name = (TextView) findViewById(R.id.main_name);
        TextView main_data = (TextView) findViewById(R.id.main_data);
        TextView main_title = (TextView) findViewById(R.id.main_title);
        TextView main_ask = (TextView) findViewById(R.id.main_ask);


        main_image.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_launcher_foreground));
        main_name.setText("전우치");
        main_data.setText("2020/12/12");
        main_title.setText("모바일 많이 어렵나요?");
        main_ask.setText("박유현 교수님 수업이 어떦가요? 진짜 ㅈㄹ같은가요?");


        //글 불러오는 역할
        //sub_name = findViewById(R.id.sub_name);
        //sub_data = findViewById(R.id.sub_data);
        //review = findViewById(R.id.review);

        id = getIntent().getStringExtra(FirebaseID.documentId);
        Log.e("ITEM DOCUMENT ID", id);

        check = mStore.collection(FirebaseID.repost).document(id).getId();
        mStore.collection(FirebaseID.repost).document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) { //문서가 없을경우 처리방법
                                if (task.getResult() != null) {
                                    Map<String, Object> snap = task.getResult().getData();
                                    String name = String.valueOf(snap.get(FirebaseID.name));
                                    String data = String.valueOf(snap.get(FirebaseID.timestamp));
                                    String review_a = String.valueOf(snap.get(FirebaseID.contents));


                                    //sub_name.setText(name);
                                    //sub_data.setText(data);
                                    //review.setText(review_a);

                                    adapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_launcher_foreground), name,
                                            data,review_a,ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_launcher_foreground), "4");


                                }
                            } else {
                                Toast.makeText(activity_notice.this, "삭제된 문서입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}