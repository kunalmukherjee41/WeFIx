package com.example.wefix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wefix.Api.RetrofitClient;
import com.example.wefix.model.Address;
import com.example.wefix.model.AddressResponse;
import com.example.wefix.model.Category;
import com.example.wefix.model.Company;
import com.example.wefix.model.CompanyResponse;
import com.example.wefix.model.Service;
import com.example.wefix.model.Service1Response;
import com.example.wefix.storage.SharedPrefManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddLogActivity extends AppCompatActivity {

    ImageView image;
    TextView address, item_name, charge;

    Address addressData;
    Service service;

    EditText name, address1, zip_code, phone, email, city, company_name, problem_des;

    LinearLayout layout;

    Button next, addressChange, add;

    String txt_address1,txt_name,txt_zip_code,txt_phone_number, txt_company, txt_email_id,txt_problem_des,txt_service, txt_city;
    int user_id, service_id, txt_amount, category_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_log);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        company_name = findViewById(R.id.company_name);
        problem_des = findViewById(R.id.problem_des);
        city = findViewById(R.id.city);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        zip_code = findViewById(R.id.zip_code);
        address1 = findViewById(R.id.address1);
        phone = findViewById(R.id.phone);

        item_name = findViewById(R.id.item_name);
        charge = findViewById(R.id.charge);
        image = findViewById(R.id.image);
        address = findViewById(R.id.address);

        next = findViewById(R.id.next);
        addressChange = findViewById(R.id.change_address);
        add = findViewById(R.id.add);

        layout = findViewById(R.id.layout);

        Intent intent = getIntent();
        Category category = (Category) intent.getSerializableExtra("category");

        assert category != null;
        Call<Service1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getService(category.getTbl_category_id(), "app");

        call.enqueue(
                new Callback<Service1Response>() {
                    @Override
                    public void onResponse(Call<Service1Response> call, Response<Service1Response> response) {
                        if(response.isSuccessful()){
                            service = response.body().getService();
                            txt_amount = service.getTbl_services_charge();
                            txt_service = service.getTbl_services_name();
                            String txt_charge = "Charge : " + service.getTbl_services_charge();
                            charge.setText(txt_charge);
                            service_id = service.getTbl_services_id();
                        }
                    }

                    @Override
                    public void onFailure(Call<Service1Response> call, Throwable t) {
                        Toast.makeText(AddLogActivity.this, t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        );


        txt_company = company_name.getText().toString();
        category_id = category.getTbl_category_id();

        item_name.setText(category.getTbl_category_name());
        Glide.with(this).load("http://wefix.sitdoxford.org/product/"+category.getTbl_category_image()).into(image);

        user_id = SharedPrefManager.getInstance(AddLogActivity.this).getUser().getId();

        Call<AddressResponse> call1 = RetrofitClient
                .getInstance()
                .getApi()
                .getAddress(user_id, "app");

        call1.enqueue(
                new Callback<AddressResponse>() {
                    @Override
                    public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                        if(response.isSuccessful()){
                            assert response.body() != null;
                            addressData = response.body().getAddress();
                            txt_address1 = addressData.getBillingAddress();
                            txt_city = addressData.getBillingCity();
                            txt_zip_code = addressData.getZipCode();
                            txt_name = addressData.getBillingName();
                            txt_phone_number = addressData.getMbNo();
                            String a = txt_name +"\n" + txt_address1 + " "+ txt_city + " " + "West Bengal, India\n" + "Pin: " + txt_zip_code + "\n Contact no: "+ txt_phone_number;
                            address.setText(a);
                        }
                    }

                    @Override
                    public void onFailure(Call<AddressResponse> call, Throwable t) {
                        Toast.makeText(AddLogActivity.this, t.getMessage(), Toast.LENGTH_LONG);
                    }
                }
        );

        layout.setVisibility(View.GONE);

        addressChange.setOnClickListener(
                v -> layout.setVisibility(View.VISIBLE)
        );

        add.setOnClickListener(
                v -> {
                    txt_address1 = address1.getText().toString();
                    txt_name = name.getText().toString();
                    txt_zip_code = zip_code.getText().toString();
                    txt_phone_number = phone.getText().toString();
                    txt_email_id = email.getText().toString();
                    txt_city = city.getText().toString();

                    String ab = txt_name +"\n" + txt_address1 + " "+ txt_city + " " + "West Bengal, India\n" + "Pin: " + txt_zip_code + "\n Contact no: "+ txt_phone_number;
                    address.setText(ab);

                    Call<ResponseBody> call2 = RetrofitClient
                            .getInstance()
                            .getApi()
                            .addAddress(txt_name, txt_address1, txt_city, txt_zip_code, txt_phone_number, txt_email_id, user_id);

                    call2.enqueue(
                            new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if(response.isSuccessful()){
                                        Toast.makeText(AddLogActivity.this, "Successful added address", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(AddLogActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(AddLogActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                    );
                }

        );

        next.setOnClickListener(
                v -> {

                    txt_problem_des = problem_des.getText().toString();
                    txt_company = company_name.getText().toString();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                    String currentDate = sdf.format(new Date());

                    SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
                    String currentTime = sdf1.format(new Date());

                    if (TextUtils.isEmpty(txt_address1) || TextUtils.isEmpty(txt_name) || TextUtils.isEmpty(txt_zip_code) ||
                            TextUtils.isEmpty(txt_phone_number) || TextUtils.isEmpty(txt_email_id) || TextUtils.isEmpty(txt_problem_des) ||
                            TextUtils.isEmpty(txt_service)||TextUtils.isEmpty(txt_company)) {
                        Toast.makeText(AddLogActivity.this, "All Field are required", Toast.LENGTH_LONG).show();
                    } else {
                        Call<ResponseBody> calll = RetrofitClient
                                .getInstance()
                                .getApi()
                                .addCallLog(currentDate, "APP", user_id, txt_name, txt_address1, txt_zip_code, txt_phone_number, txt_email_id, category_id, service_id, 0, txt_company, txt_amount, "POS", txt_problem_des, currentTime, "OPEN", ipAddress());

                        calll.enqueue(
                                new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(AddLogActivity.this, "Successful", Toast.LENGTH_LONG).show();

                                        } else {
                                            Toast.makeText(AddLogActivity.this, "Something went wrong Try again!", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(AddLogActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                        );
                    }
                });
    }

    public static String ipAddress () {
        try {
            for (final Enumeration<NetworkInterface> enumerationNetworkInterface = NetworkInterface.getNetworkInterfaces(); enumerationNetworkInterface.hasMoreElements(); ) {
                final NetworkInterface networkInterface = enumerationNetworkInterface.nextElement();
                for (Enumeration<InetAddress> enumerationInetAddress = networkInterface.getInetAddresses(); enumerationInetAddress.hasMoreElements(); ) {
                    final InetAddress inetAddress = enumerationInetAddress.nextElement();
                    final String ipAddress = inetAddress.getHostAddress();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return ipAddress;
                    }
                }
            }
            return null;
        } catch (final Exception e) {
            return null;
        }
    }
}