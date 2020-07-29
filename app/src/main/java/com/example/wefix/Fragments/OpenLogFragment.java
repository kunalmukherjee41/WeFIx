package com.example.wefix.Fragments;

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

import com.example.wefix.Api.RetrofitClient;
import com.example.wefix.R;
import com.example.wefix.adapter.LogHistoryAdapter;
import com.example.wefix.model.LogResponse;
import com.example.wefix.model.Logs;
import com.example.wefix.storage.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpenLogFragment extends Fragment {

    List<Logs> logsList;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_open_log, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        getLog();

        return view;
    }

    public void getLog() {

        int client_ref_id = SharedPrefManager.getInstance(getActivity()).getUser().getId();

        Call<LogResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCallLog(client_ref_id, "app");

        call.enqueue(
                new Callback<LogResponse>() {
                    @Override
                    public void onResponse(Call<LogResponse> call, Response<LogResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            logsList = response.body().getLog();
                            List<Logs> logs = new ArrayList<>();
                            for (Logs logs1 : logsList) {
                                if (logs1.getCallLogStatus().equals("OPEN")) {
                                    logs.add(logs1);
                                }
                            }
                            LogHistoryAdapter adapter = new LogHistoryAdapter(getActivity(), logs);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(getActivity(), "Something went wrong try Again", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LogResponse> call, Throwable t) {

                    }
                }
        );
    }

}
