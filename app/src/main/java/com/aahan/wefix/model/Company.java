package com.aahan.wefix.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Company {

    @SerializedName("tbl_company_id")
    @Expose
    private Integer tblCompanyId;
    @SerializedName("tbl_company_name")
    @Expose
    private String tblCompanyName;
    @SerializedName("status")
    @Expose
    private String status;

    public Company(Integer tblCompanyId, String tblCompanyName, String status) {
        this.tblCompanyId = tblCompanyId;
        this.tblCompanyName = tblCompanyName;
        this.status = status;
    }

    public Integer getTblCompanyId() {
        return tblCompanyId;
    }

    public void setTblCompanyId(Integer tblCompanyId) {
        this.tblCompanyId = tblCompanyId;
    }

    public String getTblCompanyName() {
        return tblCompanyName;
    }

    public void setTblCompanyName(String tblCompanyName) {
        this.tblCompanyName = tblCompanyName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
