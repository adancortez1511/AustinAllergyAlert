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
            // get the 3rd cell of every row after the first 3 rows
            Elements allergenData = doc.select(".allergen");
            return allergenData;
        }
        else {
            return null;
        }

    }
}