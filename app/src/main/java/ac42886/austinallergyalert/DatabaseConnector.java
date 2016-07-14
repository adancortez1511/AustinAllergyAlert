package ac42886.austinallergyalert;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ofl7_000 on 7/12/2016.
 */
public class DatabaseConnector {

    private static final String DATABASE_NAME = "AustinAllergyAlert";
    public SQLiteDatabase database;
    public DatabaseHelper adbHelper;

    public DatabaseConnector(Context context) {
        adbHelper = new DatabaseHelper(context, DATABASE_NAME, null, 1);
    }

    public void open() throws SQLException {
        database = adbHelper.getWritableDatabase();
    }

    public void close() {
        if(database != null)
            database.close();
    }

}
