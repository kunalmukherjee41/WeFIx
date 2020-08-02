package com.example.wefix;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.wefix.Api.RetrofitClient;
import com.example.wefix.Fragments.CancelledLogFragment;
import com.example.wefix.Fragments.ClosedLogFragment;
import com.example.wefix.Fragments.OpenLogFragment;
import com.example.wefix.model.LogResponse;
import com.example.wefix.model.Logs;
import com.example.wefix.storage.SharedPrefManager;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogActivity extends AppCompatActivity {

    List<Logs> logsList;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Logs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

//        getLog();
//        if(logsList != null){
//            bundle.putSerializable("Logs" , (Serializable) logsList);
//        }

        OpenLogFragment openLogFragment = new OpenLogFragment();
//        openLogFragment.setArguments(bundle);
        ClosedLogFragment closedLogFragment = new ClosedLogFragment();
//        closedLogFragment.setArguments(bundle);
        CancelledLogFragment cancelledLogFragment = new CancelledLogFragment();
//        cancelledLogFragment.setArguments(bundle);

        viewPagerAdapter.addFragment(openLogFragment, "Open Log");
        viewPagerAdapter.addFragment(closedLogFragment, "Closed Log");
        viewPagerAdapter.addFragment(cancelledLogFragment, "Cancelled Log");

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
                startActivity(new Intent(this, MainActivity.class));
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
                Intent intent1 = new Intent(this, DisplayActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
                return true;
        }
        return false;
    }
}