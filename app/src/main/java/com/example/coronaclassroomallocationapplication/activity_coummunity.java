package com.example.coronaclassroomallocationapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.admin.ConnectEvent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.coronaclassroomallocationapplication.adapters.PostAdapter;
import com.example.coronaclassroomallocationapplication.models.Post;
import com.example.coronaclassroomallocationapplication.models.Repost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.*;

public class activity_coummunity extends AppCompatActivity implements View.OnClickListener, RecyclerViewItemClickListener.OnItemClickListener {
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    int array_image[] = {R.drawable.cat, R.drawable.wolf, R.drawable.children, R.drawable.rabbit, R.drawable.dog};
    Random ram = new Random();
    int num = ram.nextInt(array_image.length);

    private RecyclerView mPostRecyclerView;
    private PostAdapter mAdapter;
    private List<Post> mDatas;

    private Intent intent;
    private ImageView homebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coummunity);

        mPostRecyclerView = findViewById(R.id.main_recyclerview);
        findViewById(R.id.main_post_edit).setOnClickListener(this);

        mPostRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this, mPostRecyclerView, this));
        homebutton = findViewById(R.id.homebutton2);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(activity_coummunity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatas = new ArrayList<>();
        mStore.collection(FirebaseID.post)
                .orderBy(FirebaseID.timestamp, Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        if(queryDocumentSnapshots != null) {
                            mDatas.clear(); //리스트가 갱신되는게 아니라 화면에 쌓이기 때문에 정리를 해주어야한다.
                            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                                Map<String, Object> shot = snap.getData();
                                String documentId = String.valueOf(shot.get(FirebaseID.documentId));
                                String title = String.valueOf(shot.get(FirebaseID.title));
                                String contents = String.valueOf(shot.get(FirebaseID.contents));
                                String name = String.valueOf(shot.get(FirebaseID.name));
                                Post data = new Post(documentId, title, contents, name);
                                mDatas.add(data);
                            }

                            mAdapter = new PostAdapter(mDatas);
                            mPostRecyclerView.setAdapter(mAdapter);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(activity_coummunity.this, activity_post.class));
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, activity_notice.class);
        intent.putExtra(FirebaseID.documentId, mDatas.get(position).getDocumentId());
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, final int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("삭제 하시겠습니까?");
        dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mStore.collection(FirebaseID.post).document(mDatas.get(position).getDocumentId()).delete();
                Toast.makeText(activity_coummunity.this, "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(activity_coummunity.this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setTitle("삭제 알림");
        dialog.show();
    }
}