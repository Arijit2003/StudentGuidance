package com.example.studentguidance.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studentguidance.ModelClasses.Developer;
import com.example.studentguidance.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AboutUsRecyclerAdapter extends RecyclerView.Adapter<AboutUsRecyclerAdapter.MyViewHolder> {

    ArrayList<Developer> developers;
    Context context;
    public AboutUsRecyclerAdapter(ArrayList<Developer> developers,Context context){
        this.context=context;
        this.developers=developers;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_about_us,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.developerNameTV.setText(developers.get(position).getName());
        holder.developerRoleTV.setText(developers.get(position).getRole());
        holder.developerDetails.setText(developers.get(position).getDetails());
        Glide.with(context)
                .load(developers.get(position).getUrl())
                .placeholder(R.drawable.person_image)
                .into(holder.developerImageView);
    }

    @Override
    public int getItemCount() {
        return developers.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView developerImageView;
        TextView developerNameTV,developerRoleTV,developerDetails;

        public MyViewHolder(@NonNull View item) {
            super(item);
            developerImageView=item.findViewById(R.id.developerImageView);
            developerDetails=item.findViewById(R.id.developerDetails);
            developerNameTV=item.findViewById(R.id.developerNameTV);
            developerRoleTV=item.findViewById(R.id.developerRoleTV);
        }
    }
}
