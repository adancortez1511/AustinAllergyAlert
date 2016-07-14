package ac42886.austinallergyalert;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;

import java.util.Date;
import java.util.List;

import org.junit.Test;

/**
 * Created by ofl7_000 on 7/11/2016.
 */
public class SQLiteUnitTests extends AndroidTestCase {
    DatabaseHelper dbh;
    SQLiteDatabase writedb;
    SQLiteDatabase readdb;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        // RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        //dbh = DatabaseHelper.getInstance(mContext);
        dbh = new DatabaseHelper(mContext);

        //TABLE AUTH TEST
        writedb = dbh.getWritableDatabase();
        System.out.println("writedb: " + writedb + " after call to getWritable databse");
        readdb = dbh.getReadableDatabase();
    }

    @Override
    public void tearDown() throws Exception {
        readdb.close();
        writedb.close();
        super.tearDown();
    }

    @Test
    public void testDBInsertAndGet() {
        AllergenService as = new AllergenService();
        List<Allergen> a1 = as.getAllergens();
        Date date = a1.get(0).getDate();
        System.out.println("writedb: " + writedb);
        for(Allergen a : a1) {
            dbh.insertAllergen(a, writedb);
        }

        List<Allergen> a2 = dbh.getAllergensByDate(date, readdb);

        for(int i = 0; i < a1.size(); ++i) {
            assertEquals(a1.get(i), a2.get(i));
        }
    }
}
