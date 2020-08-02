package com.example.wefix.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Address1Response {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("address")
    @Expose
    private List<Address> addressList;

    public Address1Response(Boolean error, List<Address> addressList) {
        this.error = error;
        this.addressList = addressList;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }
}
