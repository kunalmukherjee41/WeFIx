package com.example.wefix.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Service {

    @SerializedName("tbl_services_id")
    @Expose
    private int tblServicesId;
    @SerializedName("cat_ref_id")
    @Expose
    private int catRefId;
    @SerializedName("tbl_services_name")
    @Expose
    private String tblServicesName;
    @SerializedName("tbl_services_des")
    @Expose
    private String tblServicesDes;
    @SerializedName("tbl_services_charge")
    @Expose
    private int tblServicesCharge;
    @SerializedName("status")
    @Expose
    private String status;

    public Service(int tblServicesId, int catRefId, String tblServicesName, String tblServicesDes, int tblServicesCharge, String status) {
        this.tblServicesId = tblServicesId;
        this.catRefId = catRefId;
        this.tblServicesName = tblServicesName;
        this.tblServicesDes = tblServicesDes;
        this.tblServicesCharge = tblServicesCharge;
        this.status = status;
    }

    public int getTblServicesId() {
        return tblServicesId;
    }

    public void setTblServicesId(int tblServicesId) {
        this.tblServicesId = tblServicesId;
    }

    public int getCatRefId() {
        return catRefId;
    }

    public void setCatRefId(int catRefId) {
        this.catRefId = catRefId;
    }

    public String getTblServicesName() {
        return tblServicesName;
    }

    public void setTblServicesName(String tblServicesName) {
        this.tblServicesName = tblServicesName;
    }

    public String getTblServicesDes() {
        return tblServicesDes;
    }

    public void setTblServicesDes(String tblServicesDes) {
        this.tblServicesDes = tblServicesDes;
    }

    public int getTblServicesCharge() {
        return tblServicesCharge;
    }

    public void setTblServicesCharge(int tblServicesCharge) {
        this.tblServicesCharge = tblServicesCharge;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
