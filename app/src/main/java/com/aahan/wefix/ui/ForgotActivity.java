package com.aahan.wefix.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aahan.wefix.Api.RetrofitClient;
import com.aahan.wefix.R;
import com.aahan.wefix.model.ForgotResponse;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

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
                            .forgotPassword(txt_email, randomPassword());

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

    private String randomPassword() {
        Random random = new Random();
        String string = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder pass = new StringBuilder();
        for (int i = 0; i < 8; i++)
            pass.append(string.charAt(random.nextInt(string.length())));
        return pass.toString();
    }

    private void updateFirebasePassword(String username, String txt_password, String txt_new_password) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(username, txt_password);

        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user.updatePassword(txt_new_password).addOnCompleteListener(task1 -> Toast.makeText(this, "Password Change", Toast.LENGTH_SHORT));
                    }
                });
    }
}