package com.aahan.wefix.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aahan.wefix.R;
import com.aahan.wefix.ui.ServiceActivity;
import com.aahan.wefix.ui.ServiceActivity2;
import com.aahan.wefix.model.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DisplayCategoryAdapter extends RecyclerView.Adapter<DisplayCategoryAdapter.CategoryViewHolder> implements Filterable {

    Context mContext;
    List<Category> categoryList;
    List<Category> categoryListFull;
    Category category;
    String a;

    public DisplayCategoryAdapter(Context mContext, List<Category> categoryList, String a) {
        this.mContext = mContext;
        this.categoryList = categoryList;
        this.a = a;
        categoryListFull = new ArrayList<>(categoryList);
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
        Picasso.get().load("https://wefixservice.in/product/" + category.getTbl_category_image()).into(holder.image1);
//        Glide.with(mContext).load("https://wefixservice.in/product/" + category.getTbl_category_image()).into(holder.image1);
        holder.name1.setText(category.getTbl_category_name());
        holder.cardView.setOnClickListener(
                v -> {
                    Intent intent;
                    if (a.equals("Display")) {
                        intent = new Intent(mContext, ServiceActivity2.class);
                    } else {
                        intent = new Intent(mContext, ServiceActivity.class);
                    }
                    intent.putExtra("category", categoryList.get(position));
                    mContext.startActivity(intent);
                }
        );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Category> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(categoryListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Category item : categoryListFull) {
                    if (item.getTbl_category_name().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            categoryList.clear();
            categoryList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

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
