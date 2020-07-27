package com.example.wefix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView contact, address, category, service;
    TextView company, amount, status;
    Logs logs;

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
        service = findViewById(R.id.service);
        company = findViewById(R.id.company);
        amount = findViewById(R.id.amount);
        status = findViewById(R.id.status);

        getCompany(logs.getCallCompanyId());
        getService(logs.getRefServiceId());
        getCategory(logs.getRefCatId());

    }

    private void getCompany(int tbl_company_id) {

        Call<Company1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCompanyByID(tbl_company_id, "app");

        call.enqueue(
                new Callback<Company1Response>() {
                    @Override
                    public void onResponse(Call<Company1Response> call, Response<Company1Response> response) {
                        if(response.isSuccessful()){
                            Company c = response.body().getCompany();
//                            Toast.makeText(LogHistoryDetailsActivity.this, c.getTblCompanyName(), Toast.LENGTH_LONG).show();
                            company.setText(c.getTblCompanyName());
//                        } else {
//                            Toast.makeText(LogHistoryDetailsActivity.this, "c.getTblCompanyName()", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Company1Response> call, Throwable t) {
                        Toast.makeText(LogHistoryDetailsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

    }

    private void getService(int refServiceId) {

        Call<Service1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getService(refServiceId, "app");

        call.enqueue(
                new Callback<Service1Response>() {
                    @Override
                    public void onResponse(Call<Service1Response> call, Response<Service1Response> response) {
                        if(response.isSuccessful()){
                            Service s = response.body().getService();
                            service.setText(s.getTbl_services_name());
                        }
                    }

                    @Override
                    public void onFailure(Call<Service1Response> call, Throwable t) {

                    }
                }
        );

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
                        if(response.isSuccessful()){
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
                startActivity(new Intent(this, ContactActivity.class));
                return true;
            case R.id.call_logs:
                startActivity(new Intent(this, ServiceActivity2.class));
                return true;
            case R.id.logs_history:
                startActivity(new Intent(this, LogActivity.class));
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
}