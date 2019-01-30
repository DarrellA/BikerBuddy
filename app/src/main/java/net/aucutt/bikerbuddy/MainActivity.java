package net.aucutt.bikerbuddy;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import net.aucutt.bikerbuddy.network.Controller;

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
public class MainActivity extends Activity implements CallbackInterface {

    private TextView sunset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sunset = findViewById(R.id.sunsetText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Controller controller = new Controller(this);
        controller.start();
    }

    @Override
    public void onResult(String sunsetTime) {
        sunset.setText(getString(R.string.sunset) + sunsetTime);
    }
}
