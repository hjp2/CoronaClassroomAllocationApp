package com.example.coronaclassroomallocationapplication;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private String max;


    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;

    public ListViewAdapter(String max) {

        this.max = max;

    }

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public ListViewItem getItem(int position) {
        return listViewItemList.get(position) ;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.timelist_layout, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView time = (TextView) convertView.findViewById(R.id.TextView_time) ;
        TextView people = (TextView) convertView.findViewById(R.id.TextView_people) ;
        TextView state = (TextView) convertView.findViewById(R.id.TextView_state) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        String tmptime = listViewItem.getTime();
        String tmppeople = listViewItem.getPeople();
        String tmpstate = listViewItem.getState();
        time.setText(tmptime);
        people.setText(tmppeople);
        state.setText(tmpstate);

        if(tmpstate.equals("예약완료")) state.setTextColor(Color.BLUE);
        else state.setTextColor(Color.BLACK);

        String[] tmpsplit = tmppeople.split(" / ");


        if(Integer.parseInt(tmpsplit[0])> Integer.parseInt(max)*0.8)
            people.setTextColor(Color.RED);
        else if(Integer.parseInt(tmpsplit[0])> Integer.parseInt(max)*0.5)
                people.setTextColor(Color.parseColor("#FF6B00"));
        else people.setTextColor(Color.BLACK);


        return convertView;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String time, String people, String state) {
        ListViewItem item = new ListViewItem();

        item.setTime(time);
        item.setPeople(people);
        item.setState(state);

        listViewItemList.add(item);
    }
    public void clearItem(){
        listViewItemList.clear();
    }


}
