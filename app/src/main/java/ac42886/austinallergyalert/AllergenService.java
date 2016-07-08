package ac42886.austinallergyalert;

//import java.util.Arrays;
import java.util.Arrays;
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
                ListIterator<Element> iterator = allergenData.listIterator();
                while(iterator.hasNext()) {
                    Element element = (Element) iterator.next();
                    String s = element.html();
                    //System.out.println(s);
                    String[] strings = s.split(": ");
                    String type = strings[0];
                    int count = Integer.parseInt(strings[1]);
                    Allergen allergen = new Allergen(type, count);
                    allergens.add(allergen);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return allergens;
    }

}
