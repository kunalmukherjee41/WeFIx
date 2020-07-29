package com.example.wefix.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wefix.Api.RetrofitClient;
import com.example.wefix.MainActivity;
import com.example.wefix.R;
import com.example.wefix.ServiceActivity;
import com.example.wefix.model.Category;
import com.example.wefix.model.Service;
import com.example.wefix.model.Service1Response;
import com.example.wefix.model.ServiceResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Glide.with(mContext).load("http://wefix.sitdoxford.org/product/" + category.getTbl_category_image()).into(holder.image);
        holder.id.setText(String.valueOf(category.getTbl_category_id()));

        holder.itemView.setOnClickListener(
                v -> {
                    click(categoryList.get(position).getTbl_category_id());
                }
        );

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }


    static class UserViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, id;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.imageName);
            id = itemView.findViewById(R.id.id);
        }
    }


    private void click(int position) {


        Call<Service1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getService(position, "WeFix");

        call.enqueue(
                new Callback<Service1Response>() {
                    @Override
                    public void onResponse(Call<Service1Response> call, Response<Service1Response> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(mContext, "Successful", Toast.LENGTH_LONG).show();
                            assert response.body() != null;
//                            Service service = response.body().getService();
//                            Toast.makeText(mContext, service.getTbl_services_name(), Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(mContext, ServiceActivity.class);
//                            intent.putExtra("service", service);
                            mContext.startActivity(intent);
                        } else {
                            Toast.makeText(mContext, "Unsuccessful", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Service1Response> call, Throwable t) {
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

    }

}

