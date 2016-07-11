package ac42886.austinallergyalert;

import java.util.Date;

/**
 * Created by ofl7_000 on 7/7/2016.
 */
public class Allergen {

    /* enum describing the category the Allergen falls into */
    public enum AllergenType {
        MOLD(0), GRASS(1), WEED(2), TREE(3), OTHER(4);
        private final int value;
        private AllergenType(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    /* enum describing how low or high Allergen.count is from LOW to VERY_HIGH */
    public enum AllergenLevel {
        LOW(0), MEDIUM(1), HIGH(2), VERY_HIGH(3);
        private final int value;
        private AllergenLevel(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    /* used to determine the AllergenLevel of the various allergen types
    *  1st number in each row is the threshold of MEDIUM, 2nd is HIGH, 3rd is VERY_HIGH */
    private final int[][] AllergenLevels = {{300, 1000, 10000}, // MOLD
                                            {5,  20, 200},      // GRASS
                                            {10, 50, 500},      // WEED
                                            {15, 90, 1500},     // TREE
                                            {10, 100, 1000}};   // OTHER

    private String name;
    private AllergenType type;
    private int count;
    private AllergenLevel level;
    private Date date;

    public Allergen() {}

    public Allergen (String name, int count, Date date) {
        this.name = name;
        this.type = getType(name.toLowerCase());
        this.count = count;
        this.level = getLevel(this.type, count);
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public AllergenType getType() {
        return type;
    }

    public AllergenLevel getLevel () {
        return level;
    }

    public String toString () {
        String s = "Name: " + name + ", Type: " + type + ", Count: " + count +
                ", Level: " + level + ", Date: " + date.toString();
        return s;
    }

    public AllergenType getType(String name) {
        // mold
        if(name.contains("mold"))
            return AllergenType.MOLD;

        // grass
        else if(name.contains("grass"))
            return AllergenType.GRASS;

        // weed
        else if(name.equals("pigweed") || name.equals("pig weed") ||
                name.equals("marsh elder") || name.equals("ragweed") ||
                name.equals("rag weed"))
            return AllergenType.WEED;

        // tree
        else if(name.contains("cedar") || name.contains("elm") ||
                name.contains("oak") || name.contains("ash") ||
                name.contains("mesquite") || name.contains("pecan") ||
                name.contains("privet") || name.contains("sycamore") ||
                name.contains("mulberry") || name.contains("willow") ||
                name.contains("juniper") || name.contains("sage"))
            return AllergenType.TREE;

        // other
        else
            return AllergenType.OTHER;

    }

    public AllergenLevel getLevel(AllergenType type, int count) {
        int row = type.getValue();

        // LOW
        if (count < AllergenLevels[row][0])
            return AllergenLevel.LOW;
        // MEDIUM
        else if(count < AllergenLevels[row][1])
            return AllergenLevel.MEDIUM;
        // HIGH
        else if(count < AllergenLevels[row][2])
            return AllergenLevel.HIGH;
        // VERY HIGH
        else
            return AllergenLevel.VERY_HIGH;
    }

}
