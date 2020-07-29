package com.example.wefix.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("address")
    @Expose
    private Address address;

    public AddressResponse(Boolean error, Address address) {
        this.error = error;
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }
}
