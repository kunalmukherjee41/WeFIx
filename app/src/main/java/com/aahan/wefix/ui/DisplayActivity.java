package com.aahan.wefix.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.aahan.wefix.Api.RetrofitClient;
import com.aahan.wefix.Fragments.AddAddressFragment;
import com.aahan.wefix.Fragments.DisplayFragment;
import com.aahan.wefix.Fragments.MasterCategoryFragment;
import com.aahan.wefix.R;
import com.aahan.wefix.model.Token;
import com.aahan.wefix.model.User;
import com.aahan.wefix.model.UserResponse;
import com.aahan.wefix.storage.SharedPrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        userExist();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        UpdateToken();

        String firebaseID = FirebaseAuth.getInstance().getUid();
        String username = SharedPrefManager.getInstance(this).getUser().getUsername();
//        Toast.makeText(DisplayActivity.this, username, Toast.LENGTH_SHORT).show();

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateFirebaseID(firebaseID, username);

        call.enqueue(
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            if (!SharedPrefManager.getInstance(DisplayActivity.this).isLoggedFirebase()) {
                                SharedPrefManager.getInstance(DisplayActivity.this).saveFirebaseId(1);
//                                Toast.makeText(DisplayActivity.this, firebaseID, Toast.LENGTH_SHORT).show();
                            }
                        } else {
//                            Log.d("MainActivity123", "Field");
//                            Toast.makeText(MainActivity.this, "firebaseID", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(DisplayActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);

        bottomNavigationView.setSelectedItemId(R.id.home);

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

        View v = navigationView.getHeaderView(0);
        TextView nav_name = v.findViewById(R.id.name);
        TextView nav_email = v.findViewById(R.id.email);
        ImageView nav_image = v.findViewById(R.id.image);
        nav_name.setText(SharedPrefManager.getInstance(this).getUser().getName());
        nav_email.setText(SharedPrefManager.getInstance(this).getUser().getUsername());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new MasterCategoryFragment()).commit();
            navigationView.setCheckedItem(R.id.home);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //on menu item selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.setting:
                Intent intent = new Intent(DisplayActivity.this, SettingActivity.class);
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
                startActivity(new Intent(DisplayActivity.this, LogActivity.class));
                return true;
            case R.id.payment_history:
                startActivity(new Intent(DisplayActivity.this, PaymentActivity.class));
                return true;
            case R.id.change_password:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                return true;
            case R.id.home:
                Intent intent1 = new Intent(this, DisplayActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                return true;
        }
        return false;
    }

    //check user previously login or not
    @Override
    protected void onStart() {
        super.onStart();
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(DisplayActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new MasterCategoryFragment()).commit();
                break;

            case R.id.add_address:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new AddAddressFragment()).commit();
                break;

            case R.id.call_log:
                startActivity(new Intent(this, LogActivity.class));
                break;

            case R.id.payment_history:
                startActivity(new Intent(this, PaymentActivity.class));
                break;

            case R.id.logout:
                SharedPrefManager.getInstance(this).clear();
                startActivity(new Intent(DisplayActivity.this, LoginActivity.class));
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void UpdateToken() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Token token = new Token(refreshToken);

        assert firebaseUser != null;
        FirebaseDatabase.getInstance().getReference("Tokens").child(firebaseUser.getUid()).setValue(token)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(DisplayActivity.this, "erf", Toast.LENGTH_SHORT);
                            }
                        }
                );
    }

    private void userExist() {
        String email = SharedPrefManager.getInstance(this).getUser().getUsername();

        Call<UserResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getUserByEmail(email, "abc");

        call.enqueue(
                new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            User u = response.body().getUser();

                            assert u != null;
                            if (TextUtils.isEmpty(u.getUsername())) {
                                Toast.makeText(DisplayActivity.this, "Logout\tUser Not Exist.", Toast.LENGTH_SHORT).show();
                                SharedPrefManager.getInstance(DisplayActivity.this).clear();

                                Intent intent = new Intent(DisplayActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Toast.makeText(DisplayActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

}
