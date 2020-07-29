package com.example.wefix;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.wefix.Api.RetrofitClient;
import com.example.wefix.model.Category;
import com.example.wefix.model.Service;
import com.example.wefix.model.Service1Response;
import com.example.wefix.storage.SharedPrefManager;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Call Logs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.name);
        rs = findViewById(R.id.rs);

        addLogs = findViewById(R.id.add_log);
        imageView = findViewById(R.id.image1);

        intent = getIntent();
        category = (Category) intent.getSerializableExtra("category");
        assert category != null;
        int id = category.getTbl_category_id();
        name.setText(category.getTbl_category_name());

        Glide.with(this).load("http://wefix.sitdoxford.org/product/" + category.getTbl_category_image()).into(imageView);

        Call<Service1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getService(id, "app");

        call.enqueue(
                new Callback<Service1Response>() {
                    @Override
                    public void onResponse(Call<Service1Response> call, Response<Service1Response> response) {
                        if (response.isSuccessful()) {
                            service = response.body().getService();
                            rs.setText(String.valueOf(service.getTbl_services_charge()));
                        }
                    }

                    @Override
                    public void onFailure(Call<Service1Response> call, Throwable t) {
                        Toast.makeText(ServiceActivity2.this, t.getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.setting:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout:
                SharedPrefManager.getInstance(this).clear();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.contact:
                startActivity(new Intent(this, ContactActivity.class));
                return true;
            case R.id.logs_history:
                startActivity(new Intent(this, LogActivity.class));
                return true;
            case R.id.payment_history:
                return false;
            case R.id.home:
                Intent intent1 = new Intent(this, DisplayActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
                return true;
        }
        return false;
    }

}