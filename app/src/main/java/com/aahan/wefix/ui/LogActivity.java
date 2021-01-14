package com.aahan.wefix.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.aahan.wefix.Fragments.CancelledLogFragment;
import com.aahan.wefix.Fragments.ClosedLogFragment;
import com.aahan.wefix.Fragments.OpenLogFragment;
import com.aahan.wefix.R;
import com.aahan.wefix.storage.SharedPrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class LogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Logs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setSelectedItemId(R.id.log_history);
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

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(2);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new OpenLogFragment(), "Open Log");
        viewPagerAdapter.addFragment(new ClosedLogFragment(), "Closed Log");
        viewPagerAdapter.addFragment(new CancelledLogFragment(), "Cancelled Log");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

    }

//    public void getLog() {
//
//        int client_ref_id = SharedPrefManager.getInstance(this).getUser().getId();
//
//        Call<LogResponse> call = RetrofitClient
//                .getInstance()
//                .getApi()
//                .getCallLog(client_ref_id, "app");
//
//        call.enqueue(
//                new Callback<LogResponse>() {
//                    @Override
//                    public void onResponse(Call<LogResponse> call, Response<LogResponse> response) {
//                        if (response.isSuccessful()) {
//                            assert response.body() != null;
//                            logsList = response.body().getLog();
//                        } else {
//                            Toast.makeText(LogActivity.this, "Something went wrong try Again", Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<LogResponse> call, Throwable t) {
//
//                    }
//                }
//        );

//        return logsList;
//    }

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
                Intent intent2 = new Intent(this, MainActivity.class);
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