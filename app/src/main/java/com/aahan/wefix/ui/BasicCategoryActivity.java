package com.aahan.wefix.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.aahan.wefix.Api.RetrofitClient;
import com.aahan.wefix.R;
import com.aahan.wefix.adapter.DisplayCategoryAdapter;
import com.aahan.wefix.model.Category;
import com.aahan.wefix.model.CategoryResponse;
import com.aahan.wefix.storage.SharedPrefManager;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasicCategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Category> categoryList = new ArrayList<>();
    private DisplayCategoryAdapter adapter;
    private int master_ID;
    private ShimmerFrameLayout shimmerFrameLayout;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_category);

        Toolbar toolbar = findViewById(R.id.toolbar);
        configureToolbar(toolbar);

        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        recyclerView = findViewById(R.id.recyclerView1);
        refreshLayout = findViewById(R.id.refresh_flats_layout);
        recyclerView.setVisibility(View.GONE);

        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();

        refreshLayout.setOnRefreshListener(
                () -> {
                    shimmerFrameLayout.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.startShimmerAnimation();
                    recyclerView.setVisibility(View.GONE);
                    new FetchBasicCategory().execute();
                    refreshLayout.setRefreshing(false);
                }
        );

        recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        new FetchBasicCategory().execute();

    }

    class FetchBasicCategory extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            master_ID = SharedPrefManager.getInstance(BasicCategoryActivity.this).getMasterID();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Call<CategoryResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getCategory1(master_ID);


            call.enqueue(
                    new Callback<CategoryResponse>() {
                        @Override
                        public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                            if (response.isSuccessful()) {
                                assert response.body() != null;
                                saveData(response.body().getCategory());

                            } else {
                                Log.i("ABS", "Somthing Went Wrong");
//                                Toast.makeText, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
//                            progressBar.dismiss();
                        }

                        @Override
                        public void onFailure(Call<CategoryResponse> call, Throwable t) {
//                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
//                            progressBar.dismiss();
                        }
                    }
            );

            return null;
        }

    }

    private void saveData(List<Category> category) {

        categoryList.clear();
        categoryList = new ArrayList<>();
        categoryList.addAll(category);

        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setItemViewCacheSize(categoryList.size());
        adapter = new DisplayCategoryAdapter(BasicCategoryActivity.this, categoryList, "Display");
        adapter.setHasStableIds(true);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        shimmerFrameLayout.stopShimmerAnimation();
        shimmerFrameLayout.setVisibility(View.GONE);
        refreshLayout.setRefreshing(false);
    }

    public void configureToolbar(androidx.appcompat.widget.Toolbar toolbar) {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
//        Window window = getWindow();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
//        }
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
                Intent intent2 = new Intent(this, LoginActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent2);
                return true;
            case R.id.contact:
                startActivity(new Intent(this, ContactActivity.class));
                return true;
            case R.id.logs_history:
                startActivity(new Intent(this, LogActivity.class));
                return true;
            case R.id.payment_history:
                startActivity(new Intent(this, PaymentActivity.class));
                return true;
            case R.id.change_password:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                return true;
            case R.id.home:
                Intent intent1 = new Intent(this, DisplayActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
                return true;
        }
        return false;
    }

}