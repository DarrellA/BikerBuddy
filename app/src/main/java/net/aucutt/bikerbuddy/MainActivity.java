package net.aucutt.bikerbuddy;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import net.aucutt.bikerbuddy.network.Controller;
import net.aucutt.bikerbuddy.network.Result;
import net.aucutt.bikerbuddy.util.DateTimeUtil;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

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

    private TextView sunset;
    private  Observable<Result> observable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sunset = findViewById(R.id.sunsetText);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        String output = DateTimeUtil.UITToPST(sunSet);
        Log.d(TAG, sunSet + "  " + output);
        sunset.setText(getString(R.string.sunset) + output);
        observable.unsubscribeOn(Schedulers.io());
    }

    private void onError(Throwable t) {
        sunset.setText(t.getLocalizedMessage());
        observable.unsubscribeOn(Schedulers.io());
    }
}
