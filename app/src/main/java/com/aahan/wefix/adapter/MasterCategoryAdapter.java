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
import com.aahan.wefix.model.Category;
import com.aahan.wefix.model.Master;
import com.aahan.wefix.storage.SharedPrefManager;
import com.aahan.wefix.ui.BasicCategoryActivity;
import com.aahan.wefix.ui.DisplayActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MasterCategoryAdapter extends RecyclerView.Adapter<MasterCategoryAdapter.MyListViewHolder> implements Filterable {

    List<Master> masterList;
    Context mContext;
    List<Master> fullMasterList;

    public MasterCategoryAdapter(List<Master> masterList, Context mContext) {
        this.masterList = masterList;
        this.mContext = mContext;
        fullMasterList = new ArrayList<>(masterList);
    }

    @NonNull
    @Override
    public MyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.master_category_listview, parent, false);
        return new MyListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyListViewHolder holder, int position) {

        Master master = masterList.get(position);
        holder.masterName.setText(master.getTbl_mater_category_name());
        Glide.with(mContext).load("https://wefixservice.in/product/" + master.getTbl_mater_category_image()).into(holder.masterImage);

        holder.cardView.setOnClickListener(
                v -> {
                    SharedPrefManager.getInstance(mContext).saveMasterId(masterList.get(position).getTbl_mater_category_id());
                    Intent intent = new Intent(mContext, BasicCategoryActivity.class);
                    mContext.startActivity(intent);
                }
        );
    }

    @Override
    public int getItemCount() {
        return masterList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Master> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(fullMasterList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Master item : fullMasterList) {
                    if (item.getTbl_mater_category_name().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    } else if (item.getTbl_mater_category_des().contains(filterPattern)) {
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
            masterList.clear();
            masterList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    public class MyListViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private ImageView masterImage;
        private TextView masterName;

        public MyListViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            masterImage = itemView.findViewById(R.id.image1);
            masterName = itemView.findViewById(R.id.name1);

        }
    }
}
