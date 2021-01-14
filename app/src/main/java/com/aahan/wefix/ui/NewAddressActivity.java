package com.aahan.wefix.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aahan.wefix.Api.RetrofitClient;
import com.aahan.wefix.R;
import com.aahan.wefix.storage.SharedPrefManager;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewAddressActivity extends AppCompatActivity {

    private EditText name, email, phoneNumber;
    private EditText pinCode, address, city;
    private Button save;
    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);

        user_id = SharedPrefManager.getInstance(this).getUser().getId();

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phone_number);
        pinCode = findViewById(R.id.pin_code);
        address = findViewById(R.id.address);
        city = findViewById(R.id.city);

        save = findViewById(R.id.save);

        save.setOnClickListener(
                v -> {
                    save.setBackgroundColor(getResources().getColor(R.color.btn));
                    String txt_name = name.getText().toString();
                    String txt_email = email.getText().toString();
                    String txt_phoneNumber = phoneNumber.getText().toString();
                    String txt_pinCode = pinCode.getText().toString();
                    String txt_address = address.getText().toString();
                    String txt_city = city.getText().toString();

                    if (TextUtils.isEmpty(txt_name) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_phoneNumber) || TextUtils.isEmpty(txt_pinCode) || TextUtils.isEmpty(txt_address) || TextUtils.isEmpty(txt_city)) {
                        Toast.makeText(this, "All Field are required", Toast.LENGTH_SHORT).show();
                        save.setBackground(getResources().getDrawable(R.drawable.custom_btn2));

                    } else {

                        Call<ResponseBody> call2 = RetrofitClient
                                .getInstance()
                                .getApi()
                                .addAddress(txt_name, txt_address, txt_city, txt_pinCode, txt_phoneNumber, txt_email, user_id);

                        call2.enqueue(
                                new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(NewAddressActivity.this, "Successful added address", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(NewAddressActivity.this, LoginActivity.class));
                                        } else {
                                            Toast.makeText(NewAddressActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                        }
                                        save.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn2, null));
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(NewAddressActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                        save.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn2, null));

                                    }
                                }
                        );
                    }
                }
        );
    }
}