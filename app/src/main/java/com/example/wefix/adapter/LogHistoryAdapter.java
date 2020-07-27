package com.example.wefix.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wefix.Api.RetrofitClient;
import com.example.wefix.LogHistoryDetailsActivity;
import com.example.wefix.R;
import com.example.wefix.model.Category;
import com.example.wefix.model.Category1Response;
import com.example.wefix.model.CategoryResponse;
import com.example.wefix.model.Logs;

import java.io.Serializable;
import java.util.List;

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

        int tbl_category_id = logs.getRefCatId();

        Call<Category1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCategoryByID(tbl_category_id, "app");

        call.enqueue(
                new Callback<Category1Response>() {
                    @Override
                    public void onResponse(Call<Category1Response> call, Response<Category1Response> response) {
                        if(response.isSuccessful()) {
                            assert response.body() != null;
                            Category category = response.body().getCategory();
//                            Toast.makeText(mContext, category.getTbl_category_name(), Toast.LENGTH_LONG).show();
                            holder.type.setText(category.getTbl_category_name());
                            holder.id.setText(String.valueOf(logs.getCallLogId()));
                            holder.date.setText(logs.getCallLogDate());
                            holder.amount.setText(String.valueOf(logs.getAmount()));
                            holder.status.setText(logs.getCallLogStatus());

                        }
                    }

                    @Override
                    public void onFailure(Call<Category1Response> call, Throwable t) {
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        holder.itemView.setOnClickListener(
                v -> click(logs)
        );

    }

    @Override
    public int getItemCount() {
        return logsList.size();
    }

    public static class LogViewHolder extends RecyclerView.ViewHolder {

        TextView id, date, type, amount, status;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.id);
            date = itemView.findViewById(R.id.date);
            type = itemView.findViewById(R.id.type);
            amount = itemView.findViewById(R.id.amount);
            status = itemView.findViewById(R.id.status);

        }
    }

    private void click(Logs logs) {

        Intent intent = new Intent(mContext, LogHistoryDetailsActivity.class);
        intent.putExtra("logs", (Serializable) logs);
        mContext.startActivity(intent);

    }

}
