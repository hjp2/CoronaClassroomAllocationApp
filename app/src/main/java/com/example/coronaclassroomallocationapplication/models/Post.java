package com.example.coronaclassroomallocationapplication.models;

import android.widget.ImageView;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Post {

    private String documentId;
    private String title;
    private String contents;
    private String name;
    @ServerTimestamp
    private Date date;
    private int main_image;

    public Post() {
    }



    public Post(String documentId, String title, String contents, String name) {
        this.documentId = documentId;
        this.title = title;
        this.contents = contents;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "Post{" +
                "documentId='" + documentId + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", name='" + name + '\'' +
                ", date=" + date +
                '}';
    }
}
