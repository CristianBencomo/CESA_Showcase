package com.example.cesamockv3;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface CarMDApi {
    @Headers({"content-type: application/json",
            "authorization: Basic YWM2MTc3NmQtYTgzYy00ODhjLThmOTEtNzFmZDBiOTUyNTE5",
            "partner-token: 0cc624151a5148eea7380230db20c6ed"})
    @GET("decode")
    Call<VinDecode> getDecode(@Query("vin") String vin);
}
