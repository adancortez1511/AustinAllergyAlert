package ac42886.austinallergyalert;

import java.util.Date;

/**
 * Created by ofl7_000 on 8/7/2016.
 */
public class Rating {
    private int rating;
    private Date date;

    public Rating(int rating, Date date) {
        this.rating = rating;
        this.date = date;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getYear() {
        return date.getYear();
    }

    public int getMonth() {
        return date.getMonth();
    }

    public int getDayOfMonth() {
        return date.getDate();
    }

    public String toString() {
        return "Rating: " + rating + ", Date: " + date.toString();
    }
}
