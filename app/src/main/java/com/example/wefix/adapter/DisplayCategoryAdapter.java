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
import com.example.wefix.ServiceActivity;
import com.example.wefix.ServiceActivity2;
import com.example.wefix.model.Category;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class DisplayCategoryAdapter extends RecyclerView.Adapter<DisplayCategoryAdapter.CategoryViewHolder> {

    Context mContext;
    List<Category> categoryList;
    Category category;
    String a;

    public DisplayCategoryAdapter(Context mContext, List<Category> categoryList, String a) {
        this.mContext = mContext;
        this.categoryList = categoryList;
        this.a = a;
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
        Picasso.get().load("http://wefix.sitdoxford.org/product/" + category.getTbl_category_image()).into(holder.image1);
//        Glide.with(mContext).load("http://wefix.sitdoxford.org/product/" + category.getTbl_category_image()).into(holder.image1);
        holder.name1.setText(category.getTbl_category_name());
        holder.cardView.setOnClickListener(
                v -> {
                    if (a.equals("Display")) {
                        Intent intent = new Intent(mContext, ServiceActivity2.class);
                        intent.putExtra("category", categoryList.get(position));
                        mContext.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, ServiceActivity.class);
                        intent.putExtra("category", categoryList.get(position));
                        mContext.startActivity(intent);
                    }
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
