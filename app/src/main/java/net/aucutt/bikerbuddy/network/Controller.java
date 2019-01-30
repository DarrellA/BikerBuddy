package net.aucutt.bikerbuddy.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.aucutt.bikerbuddy.CallbackInterface;
import net.aucutt.bikerbuddy.util.DateTimeUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Controller implements Callback<Result> {

    static final private String BASE_URL = "https://api.sunrise-sunset.org";
    private final static String TAG = "Darrell";
    private CallbackInterface callback;

    public Controller(CallbackInterface callback) {
        super();
        this.callback = callback;
    }

    public void start () {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        SunriseSunsetAPI sunAPI = retrofit.create(SunriseSunsetAPI.class);

        Call<Result> call = sunAPI.loadSunriseSunset();
        call.enqueue(this);
    }


    @Override
    public void onResponse(Call<Result> call, Response<Result> response) {
        String output;
        if (response.isSuccessful()) {
            String sunSet =  response.body().getResults().getSunset();
            output =  DateTimeUtil.UITToPST(sunSet);
            Log.d(TAG, sunSet +  "  "  + output);

        } else {
            Log.e(TAG, String.valueOf(response.errorBody()));
            output = String.valueOf(response.errorBody());
        }
        callback.onResult(output);


    }

    @Override
    public void onFailure(Call<Result> call, Throwable t) {
        Log.e(TAG, "whoops " + t);
       t.printStackTrace();
        callback.onResult(t.getMessage());
    }
}
