package com.example.wefix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.wefix.Api.RetrofitClient;
import com.example.wefix.model.Category;
import com.example.wefix.model.Service;
import com.example.wefix.model.Service1Response;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceActivity2 extends AppCompatActivity {

    TextView name, rs;
    Button addLogs;

    ImageView imageView;

    Service service;
    Category category;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service2);

        name = findViewById(R.id.name);
        rs = findViewById(R.id.rs);

        addLogs = findViewById(R.id.add_log);
        imageView = findViewById(R.id.image1);

        intent = getIntent();
        category = (Category) intent.getSerializableExtra("category");
        assert category != null;
        int id = category.getTbl_category_id();
        name.setText(category.getTbl_category_name());

        Glide.with(this).load("http://wefix.sitdoxford.org/product/"+category.getTbl_category_image()).into(imageView);

        Call<Service1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getService(id, "app");

        call.enqueue(
                new Callback<Service1Response>() {
                    @Override
                    public void onResponse(Call<Service1Response> call, Response<Service1Response> response) {
                        if(response.isSuccessful()){
                            service = response.body().getService();
                            rs.setText(String.valueOf(service.getTbl_services_charge()));
                        }
                    }

                    @Override
                    public void onFailure(Call<Service1Response> call, Throwable t) {
                        Toast.makeText(ServiceActivity2.this, t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        );

        addLogs.setOnClickListener(
                v -> {
                    Intent intent1 = new Intent(ServiceActivity2.this, AddLogActivity.class);
                    intent1.putExtra("category", category);
                    startActivity(intent1);
                }
        );

    }

}