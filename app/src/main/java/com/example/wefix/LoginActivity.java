package com.example.wefix;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.wefix.Api.RetrofitClient;
import com.example.wefix.model.UserResponse;
import com.example.wefix.storage.SharedPrefManager;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    private EditText email,password;
    TextView create_account, forgot_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        Button login = findViewById(R.id.login);
        create_account = findViewById(R.id.create_account);
        forgot_password = findViewById(R.id.forgot_password);

        create_account.setOnClickListener(
                v -> startActivity(new Intent(LoginActivity.this, CreateUserActivity.class))
        );

        login.setOnClickListener(
                v -> userlogin()
        );

    }

    private void userlogin() {

        String txt_username = email.getText().toString().trim();
        String txt_password = password.getText().toString().trim();
        if(TextUtils.isEmpty(txt_password)||TextUtils.isEmpty(txt_username)){
            Toast.makeText(LoginActivity.this, "Fill Both Requirements", Toast.LENGTH_LONG).show();
        } else if(!Patterns.EMAIL_ADDRESS.matcher(txt_username).matches()) {
            Toast.makeText(LoginActivity.this, "Provided a valid Email Address", Toast.LENGTH_LONG).show();
        }else {

            Call<UserResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .userLogin(txt_username,txt_password);

            call.enqueue(new Callback<UserResponse>() {
                             @Override
                             public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                                 if(response.isSuccessful()){
                                     assert response.body() != null;
                                     UserResponse userResponse = response.body();

                                     SharedPrefManager.getInstance(LoginActivity.this).saveUser(userResponse.getUser());

                                     Toast.makeText(LoginActivity.this, userResponse.getMessage(),Toast.LENGTH_LONG).show();
                                     Intent intent = new Intent(LoginActivity.this, DisplayActivity.class);
                                     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                     startActivity(intent);
                                     email.setText("");
                                 } else {
                                     Toast.makeText(LoginActivity.this, "Something went wrong Try Again",Toast.LENGTH_LONG).show();
                                 }
                                 password.setText("");
                             }

                             @Override
                             public void onFailure(Call<UserResponse> call, Throwable t) {
                                 Toast.makeText(LoginActivity.this, t.getMessage(),Toast.LENGTH_LONG).show();
                                 password.setText("");
                             }
                         }
            );

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            Intent intent = new Intent(this, DisplayActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

}
