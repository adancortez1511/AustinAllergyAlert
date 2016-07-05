package ac42886.austinallergyalert;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AustinAllergyAlert extends AppCompatActivity {

    private static final String TAG = "AUSTIN ALLERGY ALERT:";
    private static final int SETTINGS_REQUEST = 1;

    private AllergyAlertClass mAllergy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_austin_allergy_alert);

    }

    private void refreshData()
    {
        // Call API to get allergen count and update fields

    }

}
