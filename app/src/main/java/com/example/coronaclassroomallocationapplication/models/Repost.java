package com.example.coronaclassroomallocationapplication.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Repost {

    private String documentId;
    private String writeId;
    private String userId;
    private String contents;
    private String name;
    private String curdate;
    @ServerTimestamp
    private Date date;

    public Repost() {

    }

    public Repost(String documentId, String writeId, String userId, String contents, String name) {
        this.documentId = documentId;
        this.writeId = writeId;
        this.userId = userId;
        this.contents = contents;
        this.name = name;
    }

    public String getWriteId() {
        return writeId;
    }

    public void setWriteId(String writeId) {
        this.writeId = writeId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurdate() {
        return curdate;
    }

    public void setCurdate(String curdate) {
        this.curdate = curdate;
    }

    @Override
    public String toString() {
        return "Repost{" +
                "documentId='" + documentId + '\'' +
                ", writeId='" + writeId + '\'' +
                ", userId='" + userId + '\'' +
                ", contents='" + contents + '\'' +
                ", name='" + name + '\'' +
                ", curdate='" + curdate + '\'' +
                ", date=" + date +
                '}';
    }
}

