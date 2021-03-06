package com.aahan.wefix.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aahan.wefix.Api.RetrofitClient;
import com.aahan.wefix.R;
import com.aahan.wefix.model.Address;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.ListViewHolder> {

    Context mContext;
    List<Address> addressList;

    public AddressListAdapter(Context mContext, List<Address> addressList) {
        this.mContext = mContext;
        this.addressList = addressList;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.address_list_recycler_view, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {

        Address address = addressList.get(position);
        holder.name.setText(address.getBillingName());
        holder.phoneNumber.setText(address.getMbNo());
        String a = address.getBillingAddress() + ", " + address.getBillingCity() + ", " + address.getState() + ", "
                + address.getCountry() + ",\n" + address.getZipCode();
        holder.address.setText(a);

        holder.del.setOnClickListener(
                v -> {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage("Are you want Cancel the Call Log?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", (dialog, id) -> {

                                holder.del.setBackgroundColor(mContext.getResources().getColor(R.color.btn));
                                Address add = addressList.get(position);
                                Call<ResponseBody> call = RetrofitClient
                                        .getInstance()
                                        .getApi()
                                        .deleteaddress(add.getBillingId());

                                call.enqueue(
                                        new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.isSuccessful()) {
                                                    Toast.makeText(mContext, "Delete Address", Toast.LENGTH_SHORT).show();
                                                    addressList.remove(position);
                                                    notifyDataSetChanged();
//                                        ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new AddAddressFragment()).commit();
                                                } else {
                                                    Toast.makeText(mContext, "Try Again After Some Time", Toast.LENGTH_SHORT).show();
                                                }
                                                holder.del.setBackground(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.custom_btn2, null));
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                                                holder.del.setBackground(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.custom_btn2, null));

                                            }
                                        }
                                );
                            })
                            .setNegativeButton("No", (dialog, id) -> dialog.cancel());
                    AlertDialog alert = builder.create();
                    alert.show();
                }
        );
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        TextView name, phoneNumber, address;
        Button del;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            phoneNumber = itemView.findViewById(R.id.phone_number);
            address = itemView.findViewById(R.id.address);
            del = itemView.findViewById(R.id.delete_btn);
        }
    }
}
