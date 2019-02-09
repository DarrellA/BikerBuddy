package net.aucutt.bikerbuddy.sunrisenetwork;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import net.aucutt.bikerbuddy.MainActivity;

import io.reactivex.Observable;
import io.reactivex.Observer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Controller extends Observable<Result> {

    static final private String BASE_URL = "https://api.sunrise-sunset.org";


    public Controller() {
        super();
    }

    @Override
    protected void subscribeActual(Observer<? super Result> observer) {
        Log.d(MainActivity.TAG, "Who called me?");
    }

    public Observable<Result>  start () {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        SunriseSunsetAPI sunAPI = retrofit.create(SunriseSunsetAPI.class);

        return sunAPI.loadSunriseSunset();
    }



}
