package ac42886.austinallergyalert;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ofl7_000 on 8/7/2016.
 */
public class NoteView extends AppCompatActivity implements DialogInterface.OnDismissListener {
    private static Note note;
    private static TextView dateTV;
    private static EditText noteField;
    private static DatabaseHelper dbHelper;
    private static final SimpleDateFormat df = new SimpleDateFormat("EEEE MMMM d, yyyy");
    private static boolean noteDeleted = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_view);

        dbHelper = new DatabaseHelper(this);

        setViews();
    }

    private void setViews() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        Calendar date = Calendar.getInstance();
        int year = bundle.getInt("year");
        int month = bundle.getInt("month");
        int dayOfMonth = bundle.getInt("dayOfMonth");
        date.set(year, month, dayOfMonth);
        String string = bundle.getString("string");

        note = new Note(date, string);

        dateTV = (TextView) findViewById(R.id.view_date_tv);
        noteField = (EditText) findViewById(R.id.view_note_field);
        dateTV.setText(df.format(date.getTime()));
        noteField.setText(string);

        noteField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                note.setString(s.toString());
            }
        });
    }

    public void editNote(View v) {
        dbHelper.updateNote(note);
        Intent intent = new Intent(this, Notes.class);
        startActivity(intent);
    }

    public void deleteNote(View v) {
        FragmentManager fm = getFragmentManager();
        DeleteNoteDialogFragment deleteNoteDialogFragment = new DeleteNoteDialogFragment();
        deleteNoteDialogFragment.show(fm, "delete");
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        if(noteDeleted) {
            dbHelper.deleteNote(note);
            Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Notes.class);
            startActivity(intent);
        }
    }

    public static class DeleteNoteDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), DialogFragment.STYLE_NO_FRAME);
            builder.setMessage(R.string.delete_note)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            noteDeleted = true;
                            dismiss();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dismiss();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }

        @Override
        public void onDismiss(final DialogInterface dialog) {
            super.onDismiss(dialog);
            final Activity activity = getActivity();
            if (activity instanceof DialogInterface.OnDismissListener) {
                ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
            }
        }
    }
}
