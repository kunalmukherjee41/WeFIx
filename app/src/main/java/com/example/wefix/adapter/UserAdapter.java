package com.example.wefix.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wefix.R;
import com.example.wefix.model.Category;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context mContext;
    private List<Category> categoryList;

    public UserAdapter(Context mContext, List<Category> categoryList) {
        this.mContext = mContext;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.recycle_view, parent, false);
        return new UserViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Category category = categoryList.get(position);

        holder.name.setText(category.getTbl_category_name());
        Glide.with(mContext).load("http://wefix.sitdoxford.org/product/"+category.getTbl_category_image()).into(holder.image);
        holder.id.setText(String.valueOf(category.getTbl_category_id()));

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name,id;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.imageName);
            id = itemView.findViewById(R.id.id);
        }
    }

}

