package ac42886.austinallergyalert;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ofl7_000 on 8/5/2016.
 */
public class NotesEntry extends AppCompatActivity {
    private static TextView dateTV;
    private static EditText noteField;
    private static Calendar date;
    private static final SimpleDateFormat df = new SimpleDateFormat("EEEE MMMM d, yyyy");
    private static DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_entry);

        dbHelper = new DatabaseHelper(this);

        date = Calendar.getInstance();
        dateTV = (TextView) findViewById(R.id.entry_date_tv);
        dateTV.setText(df.format(date.getTime()));
        noteField = (EditText) findViewById(R.id.entry_note_field);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void saveNote(View v) {
        Note n = new Note(date, noteField.getText().toString());
        dbHelper.insertNote(n);
        Intent intent = new Intent(this, Notes.class);
        startActivity(intent);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            int year = date.get(Calendar.YEAR);
            int month = date.get(Calendar.MONTH);
            int day = date.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), android.R.style.Theme_DeviceDefault_Dialog_Alert,
                    this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            date.set(year, month, day);
            dateTV.setText(df.format(new Date(year - 1900, month, day)));
        }
    }
}
