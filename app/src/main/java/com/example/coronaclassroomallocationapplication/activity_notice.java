package com.example.coronaclassroomallocationapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class activity_notice extends AppCompatActivity implements View.OnClickListener, RecyclerViewItemClickListener.OnItemClickListener{

    int array_image[] = {R.drawable.cat, R.drawable.wolf, R.drawable.children, R.drawable.rabbit, R.drawable.dog};
    Random ram = new Random();

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private TextView name, data, title, ask;
    private ImageView main_image, sub_image;
    private String id;
    private String check;

    //댓글 기능
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private EditText reContents;
    private String reName;
    private RecyclerView mPostRecyclerView;
    private RepostAdapter mAdapter;
    private List<Repost> mDatas;

    private ImageView backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ativity_notice);

        backbutton = findViewById(R.id.backbutton2);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //댓글 기능 역할(입력)
        reContents = findViewById(R.id.post_contents_edit2);
        findViewById(R.id.post_rewrite_button).setOnClickListener(this);

        if (mAuth.getCurrentUser() != null) {
            mStore.collection(FirebaseID.user).document(mAuth.getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult() != null) {
                                reName = (String) task.getResult().getData().get(FirebaseID.name);
                            }
                        }
                    });
        }
        mPostRecyclerView = findViewById(R.id.main_recyclerview2);
        mPostRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this, mPostRecyclerView, this));

        //글 불러오는 역할
        main_image = findViewById(R.id.main_image1);
        name = findViewById(R.id.main_name1);
        data = findViewById(R.id.main_data1);
        title = findViewById(R.id.main_title1);
        ask = findViewById(R.id.main_ask1);

        id = getIntent().getStringExtra(FirebaseID.documentId);
        Log.e("ITEM DOCUMENT ID", id);

        check = mStore.collection(FirebaseID.post).document(id).getId();

        mStore.collection(FirebaseID.post).document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) { //문서가 없을경우 처리방법
                                if (task.getResult() != null) {
                                    Map<String, Object> snap = task.getResult().getData();

                                    ram.setSeed(System.currentTimeMillis());
                                    int num = ram.nextInt(array_image.length);

                                    String title1 = String.valueOf(snap.get(FirebaseID.title));
                                    String contents = String.valueOf(snap.get(FirebaseID.contents));
                                    String name1 = String.valueOf(snap.get(FirebaseID.name));

                                    main_image.setImageResource(array_image[num]);
                                    name.setText(name1);
                                    title.setText(title1);
                                    ask.setText(contents);
                                }
                            } else {
                                Toast.makeText(activity_notice.this, "삭제된 문서입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (mAuth.getCurrentUser() != null) {
            String postId = mStore.collection(FirebaseID.post).document(id).getId();
            String repostId = mStore.collection(FirebaseID.repost).document().getId();
            Map<String, Object> data = new HashMap<>();
            data.put(FirebaseID.documentId, repostId);
            data.put(FirebaseID.userId, mAuth.getCurrentUser().getUid());
            data.put(FirebaseID.contents, reContents.getText().toString());
            data.put(FirebaseID.name, reName);
            data.put(FirebaseID.post, postId);
            data.put(FirebaseID.timestamp, FieldValue.serverTimestamp());
            if (reContents.getText().toString().equals("")) {
                Toast.makeText(activity_notice.this, "댓글을 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            mStore.collection(FirebaseID.repost).document(repostId).set(data, SetOptions.merge());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatas = new ArrayList<>();
        mStore.collection(FirebaseID.repost)
                .orderBy(FirebaseID.timestamp, Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        if (queryDocumentSnapshots != null) {
                            mDatas.clear(); //리스트가 갱신되는게 아니라 화면에 쌓이기 때문에 정리를 해주어야한다.
                            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                                Map<String, Object> shot = snap.getData();
                                String documentId = String.valueOf(shot.get(FirebaseID.documentId));
                                String userId = String.valueOf(shot.get(FirebaseID.userId));
                                String contents = String.valueOf(shot.get(FirebaseID.contents));
                                String name = String.valueOf(shot.get(FirebaseID.name));
                                String post = String.valueOf(shot.get(FirebaseID.post));
                                String date = String.valueOf(shot.get(FirebaseID.timestamp));
                                Repost data = new Repost(documentId, post, userId, contents, name);
                                if (check.equals(post)) {
                                    mDatas.add(data);
                                    reContents.setText("");
                                }
                            }
                            mAdapter = new RepostAdapter(mDatas);
                            mPostRecyclerView.setAdapter(mAdapter);
                        }
                    }
                });
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemLongClick(View view, final int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("삭제 하시겠습니까?");
        dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mStore.collection(FirebaseID.repost).document(mDatas.get(position).getDocumentId()).delete();
                Toast.makeText(activity_notice.this, "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(activity_notice.this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setTitle("삭제 알림");
        dialog.show();
    }
}