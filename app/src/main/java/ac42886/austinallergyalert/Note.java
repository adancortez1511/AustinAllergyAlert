package ac42886.austinallergyalert;

import java.util.Calendar;

/**
 * Created by ofl7_000 on 8/6/2016.
 */
public class Note {
    private Calendar date;
    private String string;

    public Note(Calendar date, String string) {
        this.date = date;
        this.string = string;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getYear() {
        return date.get(Calendar.YEAR);
    }

    public int getMonth() {
        return date.get(Calendar.MONTH);
    }

    public int getDayOfMonth() {
        return date.get(Calendar.DAY_OF_MONTH);
    }
}
