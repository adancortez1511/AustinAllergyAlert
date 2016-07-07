package ac42886.austinallergyalert;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Adan on 7/7/16.
 */
public class GraphData extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TabHostProvider tabProvider = new MyTabHostProvider(GraphData.this);
        TabView tabView = tabProvider.getTabHost("Graph");
        tabView.setCurrentView(R.layout.activity_graph_data);
        setContentView(tabView.render(2));
    }
}
