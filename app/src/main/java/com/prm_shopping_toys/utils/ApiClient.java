package com.prm_shopping_toys.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    //    URL ở nhà
    private static final String BASE_URL = "http://192.168.2.10/shopping_toys_api/";

//    URL ở NVH
//    private static final String BASE_URL = "http://192.168.137.37/shopping_toys_api/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

