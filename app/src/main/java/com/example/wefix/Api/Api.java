package com.example.wefix.Api;

import com.example.wefix.model.Address1Response;
import com.example.wefix.model.AddressResponse;
import com.example.wefix.model.Category1Response;
import com.example.wefix.model.CategoryResponse;
import com.example.wefix.model.Company1Response;
import com.example.wefix.model.CompanyResponse;
import com.example.wefix.model.LogResponse;
import com.example.wefix.model.PartsResponse;
import com.example.wefix.model.Service1Response;
import com.example.wefix.model.ServiceResponse;
import com.example.wefix.model.User;
import com.example.wefix.model.UserResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
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

    @FormUrlEncoded
    @POST("addaddress")
    Call<ResponseBody> addAddress(
            @Field("billing_name") String billing_name,
            @Field("billing_address") String billing_address,
            @Field("billing_city") String billing_city,
            @Field("zip_code") String zip_code,
            @Field("mb_no") String mb_no,
            @Field("email") String email,
            @Field("ref_id") int ref_id

//            'billing_name', 'billing_address', 'billing_city', 'zip_code', 'mb_no', 'email', 'ref_id'
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
    @PUT("getuserbyemail")
    Call<UserResponse> getUserByEmail(
            @Field("username") String username,
            @Field("password") String password
    );

    @PUT("getcompanybyid/{tbl_company_id}")
    Call<Company1Response> getCompanyByID(@Path("tbl_company_id") int tbl_company_id);

    @PUT("updatecalllog/{call_log_id}")
    Call<ResponseBody> updateCallLog(@Path("call_log_id") int call_log_id);

    @PUT("getaddress/{ref_id}")
    Call<AddressResponse> getAddress(@Path("ref_id") int ref_id);

    @DELETE("deleteaddress/{billing_id}")
    Call<ResponseBody> deleteaddress(
            @Path("billing_id") int billing_id
    );

    @PUT("getalladdress/{ref_id}")
    Call<Address1Response> getAllAddress(@Path("ref_id") int ref_id);

    @PUT("getservice/{id}")
    Call<Service1Response> getService(@Path("id") int id);

    @PUT("getcalllog/{client_ref_id}")
    Call<LogResponse> getCallLog(@Path("client_ref_id") int client_ref_id);

    @FormUrlEncoded
    @PUT("updatuserfid")
    Call<ResponseBody> updateFirebaseID(
            @Field("firebaseID") String firebaseID,
            @Field("username") String username
    );

    @PUT("getcategorybyid/{tbl_category_id}")
    Call<Category1Response> getCategoryByID(@Path("tbl_category_id") int tbl_category_id);

    @GET("getallservice")
    Call<ServiceResponse> getAllServices();

    @GET("getcompany")
    Call<CompanyResponse> getCompany();

    @PUT("getparts/{ref_log_id}")
    Call<PartsResponse> getParts(@Path("ref_log_id") int ref_log_id);

    @FormUrlEncoded
    @POST("addcalllog")
    Call<ResponseBody> addCallLog(
            @Field("call_log_date") String call_log_date,  //1
            @Field("call_log_type") String call_log_type,  //2
            @Field("client_ref_id") int client_ref_id,    //3
            @Field("client_name") String client_name,    //4
            @Field("client_address") String client_address,//5
            @Field("client_pin") String client_pin,  //6
            @Field("client_mb") String client_mb,   //7
            @Field("client_email") String client_email, //8
            @Field("ref_cat_id") int ref_cat_id,       //9
            @Field("ref_service_id") int ref_service_id, //10
            @Field("ref_company_id") int ref_company_id,  //11
            @Field("product_company") String product_company,
            @Field("amount") double amount,               //12
            @Field("payment_type") String payment_type,
            @Field("problem") String problem,          //13
            @Field("entry_tim") String entry_tim,     //14
            @Field("call_log_status") String call_log_status,  //15
            @Field("client_log_ip") String client_log_ip

            //$call_log_date, $call_log_type, $client_ref_id, $client_name, $client_address,
            //$client_pin, $client_mb, $client_email, $ref_cat_id, $ref_service_id, $ref_company_id,
            // $amount, $problem, $entry_tim, $call_log_status, $client_log_ip, $client_log_timezone
    );

}
