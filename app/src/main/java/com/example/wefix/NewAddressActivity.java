package com.example.wefix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wefix.Api.RetrofitClient;
import com.example.wefix.Fragments.AddAddressFragment;
import com.example.wefix.adapter.AddressListAdapter;
import com.example.wefix.model.Address;
import com.example.wefix.model.Address1Response;
import com.example.wefix.storage.SharedPrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewAddressActivity extends AppCompatActivity {

    EditText name, email, phoneNumber, pinCode, address, city;
    Button save;
    int user_id;

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
                                        save.setBackground(getResources().getDrawable(R.drawable.custom_btn2));
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(NewAddressActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                        save.setBackground(getResources().getDrawable(R.drawable.custom_btn2));

                                    }
                                }
                        );
                    }
                }
        );

    }
}