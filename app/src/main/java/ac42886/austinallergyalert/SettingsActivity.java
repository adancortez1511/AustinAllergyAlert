package ac42886.austinallergyalert;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Adan on 7/5/16.
 */
public class SettingsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction() .replace(android.R.id.content, new SettingsFragment()) .commit();
    }

    public static class SettingsFragment extends PreferenceFragment
    {
        private SharedPreferences sharedPrefs;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // set up shared preferences file
            getPreferenceManager().setSharedPreferencesName("AustinAllergyAlert");
            sharedPrefs = getActivity().getSharedPreferences("AustinAllergyAlert", MODE_PRIVATE);

            // display preferences
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}