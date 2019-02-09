package net.aucutt.bikerbuddy.sunrisenetwork;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface SunriseSunsetAPI {

    @GET("/json?lat=47.581009&lng=-122.335738&date=today&formatted=0")
    Observable<Result> loadSunriseSunset();

}
