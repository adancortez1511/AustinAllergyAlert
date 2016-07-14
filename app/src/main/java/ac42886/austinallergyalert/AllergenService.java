package ac42886.austinallergyalert;

//import java.util.Arrays;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;
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

public class AllergenService extends AsyncTask<Void, Void, List<Allergen>> {

    private static final String SOURCE = "http://scripts.kxan.com/wp_embeds/weather/allergy/allergy_output_v2.html";
    private JSoupHandler jSoupHandler = new JSoupHandler();

    public List<Allergen> doInBackground(Void... params) {
        List<Allergen> allergens = new ArrayList<Allergen>();

        try {
            Elements allergenData = jSoupHandler.getElements(SOURCE);

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

        return allergens;
    }


    public void onPostExecute(List<Allergen> allergens) {
        Log.d("AllergenService ", Arrays.toString(allergens.toArray()));
    }
}
