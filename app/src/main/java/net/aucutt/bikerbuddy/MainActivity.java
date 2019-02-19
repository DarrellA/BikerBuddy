package net.aucutt.bikerbuddy;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.aucutt.bikerbuddy.sunrisenetwork.Controller;
import net.aucutt.bikerbuddy.sunrisenetwork.Result;
import net.aucutt.bikerbuddy.util.DateTimeAndTempUtil;
import net.aucutt.bikerbuddy.util.TextWeatherToImageUtil;
import net.aucutt.bikerbuddy.weathernetwork.ForecastData;
import net.aucutt.bikerbuddy.weathernetwork.List;
import net.aucutt.bikerbuddy.weathernetwork.WeatherController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 * https://api.sunrise-sunset.org/json?lat=47.581009&lng=-122.335738&date=today
 */
public class MainActivity extends Activity  {

    public  final static String TAG = "Darrell";
    public enum CommuteTimes  {Evening, NextMorning, NextEvening, Morning};
    private TextView sunset;
    private TextView currentTime;
    private Button updateButton;
    private Observable<Result> observable;
    private Observable<ForecastData> weatherObservable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sunset = findViewById(R.id.sunsetText);
        currentTime = findViewById(R.id.currentTime);
        updateButton = findViewById(R.id.updateButton);
        currentTime.setText(DateTimeAndTempUtil.getCurrentTimeFormatted());

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSunset();
        updateForecast();

        Observable.interval(30, TimeUnit.SECONDS)
                .timeInterval()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( what->displayTime(what));
    }

    public void attemptToUpdate(View view) {
        updateSunset();
    }

    private void updateSunset() {
        Controller controller = new Controller();
        observable= controller.start();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable->onError(throwable))
                .subscribe(result->parseResponse(result));
    }

    private void parseResponse(Result response) {
        String sunSet = response.getResults().getSunset();
        String output = DateTimeAndTempUtil.UITToPST(sunSet);
        Log.d(TAG, sunSet + "  " + output);
        sunset.setText(getString(R.string.sunset) + output);
        observable.unsubscribeOn(Schedulers.io());
        Log.d(TAG,DateTimeAndTempUtil.getCurrentDateFormatted() );
        updateButton.setText( DateTimeAndTempUtil.getCurrentDateFormatted());
    }

    private void onError(Throwable t) {
       Log.e(TAG, "you got errors, son : " +  t.getLocalizedMessage());
        observable.unsubscribeOn(Schedulers.io());
        updateButton.setText( t.getLocalizedMessage());
    }

    private void displayTime(io.reactivex.schedulers.Timed timed) {
        currentTime.setText(DateTimeAndTempUtil.getCurrentTimeFormatted());

    }

    private void updateForecast() {
        WeatherController controller = new WeatherController();
        weatherObservable = controller.start();
        weatherObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable->onError(throwable))
                .subscribe(result->parseForecastResponse(result));
    }

    private  void parseForecastResponse(ForecastData forecastData) {

        Log.d(TAG, forecastData.getCity().getName() + " " + forecastData.getList().size() + "  " + System.currentTimeMillis() / 1000 + "   " + System.currentTimeMillis() + " " + DateTimeAndTempUtil.getSecondsPastEpochFormatted((int) (System.currentTimeMillis() / 1000)));
        HashMap<CommuteTimes, List> desiredList =   new HashMap<>();
        Iterator<List> iterator = forecastData.getList().iterator();
        do {
            List dataList = iterator.next();
            switch (DateTimeAndTempUtil.getHourPastEpoch(dataList.getDt())) {


                case "16":
                    if (!desiredList.containsKey(CommuteTimes.Evening)) {
                        desiredList.put(CommuteTimes.Evening, dataList);
                    } else  if (!desiredList.containsKey(CommuteTimes.NextEvening)) {
                        desiredList.put(CommuteTimes.NextEvening, dataList);
                    }
                    break;
                case "07":
                    if (!desiredList.containsKey(CommuteTimes.Morning)) {
                        desiredList.put(CommuteTimes.Morning, dataList);
                    } else if (!desiredList.containsKey(CommuteTimes.NextMorning)) {
                        desiredList.put(CommuteTimes.NextMorning, dataList);
                    }

            }
        } while (iterator.hasNext());

        for ( CommuteTimes commute : CommuteTimes.values() ) {
            List check = desiredList.get(commute);
            Log.d(TAG, commute.name() + "  " + DateTimeAndTempUtil.kelvinToFarenheit(check.getMain().getTemp()) + " " + check.getWeather().get(0).getMain() + "  " + DateTimeAndTempUtil.getSecondsPastEpochFormatted(check.getDt()));
        }
        updateWeather(desiredList);

    }

    private void updateWeather( HashMap<CommuteTimes, List> data) {
        //have are data, the only thing we don't know is we display evening first or morning.
        ArrayList<List> order =  new ArrayList<>(3);

        if ( data.get(CommuteTimes.Evening).getDt() <  data.get(CommuteTimes.Morning).getDt()) {
            //first is evening
            order.add(data.get(CommuteTimes.Evening));
            order.add(data.get(CommuteTimes.Morning));
            order.add(data.get(CommuteTimes.NextEvening));

        } else {
            order.add(data.get(CommuteTimes.Morning));
            order.add(data.get(CommuteTimes.Evening));
            order.add(data.get(CommuteTimes.NextMorning));

        }
        for ( List check : order) {
            Log.d(TAG, DateTimeAndTempUtil.kelvinToFarenheit(check.getMain().getTemp()) + " " + check.getWeather().get(0).getMain() + "  " + DateTimeAndTempUtil.getSecondsPastEpochFormatted(check.getDt()));

        }
        //forecast.setBackground( getDrawable(TextWeatherToImageUtil.getDrawableId(order.get(0).getWeather().get(0).getMain())));
        updateWidget(order.get(0),  findViewById(R.id.forecast), findViewById(R.id.forecastText));
        updateWidget(order.get(1),  findViewById(R.id.forecast1), findViewById(R.id.forecastText1));
        updateWidget(order.get(2),  findViewById(R.id.forecast2), findViewById(R.id.forecastText2));


    }

    private void updateWidget( List data, ImageView forecastImage, TextView forecastText ) {
        Log.d(TAG, TextWeatherToImageUtil.getDrawableId(data.getWeather().get(0).getMain())  +  "  "  +  DateTimeAndTempUtil.kelvinToFarenheit(data.getMain().getTemp()).toString()  +  "  " +  DateTimeAndTempUtil.getHoursPastEpochFormatted(data.getDt()) );
        forecastImage.setBackground( getDrawable(TextWeatherToImageUtil.getDrawableId(data.getWeather().get(0).getMain())));
        forecastText.setText( DateTimeAndTempUtil.kelvinToFarenheit(data.getMain().getTemp()).toString()   + "\n" +  data.getWeather().get(0).getMain() +
                "\n"  +  DateTimeAndTempUtil.getHoursPastEpochFormatted(data.getDt()));
    }

}
