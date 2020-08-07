package com.example.wefix.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("name")
    @Expose
    private String name = null;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("last_login")
    @Expose
    private String lastLogin;
    @SerializedName("field")
    @Expose
    private String field;

    public User(int id, String username, String name, String designation, String phone, String lastLogin, String field) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.designation = designation;
        this.phone = phone;
        this.lastLogin = lastLogin;
        this.field = field;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

}
