package com.example.wefix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.wefix.storage.SharedPrefManager;

import java.util.ArrayList;

public class DisplayActivity extends AppCompatActivity {

    CardView account_info, billing_address_info, contact_info;
    CardView payment_info, payment_history_info, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        account_info = findViewById(R.id.account_info);
        billing_address_info = findViewById(R.id.billing_address_info);
        contact_info = findViewById(R.id.contact_info);
        payment_history_info = findViewById(R.id.payment_history_info);
        payment_info = findViewById(R.id.payment_info);
        logout = findViewById(R.id.logout);

        logout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPrefManager.getInstance(DisplayActivity.this).clear();
                        startActivity(new Intent(DisplayActivity.this, MainActivity.class));
                    }
                }
        );

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
                Intent intent = new Intent(DisplayActivity.this, SettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout:
                SharedPrefManager.getInstance(this).clear();
                startActivity(new Intent(DisplayActivity.this, MainActivity.class));
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

    @Override
    protected void onStart() {
        super.onStart();
        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            Intent intent = new Intent(DisplayActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

}
