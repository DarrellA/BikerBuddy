package net.aucutt.bikerbuddy.weathernetwork;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import io.reactivex.Observer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherController extends Observable<ForecastData> {

    static final private String BASE_URL = "https://api.openweathermap.org";

    @Override
    protected void subscribeActual(Observer<? super ForecastData> observer) {

    }

    public Observable<ForecastData>  start () {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        ForecastAPI forecastAPI = retrofit.create(ForecastAPI.class);

        return forecastAPI.getForecast();
    }
}
