package com.example.wefix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wefix.model.Service;

import java.util.Objects;

public class ServiceActivity extends AppCompatActivity {

    TextView service_charge, service_name, service_des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        service_charge = findViewById(R.id.service_charge);
        service_des = findViewById(R.id.service_des);
        service_name = findViewById(R.id.service_name);

        Intent intent = getIntent();
//        Service service = intent.getParcelableExtra("service");
        Service service = (Service) intent.getSerializableExtra("service");

//        Toast.makeText(this, service.getTbl_services_name()+"jfvnlku", Toast.LENGTH_LONG).show();

        assert service != null;
        service_name.setText(service.getTbl_services_name());
        service_des.setText(service.getTbl_services_des());
        service_charge.setText(String.valueOf(service.getTbl_services_charge()));

    }
}