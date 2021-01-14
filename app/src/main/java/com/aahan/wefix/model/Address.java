package com.aahan.wefix.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("billing_id")
    @Expose
    private int billingId;
    @SerializedName("billing_name")
    @Expose
    private String billingName;
    @SerializedName("billing_address")
    @Expose
    private String billingAddress;
    @SerializedName("billing_city")
    @Expose
    private String billingCity;
    @SerializedName("zip_code")
    @Expose
    private String zipCode;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("mb_no")
    @Expose
    private String mbNo;
    @SerializedName("email")
    @Expose
    private String email;

    public Address(int billingId, String billingName, String billingAddress, String billingCity, String zipCode, String state, String country, String mbNo, String email) {
        this.billingId = billingId;
        this.billingName = billingName;
        this.billingAddress = billingAddress;
        this.billingCity = billingCity;
        this.zipCode = zipCode;
        this.state = state;
        this.country = country;
        this.mbNo = mbNo;
        this.email = email;
    }

    public int getBillingId() {
        return billingId;
    }

    public void setBillingId(int billingId) {
        this.billingId = billingId;
    }

    public String getBillingName() {
        return billingName;
    }

    public void setBillingName(String billingName) {
        this.billingName = billingName;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getBillingCity() {
        return billingCity;
    }

    public void setBillingCity(String billingCity) {
        this.billingCity = billingCity;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMbNo() {
        return mbNo;
    }

    public void setMbNo(String mbNo) {
        this.mbNo = mbNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
