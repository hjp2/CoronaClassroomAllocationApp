package com.example.coronaclassroomallocationapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coronaclassroomallocationapplication.R;
import com.example.coronaclassroomallocationapplication.models.Repost;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RepostAdapter extends RecyclerView.Adapter<RepostAdapter.RepostViewHolder>{

    private List<Repost> datas;
    public RepostAdapter(List<Repost> datas) {
        this.datas = datas;
    }

    @NonNull
    @Override
    public RepostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RepostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repost, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RepostViewHolder holder, int position) {

        Repost data = datas.get(position); //Repost객체 생성, position은 0,1,2이 순서로 내려온다.
        holder.contents.setText(data.getContents()); //Contents 아이템을 넣어준다.
        holder.name.setText(data.getName()); //name 아이템을 넣어준다.
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class RepostViewHolder extends RecyclerView.ViewHolder{

        private TextView contents;
        private TextView name;

        public RepostViewHolder(@NonNull View itemView) {
            super(itemView);

            contents = itemView.findViewById(R.id.item_repost_contents);
            name = itemView.findViewById(R.id.item_repost_name);
        }
    }

}
