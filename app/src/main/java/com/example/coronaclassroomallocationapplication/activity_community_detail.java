package com.example.coronaclassroomallocationapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coronaclassroomallocationapplication.adapters.PostAdapter;
import com.example.coronaclassroomallocationapplication.adapters.RepostAdapter;
import com.example.coronaclassroomallocationapplication.models.Post;
import com.example.coronaclassroomallocationapplication.models.Repost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class activity_community_detail extends AppCompatActivity implements View.OnClickListener, RecyclerViewItemClickListener.OnItemClickListener {

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private TextView mTitleText, mContentsText, mNameText;
    private String id;
    private String check;

    //댓글 기능
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private EditText reContents;
    private String reName;
    private RecyclerView mPostRecyclerView;
    private RepostAdapter mAdapter;
    private List<Repost> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_detail);

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
        mTitleText = findViewById(R.id.post2_title);
        mContentsText = findViewById(R.id.post2_contents);
        mNameText = findViewById(R.id.post2_name);

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
                                    String title = String.valueOf(snap.get(FirebaseID.title));
                                    String contents = String.valueOf(snap.get(FirebaseID.contents));
                                    String name = String.valueOf(snap.get(FirebaseID.name));

                                    mTitleText.setText(title);
                                    mContentsText.setText(contents);
                                    mNameText.setText(name);
                                }
                            } else {
                                Toast.makeText(activity_community_detail.this, "삭제된 문서입니다.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(activity_community_detail.this, "댓글을 입력하세요.", Toast.LENGTH_SHORT).show();
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
                                Repost data = new Repost(documentId, userId, contents, name, post);
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
                    Toast.makeText(activity_community_detail.this, "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(activity_community_detail.this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.setTitle("삭제 알림");
            dialog.show();
    }
}