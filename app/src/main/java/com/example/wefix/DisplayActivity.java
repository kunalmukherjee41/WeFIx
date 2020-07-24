package com.example.wefix;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.wefix.storage.SharedPrefManager;

import java.util.Objects;

public class DisplayActivity extends AppCompatActivity {

    private CardView account_info, billing_address_info, contact_info;
    private CardView payment_info, payment_history_info, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        account_info = findViewById(R.id.account_info);
        billing_address_info = findViewById(R.id.billing_address_info);
        contact_info = findViewById(R.id.contact_info);
        payment_history_info = findViewById(R.id.payment_history_info);
        payment_info = findViewById(R.id.payment_info);
        logout = findViewById(R.id.logout);

//        startActivity(new Intent(this, LogActivity.class));

        //logout and clear the shared storage class data
        logout.setOnClickListener(
                v -> {
                    SharedPrefManager.getInstance(DisplayActivity.this).clear();
                    startActivity(new Intent(DisplayActivity.this, MainActivity.class));
                }
        );

        //goto contact activity
        contact_info.setOnClickListener(
                v -> {
                    startActivity(new Intent(DisplayActivity.this, ContactActivity.class));
                }
        );

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    //on menu item selected
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
            case R.id.call_logs:
                startActivity(new Intent(DisplayActivity.this, ServiceActivity2.class));
                return true;
            case R.id.contact:
                startActivity(new Intent(this, ContactActivity.class));
                return true;
            case R.id.logs_history:
                startActivity(new Intent(DisplayActivity.this, LogActivity.class));
                return true;
            case R.id.payment_history:
                return false;
            case R.id.home:
                Intent intent1 = new Intent(this, DisplayActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
                return true;
        }
        return false;
    }

    //check user previously login or not
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
