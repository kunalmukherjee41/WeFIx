package com.example.wefix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wefix.Api.RetrofitClient;
import com.example.wefix.adapter.LogHistoryAdapter;
import com.example.wefix.model.LogResponse;
import com.example.wefix.model.Logs;
import com.example.wefix.storage.SharedPrefManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogActivity extends AppCompatActivity {

    private List<Logs> logsList;
    private Button search_log;
    private RecyclerView recyclerView;

    private String txt_year;

    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Logs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        add = findViewById(R.id.add);
        add.setOnClickListener(
                v -> startActivity(new Intent(LogActivity.this, ServiceActivity2.class))
        );

        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = thisYear; i >= 2008; i--) { years.add(String.valueOf(i)); }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, years);

        Spinner year = findViewById(R.id.year);
        year.setAdapter(adapter);

        year.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        txt_year = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        recyclerView = findViewById(R.id.logs_details);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        search_log = findViewById(R.id.search_log);

        search_log.setOnClickListener(
                v -> getLogHistory()
        );

    }

    private void getLogHistory() {

        int client_ref_id = SharedPrefManager.getInstance(this).getUser().getId();

        String call_log_date1 = txt_year + "-01-01";
        String call_log_date2 = txt_year + "-12-31";

        Call<LogResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCallLog(client_ref_id, call_log_date1, call_log_date2);

        call.enqueue(
                new Callback<LogResponse>() {
                    @Override
                    public void onResponse(Call<LogResponse> call, Response<LogResponse> response) {
                        if (response.isSuccessful()){
                            assert response.body() != null;
                            logsList = response.body().getLog();
//                            Toast.makeText(LogActivity.this, String.valueOf(logsList.get(0).getAmount()), Toast.LENGTH_LONG).show();
                            LogHistoryAdapter logHistoryAdapter = new LogHistoryAdapter(LogActivity.this, logsList);
                            recyclerView.setAdapter(logHistoryAdapter);
                        } else {
                            Toast.makeText(LogActivity.this, "Something went wrong try Again", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LogResponse> call, Throwable t) {

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
            case R.id.logs_history:
                startActivity(new Intent(this, LogActivity.class));
                return true;
            case R.id.call_logs:
                startActivity(new Intent(this, ServiceActivity2.class));
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