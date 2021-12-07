package ibf2021.d2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ShopCartTest {
    private ShopCart sc;

    @Before
    public void setUp() throws Exception {
        sc = new ShopCart();
    }

    @After
    public void tearDown() throws Exception {
        sc = null;
    }

    @Test
    public void addCartItems_OneItemAdded_CountShouldIncreaseByOne() {
        int initialCount = sc.getItemsCount();
        sc.add("apple");
        int countAfterAddOne = sc.getItemsCount();
        assertTrue(countAfterAddOne == initialCount + 1);
    }

    @Test
    public void deleteCartItems_OneItemDeleted_CountShouldDecreaseByOne() {
        sc.add("apple, orange, pear");
        int count = sc.getItemsCount();
        sc.delete(0);
        int countAfterDelete = sc.getItemsCount();
        assertTrue(countAfterDelete == count - 1);
    }

    @Test
    public void addCartItems_AddDuplicateItems_ShouldNotAddDuplicates() {
        sc.add("apple, orange");
        int count = sc.getItemsCount();
        sc.add("apple");
        sc.add("apple, apple");
        int countAfterDuplicates = sc.getItemsCount();
        assertEquals(countAfterDuplicates, count);
    }

    @Test
    public void deleteCartItems_RemoveItems_RemainingItemsShouldMatch () {
        sc.add("apple, pie");
        List<String> cartBeforeDelete = sc.getItems();
        sc.add("orange, carrot");
        assertTrue(sc.getItemsCount() == 4);
        sc.delete(3);
        sc.delete(2);
        List<String> expected = Arrays.asList("apple", "pie");
        assertEquals(expected, cartBeforeDelete);
    }


}
