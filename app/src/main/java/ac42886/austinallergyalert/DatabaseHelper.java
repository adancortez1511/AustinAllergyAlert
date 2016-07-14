package ac42886.austinallergyalert;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ofl7_000 on 7/11/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance;

    public static DatabaseHelper getInstance(Context ctx){
        if(instance==null){
            instance = new DatabaseHelper(ctx);
        }
        return instance;
    }

    /* Inner class that defines the table contents */
    private static abstract class AllergensDbEntry implements BaseColumns {
        public static final String TABLE_NAME = "allergens";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_COUNT = "count";
        public static final String COLUMN_LEVEL = "level";
        public static final String COLUMN_DATE = "date";
    }

    /* contract class, used to define the names of tables and columns */
    private class AllergensDbContract {
        // To prevent someone from accidentally instantiating the contract class,
        // give it an empty constructor.
        public AllergensDbContract() {}

        // table creation strings
        private static final String TEXT_TYPE = " TEXT";
        private static final String INT_TYPE = " INTEGER";
        private static final String COMMA_SEP = ",";
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + AllergensDbEntry.TABLE_NAME + " (" +
                        AllergensDbEntry._ID + " INTEGER PRIMARY KEY," +
                        AllergensDbEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                        //AllergensDbEntry.COLUMN_TYPE + INT_TYPE + COMMA_SEP +
                        AllergensDbEntry.COLUMN_COUNT + INT_TYPE + COMMA_SEP +
                        //AllergensDbEntry.COLUMN_LEVEL + INT_TYPE + COMMA_SEP +
                        AllergensDbEntry.COLUMN_DATE + TEXT_TYPE +
                        " );";

        // table deletion string
        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + AllergensDbEntry.TABLE_NAME;

    }


    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "AustinAllergyAlert";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory cf, int verison) {
        super(context, name, cf, verison);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AllergensDbContract.SQL_CREATE_ENTRIES);
//        db.execSQL("CREATE TABLE allergens (id INTEGER PRIMARY KEY, name TEXT, count INTEGER, date TEXT);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(AllergensDbContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void insertAllergen(Allergen a) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(AllergensDbEntry.COLUMN_NAME, a.getName());
        values.put(AllergensDbEntry.COLUMN_COUNT, a.getCount());
        values.put(AllergensDbEntry.COLUMN_DATE, a.getDate().toString());

        // Insert the new row
        Log.d("DB", "db test: " + db);
        db.insert(AllergensDbEntry.TABLE_NAME, null, values);
    }


    // gets a list of allergens by their dates
    public List<Allergen> getAllergensByDate (Date lookupDate) {
        List<Allergen> allergens = new ArrayList<Allergen>();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                AllergensDbEntry.COLUMN_NAME,
                AllergensDbEntry.COLUMN_COUNT,
                AllergensDbEntry.COLUMN_DATE
        };

        String selection = AllergensDbEntry.COLUMN_DATE + " = ?";
        String[] selectionArgs = new String[] {lookupDate.toString()};
        Log.d("getAllergens ", "lookupDate = " + lookupDate.toString());

        SQLiteDatabase db = this.getReadableDatabase();
        // this query is equivalent to:
        // "SELECT name,count,date FROM allergens WHERE date = <lookupDate>
        Cursor cursor = db.query(
                AllergensDbEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndex(AllergensDbEntry.COLUMN_NAME));
            int count = cursor.getInt(cursor.getColumnIndex(AllergensDbEntry.COLUMN_COUNT));
            String stringDate = cursor.getString(cursor.getColumnIndex(AllergensDbEntry.COLUMN_DATE));
            Log.d("Date", stringDate);
            DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            Date date = new Date();
            try {
                date = df.parse(stringDate);
            } catch (ParseException e) {
                Log.e("ERROR:getAllergens ", "error parsing date");
            }
            Log.d("Date", date.toString());
            allergens.add(new Allergen(name, count, date));
            cursor.moveToNext();
        }

        return allergens;
    }
}
