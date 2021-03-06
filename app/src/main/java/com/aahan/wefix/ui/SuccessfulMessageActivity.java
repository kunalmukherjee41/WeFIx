package com.aahan.wefix.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.aahan.wefix.R;
import com.aahan.wefix.storage.SharedPrefManager;

import java.util.Objects;

public class SuccessfulMessageActivity extends AppCompatActivity {

    private String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Message");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView name = findViewById(R.id.log_submission);
        Intent intent = getIntent();

        s = intent.getStringExtra("string");

        name.setText(s);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if ("Thank you for submit Call Log".equals(s)) {
                startActivity(new Intent(this, DisplayActivity.class));
            } else {
                startActivity(new Intent(this, LogActivity.class));
            }
            finish();
        }, 4000);

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
                Intent intent2 = new Intent(this, LoginActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent2);
                return true;
            case R.id.contact:
                startActivity(new Intent(this, ContactActivity.class));
                return true;
            case R.id.logs_history:
                startActivity(new Intent(this, LogActivity.class));
                return true;
            case R.id.payment_history:
                startActivity(new Intent(this, PaymentActivity.class));
                return true;
            case R.id.change_password:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                return true;
            case R.id.home:
                Intent intent1 = new Intent(this, DisplayActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
                return true;
        }
        return false;
    }

}