package com.example.wefix.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wefix.Api.RetrofitClient;
import com.example.wefix.R;
import com.example.wefix.adapter.ServiceListAdapter;
import com.example.wefix.model.Category;
import com.example.wefix.model.Service;
import com.example.wefix.model.Service1Response;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceActivity extends AppCompatActivity {

    private List<Service> service;
    private ProgressDialog progressBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = new ProgressDialog(this);
        progressBar.show();
        progressBar.setContentView(R.layout.progress_dialog);
        Objects.requireNonNull(progressBar.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        TextView name = findViewById(R.id.name);
        ImageView imageView = findViewById(R.id.image1);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        Category category = (Category) intent.getSerializableExtra("category");
        assert category != null;

        int id = category.getTbl_category_id();
        name.setText(category.getTbl_category_name());

        Glide.with(this).load("https://wefixservice.in/product/" + category.getTbl_category_image()).into(imageView);

        Call<Service1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getService(id);

        call.enqueue(
                new Callback<Service1Response>() {
                    @Override
                    public void onResponse(Call<Service1Response> call, Response<Service1Response> response) {
                        if (response.isSuccessful()) {
                            service = response.body().getService();
//                            rs.setText(String.valueOf(service.getTbl_services_charge()));
//                            Toast.makeText(ServiceActivity.this, service.get(0).getTbl_services_id(), Toast.LENGTH_SHORT).show();
                            ServiceListAdapter adapter = new ServiceListAdapter(ServiceActivity.this, service, category, "NO");
                            recyclerView.setAdapter(adapter);

                        } else {
                            Toast.makeText(ServiceActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                        }
                        progressBar.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Service1Response> call, Throwable t) {
                        Toast.makeText(ServiceActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.dismiss();
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        progressBar.dismiss();
    }
}