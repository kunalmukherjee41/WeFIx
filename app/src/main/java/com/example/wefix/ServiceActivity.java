package com.example.wefix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wefix.Api.RetrofitClient;
import com.example.wefix.adapter.ServiceListAdapter;
import com.example.wefix.model.Category;
import com.example.wefix.model.Service;
import com.example.wefix.model.Service1Response;
import com.example.wefix.model.ServiceResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceActivity extends AppCompatActivity {

//    private Spinner services;
//
//    private List<Service> serviceList;
//    private ArrayList<String> nameList;
//
//    private TextView service_charge;
//    private TextView service_name;
//    private TextView service_des;
//    private TextView category;

    TextView name;
    ImageView imageView;
    List<Service> service;
    Category category;
    Intent intent;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.name);
        imageView = findViewById(R.id.image1);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        Category category = (Category) intent.getSerializableExtra("category");
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
//                            rs.setText(String.valueOf(service.getTbl_services_charge()));

                            ServiceListAdapter adapter = new ServiceListAdapter(ServiceActivity.this, service, category, "NO");
                            recyclerView.setAdapter(adapter);

                        } else {
                            Toast.makeText(ServiceActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Service1Response> call, Throwable t) {
                        Toast.makeText(ServiceActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

//        nameList = new ArrayList<>();
//        nameList.add("Select from below");
//        TextView login = findViewById(R.id.login);

        //get all service data
//        getData();

        //textView to login page
//        login.setOnClickListener(
//                v -> startActivity(new Intent(ServiceActivity.this, LoginActivity.class))
//        );

//        services = findViewById(R.id.categorys);
//        category = findViewById(R.id.category);
//        service_charge = findViewById(R.id.service_charge);
//        service_des = findViewById(R.id.service_des);
//        service_name = findViewById(R.id.service_name);

//        setView(service);
//
//        //set all the textView in spinner click
//        services.setOnItemSelectedListener(
//                new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                        String item = parent.getItemAtPosition(position).toString();
//
//                        for (Service service1 : serviceList) {
//                            if (item.equals(service1.getTbl_services_name())) {
//                                setView(service1);
//                            }
//                        }
//
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                }
//        );

    }

    //set all the textView
//    private void setView(Service service) {
//
//        service_name.setText(service.getTbl_services_name());
//        service_des.setText(service.getTbl_services_des());
//        service_charge.setText(String.valueOf(service.getTbl_services_charge()));
//        category.setText(service.getTbl_services_name());

//    }

    //fetch services from database and set in spinner
//    private void getData() {
//
//        Call<ServiceResponse> call = RetrofitClient
//                .getInstance()
//                .getApi()
//                .getAllServices();
//
//        call.enqueue(
//                new Callback<ServiceResponse>() {
//                    @Override
//                    public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
//                        if (response.isSuccessful()) {
//                            assert response.body() != null;
//                            serviceList = response.body().getService();
//                            for (Service service : serviceList) {
//                                nameList.add(service.getTbl_services_name());
//                            }
//                            ArrayAdapter<String> adapter = new ArrayAdapter<>(ServiceActivity.this, R.layout.support_simple_spinner_dropdown_item, nameList);
//                            services.setAdapter(adapter);
//                        } else {
//                            Toast.makeText(ServiceActivity.this, "Something Went Wrong Try Again", Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ServiceResponse> call, Throwable t) {
//                        Toast.makeText(ServiceActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                }
//        );
//
//    }

}