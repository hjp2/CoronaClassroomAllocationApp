package com.example.coronaclassroomallocationapplication;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class userInfo {
    public String id; //사용자 아이디
    public String pw; //사용자 비밀번호
    public String name; //사용자 이름
    public String email; //사용자 이메일
    public String phonenum; //사용자 휴대폰 번호

    public userInfo(){
        // Default constructor required for calls to DataSnapshot.getValue(userInfo.class)
    }

    public userInfo(String id, String pw, String name, String email, String phonenum){
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.email = email;
        this.phonenum = phonenum;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("pw", pw);
        result.put("name", name);
        result.put("email", email);
        result.put("phonenum", phonenum);
        return result;
    }


    public String getUser_id() {
        return id;
    }

    public String getUser_pw() {
        return pw;
    }

    public String getUser_name() {
        return name;
    }

    public String getUser_email() {
        return email;
    }

    public String getUser_phonenum() {
        return phonenum;
    }
}
