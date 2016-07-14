package ac42886.austinallergyalert;

import android.app.Application;
import android.os.SystemClock;
import java.util.concurrent.TimeUnit;

/**
 * Created by Adan on 7/11/16.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Don't do this! This is just so cold launches take some time
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(5));
    }
}