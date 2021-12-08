package ibf2021.d2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ShopCartTest {
    private ShopCart sc;
    private Cart cart;
    private ShoppingCartDB cartDB;
    private static final String LINESEP = System.lineSeparator();

    private static ByteArrayInputStream stringToInputStream(String string) throws IOException {
        return new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
    }

    @Before
    public void setUp() throws Exception {
        cartDB = new ShoppingCartDB();
        sc = new ShopCart(cartDB);
        cart = sc.getCart();
    }

    @After
    public void tearDown() throws Exception {
        sc = null;
        cart = null;
        cartDB = null;
    }

    @Test
    public void addCartItems_OneItemAdded_CountShouldIncreaseByOne() {
        int initialCount = cart.getItemsCount();
        cart.add("apple");
        int countAfterAddOne = cart.getItemsCount();
        assertTrue(countAfterAddOne == initialCount + 1);
    }

    @Test
    public void addCartItemsCommand_OneItemAdded_CountShouldIncreaseByOne() throws IOException {
        int initialCount = cart.getItemsCount();
        sc.commandInput(stringToInputStream("add apple"));
        int countAfterAddOne = cart.getItemsCount();
        assertTrue(countAfterAddOne == initialCount + 1);
    }

    @Test
    public void deleteCartItems_OneItemDeleted_CountShouldDecreaseByOne() {
        cart.add("apple, orange, pear");
        int count = cart.getItemsCount();
        cart.delete(0);
        int countAfterDelete = cart.getItemsCount();
        assertTrue(countAfterDelete == count - 1);
    }

    @Test
    public void deleteCartItemsCommand_OneItemDeleted_CountShouldDecreaseByOne() throws IOException {
        sc.commandInput(stringToInputStream("add apple, orange, pear"));
        int count = cart.getItemsCount();
        sc.commandInput(stringToInputStream("delete 1"));
        int countAfterDelete = cart.getItemsCount();
        assertTrue(countAfterDelete == count - 1);
    }

    @Test
    public void addCartItems_AddDuplicateItems_ShouldNotAddDuplicates() {
        cart.add("apple, orange");
        int count = cart.getItemsCount();
        cart.add("apple");
        cart.add("apple, apple");
        int countAfterDuplicates = cart.getItemsCount();
        assertEquals(countAfterDuplicates, count);
    }

    @Test
    public void addCartItemsCommand_AddDuplicateItems_ShouldNotAddDuplicates() throws IOException {
        sc.commandInput(stringToInputStream("add apple, orange"));
        int count = cart.getItemsCount();
        sc.commandInput(stringToInputStream("add apple"));
        sc.commandInput(stringToInputStream("add apple, apple"));
        int countAfterDuplicates = cart.getItemsCount();
        assertEquals(countAfterDuplicates, count);
    }

    @Test
    public void deleteCartItems_RemoveItems_RemainingItemsShouldMatch () {
        cart.add("apple, pie");
        List<String> cartBeforeDelete = cart.getItems();
        cart.add("orange, carrot");
        assertTrue(cart.getItemsCount() == 4);
        cart.delete(3);
        cart.delete(2);
        List<String> expected = Arrays.asList("apple", "pie");
        assertEquals(expected, cartBeforeDelete);
    }

    @Test
    public void deleteCartItemsCommand_RemoveItems_RemainingItemsShouldMatch () throws IOException {
        sc.commandInput(stringToInputStream("add apple, pie"));
        List<String> cartBeforeDelete = cart.getItems();
        sc.commandInput(stringToInputStream("add orange, carrot"));
        assertTrue(cart.getItemsCount() == 4);
        sc.commandInput(stringToInputStream("delete 4"));
        sc.commandInput(stringToInputStream("delete 3"));
        List<String> expected = Arrays.asList("apple", "pie");
        assertEquals(expected, cartBeforeDelete);
    }

    @Test
    public void loginUser_LoginUser_ShouldLogin() throws IOException {
        String expectedUserName = "TestUserNoob";
        sc.commandInput(stringToInputStream("login " + expectedUserName));
        String userName = cartDB.getUserName();
        assertEquals(expectedUserName, userName);
    }

    @Test
    public void loginUserRestoreCart_LoginUser_ShouldRestoreCartItems() throws IOException {
        String userA = "TestUserNoob";
        String userB = "TestUserSensei";
        // login first user
        sc.commandInput(stringToInputStream("login " + userA));
        int itemsCount = cart.getItemsCount();
        // empty out existing items first
        for (int i = itemsCount; i > 0; i--) {
            sc.commandInput(stringToInputStream("delete " + i));
        }
        // add items to cart
        sc.commandInput(stringToInputStream("add apple, orange, pear"));
        // save items to file
        sc.commandInput(stringToInputStream("save"));
        // login second user
        sc.commandInput(stringToInputStream("login " + userB));
        itemsCount = cart.getItemsCount();
        // empty out existing items first
        for (int i = itemsCount; i > 0; i--) {
            sc.commandInput(stringToInputStream("delete " + i));
        }
        sc.commandInput(stringToInputStream("add soba, soju, sake"));
        sc.commandInput(stringToInputStream("save"));
        // login first user again
        sc.commandInput(stringToInputStream("login " + userA));
        List<String> expectedCartItems = new ArrayList<String>(Arrays.asList("apple", "orange", "pear"));
        List<String> firstCartItems = cart.getItems();
        assertEquals(expectedCartItems, firstCartItems);

    }

    @Test
    public void loginUserSaveCart_SaveCommandNotRun_ShouldNotSaveCartItems() throws IOException {
        String userA = "TestUserNoob";
        String userB = "TestUserSensei";
        // login first user
        sc.commandInput(stringToInputStream("login " + userA));
        int itemsCount = cart.getItemsCount();
        // empty out existing items first
        for (int i = itemsCount; i > 0; i--) {
            sc.commandInput(stringToInputStream("delete " + i));
        }
        // save empty cart to file
        sc.commandInput(stringToInputStream("save"));
        // add items to cart
        sc.commandInput(stringToInputStream("add apple, orange, pear"));
        // login second user
        sc.commandInput(stringToInputStream("login " + userB));
        // login first user again
        sc.commandInput(stringToInputStream("login " + userA));
        List<String> expectedEmptyCartItems = new ArrayList<String>();
        List<String> firstCartItems = cart.getItems();
        assertEquals(expectedEmptyCartItems, firstCartItems);

    }



}
