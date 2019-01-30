package net.aucutt.bikerbuddy.network;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SunriseSunsetAPI {

    @GET("/json?lat=47.581009&lng=-122.335738&date=today&formatted=0")
    Call<Result> loadSunriseSunset();

}
