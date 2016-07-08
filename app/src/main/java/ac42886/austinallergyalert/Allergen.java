package ac42886.austinallergyalert;

/**
 * Created by ofl7_000 on 7/7/2016.
 */
public class Allergen {
    private String type;
    private int count;

    public Allergen() {}

    public Allergen (String type, int count) {
        this.type = type;
        this.count = count;
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

    public String toString () {
        String s = "Type: " + type + ", Count: " + count;
        return s;
    }

}
