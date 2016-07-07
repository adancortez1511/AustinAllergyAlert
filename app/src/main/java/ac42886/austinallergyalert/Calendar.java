package ac42886.austinallergyalert;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Adan on 7/5/16.
 */
public class Calendar extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TabHostProvider tabProvider = new MyTabHostProvider(Calendar.this);
        TabView tabView = tabProvider.getTabHost("Calendar");
        tabView.setCurrentView(R.layout.activity_calendar);
        setContentView(tabView.render(1));
    }
}
