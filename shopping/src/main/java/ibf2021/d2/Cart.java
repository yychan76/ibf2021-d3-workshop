package ibf2021.d2;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    protected List<String> cartItems;

    public Cart() {
        System.out.println("Welcome to your shopping cart");
        cartItems = new ArrayList<String>();
    }

    public static void displayItems(List<String> items) {
        for (int i=0; i < items.size(); i++) {
            System.out.printf("%d. %s %n", i + 1, items.get(i));
        }
    }

    public void list () {
        if (cartItems.size() == 0) {
            System.out.println("Your cart is empty");
        } else {
            displayItems(cartItems);
        }
    }

    public void add (String items) {
        for (String item : items.split(",")) {
            item = item.trim();
            if (cartItems.contains(item)) {
                System.out.printf("You have %s in your cart%n", item);
            } else {
                this.cartItems.add(item);
                System.out.printf("%s added to cart%n", item);
            }
        }
    }

    public void delete (int index) {
        if (index >= 0 && index < this.cartItems.size()) {
            System.out.printf("%s removed from cart%n", this.cartItems.get(index));
            this.cartItems.remove(index);
            System.out.println("Items remaining in cart: " + this.cartItems.size());
        } else {
            System.out.println("Incorrect item index");
        }
    }

    public int getItemsCount() {
        return this.cartItems.size();
    }

    public List<String> getItems() {
        return this.cartItems;
    }

    public void setItems(List<String> items) {
        this.cartItems = items;
    }
}
