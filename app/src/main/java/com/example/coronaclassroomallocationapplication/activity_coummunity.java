package com.example.coronaclassroomallocationapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coronaclassroomallocationapplication.adapters.PostAdapter;
import com.example.coronaclassroomallocationapplication.models.Post;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.*;

//커뮤니티의 메인 화면
public class activity_coummunity extends AppCompatActivity implements View.OnClickListener, RecyclerViewItemClickListener.OnItemClickListener {
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance(); //mStore의 인스턴스 변수를 생성하고 초기화
    int array_image[] = {R.drawable.cat, R.drawable.wolf, R.drawable.children, R.drawable.rabbit, R.drawable.dog}; //이미지를 넣은 배열
    Random ram = new Random(); //랜덤 값
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

        mPostRecyclerView = findViewById(R.id.main_recyclerview); //리사이클러뷰 ViewId 찾는 과정
        findViewById(R.id.main_post_edit).setOnClickListener(this); //리사이클러뷰 클릭 이벤트

        mPostRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this, mPostRecyclerView, this));

        homebutton = findViewById(R.id.homebutton2); //홈버튼 클릭 이벤트
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //메인 액티비티로 이동동
               intent = new Intent(activity_coummunity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatas = new ArrayList<>(); //데이터를 담을 어레이리스트 변수 생성
        mStore.collection(FirebaseID.post)
                .orderBy(FirebaseID.timestamp, Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        //collection 스트림에서 모든 문서 정보가 든 DocumentSnapshot을 얻는다.
                        if(queryDocumentSnapshots != null) {
                            mDatas.clear(); //리스트가 갱신되는게 아니라 화면에 쌓이기 때문에 정리를 해주어야한다.
                            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                                Map<String, Object> shot = snap.getData(); //키와 값으로 이루어진 shot 변수 생성
                                String documentId = String.valueOf(shot.get(FirebaseID.documentId)); //도큐먼트 아이디 삽입
                                String title = String.valueOf(shot.get(FirebaseID.title)); //타이틀 삽입
                                String contents = String.valueOf(shot.get(FirebaseID.contents)); //콘텐츠 삽입
                                String name = String.valueOf(shot.get(FirebaseID.name)); //이름 삽입
                                Post data = new Post(documentId, title, contents, name);
                                mDatas.add(data); //생성한 어레이리스트에 추가
                            }

                            mAdapter = new PostAdapter(mDatas); //생성자에 매개변수로 전송
                            mPostRecyclerView.setAdapter(mAdapter); //어뎁터를 설정하여, 화면에 표현
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        //글 작성을 하기 위한 이벤트
        startActivity(new Intent(activity_coummunity.this, activity_post.class));
    }

    @Override
    public void onItemClick(View view, int position) {
        //상세 글로 들어가기 위한 이벤트
        Intent intent = new Intent(this, activity_notice.class);
        intent.putExtra(FirebaseID.documentId, mDatas.get(position).getDocumentId());
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, final int position) {
        //항목을 길게 누를경우 글 삭제를 위한 이벤트
        AlertDialog.Builder dialog = new AlertDialog.Builder(this); //알림을 받기 위해 선언
        dialog.setMessage("삭제 하시겠습니까?");
        dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { //삭제 버튼을 눌렀을때 이벤트
                mStore.collection(FirebaseID.post).document(mDatas.get(position).getDocumentId()).delete();
                Toast.makeText(activity_coummunity.this, "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { //취소 버튼을 눌렀을때 이벤트
                Toast.makeText(activity_coummunity.this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setTitle("삭제 알림"); //타이틀 이름
        dialog.show();
    }
}