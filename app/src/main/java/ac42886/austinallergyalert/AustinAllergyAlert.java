package ac42886.austinallergyalert;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
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
    private DatabaseHelper dbHelper;
    private AllergenService allergenService;
    private Date todaysDate;
    private Date lastDateLogged;
    private List<Allergen> allergens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_austin_allergy_alert);
        TabHostProvider tabProvider = new MyTabHostProvider(AustinAllergyAlert.this);
        TabView tabView = tabProvider.getTabHost("Home");
        tabView.setCurrentView(R.layout.activity_austin_allergy_alert);
        setContentView(tabView.render(0));

        // restore preferences
        setVarsFromSharedPrefs();

        // get today's allergen counts if needed
        dbHelper = new DatabaseHelper(this);
        allergenService = new AllergenService();
        todaysDate = getDate(new Date());
        if(todaysDate.compareTo(lastDateLogged) == 0) {
            // read allergens from the database
            Log.d("onCreate ", "reading allergens from database");
            allergens = dbHelper.getAllergensByDate(lastDateLogged);
        } else {
            // fetch today's allergens
            try {
                refreshData();
            } catch (Exception e) {
                Log.e("ERROR:onCreate ", e.toString() + " exception thrown trying allergenService.execute()");
            }
        }
        Log.d("onCreate ", "allergens = " + Arrays.toString(allergens.toArray()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("lastDateLogged", lastDateLogged.toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        try {
            lastDateLogged = df.parse(savedInstanceState.getString("lastDateLogged"));
        } catch (ParseException e) {
            lastDateLogged = new Date(93, 6, 30);
            Log.e("ERROR:onRestore ", "error parsing date, setting to 07/30/93");
        }
        Log.d("onRestore ", "lastDateLogged = " + lastDateLogged.toString());
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences sharedPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putString("lastDateLogged", lastDateLogged.toString());
        Log.d("onStop ", "lastDateLogged = " + lastDateLogged.toString());
        editor.apply();
    }

    private void refreshData() throws ExecutionException, InterruptedException {
        allergens = new ArrayList<Allergen>();

        // Call API to get allergen count and update fields
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            allergenService = new AllergenService();
            allergens = allergenService.execute().get();
        } else {
            Log.e("ERROR:refreshData ", "No network connection available.");
        }

        // insert allergens into database
        Log.d("refreshData ", "Inserting allergens into table");
        for (Allergen a : allergens)
            dbHelper.insertAllergen(a);
        Log.d("refreshData ", Arrays.toString(allergens.toArray()));

        // update lastDateLogged
        lastDateLogged = getDate(new Date());
    }

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


    // simple method to get the day's date in YYMMDD form
    public Date getDate(Date date) {
        return new Date(date.getYear(), date.getMonth(), date.getDate());
    }

    private void setVarsFromSharedPrefs() {
        SharedPreferences sharedPrefs = getPreferences(MODE_PRIVATE);

        // restore lastDateLogged
        // if lastDateLogged has never been saved, set it to some arbitrary date in the past
        String dateString = sharedPrefs.getString("lastDateLogged", "X");
        if(dateString.equals("X")) {
            lastDateLogged = new Date(93, 6, 30);
        } else {
            DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            try {
                Log.d("setVars ", "dateString = " + dateString);
                lastDateLogged = df.parse(dateString);
            } catch (ParseException e) {
                lastDateLogged = new Date(93, 6, 30);
                Log.e("ERROR:setVars ", "error parsing date, setting date to 07/30/93");
            }
        }
        Log.d("setVars ", "lastDateLogged = " + lastDateLogged.toString());
    }

}
