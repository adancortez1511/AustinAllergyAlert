package ac42886.austinallergyalert;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

public class AustinAllergyAlert extends AppCompatActivity {

    private static final String TAG = "AUSTIN ALLERGY ALERT:";
    private static final int SETTINGS_REQUEST = 1;

    private AllergyAlertClass mAllergy;
    private DatabaseHelper dbHelper;
    private AllergenService allergenService;
    private static Date todaysDate;
    private static Date lastDateLogged;
    private static List<Allergen> allergens;

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

        // start AllergenTimerService, must happen after setVarsFromSharedPrefs
        startService(new Intent(this, AllergenTimerService.class));

        // get today's allergen counts if needed
        dbHelper = new DatabaseHelper(this);
        todaysDate = truncateDate(new Date());
        try {
            refreshData();
        } catch (Exception e) {
            Log.e("ERROR:onCreate ", e.toString() + " exception thrown trying allergenService.execute()");
        }

        // create the chart
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new DailyChartFragment()).commit();
        }
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

        SharedPreferences sharedPrefs = getSharedPreferences("PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putString("lastDateLogged", lastDateLogged.toString());
        Log.d("onStop ", "lastDateLogged = " + lastDateLogged.toString());
        editor.apply();
    }

    private void refreshData() throws ExecutionException, InterruptedException {
        allergens = new ArrayList<Allergen>();

        if(todaysDate.compareTo(lastDateLogged) == 0) {
            // read allergens from the database
            Log.d("refreshData ", "reading allergens from database");
            allergens = dbHelper.getAllergensByDate(lastDateLogged);
        } else {
            // Call API to get allergen count and update fields
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                allergenService = new AllergenService();
                allergens = allergenService.execute().get();
                Log.d("refreshData ", "allergens = " + Arrays.toString(allergens.toArray()));
            } else {
                Log.e("ERROR:refreshData ", "No network connection available.");
            }

            // insert allergens into database if needed
            if(allergens.size() > 0 && lastDateLogged.compareTo(allergens.get(0).getDate()) < 0) {
                Log.d("refreshData ", "Inserting allergens into table");
                for (Allergen a : allergens)
                    dbHelper.insertAllergen(a);
                Log.d("refreshData ", Arrays.toString(allergens.toArray()));

                // update lastDateLogged
                lastDateLogged = allergens.get(0).getDate();
            } else
                Log.d("refreshData ", "not inserting into table");
        }

    }

    // simple method to get get rid of the date's time information
    public Date truncateDate(Date date) {
        return new Date(date.getYear(), date.getMonth(), date.getDate());
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


    // chart stuff
    public static class DailyChartFragment extends Fragment {

        private ColumnChartView chart;
        private ColumnChartData data;
        private boolean hasLabels = true;
        private boolean hasLabelsForSelected = false;
        private TextView dateView;

        public DailyChartFragment() { }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_column_chart, container, false);

            chart = (ColumnChartView) rootView.findViewById(R.id.chart);
            chart.setOnValueTouchListener(new ValueTouchListener());

            // set the dateView
            SimpleDateFormat df = new SimpleDateFormat("EEEE MMMM d, yyyy");
            dateView = (TextView) rootView.findViewById(R.id.fragment_date);
            dateView.setText(df.format(lastDateLogged));

            generateColumns();

            return rootView;
        }

        private void generateColumns() {
            // use this to test multiple allergens
            //allergens = getTestAllergens();

            Log.d("generateColumns ", "allergens = " + Arrays.toString(allergens.toArray()));
            int nColumns = allergens.size();

            List<Column> columns = new ArrayList<Column>();
            List<AxisValue> axisValues = new ArrayList<AxisValue>();

            for(int i = 0; i < nColumns; ++i) {
                // decide what color the column will be
                int color = getColor(allergens.get(i));

                // create the list of SubColumnValues that will be used
                // to create a single column
                List<SubcolumnValue> subcolumnValues = new ArrayList<SubcolumnValue>();
                subcolumnValues.add(new SubcolumnValue(allergens.get(i).getCount(), color));
                Column column = new Column(subcolumnValues);

                // create a new AxisValue and add it to the list of AxisValues
                AxisValue av = new AxisValue(i);
                av.setLabel(allergens.get(i).getName());
                axisValues.add(av);

                // give the column labels
                column.setHasLabels(hasLabels);
                column.setHasLabelsOnlyForSelected(hasLabelsForSelected);

                // add the column to the list of columns
                columns.add(column);
            }

            // create the ColumnChartData

            // create the axes and set their names
            Axis xAxis = new Axis(axisValues);
            Axis yAxis = new Axis();
            xAxis.setName("Allergens");
            yAxis.setName("Counts");

            // create ColumnChartData
            data = new ColumnChartData(columns);
            data.setAxisXBottom(xAxis);
            data.setAxisYLeft(yAxis);

            chart.setColumnChartData(data);

        }

        private int getColor (Allergen a) {
            int color = 0xffffffff;                 // DEFAULT WHITE
            switch(a.getLevel()) {
                case LOW: color = 0xff00ff00;       // GREEN
                    break;
                case MEDIUM: color = 0xffffff00;    // YELLOW
                    break;
                case HIGH: color = 0xffffa500;      // ORANGE
                    break;
                case VERY_HIGH: color = 0xffff0000; // RED
                    break;
            }
            return color;
        }

        private class ValueTouchListener implements ColumnChartOnValueSelectListener {

            @Override
            public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
                Allergen a = allergens.get(columnIndex);
                String toastString = a.getName() + "\nType: "+ a.getType().toString() +
                        "\nCount: " + a.getCount() + "\nLevel: " + a.getLevel().toString();



            }

            @Override
            public void onValueDeselected() { }

        }

    }


    public static List<Allergen> getTestAllergens(){
        List<Allergen> testAllergens = new ArrayList<Allergen>();
        Allergen[] testAllergenArray = {
                new Allergen("Cedar", 2074, todaysDate),
                new Allergen("Pigweed", 28, todaysDate),
                new Allergen("Mold", 1278, todaysDate),
                new Allergen("Grass", 3, todaysDate)};
        for(Allergen a : testAllergenArray)
            testAllergens.add(a);

        return testAllergens;
    }
}
