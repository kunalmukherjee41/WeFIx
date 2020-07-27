package com.example.wefix.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Company1Response {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("company")
    @Expose
    private Company company;

    public Company1Response(Boolean error, Company company) {
        this.error = error;
        this.company = company;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
