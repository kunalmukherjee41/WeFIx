package com.example.wefix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wefix.Api.RetrofitClient;
import com.example.wefix.model.Category;
import com.example.wefix.model.Category1Response;
import com.example.wefix.model.Company;
import com.example.wefix.model.Company1Response;
import com.example.wefix.model.Logs;
import com.example.wefix.model.Service;
import com.example.wefix.model.Service1Response;
import com.example.wefix.storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogHistoryDetailsActivity extends AppCompatActivity {

    TextView log_id, date, log_type, name;
    TextView contact, address, category;
    TextView company, amount, status;
    Logs logs;
    ImageView image1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_history_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Logs Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();

        logs = (Logs) i.getSerializableExtra("logs");

        log_id = findViewById(R.id.log_id);
        date = findViewById(R.id.date);
        log_type = findViewById(R.id.log_type);
        name = findViewById(R.id.name);
        contact = findViewById(R.id.contact);
        address = findViewById(R.id.address);
        category = findViewById(R.id.category);
        image1 = findViewById(R.id.image1);
        company = findViewById(R.id.company);
        amount = findViewById(R.id.amount);
        status = findViewById(R.id.status);

        getCategory(logs.getRefCatId());

    }

    private void getCategory(int refCatId) {

        Call<Category1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCategoryByID(refCatId, "aa");

        call.enqueue(
                new Callback<Category1Response>() {
                    @Override
                    public void onResponse(Call<Category1Response> call, Response<Category1Response> response) {
                        if (response.isSuccessful()) {
                            Category cat = response.body().getCategory();
                            category.setText(cat.getTbl_category_name());
                            log_id.setText(String.valueOf(logs.getCallLogId()));
                            date.setText(logs.getCallLogDate());
                            log_type.setText(logs.getCallLogType());
                            name.setText(logs.getClientName());
                            String txt_contact = logs.getClientEmail() + "\n" + logs.getClientMb();
                            contact.setText(txt_contact);
                            address.setText(logs.getClientAddress());
                            amount.setText(String.valueOf(logs.getAmount()));
                            status.setText(logs.getCallLogStatus());
                            Glide.with(LogHistoryDetailsActivity.this).load("http://wefix.sitdoxford.org/product/" + cat.getTbl_category_image()).into(image1);
                            company.setText(logs.getProductCompany());
                        }
                    }

                    @Override
                    public void onFailure(Call<Category1Response> call, Throwable t) {

                    }
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