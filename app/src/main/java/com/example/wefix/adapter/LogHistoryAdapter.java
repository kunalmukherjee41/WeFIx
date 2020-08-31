package com.example.wefix.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wefix.Api.RetrofitClient;
import com.example.wefix.LogHistoryDetailsActivity;
import com.example.wefix.R;
import com.example.wefix.SuccessfulMessageActivity;
import com.example.wefix.model.Category;
import com.example.wefix.model.Category1Response;
import com.example.wefix.model.Logs;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogHistoryAdapter extends RecyclerView.Adapter<LogHistoryAdapter.LogViewHolder> {

    private Context mContext;
    private List<Logs> logsList;

    public LogHistoryAdapter(Context mContext, List<Logs> logsList) {
        this.mContext = mContext;
        this.logsList = logsList;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.log_history, parent, false);
        return new LogViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {

        Logs logs = logsList.get(position);

        holder.id.setText(String.valueOf(logs.getCallLogId()));
        holder.date.setText(logs.getCallLogDate());
        holder.charge.setText(String.valueOf(logs.getAmount()));
        holder.company.setText(logs.getProductCompany());

        if (!logs.getCallLogStatus().equals("OPEN")) {
            holder.cancel.setVisibility(View.GONE);
        }

        int tbl_category_id = logs.getRefCatId();

        Call<Category1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCategoryByID(tbl_category_id, "app");

        call.enqueue(
                new Callback<Category1Response>() {
                    @Override
                    public void onResponse(Call<Category1Response> call, Response<Category1Response> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            Category category = response.body().getCategory();
                            holder.name.setText(category.getTbl_category_name());
                            holder.name1.setText(category.getTbl_category_name());
//                            Toast.makeText(mContext, category.getTbl_category_image(), Toast.LENGTH_LONG).show();
//                            Glide.with(mContext).load("http://wefix.sitdoxford.org/product/" + category.getTbl_category_image()).into(holder.image);
                            Picasso.get().load("http://wefix.sitdoxford.org/product/" + category.getTbl_category_image()).into(holder.image);
                        }
                    }

                    @Override
                    public void onFailure(Call<Category1Response> call, Throwable t) {
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        holder.itemView.setOnClickListener(
                v -> {
                    Intent intent = new Intent(mContext, LogHistoryDetailsActivity.class);
                    intent.putExtra("logs", logsList.get(position));
                    mContext.startActivity(intent);
                }
        );

        holder.cancel.setOnClickListener(
                v -> {
                    holder.cancel.setBackgroundColor(mContext.getResources().getColor(R.color.btn));
                    int call_log_id = logsList.get(position).getCallLogId();
                    Call<ResponseBody> call1 = RetrofitClient
                            .getInstance()
                            .getApi()
                            .updateCallLog(call_log_id, "app");

                    call1.enqueue(
                            new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        Snackbar.make(holder.layout, "Successfully Cancel Log", Snackbar.LENGTH_LONG)
                                                .setAction("Close", v1 -> {

                                                }).setActionTextColor(mContext.getResources().getColor(R.color.colorAccent)).show();
                                        Intent intent1 = new Intent(mContext, SuccessfulMessageActivity.class);
                                        intent1.putExtra("string", "Logs Cancel Successful");
                                        mContext.startActivity(intent1);
                                        holder.cancel.setBackground(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.custom_btn2, null));
                                    } else {
                                        Snackbar.make(holder.layout, "Try Again!", Snackbar.LENGTH_LONG)
                                                .setAction("Close", v1 -> {

                                                }).setActionTextColor(mContext.getResources().getColor(R.color.colorAccent)).show();
                                        holder.cancel.setBackground(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.custom_btn2, null));

                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    holder.cancel.setBackground(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.custom_btn2, null));

                                }
                            }
                    );

                }
        );

    }

    @Override
    public int getItemCount() {
        return logsList.size();
    }

    public static class LogViewHolder extends RecyclerView.ViewHolder {

        TextView date, id, details, name;
        ImageView image;
        TextView name1, company, charge;
        Button cancel;
        private RelativeLayout layout;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            id = itemView.findViewById(R.id.id);
            details = itemView.findViewById(R.id.details);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            name1 = itemView.findViewById(R.id.name1);
            company = itemView.findViewById(R.id.company);
            charge = itemView.findViewById(R.id.charge);
            cancel = itemView.findViewById(R.id.cancel_log);
            layout = itemView.findViewById(R.id.layout);

        }
    }

}
