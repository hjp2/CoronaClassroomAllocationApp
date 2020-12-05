package com.example.coronaclassroomallocationapplication.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coronaclassroomallocationapplication.R;
import com.example.coronaclassroomallocationapplication.models.Repost;

import java.util.List;
import java.util.Random;

public class RepostAdapter extends RecyclerView.Adapter<RepostAdapter.RepostViewHolder>{

    int array_image[] = {R.drawable.cat, R.drawable.wolf, R.drawable.children, R.drawable.rabbit, R.drawable.dog};
    Random ram = new Random();


    private List<Repost> datas;
    public RepostAdapter(List<Repost> datas) { this.datas = datas; }

    @NonNull
    @Override
    public RepostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RepostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ativity_notice_listview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RepostViewHolder holder, int position) {
        Repost data = datas.get(position); //Repost객체 생성, position은 0,1,2이 순서로 내려온다.

        ram.setSeed(System.currentTimeMillis());
        int num = ram.nextInt(array_image.length);

        holder.sub_name.setText(data.getName());
        holder.sub_image.setImageResource(array_image[num]);
        holder.review.setText(data.getContents());
        //holder.sub_data.setText(data.getCurdate());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class RepostViewHolder extends RecyclerView.ViewHolder{

        //리스트 뷰에 담을 내용을 담고 있는 xml
        private ImageView sub_image;
        private ImageView heart;
        private TextView sub_name;
        private TextView sub_data;
        private TextView review;
        private TextView heart_number;

        public RepostViewHolder(@NonNull View itemView) {
            super(itemView);

            sub_name = itemView.findViewById(R.id.sub_name);
            sub_image = itemView.findViewById(R.id.sub_image);
            review = itemView.findViewById(R.id.review);
            sub_data = itemView.findViewById(R.id.sub_data);

        }
    }

}