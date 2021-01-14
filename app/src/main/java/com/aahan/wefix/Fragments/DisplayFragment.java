package com.aahan.wefix.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aahan.wefix.Api.RetrofitClient;
import com.aahan.wefix.R;
import com.aahan.wefix.adapter.DisplayCategoryAdapter;
import com.aahan.wefix.model.Category;
import com.aahan.wefix.model.CategoryResponse;

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
    private DisplayCategoryAdapter adapter;

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

        getLog();

        SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null)
                    adapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    void getLog() {
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
                            recyclerView.setItemViewCacheSize(categoryList.size());
                            adapter = new DisplayCategoryAdapter(getContext(), categoryList, "Display");
                            adapter.setHasStableIds(true);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                            call.cancel();

                        } else {
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                        progressBar.dismiss();
                    }

                    @Override
                    public void onFailure(Call<CategoryResponse> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.dismiss();
                    }
                }
        );
    }
}