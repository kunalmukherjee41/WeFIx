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

import com.bumptech.glide.Glide;
import com.example.wefix.Api.RetrofitClient;
import com.example.wefix.model.Category;
import com.example.wefix.model.Category1Response;
import com.example.wefix.model.Logs;
import com.example.wefix.model.Service;
import com.example.wefix.model.Service1Response;
import com.example.wefix.storage.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogHistoryDetailsActivity extends AppCompatActivity {

    TextView date, name, email, phone, problemDesc;
    TextView address, category, workType;
    TextView company, amount, status, pinCode;
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

        pinCode = findViewById(R.id.pin);
        date = findViewById(R.id.date);
        workType = findViewById(R.id.service);
        problemDesc = findViewById(R.id.problem_des);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        category = findViewById(R.id.category);
        company = findViewById(R.id.company);
        amount = findViewById(R.id.amount);
        status = findViewById(R.id.status);

        getService(logs.getRefServiceId(), logs.getRefCatId());

        getCategory(logs.getRefCatId());

    }

    private void getCategory(int refCatId) {

        Call<Category1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCategoryByID(refCatId);

        call.enqueue(
                new Callback<Category1Response>() {
                    @Override
                    public void onResponse(Call<Category1Response> call, Response<Category1Response> response) {
                        if (response.isSuccessful()) {
                            Category cat = response.body().getCategory();
                            category.setText(cat.getTbl_category_name());
//                            log_id.setText(String.valueOf(logs.getCallLogId()));
                            date.setText(logs.getCallLogDate());
                            pinCode.setText(logs.getClientPin());
//                            log_type.setText(logs.getCallLogType());
                            name.setText(logs.getClientName());
                            String txt_contact = logs.getClientEmail() + "\n" + logs.getClientMb();
//                            contact.setText(txt_contact);
                            email.setText(logs.getClientEmail());
                            phone.setText(logs.getClientMb());
                            address.setText(logs.getClientAddress());
                            amount.setText(String.valueOf(logs.getAmount()));
                            status.setText(logs.getCallLogStatus());
                            company.setText(logs.getProductCompany());
                            problemDesc.setText(logs.getProblem());
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
                return false;
            case R.id.home:
                Intent intent1 = new Intent(this, DisplayActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
                return true;
        }
        return false;
    }

    private void getService(int refServiceId, int refCatId) {

        final String[] serviceName = new String[1];

        Call<Service1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getService(refCatId);

        call.enqueue(
                new Callback<Service1Response>() {
                    @Override
                    public void onResponse(Call<Service1Response> call, Response<Service1Response> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            List<Service> serviceList = response.body().getService();
                            for (Service s : serviceList) {
                                if (s.getTbl_services_id() == refServiceId) {
                                    serviceName[0] = s.getTbl_services_name();
                                }
//                                serviceItem.add(s.getTbl_services_name());
                            }
//                            ArrayAdapter<String> adapter = new ArrayAdapter<>(LogHistoryDetailsActivity.this, R.layout.support_simple_spinner_dropdown_item, serviceItem);
                            workType.setText(serviceName[0]);

                        }
                    }

                    @Override
                    public void onFailure(Call<Service1Response> call, Throwable t) {

                    }
                }
        );

    }
}