package com.example.wefix;

import androidx.appcompat.app.AppCompatActivity;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wefix.Api.RetrofitClient;
import com.example.wefix.model.Category;
import com.example.wefix.model.CategoryResponse;
import com.example.wefix.model.Company;
import com.example.wefix.model.CompanyResponse;
import com.example.wefix.model.Service;
import com.example.wefix.model.Service1Response;
import com.example.wefix.model.ServiceResponse;
import com.example.wefix.storage.SharedPrefManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceActivity2 extends AppCompatActivity {

    private List<Service> serviceList;
    private List<Category> categoryList;
    private List<Company> companyList;
    private ArrayAdapter<String> adapter;

    private int service_id;
    private int category_id;
    private int company_id;
    private int user_id;

    private String txt_category1,txt_service, txt_company, txt_log_type;

    private Service s;

    private Button log_view,add;
    private Spinner log_type, company, category1, service1, service;
    private EditText billing_name,billing_address,zip_code,phone_number,email_id, amount,problem_des;
    private TextView service_charge, service_name, service_des,category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service2);

        service = findViewById(R.id.categorys);
        category = findViewById(R.id.category);
        service_charge = findViewById(R.id.service_charge);
        service_des = findViewById(R.id.service_des);
        service_name = findViewById(R.id.service_name);

        //spinner
        log_type = findViewById(R.id.log_type);
        company = findViewById(R.id.company);
        category1 = findViewById(R.id.category1);
        service1 = findViewById(R.id.service);

        //textView
        billing_address = findViewById(R.id.billing_address);
        billing_name = findViewById(R.id.billing_name);
        zip_code = findViewById(R.id.zip_code);
        phone_number = findViewById(R.id.phone_number);
        email_id = findViewById(R.id.email_id);
        amount = findViewById(R.id.amount);
        problem_des = findViewById(R.id.problem_des);

        add = findViewById(R.id.add);
        log_view = findViewById(R.id.view_log);

        //set all the spinner
        setLogType();
        getCategory();
        getService();
        getCompany();
        email_id.setText(SharedPrefManager.getInstance(this).getUser().getUsername());
        email_id.setFocusable(false);

        //for service charge details
        service.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String item = parent.getItemAtPosition(position).toString();

                        for(Service service1 : serviceList){
                            if(item.equals(service1.getTbl_services_name())){
                                setView(service1);
                            }
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        //get the category from spinner
        category1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        getService1(position);
                        txt_category1 = parent.getItemAtPosition(position).toString();
                        for(Category category2 : categoryList){
                            if(category2.getTbl_category_name().equals(txt_category1)){
                                category_id = category2.getTbl_category_id();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        //get service from spinner
        service1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        txt_service = parent.getItemAtPosition(position).toString();
                        for(Service service2 : serviceList){
                            if(service2.getTbl_services_name().equals(txt_service)){
                                service_id = service2.getTbl_services_id();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        //get log type
        log_type.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        txt_log_type = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        //get company
        company.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        txt_company = parent.getItemAtPosition(position).toString();
                        for(Company company1 : companyList){
                            if(company1.getTblCompanyName().equals(txt_company)){
                                company_id = company1.getTblCompanyId();
                            }
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        //set all the data to database in callLog
        add.setOnClickListener(
                v -> {
                    String txt_billing_address = billing_address.getText().toString();
                    String txt_billing_name = billing_name.getText().toString();
                    String txt_zip_code = zip_code.getText().toString();
                    String txt_phone_number = phone_number.getText().toString();
                    String txt_email_id = email_id.getText().toString();
                    double txt_amount = Double.parseDouble(amount.getText().toString());
                    String txt_problem_des = problem_des.getText().toString();
                    user_id = SharedPrefManager.getInstance(this).getUser().getId();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                    String currentDate = sdf.format(new Date());

                    SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
                    String currentTime = sdf1.format(new Date());

                    if(TextUtils.isEmpty(txt_billing_address)||TextUtils.isEmpty(txt_billing_name)||TextUtils.isEmpty(txt_zip_code)||
                            TextUtils.isEmpty(txt_phone_number)||TextUtils.isEmpty(txt_email_id)||TextUtils.isEmpty(txt_problem_des)||
                            TextUtils.isEmpty(txt_service)||TextUtils.isEmpty(txt_category1)||TextUtils.isEmpty(txt_company)||TextUtils.isEmpty(txt_log_type)){
                        Toast.makeText(ServiceActivity2.this, "All Field are required", Toast.LENGTH_LONG).show();
                    } else {
                        Call<ResponseBody> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .addCallLog(currentDate, txt_log_type, user_id, txt_billing_name, txt_billing_address, txt_zip_code, txt_phone_number, txt_email_id, category_id, service_id, company_id, txt_amount, txt_problem_des, currentTime,"OPEN", ipAddress());

                        call.enqueue(
                                new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if(response.isSuccessful()){
                                            Toast.makeText(ServiceActivity2.this, "Successful", Toast.LENGTH_LONG).show();

                                            billing_name.setText("");
                                            billing_address.setText("");
                                            zip_code.setText("");
                                            phone_number.setText("");
                                            amount.setText("");
                                            problem_des.setText("");
                                            setLogType();
                                            getCategory();
                                            getService();
                                            getCompany();

                                        } else {
                                            Toast.makeText(ServiceActivity2.this, "Something went wrong Try again!", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                }
                        );
                    }
                }
        );

    }

    //fetch all the company from database
    private void getCompany() {

        ArrayList<String> nameList = new ArrayList<>();
        nameList.add("Select from below");

        Call<CompanyResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCompany();

        call.enqueue(
                new Callback<CompanyResponse>() {
                    @Override
                    public void onResponse(Call<CompanyResponse> call, Response<CompanyResponse> response) {
                        if(response.isSuccessful()){
                            companyList = response.body().getCompany();
                            for(Company c : companyList){
                                nameList.add(c.getTblCompanyName());
                            }
                            adapter = new ArrayAdapter<>(ServiceActivity2.this, R.layout.support_simple_spinner_dropdown_item, nameList);
                            company.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<CompanyResponse> call, Throwable t) {

                    }
                }
        );

    }


    //when category get selected
    private void getService1(int position) {

        Call<Service1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getService(position, "WeFix");

        call.enqueue(
                new Callback<Service1Response>() {
                    @Override
                    public void onResponse(Call<Service1Response> call, Response<Service1Response> response) {
                        if(response.isSuccessful()) {
                            assert response.body() != null;
                            s = response.body().getService();
                            ArrayList<String> nameList = new ArrayList<>();
                            nameList.add("Select from below");
                            nameList.add(s.getTbl_services_name());
                            adapter = new ArrayAdapter<>(ServiceActivity2.this, R.layout.support_simple_spinner_dropdown_item, nameList);
                            service1.setAdapter(adapter);
                            amount.setText(String.valueOf(s.getTbl_services_charge()));
                            amount.setFocusable(false);

                        } else {
                            Toast.makeText(ServiceActivity2.this, "Something went wrong Try Again!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Service1Response> call, Throwable t) {
                        Toast.makeText(ServiceActivity2.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

    }

    //fetch category from database
    private void getCategory() {

        ArrayList<String> nameList;
        nameList = new ArrayList<>();
        nameList.add("Select from below");

        Call<CategoryResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCategory1();

        call.enqueue(
                new Callback<CategoryResponse>() {
                    @Override
                    public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                        if(response.isSuccessful()){
                            categoryList = response.body().getCategory();
                            for(Category category2 : categoryList){
                                nameList.add(category2.getTbl_category_name());
                            }

                            adapter = new ArrayAdapter<>(ServiceActivity2.this, R.layout.support_simple_spinner_dropdown_item, nameList);
                            category1.setAdapter(adapter);

                        } else {
                            Toast.makeText(ServiceActivity2.this, "Something went wrong try Again",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CategoryResponse> call, Throwable t) {

                    }
                }
        );

    }

    //Set default Log Type
    private void setLogType() {

        ArrayList<String> nameList;
        nameList = new ArrayList<>();
        nameList.add("Select from below");

        nameList.add("App");
        adapter = new ArrayAdapter<>(ServiceActivity2.this, R.layout.support_simple_spinner_dropdown_item, nameList);
        log_type.setAdapter(adapter);

    }

    private void setView(Service service) {

        service_name.setText(service.getTbl_services_name());
        service_des.setText(service.getTbl_services_des());
        service_charge.setText(String.valueOf(service.getTbl_services_charge()));
        category.setText(service.getTbl_services_name());

    }

    // fetch services name from database
    private void getService(){

        ArrayList<String> nameList;
        nameList = new ArrayList<>();
        nameList.add("Select from below");

        Call<ServiceResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getAllServices();

        call.enqueue(
                new Callback<ServiceResponse>() {
                    @Override
                    public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            serviceList = response.body().getService();
                            for (Service ser : serviceList){
                                nameList.add(ser.getTbl_services_name());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(ServiceActivity2.this, R. layout.support_simple_spinner_dropdown_item, nameList);
                            service1.setAdapter(adapter);
                            service.setAdapter(adapter);
                        } else {
                            Toast.makeText(ServiceActivity2.this, "Something Went Wrong Try Again", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ServiceResponse> call, Throwable t){
                        Toast.makeText(ServiceActivity2.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

    }

    public static String ipAddress() {
        try {
            for (final Enumeration<NetworkInterface> enumerationNetworkInterface = NetworkInterface.getNetworkInterfaces(); enumerationNetworkInterface.hasMoreElements();) {
                final NetworkInterface networkInterface = enumerationNetworkInterface.nextElement();
                for (Enumeration<InetAddress> enumerationInetAddress = networkInterface.getInetAddresses(); enumerationInetAddress.hasMoreElements();) {
                    final InetAddress inetAddress = enumerationInetAddress.nextElement();
                    final String ipAddress = inetAddress.getHostAddress();
                    if(!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return ipAddress;
                    }
                }
            }
            return null;
        }
        catch (final Exception e) {
//            LogHelper.wtf(null, e);
            return null;
        }
    }
}