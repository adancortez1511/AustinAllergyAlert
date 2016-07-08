package ac42886.austinallergyalert;

import java.util.Date;

/**
 * Created by ofl7_000 on 7/7/2016.
 */
public class Allergen {
    private String type;
    private int count;
    private Date date;

    public Allergen() {}

    public Allergen (String type, int count, Date date) {
        this.type = type;
        this.count = count;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String toString () {
        String s = "Type: " + type + ", Count: " + count + ", Date: " + date.toString();
        return s;
    }

}
