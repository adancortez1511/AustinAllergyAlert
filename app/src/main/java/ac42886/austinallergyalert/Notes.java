package ac42886.austinallergyalert;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Adan on 7/7/16.
 */
public class Notes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TabHostProvider tabProvider = new MyTabHostProvider(Notes.this);
        TabView tabView = tabProvider.getTabHost("Notes");
        tabView.setCurrentView(R.layout.activity_notes);
        setContentView(tabView.render(3));
    }
}
