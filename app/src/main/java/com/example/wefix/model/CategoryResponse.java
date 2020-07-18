package com.example.wefix.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("category")
    @Expose
    private List<Category> category;

    public CategoryResponse(Boolean error, List<Category> category) {
        this.error = error;
        this.category = category;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

}
