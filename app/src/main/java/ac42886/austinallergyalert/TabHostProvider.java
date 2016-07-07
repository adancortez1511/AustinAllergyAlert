package ac42886.austinallergyalert;

/**
 * Created by Adan on 7/7/16.
 */

import android.app.Activity;

public abstract class TabHostProvider {
    public Activity context;

    public TabHostProvider(Activity context) {
        this.context = context;
    }

    public abstract TabView getTabHost(String category);
}