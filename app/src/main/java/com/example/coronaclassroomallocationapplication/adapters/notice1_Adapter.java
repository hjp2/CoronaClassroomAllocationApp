package com.example.coronaclassroomallocationapplication.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coronaclassroomallocationapplication.R;
import com.example.coronaclassroomallocationapplication.freenotice_listitem;
import com.example.coronaclassroomallocationapplication.models.Repost;

import java.util.ArrayList;
import java.util.List;


public class notice1_Adapter extends BaseAdapter {

    //ativity_notice_listview
    private ArrayList<freenotice_listitem> listviewitemList = new ArrayList<freenotice_listitem>() ;

    public notice1_Adapter() { }

    @Override
    public int getCount() { return listviewitemList.size() ; }

    @Override
    public Object getItem(int position) { return listviewitemList.get(position) ; }

    @Override
    public long getItemId(int position) { return position ; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ativity_notice_listview, parent, false);
        }

        //sub
        ImageView sub_image = (ImageView) convertView.findViewById(R.id.sub_image);
        TextView sub_name = (TextView) convertView.findViewById(R.id.sub_name);
        TextView sub_data = (TextView) convertView.findViewById(R.id.sub_data);
        TextView review = (TextView) convertView.findViewById(R.id.review);


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        freenotice_listitem listViewItem = listviewitemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        sub_image.setImageDrawable(listViewItem.getSub_image());
        sub_name.setText(listViewItem.getSub_name());
        sub_data.setText(listViewItem.getSub_data());
        review.setText(listViewItem.getReview());

        return convertView;
    }


    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Drawable sub_image, String sub_name, String sub_data, String review, Drawable heart, String heart_number) {
        freenotice_listitem item = new freenotice_listitem();

        item.setSub_image(sub_image);
        item.setSub_name(sub_name);
        item.setSub_data(sub_data);
        item.setReview(review);
        listviewitemList.add(item);
    }

    public void settitle(Drawable title_image, String main_name, String main_data, String main_title, String main_ask){
        freenotice_listitem item = new freenotice_listitem();
        item.setMain_image(title_image);
        item.setMain_name(main_name);
        item.setMain_data(main_data);
        item.setMain_title(main_title);
        item.setMain_ask(main_ask);

        listviewitemList.add(item);
    }
}
