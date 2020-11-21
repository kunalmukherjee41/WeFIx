package com.example.wefix.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wefix.Api.RetrofitClient;
import com.example.wefix.ui.PaymentDetailsActivity;
import com.example.wefix.R;
import com.example.wefix.model.Category;
import com.example.wefix.model.Category1Response;
import com.example.wefix.model.Logs;
import com.example.wefix.model.Service;
import com.example.wefix.model.ServiceResponse;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.HistoryViewAdapter> {

    private Context mContext;
    private List<Logs> logsList;
    private ProgressDialog progressBar;

    public PaymentHistoryAdapter(Context mContext, List<Logs> logsList) {
        this.mContext = mContext;
        this.logsList = logsList;
    }

    @NonNull
    @Override
    public HistoryViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.payment_list, parent, false);
        return new HistoryViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewAdapter holder, int position) {

        Logs logs = logsList.get(position);

        holder.id.setText(String.valueOf(logs.getCallLogId()));
        holder.date.setText(logs.getCallLogDate());
        holder.charge.setText(String.valueOf(logs.getAmount()));
        holder.company.setText(logs.getProductCompany());

        int tbl_category_id = logs.getRefCatId();

        Call<Category1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCategoryByID(tbl_category_id);

        call.enqueue(
                new Callback<Category1Response>() {
                    @Override
                    public void onResponse(Call<Category1Response> call, Response<Category1Response> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            Category category = response.body().getCategory();
                            holder.name.setText(category.getTbl_category_name());
                            holder.name1.setText(category.getTbl_category_name());
                        }
                    }

                    @Override
                    public void onFailure(Call<Category1Response> call, Throwable t) {
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        holder.go.setOnClickListener(
                v -> {

                    progressBar = new ProgressDialog(mContext);
                    progressBar.show();
                    progressBar.setContentView(R.layout.progress_dialog);
                    Objects.requireNonNull(progressBar.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

                    Logs logs1 = logsList.get(position);
                    int cat_id = logs1.getRefCatId();

                    Call<Category1Response> c = RetrofitClient
                            .getInstance()
                            .getApi()
                            .getCategoryByID(cat_id);

                    c.enqueue(
                            new Callback<Category1Response>() {
                                @Override
                                public void onResponse(Call<Category1Response> call, Response<Category1Response> response) {
                                    if (response.isSuccessful()) {
                                        assert response.body() != null;
                                        Category category = response.body().getCategory();
                                        getSerVice(logs1, category);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Category1Response> call, Throwable t) {
                                    Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                    );
                }
        );

    }

    private void getSerVice(Logs logs1, Category category) {

        int serID = logs1.getRefServiceId();

        Call<ServiceResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getAllServices();

        call.enqueue(
                new Callback<ServiceResponse>() {
                    @Override
                    public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                        if (response.isSuccessful()) {
                            progressBar.dismiss();
                            List<Service> service = response.body().getService();
                            for (Service s : service) {
                                if (s.getTbl_services_id() == serID) {
                                    Intent intent = new Intent(mContext, PaymentDetailsActivity.class);
                                    intent.putExtra("log", logs1);
                                    intent.putExtra("category", category);
                                    intent.putExtra("service", s);
                                    intent.putExtra("name", s.getTbl_services_name());
                                    mContext.startActivity(intent);
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ServiceResponse> call, Throwable t) {

                    }
                }
        );

    }

    @Override
    public int getItemCount() {
        return logsList.size();
    }

    public static class HistoryViewAdapter extends RecyclerView.ViewHolder {

        TextView date, id, name;
        TextView name1, company, charge;
        Button go;

        public HistoryViewAdapter(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.name);
            name1 = itemView.findViewById(R.id.name1);
            company = itemView.findViewById(R.id.company);
            charge = itemView.findViewById(R.id.charge);
            go = itemView.findViewById(R.id.go);
        }
    }
}
