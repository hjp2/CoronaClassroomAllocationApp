package com.example.coronaclassroomallocationapplication.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coronaclassroomallocationapplication.models.MyPost;
import com.example.coronaclassroomallocationapplication.R;
import com.example.coronaclassroomallocationapplication.models.Post;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Random;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.MyPostViewHolder> {


    int array_image[] = {R.drawable.cat, R.drawable.wolf, R.drawable.children, R.drawable.rabbit, R.drawable.dog};
    Random ram = new Random();

    private List<MyPost> datas;

    public MyPostAdapter(List<MyPost> datas) {
        this.datas = datas;
    }

    @NonNull
    @Override
    public MyPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyPostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostViewHolder holder, int position) {
        MyPost data = datas.get(position); //Post객체 생성, position은 0,1,2이 순서로 내려온다.

        ram.setSeed(System.currentTimeMillis());
        int num = ram.nextInt(array_image.length);

        holder.main_image.setImageResource(array_image[num]);
        holder.main_name.setText(data.getName());
        //holder.main_data.setText(data.getDate().toString());
        holder.main_title.setText((data.getTitle()));
        holder.main_ask.setText(data.getContents());

    }

    @Override
    public int getItemCount() { //아이템의 총 길이
        return datas.size();
    }

    class MyPostViewHolder extends RecyclerView.ViewHolder{

        private ImageView main_image ;
        private ImageView support_image;
        private TextView main_name;
        private TextView main_data;
        private TextView main_title;
        private TextView main_ask;

        private TextView title;
        private TextView contents;
        private TextView name;

        public MyPostViewHolder(@NonNull View itemView) {
            super(itemView);

            main_image = itemView.findViewById(R.id.main_image);
            main_name = itemView.findViewById(R.id.main_name);
            main_data = itemView.findViewById(R.id.main_data);
            main_title = itemView.findViewById(R.id.main_title);
            main_ask = itemView.findViewById(R.id.main_ask);
        }
    }

}
