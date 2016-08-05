package ac42886.austinallergyalert;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ofl7_000 on 7/24/2016.
 */
public class AllergenTimerService extends Service {

    public static final long TEST_INTERVAL = 10 * 1000; // 10 seconds, used for debug
    public static final long FETCH_INTERVAL = 60 * 60 * 1000; // 1 hour

    // run on another thread to avoid crash
    private Handler handler = new Handler();

    private Timer timer = null;

    private List<Allergen> allergens;
    private Date todaysDate;
    private Date lastDateFetched;
    private Date lastDateLogged;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        // cancel if already exists
        if(timer != null) {
            timer.cancel();
        } else {
            // recreate new
            timer = new Timer();
        }

        // set lastDateFetched to an arbitrary date in the past
        lastDateFetched = new Date(93, 6, 30);

        // schedule task
        timer.scheduleAtFixedRate(new FetchAllergens(), 0, FETCH_INTERVAL);
    }

    public List<Allergen> runFetchAllergens() {
        new FetchAllergens().run();
        return allergens;
    }

    // simple method to get get rid of the date's time information
    public Date truncateDate(Date date) {
        return new Date(date.getYear(), date.getMonth(), date.getDate());
    }

    private class FetchAllergens extends TimerTask {
        @Override
        public void run() {
            setVarsFromSharedPrefs();

            // run on another thread
            handler.post(new Runnable() {
                @Override
                public void run() {
                    // update todaysDate
                    todaysDate = truncateDate(new Date());

                    // fetch allergens if needed
                    if(todaysDate.compareTo(lastDateFetched) > 0) {
                        refreshData();
                    } else {
                        Log.d("AllergenTimerService ", "allergens already found for today, allergens = " + Arrays.toString(allergens.toArray()));
                    }
                }
            });
        }

        public void refreshData() {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                try {
                    // create allergenService
                    Log.d("AllergenTimerService ", "fetching allergens");
                    AllergenService allergenWebService = new AllergenService();
                    allergens = allergenWebService.execute().get();

                    // update lastDateFetched
                    lastDateFetched = allergens.get(0).getDate();
                } catch (Exception e) {
                    Log.e("ERROR:AllergenTimerSer ", e.toString() + ", error fetching allergens");
                }
            } else {
                Log.e("ERROR:AllergenTimerSer ", "No network connection available.");
            }

            // insert allergens into database if needed
            if(allergens.size() > 0 && lastDateLogged.compareTo(allergens.get(0).getDate()) < 0) {
                Log.d("AllergenTimerService ", "Inserting allergens into table");
                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                for (Allergen a : allergens)
                    dbHelper.insertAllergen(a);
                Log.d("AllergenTimerService  ", Arrays.toString(allergens.toArray()));

                // update lastDateLogged
                lastDateLogged = allergens.get(0).getDate();
                SharedPreferences sharedPrefs = getSharedPreferences("AustinAllergyAlert", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("AllergenTimerService ", lastDateLogged.toString());
                editor.apply();

            } else {
                Log.d("AllergenTimerService ", "not inserting into table");
            }
        }

        private void setVarsFromSharedPrefs() {
            SharedPreferences sharedPrefs = getSharedPreferences("AustinAllergyAlert", MODE_PRIVATE);

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
}
