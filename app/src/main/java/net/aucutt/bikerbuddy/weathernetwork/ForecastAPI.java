package net.aucutt.bikerbuddy.weathernetwork;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ForecastAPI {

   @GET("/data/2.5/forecast?q=Seattle,US&appid=10621a665b0ee80a9396ee743aea7ee3")
   Observable<ForecastData> getForecast();

}
