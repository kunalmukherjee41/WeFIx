package com.example.wefix.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompanyResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("company")
    @Expose
    private List<Company> company;

    public CompanyResponse(Boolean error, List<Company> company) {
        this.error = error;
        this.company = company;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<Company> getCompany() {
        return company;
    }

    public void setCompany(List<Company> company) {
        this.company = company;
    }
}
