package com.example.wefix;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.example.wefix.Api.RetrofitClient;
import com.example.wefix.model.Address;
import com.example.wefix.model.Address1Response;
import com.example.wefix.model.AddressResponse;
import com.example.wefix.model.Category;
import com.example.wefix.model.Service;
import com.example.wefix.storage.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
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
    TextView item_name, charge;

    List<Address> addressData;
    Service service;
    RadioGroup radioGroup;
    RadioGroup.LayoutParams r1;
    EditText name, address1, zip_code, phone, email, city, company_name, problem_des;

    LinearLayout layout;

    Button next, addressChange, add;

    String txt_address1, txt_name, txt_zip_code, txt_phone_number, txt_company_name;
    String txt_email_id, txt_problem_des, txt_service, txt_city;
    int user_id, service_id, txt_amount, category_id;

    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_log);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Call Logs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = new ProgressDialog(this);
        progressBar.show();
        progressBar.setContentView(R.layout.progress_dialog);
        Objects.requireNonNull(progressBar.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        company_name = findViewById(R.id.company_name);
        problem_des = findViewById(R.id.problem_des);
        city = findViewById(R.id.city);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        zip_code = findViewById(R.id.zip_code);
        address1 = findViewById(R.id.address1);
        phone = findViewById(R.id.phone);

        radioGroup = findViewById(R.id.address);
        radioGroup.setOrientation(RadioGroup.VERTICAL);

        item_name = findViewById(R.id.item_name);
        charge = findViewById(R.id.charge);
        image = findViewById(R.id.image);
//        address = findViewById(R.id.address);

        next = findViewById(R.id.next);
        addressChange = findViewById(R.id.change_address);
        add = findViewById(R.id.add);

        layout = findViewById(R.id.layout);

        Intent intent = getIntent();
        Category category = (Category) intent.getSerializableExtra("category");

        service = (Service) intent.getSerializableExtra("service");

        assert service != null;
        txt_amount = service.getTbl_services_charge();
        txt_service = service.getTbl_services_name();
        String txt_charge = "Charge : " + service.getTbl_services_charge();
        charge.setText(txt_charge);
        service_id = service.getTbl_services_id();

        assert category != null;
        category_id = category.getTbl_category_id();

        item_name.setText(category.getTbl_category_name());
        Glide.with(this).load("http://wefix.sitdoxford.org/product/" + category.getTbl_category_image()).into(image);

        user_id = SharedPrefManager.getInstance(AddLogActivity.this).getUser().getId();

        Call<Address1Response> call1 = RetrofitClient
                .getInstance()
                .getApi()
                .getAllAddress(user_id, "app");

        call1.enqueue(
                new Callback<Address1Response>() {
                    @Override
                    public void onResponse(Call<Address1Response> call, Response<Address1Response> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            addressData = response.body().getAddressList();
                            for (Address address : addressData) {
//                                txt_address1 = address.getBillingAddress();
//                                txt_city = address.getBillingCity();
//                                txt_zip_code = address.getZipCode();
//                                txt_name = address.getBillingName();
//                                txt_phone_number = address.getMbNo();
                                RadioButton r2 = new RadioButton(AddLogActivity.this);
                                String a = address.getBillingName() + "\n" + address.getBillingAddress() + " " + address.getBillingCity() + " " + "West Bengal, India\n" + "Pin: " + address.getZipCode() + "\nContact no: " + address.getMbNo();
//                            address.setText(a);
                                r2.setText(a);
                                r1 = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                                r1.setMargins(0, 10, 0, 0);
                                r2.setId(address.getBillingId());
                                radioGroup.addView(r2, r1);
                            }
                        }
                        progressBar.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Address1Response> call, Throwable t) {
                        Toast.makeText(AddLogActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.dismiss();
                    }
                }
        );

        layout.setVisibility(View.GONE);

        addressChange.setOnClickListener(
                v -> {
                    addressChange.setBackgroundColor(getResources().getColor(R.color.btn));
                    layout.setVisibility(View.VISIBLE);
                    email.setText(SharedPrefManager.getInstance(this).getUser().getUsername());
                    email.setFocusable(false);
                    addressChange.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
                }
        );

        add.setOnClickListener(
                v -> {
                    add.setBackgroundColor(getResources().getColor(R.color.btn));
                    txt_address1 = address1.getText().toString();
                    txt_name = name.getText().toString();
                    txt_zip_code = zip_code.getText().toString();
                    txt_phone_number = phone.getText().toString();
                    txt_email_id = email.getText().toString();
                    txt_city = city.getText().toString();

//                    String ab = txt_name + "\n" + txt_address1 + " " + txt_city + " " + "West Bengal, India\n" + "Pin: " + txt_zip_code + "\nContact no: " + txt_phone_number;
//                    address.setText(ab);

                    if (TextUtils.isEmpty(txt_address1) || TextUtils.isEmpty(txt_zip_code) || TextUtils.isEmpty(txt_name) || TextUtils.isEmpty(txt_phone_number) || TextUtils.isEmpty(txt_email_id) || TextUtils.isEmpty(txt_city)) {

                        Toast.makeText(AddLogActivity.this, "All Field Are Required", Toast.LENGTH_SHORT).show();
                        add.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn2, null));

                    } else {

                        Call<ResponseBody> call2 = RetrofitClient
                                .getInstance()
                                .getApi()
                                .addAddress(txt_name, txt_address1, txt_city, txt_zip_code, txt_phone_number, txt_email_id, user_id);

                        call2.enqueue(
                                new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(AddLogActivity.this, "Successful added address", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(AddLogActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                                        }
                                        add.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn2, null));
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(AddLogActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                        add.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn2, null));
                                    }
                                }
                        );
                    }
                }

        );
        next.setOnClickListener(
                v -> {
                    txt_problem_des = problem_des.getText().toString();
                    txt_company_name = company_name.getText().toString();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                    String currentDate = sdf.format(new Date());

                    SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
                    String currentTime = sdf1.format(new Date());

                    txt_email_id = SharedPrefManager.getInstance(this).getUser().getUsername();

                    progressBar = new ProgressDialog(this);
                    progressBar.show();
                    progressBar.setContentView(R.layout.progress_dialog);
                    Objects.requireNonNull(progressBar.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

                    next.setBackgroundColor(getResources().getColor(R.color.btn));

                    if (TextUtils.isEmpty(txt_name)) {
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        Address address = null;
                        for (Address a : addressData) {
                            if (selectedId == a.getBillingId()) {
                                address = a;
                                break;
                            }
                        }
                        if (address != null) {

                            txt_address1 = address.getBillingAddress();
                            txt_city = address.getBillingCity();
                            txt_zip_code = address.getZipCode();
                            txt_name = address.getBillingName();
                            txt_phone_number = address.getMbNo();
                        }
                        next.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
                        progressBar.dismiss();
                        Toast.makeText(AddLogActivity.this, "Select The address first", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(txt_address1)) {
                        Toast.makeText(AddLogActivity.this, "Enter the address", Toast.LENGTH_LONG).show();
                        next.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
                        progressBar.dismiss();

                    } else if (TextUtils.isEmpty(txt_name)) {
                        Toast.makeText(AddLogActivity.this, "Enter Name", Toast.LENGTH_LONG).show();
                        next.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
                        progressBar.dismiss();

                    } else if (TextUtils.isEmpty(txt_zip_code)) {
                        Toast.makeText(AddLogActivity.this, "Enter zip code", Toast.LENGTH_LONG).show();
                        next.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
                        progressBar.dismiss();

                    } else if (TextUtils.isEmpty(txt_phone_number)) {
                        Toast.makeText(AddLogActivity.this, "Enter phone number", Toast.LENGTH_LONG).show();
                        next.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
                        progressBar.dismiss();

                    } else if (TextUtils.isEmpty(txt_email_id)) {
                        Toast.makeText(AddLogActivity.this, "Enter email id", Toast.LENGTH_LONG).show();
                        next.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
                        progressBar.dismiss();

                    } else if (TextUtils.isEmpty(txt_problem_des)) {
                        Toast.makeText(AddLogActivity.this, "Enter problem des", Toast.LENGTH_LONG).show();
                        next.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
                        progressBar.dismiss();

                    } else if (TextUtils.isEmpty(txt_service)) {
                        Toast.makeText(AddLogActivity.this, "Enter service", Toast.LENGTH_LONG).show();
                        next.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
                        progressBar.dismiss();

                    } else if (TextUtils.isEmpty(txt_company_name)) {
                        Toast.makeText(AddLogActivity.this, "Enter company name", Toast.LENGTH_LONG).show();
                        next.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
                        progressBar.dismiss();

                    } else {
                        Call<ResponseBody> calll = RetrofitClient
                                .getInstance()
                                .getApi()
                                .addCallLog(currentDate, "APP", user_id, txt_name, txt_address1, txt_zip_code, txt_phone_number, txt_email_id, category_id, service_id, 0, txt_company_name, txt_amount, "POS", txt_problem_des, currentTime, "OPEN", ipAddress());

                        calll.enqueue(
                                new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            progressBar.dismiss();
                                            Toast.makeText(AddLogActivity.this, "Successful", Toast.LENGTH_LONG).show();
                                            Snackbar.make(layout, "Thank You for Submit Logs!", Snackbar.LENGTH_LONG)
                                                    .setAction("Close", v1 -> {
                                                    }).setActionTextColor(getResources().getColor(R.color.colorAccent)).show();
                                            company_name.setText("");
                                            problem_des.setText("");
                                            next.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));

                                            Intent intent1 = new Intent(AddLogActivity.this, SuccessfulMessageActivity.class);
                                            intent1.putExtra("string", "Thank you for submit Call Log");
                                            startActivity(intent1);

                                        } else {
                                            progressBar.dismiss();
                                            Toast.makeText(AddLogActivity.this, "Something went wrong Try again!", Toast.LENGTH_LONG).show();
                                            next.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        progressBar.dismiss();
                                        Toast.makeText(AddLogActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                        next.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));

                                    }
                                }
                        );
                    }
                });
    }

    public static String ipAddress() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.setting:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout:
                SharedPrefManager.getInstance(this).clear();
                Intent intent1 = new Intent(this, MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
                return true;
            case R.id.contact:
                startActivity(new Intent(this, ContactActivity.class));
                return true;
            case R.id.logs_history:
                startActivity(new Intent(this, LogActivity.class));
                return true;
            case R.id.payment_history:
                return false;
            case R.id.home:
                startActivity(new Intent(this, DisplayActivity.class));
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        progressBar.dismiss();
    }
}