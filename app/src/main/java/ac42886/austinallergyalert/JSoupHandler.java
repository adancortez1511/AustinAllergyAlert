package ac42886.austinallergyalert;

/**
 * Created by ofl7_000 on 7/7/2016.
 */

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class JSoupHandler {

    public Elements getElements(String source) {

        Document doc = null;
        try {
            doc = Jsoup.connect(source).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (doc != null) {
            // get the date reported and all allergen's and their levels
            Elements allergenData = doc.select("h2, .allergen");

            return allergenData;
        }
        else {
            return null;
        }

    }
}