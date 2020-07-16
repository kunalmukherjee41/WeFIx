package com.example.wefix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wefix.Api.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateUserActivity extends AppCompatActivity {

    EditText name,email,password,phone;
    Button create_user;
    TextView forgot_password,login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        create_user = findViewById(R.id.create_user);

        login = findViewById(R.id.login);
        forgot_password = findViewById(R.id.forgot_password);

        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(CreateUserActivity.this, LoginActivity.class));
                    }
                }
        );

        create_user.setOnClickListener(
                v -> createUser()
        );
    }

    private void createUser() {

        String txt_email = email.getText().toString();
        String txt_phone = phone.getText().toString();
        String txt_name = name.getText().toString();
        String txt_password = password.getText().toString();

        if(TextUtils.isEmpty(txt_email)||TextUtils.isEmpty(txt_name)||TextUtils.isEmpty(txt_password)||TextUtils.isEmpty(txt_phone)){
            Toast.makeText(CreateUserActivity.this,"All Field are Required",Toast.LENGTH_LONG).show();
        } else if(!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()){
            Toast.makeText(CreateUserActivity.this,"Provide a Valid Email Address",Toast.LENGTH_LONG).show();
        } else if(txt_password.length()<6){
            Toast.makeText(CreateUserActivity.this,"Password should be atLeast 6 character", Toast.LENGTH_LONG).show();
        } else {
            Call<ResponseBody> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .createUser(txt_email, txt_password, txt_name, "Client", txt_phone, Calendar.getInstance().getTime().toString(), "App");

            call.enqueue(new Callback<ResponseBody>() {
                             @Override
                             public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                 if(response.isSuccessful()) {
                                     try {
                                         assert response.body() != null;
                                         String s = response.body().string();
                                         Toast.makeText(CreateUserActivity.this, s, Toast.LENGTH_LONG).show();
                                         if(response.code() == 201){
                                             Intent intent = new Intent(CreateUserActivity.this, LoginActivity.class);
                                             startActivity(intent);
                                         }
                                         email.setText("");
                                         password.setText("");
                                         name.setText("");
                                         phone.setText("");
                                         finish();
                                     } catch (IOException e) {
                                         e.printStackTrace();
                                     }
                                 } else {
                                     Toast.makeText(CreateUserActivity.this, "Something Went Wrong Try Again", Toast.LENGTH_LONG).show();
                                 }
                             }

                             @Override
                             public void onFailure(Call<ResponseBody> call, Throwable t) {
                                 Toast.makeText(CreateUserActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                             }
                         }
            );

        }

    }

}
