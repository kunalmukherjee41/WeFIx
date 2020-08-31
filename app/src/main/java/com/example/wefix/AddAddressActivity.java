package com.example.wefix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
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

public class AddAddressActivity extends AppCompatActivity {

    EditText name, email, phoneNumber, pinCode, address, city;
    Button addAddress, save;
    TextView cancel;
    RecyclerView recyclerView;
    ProgressDialog progressBar;
    LinearLayout layout;
    int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        user_id = SharedPrefManager.getInstance(this).getUser().getId();

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        email.setText(SharedPrefManager.getInstance(this).getUser().getUsername());
        email.setFocusable(false);
        phoneNumber = findViewById(R.id.phone_number);
        pinCode = findViewById(R.id.pin_code);
        address = findViewById(R.id.address);
        city = findViewById(R.id.city);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);

        bottomNavigationView.setSelectedItemId(R.id.address);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.home:
                            startActivity(new Intent(this, DisplayActivity.class));
                            overridePendingTransition(0, 0);
                            return true;

                        case R.id.log_history:
                            startActivity(new Intent(this, LogActivity.class));
                            overridePendingTransition(0, 0);
                            return true;

                        case R.id.address:
                            startActivity(new Intent(this, AddAddressActivity.class));
                            overridePendingTransition(0, 0);
                            return true;

                        case R.id.contact:
                            startActivity(new Intent(this, ContactActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                    }
                    return false;
                }
        );

        layout = findViewById(R.id.linear_layout);

        addAddress = findViewById(R.id.add_address);
        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);
        recyclerView = findViewById(R.id.recyclerView);

        layout.setVisibility(View.GONE);

        addAddress.setOnClickListener(
                v -> {
                    addAddress.setBackgroundColor(getResources().getColor(R.color.btn));
                    addAddress.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                    addAddress.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn2, null));
                }
        );

        cancel.setOnClickListener(
                v -> {
                    layout.setVisibility(View.GONE);
                    addAddress.setVisibility(View.VISIBLE);
                }
        );

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
                                            Toast.makeText(AddAddressActivity.this, "Successful added address", Toast.LENGTH_SHORT).show();
                                            layout.setVisibility(View.GONE);
                                            addAddress.setVisibility(View.VISIBLE);
                                            ((AppCompatActivity) Objects.requireNonNull(AddAddressActivity.this)).getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new AddAddressFragment()).commit();
                                            save.setBackground(getResources().getDrawable(R.drawable.custom_btn2));

                                        } else {
                                            Toast.makeText(AddAddressActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                            save.setBackground(getResources().getDrawable(R.drawable.custom_btn2));

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(AddAddressActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                        save.setBackground(getResources().getDrawable(R.drawable.custom_btn2));

                                    }
                                }
                        );
                    }
                }
        );

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        progressBar = new ProgressDialog(this);
        progressBar.show();
        progressBar.setContentView(R.layout.progress_dialog);
        Objects.requireNonNull(progressBar.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);


        Call<Address1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getAllAddress(user_id, "app");

        call.enqueue(
                new Callback<Address1Response>() {
                    @Override
                    public void onResponse(Call<Address1Response> call, Response<Address1Response> response) {
                        progressBar.dismiss();
                        if (response.isSuccessful()) {
                            List<Address> addressList = response.body().getAddressList();
                            AddressListAdapter addressListAdapter = new AddressListAdapter(AddAddressActivity.this, addressList);
                            recyclerView.setAdapter(addressListAdapter);
                        } else {
                            Toast.makeText(AddAddressActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Address1Response> call, Throwable t) {
                        progressBar.dismiss();
                        Toast.makeText(AddAddressActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }
}