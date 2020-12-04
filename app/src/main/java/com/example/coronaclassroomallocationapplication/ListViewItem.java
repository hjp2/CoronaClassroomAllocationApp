package com.example.coronaclassroomallocationapplication;

import android.view.View;

public class ListViewItem {
    private String time;
    private String people;
    private String state;
    public View.OnClickListener onClickListener;



    public void setTime(String time) {
        this.time = time;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return this.time;
    }

    public String getPeople() {
        return this.people;
    }

    public String getState() {
        return this.state;
    }
}


