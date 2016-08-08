package ac42886.austinallergyalert;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * Created by Adan on 7/5/16.
 */
public class CalendarActivity extends AppCompatActivity implements OnDateSelectedListener{

    private static final String TAG = "Calendar Activity:";
    private static final int SETTINGS_REQUEST = 1;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    private TextView calendarTextView;
    private TextView seekBarValue;
    private TextView ratingMessage;
    private SeekBar allergyResponse;
    private DatabaseHelper dbHelper;
    private Date selectedDate;
    private List<CalendarDay> entryDates;
    private static final SimpleDateFormat df = new SimpleDateFormat("EEEE MMMM d, yyyy");

    private List<CalendarDay> redDays= new ArrayList<CalendarDay>();
    private List<CalendarDay> orangeDays= new ArrayList<CalendarDay>();
    private List<CalendarDay> yellowDays= new ArrayList<CalendarDay>();
    private List<CalendarDay> yellowgreenDays= new ArrayList<CalendarDay>();
    private List<CalendarDay> greenDays= new ArrayList<CalendarDay>();

    @Bind(R.id.calendarView)
    MaterialCalendarView widget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TabHostProvider tabProvider = new MyTabHostProvider(CalendarActivity.this);
        TabView tabView = tabProvider.getTabHost("Calendar");
        tabView.setCurrentView(R.layout.activity_calendar);
        setContentView(tabView.render(1));

        // create DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // set selectedDate and TextView
        selectedDate = truncateDate(new Date());
        calendarTextView = (TextView)  findViewById(R.id.calendar_text);
        calendarTextView.setText(df.format(selectedDate));

        // Links SeekBar and TextView on Calendar screen
        allergyResponse = (SeekBar) findViewById(R.id.seekbar);
        seekBarValue = (TextView) findViewById(R.id.seekbar_value);
        ratingMessage = (TextView) findViewById(R.id.rating_message);
        entryDates = null;

        // Calls SeekBar listener function
        setSeekBarListener();

        int rating = dbHelper.getRatingByDate(selectedDate);
        if (rating == -1)
            allergyResponse.setProgress(0);
        else
            allergyResponse.setProgress(rating);

        ButterKnife.bind(this);

        widget.setOnDateChangedListener(this);
        widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);

        Calendar instance = Calendar.getInstance();
        widget.setSelectedDate(instance.getTime());

        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance1.get(Calendar.YEAR), Calendar.JANUARY, 1);

        Calendar instance2 = Calendar.getInstance();
        instance2.set(instance2.get(Calendar.YEAR), Calendar.DECEMBER, 31);

        widget.state().edit()
                .setMinimumDate(instance1.getTime())
                .setMaximumDate(instance2.getTime())
                .commit();

        widget.addDecorators(
                new MySelectorDecorator(this),
                new HighlightWeekendsDecorator(),
                oneDayDecorator
        );

        new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());
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
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

        //If you change a decorate, you need to invalidate decorators
        selectedDate = date.getDate();
        oneDayDecorator.setDate(selectedDate);
        widget.invalidateDecorators();
        calendarTextView.setText(df.format(selectedDate));
        Log.d(TAG, Calendar.getInstance().getTime().toString());

        // Olivia - Check to see if user has entered a value for this date if yes then change the 0 to that progress value
        int rating = dbHelper.getRatingByDate(date.getDate());
        if (rating == -1)
            allergyResponse.setProgress(0);
        else
            allergyResponse.setProgress(rating);

        if (selectedDate.after(CalendarDay.today().getDate()))
        {
            // Checks to see if the selected date is in the future
            ratingMessage.setText(R.string.rating_message_invalid);
            allergyResponse.setVisibility(View.INVISIBLE);
            seekBarValue.setVisibility(View.INVISIBLE);
        }
        else
        {
            // Olivia - I'm not sure we need to do anything here
            ratingMessage.setText(R.string.rating_message_valid);
            allergyResponse.setVisibility(View.VISIBLE);
            seekBarValue.setVisibility(View.VISIBLE);
        }
    }

    private void setSeekBarListener()
    {
        // Initializes SeekBar to 0 and sets the increments by 1 with a max of 4,
        // but will displayed as 1-5
        allergyResponse.setMax(4);
        allergyResponse.setProgress(0);
        allergyResponse.incrementProgressBy(1);

        allergyResponse.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValue.setText(String.valueOf(progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Olivia - Not sure if we need to start tracking change
                // I'll look if we can change the color of the SeekBar as it changes (low priority)
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int rating = seekBar.getProgress();
                Log.d("onStopTracking ", "inserting rating into database");
                dbHelper.insertRating(rating, selectedDate);

                // change the color of the decorator
                List<CalendarDay> newColor = new ArrayList<CalendarDay>();
                CalendarDay c = new CalendarDay(selectedDate);
                newColor.add(c);
                switch (rating) {
                    case 0:
                        widget.addDecorator(new EventDecorator(0xff009900, newColor));
                        break;
                    case 1:
                        widget.addDecorator(new EventDecorator(0xff9acd32, newColor));
                        break;
                    case 2:
                        widget.addDecorator(new EventDecorator(0xfffff000, newColor));
                        break;
                    case 3:
                        widget.addDecorator(new EventDecorator(0xffffa500, newColor));
                        break;
                    case 4:
                        widget.addDecorator(new EventDecorator(Color.RED, newColor));
                        break;
                }

                // create the toast
                Context context = getApplicationContext();
                CharSequence text = "Your rating has been saved!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }

    /**
     * Simulate an API call to show how to add decorators
     */
    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            List<Rating> ratings = dbHelper.getAllRatings();

            for(Rating r : ratings) {
                CalendarDay c = new CalendarDay(r.getDate());
                switch (r.getRating()) {
                    case 0:
                        greenDays.add(c);
                        break;
                    case 1:
                        yellowgreenDays.add(c);
                        break;
                    case 2:
                        yellowDays.add(c);
                        break;
                    case 3:
                        orangeDays.add(c);
                        break;
                    case 4:
                        redDays.add(c);
                        break;
                }
            }

            calendar.add(Calendar.MONTH, -2);
            ArrayList<CalendarDay> dates = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);
                calendar.add(Calendar.DATE, 5);
            }
            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            if(redDays.size() > 0)
                widget.addDecorator(new EventDecorator(Color.RED, redDays));
            if(orangeDays.size() > 0)
                widget.addDecorator(new EventDecorator(0xffffa500, orangeDays));
            if(yellowDays.size() > 0)
                widget.addDecorator(new EventDecorator(0xfffff000, yellowDays));
            if(yellowgreenDays.size() > 0)
                widget.addDecorator(new EventDecorator(0xff9acd32, yellowgreenDays));
            if(greenDays.size() > 0)
                widget.addDecorator(new EventDecorator(0xff009900, greenDays));

        }
    }

    // simple method to get get rid of the date's time information
    public Date truncateDate(Date date) {
        return new Date(date.getYear(), date.getMonth(), date.getDate());
    }

}