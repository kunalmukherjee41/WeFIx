package com.example.wefix.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wefix.Api.RetrofitClient;
import com.example.wefix.R;
import com.example.wefix.adapter.DisplayCategoryAdapter;
import com.example.wefix.model.Category;
import com.example.wefix.model.CategoryResponse;
import com.example.wefix.ui.PaymentActivity;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Category> categoryList;
    //    private ImageSlider imageSlider;
    private ProgressDialog progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        progressBar = new ProgressDialog(getActivity());
        progressBar.show();
        progressBar.setContentView(R.layout.progress_dialog);
        Objects.requireNonNull(progressBar.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        return inflater.inflate(R.layout.fragment_display, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        Call<CategoryResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCategory1();

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
//                            Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                            DisplayCategoryAdapter adapter = new DisplayCategoryAdapter(getContext(), categoryList, "Display");
                            recyclerView.setAdapter(adapter);
                            call.cancel();

                        } else {
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                        progressBar.dismiss();
                    }

                    @Override
                    public void onFailure(Call<CategoryResponse> call, Throwable t) {
                        progressBar.dismiss();
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

    }
}