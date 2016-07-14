package ac42886.austinallergyalert;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class AustinAllergyAlert extends AppCompatActivity {

    private static final String TAG = "AUSTIN ALLERGY ALERT:";
    private static final int SETTINGS_REQUEST = 1;

    private AllergyAlertClass mAllergy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_austin_allergy_alert);
        TabHostProvider tabProvider = new MyTabHostProvider(AustinAllergyAlert.this);
        TabView tabView = tabProvider.getTabHost("Home");
        tabView.setCurrentView(R.layout.activity_austin_allergy_alert);
        setContentView(tabView.render(0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fm = getFragmentManager();
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, SETTINGS_REQUEST);
                return true;
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refreshData()
    {
        // Call API to get allergen count and update fields

    }

}
