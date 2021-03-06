package com.aahan.wefix.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aahan.wefix.Api.RetrofitClient;
import com.aahan.wefix.R;
import com.aahan.wefix.adapter.LogHistoryAdapter;
import com.aahan.wefix.model.LogResponse;
import com.aahan.wefix.model.Logs;
import com.aahan.wefix.storage.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancelledLogFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Logs> logsList;

    private ProgressDialog progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_cancelled_log, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        getLog();

    }

    public void getLog() {

        progressBar = new ProgressDialog(getContext());
        progressBar.show();
        progressBar.setContentView(R.layout.progress_dialog);
        Objects.requireNonNull(progressBar.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        int client_ref_id = SharedPrefManager.getInstance(getActivity()).getUser().getId();

        Call<LogResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCallLog(client_ref_id);

        call.enqueue(
                new Callback<LogResponse>() {
                    @Override
                    public void onResponse(Call<LogResponse> call, Response<LogResponse> response) {
//                        logsList.clear();
                        if (response.isSuccessful()) {
                            if (logsList != null) {
                                logsList.clear();
                            }
                            progressBar.dismiss();
                            assert response.body() != null;
                            logsList = response.body().getLog();
                            List<Logs> logs = new ArrayList<>();
                            for (Logs logs1 : logsList) {
                                if (logs1.getCallLogStatus().toUpperCase().equals("CANCEL") || logs1.getCallLogStatus().toUpperCase().equals("REJECT")) {
                                    logs.add(logs1);
                                }
                            }
                            recyclerView.setItemViewCacheSize(logs.size());
                            LogHistoryAdapter adapter = new LogHistoryAdapter(getActivity(), logs);
                            adapter.setHasStableIds(true);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                        } else {
                            progressBar.dismiss();
                            Toast.makeText(getActivity(), "Something went wrong try Again", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LogResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.dismiss();

                    }
                }
        );
    }
}
