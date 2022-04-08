package com.example.cesamockv3;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface CarMDApi {
    @Headers({"content-type: application/json",
            "authorization: Basic ENTERTOKEN",
            "partner-token: ENTERTOKEN"})
    @GET("decode")
    Call<VinDecode> getDecode(@Query("vin") String vin);
}
