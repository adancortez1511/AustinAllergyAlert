package ac42886.austinallergyalert;

//import java.util.Arrays;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by ofl7_000 on 7/7/2016.
 */

public class AllergenService {

    private JSoupHandler jSoupHandler = new JSoupHandler();

    public List<Allergen> getAllergens() {
        List<Allergen> allergens = new ArrayList<Allergen>();

        try {
            String source = "http://scripts.kxan.com/wp_embeds/weather/allergy/allergy_output_v2.html";
            Elements allergenData = jSoupHandler.getElements(source);

            if (allergenData != null) {
                // create iterator
                ListIterator<Element> iterator = allergenData.listIterator();

                // get date reported
                Element dateElement = iterator.next();
                String dateString = dateElement.html();
                DateFormat df = DateFormat.getDateInstance();
                Date date = df.parse(dateString, new ParsePosition(27));
                //System.out.println(date.toString());

                // get allergen types and their counts
                while(iterator.hasNext()) {
                    Element element = iterator.next();
                    String s = element.html();
                    //System.out.println(s);
                    String[] strings = s.split(": ");
                    String type = strings[0];
                    int count = Integer.parseInt(strings[1]);
                    Allergen allergen = new Allergen(type, count, date);
                    allergens.add(allergen);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //System.out.println(Arrays.toString(allergens.toArray()));
        return allergens;
    }

}
