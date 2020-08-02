package com.example.wefix.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wefix.AddLogActivity;
import com.example.wefix.LoginActivity;
import com.example.wefix.R;
import com.example.wefix.model.Category;
import com.example.wefix.model.Service;

import java.util.List;

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ListViewHolder> {

    Context mContext;
    List<Service> serviceList;
    Category category;
    String yes;

    public ServiceListAdapter(Context mContext, List<Service> serviceList, Category category, String yes) {
        this.mContext = mContext;
        this.category = category;
        this.yes = yes;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.service_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {

        Service service = serviceList.get(position);
        holder.name.setText(service.getTbl_services_name());
        holder.des.setText(service.getTbl_services_des());
        String a = "Charge : " +service.getTbl_services_charge();
        holder.charge.setText(a);

        if (yes.equals("YES")) {

            holder.add.setOnClickListener(
                    v -> {
                        if (yes.equals("YES")) {
                            Intent intent = new Intent(mContext, AddLogActivity.class);
                            intent.putExtra("service", service);
                            intent.putExtra("category", category);
                            mContext.startActivity(intent);
                        } else {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            mContext.startActivity(intent);
                        }
                    }
            );

        } else {
            holder.add.setOnClickListener(
                    v -> mContext.startActivity(new Intent(mContext, LoginActivity.class))
            );
        }

    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        TextView name, des, charge;
        Button add;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            des = itemView.findViewById(R.id.des);
            charge = itemView.findViewById(R.id.charge);
            add = itemView.findViewById(R.id.add);

        }
    }
}
