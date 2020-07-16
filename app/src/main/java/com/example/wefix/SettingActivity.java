package com.example.wefix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.wefix.Api.RetrofitClient;
import com.example.wefix.model.User;
import com.example.wefix.storage.SharedPrefManager;

import java.util.ArrayList;

import retrofit2.Call;

public class SettingActivity extends AppCompatActivity {

    TextView id,name,phone_number,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id = findViewById(R.id.id);
        name = findViewById(R.id.name);
        phone_number = findViewById(R.id.phone_number);
        email = findViewById(R.id.email);

        User user = SharedPrefManager.getInstance(this).getUser();

        id.setText(String.valueOf(user.getId()));
        name.setText(user.getName());
        phone_number.setText(user.getPhone());
        email.setText(user.getUsername());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.setting:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout:
                SharedPrefManager.getInstance(this).clear();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.contact:
            case R.id.billing_address:
            case R.id.payment_history:
                return false;
            case R.id.home:
                startActivity(new Intent(this, DisplayActivity.class));
                return true;
        }
        return false;
    }
}