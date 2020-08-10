package com.example.wefix.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wefix.Api.RetrofitClient;
import com.example.wefix.R;
import com.example.wefix.adapter.AddressListAdapter;
import com.example.wefix.model.Address;
import com.example.wefix.model.Address1Response;
import com.example.wefix.storage.SharedPrefManager;

import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddressFragment extends Fragment {

    EditText name, email, phoneNumber, pinCode, address, city;
    Button addAddress, save;
    TextView cancel;
    RecyclerView recyclerView;
    ProgressDialog progressBar;

    LinearLayout layout;

    int user_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_add_address, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user_id = SharedPrefManager.getInstance(getContext()).getUser().getId();

        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        phoneNumber = view.findViewById(R.id.phone_number);
        pinCode = view.findViewById(R.id.pin_code);
        address = view.findViewById(R.id.address);
        city = view.findViewById(R.id.city);

        email.setText(SharedPrefManager.getInstance(getContext()).getUser().getUsername());
        email.setFocusable(false);

        layout = view.findViewById(R.id.linear_layout);

        addAddress = view.findViewById(R.id.add_address);
        save = view.findViewById(R.id.save);
        cancel = view.findViewById(R.id.cancel);
        recyclerView = view.findViewById(R.id.recyclerView);

        layout.setVisibility(View.GONE);

        addAddress.setOnClickListener(
                v -> {
                    addAddress.setBackgroundColor(getResources().getColor(R.color.btn));
                    addAddress.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                    addAddress.setBackground(getResources().getDrawable(R.drawable.custom_btn2));
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
                        Toast.makeText(getContext(), "All Field are required", Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(getContext(), "Successful added address", Toast.LENGTH_SHORT).show();
                                            layout.setVisibility(View.GONE);
                                            addAddress.setVisibility(View.VISIBLE);
                                            ((AppCompatActivity) Objects.requireNonNull(getContext())).getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new AddAddressFragment()).commit();
                                            save.setBackground(getResources().getDrawable(R.drawable.custom_btn2));

                                        } else {
                                            Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                            save.setBackground(getResources().getDrawable(R.drawable.custom_btn2));

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                        save.setBackground(getResources().getDrawable(R.drawable.custom_btn2));

                                    }
                                }
                        );
                    }
                }
        );

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        progressBar = new ProgressDialog(getContext());
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
                        if (response.isSuccessful()) {
                            progressBar.dismiss();
                            List<Address> addressList = response.body().getAddressList();
                            AddressListAdapter addressListAdapter = new AddressListAdapter(getContext(), addressList);
                            recyclerView.setAdapter(addressListAdapter);
                        } else {
                            progressBar.dismiss();
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Address1Response> call, Throwable t) {
                        progressBar.dismiss();
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

}