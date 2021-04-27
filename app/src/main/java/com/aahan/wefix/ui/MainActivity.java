package com.aahan.wefix.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aahan.wefix.Api.RetrofitClient;
import com.aahan.wefix.R;
import com.aahan.wefix.adapter.DisplayCategoryAdapter;
import com.aahan.wefix.model.Category;
import com.aahan.wefix.model.CategoryResponse;
import com.aahan.wefix.storage.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Category> categoryList;
//    ImageSlider imageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView login = findViewById(R.id.login);
        categoryList = new ArrayList<>();
//        imageSlider = findViewById(R.id.image_slider);

        //goto login page
        login.setOnClickListener(
                v -> startActivity(new Intent(MainActivity.this, LoginActivity.class))
        );

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView.setLayoutManager(layoutManager);

        getData();

    }

    //Fetch category from database
    private void getData() {

        int master_id = SharedPrefManager.getInstance(this).getMasterID();

        Call<CategoryResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCategory1(master_id);

        call.enqueue(
                new Callback<CategoryResponse>() {
                    @Override
                    public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            categoryList = response.body().getCategory();
//                            List<SlideModel> slideModels = new ArrayList<>();
//                            for (Category category : categoryList) {
//                                slideModels.add(new SlideModel("https://wefixservice.in/product/" + category.getTbl_category_image(), category.getTbl_category_name()));
//                            }
//                            imageSlider.setImageList(slideModels, true);
                            DisplayCategoryAdapter adapter = new DisplayCategoryAdapter(MainActivity.this, categoryList, "Main");
                            recyclerView.setAdapter(adapter);
//                            Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Something Went Wrong Try Again", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CategoryResponse> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

    }

    //check previously login or not
    @Override
    protected void onStart() {
        super.onStart();
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

}