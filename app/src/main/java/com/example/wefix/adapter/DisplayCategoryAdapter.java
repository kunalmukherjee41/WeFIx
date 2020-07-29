package com.example.wefix.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wefix.R;
import com.example.wefix.ServiceActivity2;
import com.example.wefix.model.Category;

import java.io.Serializable;
import java.util.List;

public class DisplayCategoryAdapter extends RecyclerView.Adapter<DisplayCategoryAdapter.CategoryViewHolder> {

    Context mContext;
    List<Category> categoryList;
    Category category;

    public DisplayCategoryAdapter(Context mContext, List<Category> categoryList) {
        this.mContext = mContext;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.display_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        category = categoryList.get(position);
        Glide.with(mContext).load("http://wefix.sitdoxford.org/product/" + category.getTbl_category_image()).into(holder.image1);
        holder.name1.setText(category.getTbl_category_name());
        holder.cardView.setOnClickListener(
                v -> {
                    Intent intent = new Intent(mContext, ServiceActivity2.class);
                    intent.putExtra("category", categoryList.get(position));
                    mContext.startActivity(intent);
                }
        );
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static public class CategoryViewHolder extends RecyclerView.ViewHolder {

        ImageView image1;
        TextView name1;
        CardView cardView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            image1 = itemView.findViewById(R.id.image1);
            name1 = itemView.findViewById(R.id.name1);
            cardView = itemView.findViewById(R.id.card_view);

        }
    }
}
