package com.example.coronaclassroomallocationapplication;

import android.graphics.drawable.Drawable;

public class freenotice_listitem {

    //메인 xml
    private Drawable main_image ;
    private Drawable support_image;
    private String main_name;
    private String main_data;
    private String main_title;
    private String main_ask;

    //리스트 뷰에 담을 내용을 담고 있는 xml
    private Drawable sub_image;
    private Drawable heart;
    private String sub_name;
    private String sub_data;
    private String review;
    private String heart_number;

    //메인 xml의 위젯 getter
    public Drawable getMain_image() {  return main_image; }
    public Drawable getSupport_image() { return support_image; }
    public String getMain_name() { return main_name; }
    public String getMain_data() { return main_data; }
    public String getMain_title() { return main_title; }
    public String getMain_ask() { return main_ask; }

    //리스트 뷰에 담을 내용을 담고 잇는 위젯 getter
    public Drawable getSub_image() { return sub_image; }
    public Drawable getHeart() { return heart; }
    public String getSub_name() { return sub_name; }
    public String getSub_data() { return sub_data; }
    public String getReview() { return review; }
    public String getHeart_number() { return heart_number; }

    //메인 xml의 위젯 setter
    public void setMain_image(Drawable main_image) { this.main_image = main_image; }
    public void setSupport_image(Drawable support_image) { this.support_image = support_image; }
    public void setMain_name(String main_name) { this.main_name = main_name; }
    public void setMain_data(String main_data) { this.main_data = main_data; }
    public void setMain_title(String main_title) { this.main_title = main_title; }
    public void setMain_ask(String main_ask) { this.main_ask = main_ask; }

    //리스트 뷰에 담을 내용을 담고 잇는 위젯 setter
    public void setSub_image(Drawable sub_image) { this.sub_image = sub_image; }
    public void setHeart(Drawable heart) { this.heart = heart; }
    public void setSub_name(String sub_name) { this.sub_name = sub_name; }
    public void setSub_data(String sub_data) { this.sub_data = sub_data; }
    public void setReview(String review) { this.review = review; }
    public void setHeart_number(String heart_number) { this.heart_number = heart_number; }
}

