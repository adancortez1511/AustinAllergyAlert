package ac42886.austinallergyalert;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adan on 7/7/16.
 */
public class Notes extends AppCompatActivity {

    private static final int SETTINGS_REQUEST = 1;
    private static List<Note> notes;
    private static DatabaseHelper dbHelper;
    private static final SimpleDateFormat df = new SimpleDateFormat("EEEE MMMM d, yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TabHostProvider tabProvider = new MyTabHostProvider(Notes.this);
        TabView tabView = tabProvider.getTabHost("Notes");
        tabView.setCurrentView(R.layout.activity_notes);
        setContentView(tabView.render(3));

        dbHelper = new DatabaseHelper(this);

        notes = dbHelper.getNotes();

        updateListView();
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

    private void updateListView() {
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), NoteView.class);

                Bundle bundle = new Bundle();
                bundle.putInt("year", notes.get(position).getYear());
                bundle.putInt("month", notes.get(position).getMonth());
                bundle.putInt("dayOfMonth", notes.get(position).getDayOfMonth());
                bundle.putString("string", notes.get(position).getString());

                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        List<Map<String, String>> items = new ArrayList<Map<String, String>>();
        for (Note n : notes) {
            Map<String, String> item = new HashMap<String, String>(2);
            item.put("date", df.format(n.getDate().getTime()));
            item.put("string", n.getString());
            items.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.notes_listview, new String[]{"date", "string"}, new int[]{R.id.listview_date, R.id.listview_string});


        listView.setAdapter(adapter);
    }

    public void newNote(View view) {
        Intent intent = new Intent(this, NotesEntry.class);
        startActivity(intent);
    }

}
