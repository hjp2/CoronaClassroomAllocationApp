package com.example.coronaclassroomallocationapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class activity_secondprivateinfo extends AppCompatActivity {
    private Button bt_deleteuser; //회원 탈퇴
    private Button bt_changeuser; //회원정보변경
    private TextInputEditText info_pw;
    private TextInputEditText info_name;
    private TextInputEditText info_address;
    private TextInputEditText info_phonenum;
    private TextView user_info_id;
    private TextView user_info_name;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private ImageView privateinfo_back_button;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privateinfo2);

        mAuth = FirebaseAuth.getInstance();
        bt_deleteuser = (Button)findViewById(R.id.delete_user); //회원 탈퇴
        info_pw = findViewById(R.id.info_input_pw);
        info_name = findViewById(R.id.info_input_name);
        info_address = findViewById(R.id.info_input_address);
        info_phonenum = findViewById(R.id.info_input_phonenum);
        user_info_id = findViewById(R.id.user_info_id);
        user_info_name = findViewById(R.id.user_info_name);
        bt_changeuser = findViewById(R.id.info_change);
        privateinfo_back_button = findViewById(R.id.privateinfo_back_button);

        String user = mAuth.getInstance().getUid();
        mStore.collection(FirebaseID.user).document(user)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) { //문서가 없을경우 처리방법
                                if (task.getResult() != null) {
                                    Map<String, Object> snap = task.getResult().getData();
                                    String id = String.valueOf(snap.get(FirebaseID.email));
                                    String pw = String.valueOf(snap.get(FirebaseID.password));
                                    String name = String.valueOf(snap.get(FirebaseID.name));
                                    String address = String.valueOf(snap.get(FirebaseID.address));
                                    String phonenum = String.valueOf(snap.get(FirebaseID.phonenum));
                                    info_pw.setText(pw);
                                    user_info_id.setText(id);
                                    info_name.setText(name);
                                    user_info_name.setText(name);
                                    info_address.setText(address);
                                    info_phonenum.setText(phonenum);
                                }
                            }
                        }
                    }
                });

        bt_changeuser.setOnClickListener(new View.OnClickListener() {
            String user = mAuth.getInstance().getUid();
            @Override
            public void onClick(View v) {
                mStore.collection(FirebaseID.user).document(user)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        Map<String, Object> userMap = new HashMap<>();
                                        userMap.put(FirebaseID.password, info_pw.getText().toString());
                                        userMap.put(FirebaseID.name, info_name.getText().toString());
                                        userMap.put(FirebaseID.address, info_address.getText().toString());
                                        userMap.put(FirebaseID.phonenum, info_phonenum.getText().toString());
                                        mStore.collection(FirebaseID.user).document(user.getUid()).set(userMap, SetOptions.merge()); //SetOptions.merge는 덮어쓰기 효과
                                        Toast.makeText(activity_secondprivateinfo.this, "정보 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });

        //고지훈 추가(회원 탈퇴) -> 문제시 이야기해주세요.
        bt_deleteuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getCurrentUser().delete();
                Toast.makeText(activity_secondprivateinfo.this, "그동안 이용해주셔서 감사합니다.", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        });

        privateinfo_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_secondprivateinfo.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });;
    }
}
