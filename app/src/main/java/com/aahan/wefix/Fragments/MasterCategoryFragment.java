package com.aahan.wefix.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aahan.wefix.Api.RetrofitClient;
import com.aahan.wefix.R;
import com.aahan.wefix.adapter.MasterCategoryAdapter;
import com.aahan.wefix.model.Master;
import com.aahan.wefix.model.MasterResponse;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasterCategoryFragment extends Fragment {

    private List<Master> masterList = new ArrayList<>();
    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView recyclerView;
    private MasterCategoryAdapter adapter;
    private SearchView searchView;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_master_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        recyclerView = view.findViewById(R.id.recycler_view);
        refreshLayout = view.findViewById(R.id.refresh_layout);
        recyclerView.setVisibility(View.GONE);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        searchView = view.findViewById(R.id.search_view);

        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();
        new FetchMasterCategory().execute();

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
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
                }
        );
        refreshLayout.setOnRefreshListener(
                () -> {
                    shimmerFrameLayout.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.startShimmerAnimation();
                    recyclerView.setVisibility(View.GONE);
                    new FetchMasterCategory().execute();
                    refreshLayout.setRefreshing(false);
                }
        );

    }

    class FetchMasterCategory extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            masterList.clear();
            masterList = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(String... strings) {

            Call<MasterResponse> call = RetrofitClient.getInstance().getApi().getAllMasterCategory();

            call.enqueue(
                    new Callback<MasterResponse>() {
                        @Override
                        public void onResponse(Call<MasterResponse> call, Response<MasterResponse> response) {
                            if (response.isSuccessful()) {
                                if (response.body() != null)
                                    setServerData(response.body());
                                Log.i("Abc", response.body().getMessage());
                            } else {
                                Log.i("Abc", "Response has Error");
                            }
                        }

                        @Override
                        public void onFailure(Call<MasterResponse> call, Throwable t) {
                            Log.i("Abc", "OnFailure");
                        }
                    }
            );

            return null;
        }
    }

    private void setServerData(MasterResponse body) {
        shimmerFrameLayout.stopShimmerAnimation();
        shimmerFrameLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        masterList = body.getMasterList();
        recyclerView.setItemViewCacheSize(masterList.size());
        adapter = new MasterCategoryAdapter(masterList, getActivity());
        recyclerView.setAdapter(adapter);

        Log.i("ABC", Arrays.toString(masterList.toArray()) + " ");
    }

}