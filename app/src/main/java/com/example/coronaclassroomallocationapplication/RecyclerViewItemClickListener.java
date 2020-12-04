package com.example.coronaclassroomallocationapplication;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewItemClickListener implements RecyclerView.OnItemTouchListener {

    private OnItemClickListener listener;
    private GestureDetector gestureDetector; //손가락의 움직임을 캐치해주는 변수

    public interface OnItemClickListener{
        void onItemClick(View view, int position); //클릭했을때 이벤트, 어떤 view가 콜백을 받을건지 position값을 알기위해 매개변수 설정
        void onItemLongClick(View view, int position); //더블클릭했을 때 이벤트
    }

    //클래스의 생성자
    public RecyclerViewItemClickListener(Context context, final RecyclerView recyclerView,final OnItemClickListener listener) {
        this.listener = listener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){//SimpleOnGestureListener에 다양한 메소드가 있고 오버라이드해서 사용하면 된다.
            @Override
            public boolean onSingleTapUp(MotionEvent e) { //한번 클릭했을때
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) { //길게 누를때
                View v = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if(v != null && listener != null){
                    listener.onItemLongClick(v, recyclerView.getChildAdapterPosition(v));
                }
            }
        });
    }

    //인터페이스 번외
    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        View view = rv.findChildViewUnder(e.getX(),e.getY());
        if(view != null && gestureDetector.onTouchEvent(e)){ //onTouchEvenet는 사용자가 화면을 조작할때 생기는 이벤트를 말한다.
            listener.onItemClick(view, rv.getChildAdapterPosition(view));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
