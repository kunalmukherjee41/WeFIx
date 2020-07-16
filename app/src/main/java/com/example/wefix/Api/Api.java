package com.example.wefix.Api;

import com.example.wefix.model.CategoryResponse;
import com.example.wefix.model.ServiceResponse;
import com.example.wefix.model.UserResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api {

    @FormUrlEncoded
    @POST("createuser")
    Call<ResponseBody> createUser(
            @Field("username") String username,
            @Field("password") String password,
            @Field("name") String name,
            @Field("designation") String designation,
            @Field("phone") String phone,
            @Field("last_login") String last_login,
            @Field("field") String field

            //$username, $password, $name, $designation, $phone, $last_login, $field

    );

    @GET("getcategory")
    Call<CategoryResponse> getCategory1();

    @FormUrlEncoded
    @POST("userlogin")
    Call<UserResponse> userLogin(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @PUT("updateuser/{id}")
    Call<ServiceResponse> updateUser(
            @Path("id") int id
    );

}
