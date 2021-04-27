package com.aahan.wefix.model;

public class Master {

    private int tbl_mater_category_id;
    private String tbl_mater_category_name;
    private String tbl_mater_category_des;
    private String tbl_mater_category_image;
    private String status;

    public Master() {
    }

    public Master(int tbl_mater_category_id, String tbl_mater_category_name, String tbl_mater_category_des, String tbl_mater_category_image, String status) {
        this.tbl_mater_category_id = tbl_mater_category_id;
        this.tbl_mater_category_name = tbl_mater_category_name;
        this.tbl_mater_category_des = tbl_mater_category_des;
        this.tbl_mater_category_image = tbl_mater_category_image;
        this.status = status;
    }

    public int getTbl_mater_category_id() {
        return tbl_mater_category_id;
    }

    public void setTbl_mater_category_id(int tbl_mater_category_id) {
        this.tbl_mater_category_id = tbl_mater_category_id;
    }

    public String getTbl_mater_category_name() {
        return tbl_mater_category_name;
    }

    public void setTbl_mater_category_name(String tbl_mater_category_name) {
        this.tbl_mater_category_name = tbl_mater_category_name;
    }

    public String getTbl_mater_category_des() {
        return tbl_mater_category_des;
    }

    public void setTbl_mater_category_des(String tbl_mater_category_des) {
        this.tbl_mater_category_des = tbl_mater_category_des;
    }

    public String getTbl_mater_category_image() {
        return tbl_mater_category_image;
    }

    public void setTbl_mater_category_image(String tbl_mater_category_image) {
        this.tbl_mater_category_image = tbl_mater_category_image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
