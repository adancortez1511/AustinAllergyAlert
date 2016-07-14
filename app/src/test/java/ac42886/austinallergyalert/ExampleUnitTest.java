package ac42886.austinallergyalert;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void runAllergenService() throws Exception {
        AllergenService as = new AllergenService();
        List<Allergen> allergens = as.getAllergens();
        System.out.println(Arrays.toString(allergens.toArray()));
    }

    @Test
    public void testAllergen1() throws Exception {
        Allergen a = new Allergen("Mt. Cedar", 900, new Date());
        assertEquals(Allergen.AllergenType.TREE, a.getType());
        assertEquals(Allergen.AllergenLevel.HIGH, a.getLevel());
    }

    @Test
    public void testAllergen2() throws Exception {
        Allergen a = new Allergen("asdf", 100, new Date());
        assertEquals(Allergen.AllergenType.OTHER, a.getType());
        assertEquals(Allergen.AllergenLevel.HIGH, a.getLevel());
    }
}