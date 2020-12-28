package com.example.wefix.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.wefix.Api.RetrofitClient;
import com.example.wefix.R;
import com.example.wefix.model.ForgotResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText email = findViewById(R.id.email);
        Button submit = findViewById(R.id.submit);
        ProgressBar progressBar = findViewById(R.id.progress_bar);

        submit.setOnClickListener(
                v -> {
                    progressBar.setVisibility(View.VISIBLE);
                    submit.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn2, null));

                    String txt_email = email.getText().toString().trim();
                    Call<ForgotResponse> call = RetrofitClient
                            .getInstance()
                            .getApi()
                            .forgotPassword(txt_email);

                    call.enqueue(
                            new Callback<ForgotResponse>() {
                                @Override
                                public void onResponse(Call<ForgotResponse> call, Response<ForgotResponse> response) {
                                    if (response.isSuccessful()) {
                                        assert response.body() != null;
                                        Toast.makeText(ForgotActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        email.setText("");
                                        progressBar.setVisibility(View.GONE);
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ForgotResponse> call, Throwable t) {
                                    Toast.makeText(ForgotActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    );

                }
        );

    }
}