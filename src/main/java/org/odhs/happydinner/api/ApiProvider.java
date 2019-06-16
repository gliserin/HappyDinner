package org.odhs.happydinner.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.odhs.happydinner.listener.ApiCallback;
import retrofit2.*;

import retrofit2.converter.gson.GsonConverterFactory;

public class ApiProvider {

    public static final String DIMIGOIN_URL = "https://dev-api.dimigo.in/";

    public static DimigoinApi dimigoinApi() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DIMIGOIN_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(DimigoinApi.class);
    }

    public static <T> void execute(Call<T> call, ApiCallback<T> callback) {

        Response<T> response;

        try {
            response = call.execute();
            if(response.code() != 200) {
                callback.onFail(new Throwable("Error Code : " + response.code()));
                return;
            }

            if(response.body() != null) {
                callback.onSuccess(response.body());
            } else {
                callback.onFail(new Throwable("I dont know about error."));
            }

        } catch(Throwable t) {
            t.printStackTrace();
            callback.onFail(t);
        }
    }
}
