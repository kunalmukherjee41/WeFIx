package com.example.wefix.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wefix.R;
import com.example.wefix.model.Address;

import java.util.List;

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
                + address.getCountry() + ",\n-" + address.getZipCode();
        holder.address.setText(a);

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
