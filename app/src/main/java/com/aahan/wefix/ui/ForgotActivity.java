package com.aahan.wefix.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.aahan.wefix.Api.RetrofitClient;
import com.aahan.wefix.R;
import com.aahan.wefix.model.ForgotResponse;
import com.aahan.wefix.model.Service1Response;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private String txt_old_pass, username;
    private String txt_new_pass;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        randomPassword();

        progressBar = findViewById(R.id.progress_bar);

        email = findViewById(R.id.email);
        Button submit = findViewById(R.id.submit);

        submit.setOnClickListener(
                v -> {
                    progressBar.setVisibility(View.VISIBLE);
                    submit.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn2, null));
                    username = email.getText().toString().trim();

                    Call<Service1Response> call = RetrofitClient
                            .getInstance()
                            .getApi()
                            .getPassByEmail(username);

                    call.enqueue(
                            new Callback<Service1Response>() {
                                @Override
                                public void onResponse(Call<Service1Response> call, Response<Service1Response> response) {
                                    if (response.isSuccessful()) {
                                        Service1Response service1Response = response.body();
                                        assert service1Response != null;
                                        txt_old_pass = service1Response.getMessage();
                                        Log.d("TAG", "onResponse: " + service1Response.getMessage());
                                        update();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Service1Response> call, Throwable t) {

                                }
                            }
                    );

                }
        );
    }

    private void update() {
        Log.d("TAG", "update: password id " + txt_new_pass);
        Call<ForgotResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .forgotPassword(username, txt_new_pass);

        call.enqueue(
                new Callback<ForgotResponse>() {
                    @Override
                    public void onResponse(Call<ForgotResponse> call, Response<ForgotResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            updateFirebasePassword();
                            Toast.makeText(ForgotActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            email.setText("");
                        }
                    }

                    @Override
                    public void onFailure(Call<ForgotResponse> call, Throwable t) {
                        Toast.makeText(ForgotActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void randomPassword() {
        Random random = new Random();
        String string = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder pass = new StringBuilder();
        for (int i = 0; i < 8; i++)
            pass.append(string.charAt(random.nextInt(string.length())));
        txt_new_pass = pass.toString();
    }

    private void updateFirebasePassword() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(username, txt_old_pass)
                .addOnCompleteListener(
                        task12 -> {
                            if (task12.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                assert user != null;
                                user.updatePassword(LoginActivity.getMD5(txt_new_pass))
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Log.d("TAG", "User password updated." + LoginActivity.getMD5(txt_new_pass) + "  " + txt_new_pass);
                                            } else {
                                                Log.d("TAG", "User password not updated.");
                                            }
                                        });
                            } else {
                                Log.d("TAG", "updateFirebasePassword: Failed");
                                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
        progressBar.setVisibility(View.GONE);
    }
}