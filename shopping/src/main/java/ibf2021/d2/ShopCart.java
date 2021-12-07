package ibf2021.d2;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ShopCart {
    protected List<String> cartItems;

    public ShopCart() {
        System.out.println("Welcome to your shopping cart");
        cartItems = new ArrayList<String>();
    }

    public void list () {
        if (cartItems.size() == 0) {
            System.out.println("Your cart is empty");
        } else {
            for (int i=0; i < cartItems.size(); i++) {
                System.out.printf("%d. %s %n", i + 1, cartItems.get(i));
            }
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

    private String getInput() {
        Console cons = System.console();
        return cons.readLine("> ");
    }

    private String getScannerInput(Scanner scan) {
        System.out.print("> ");
        String input = scan.nextLine();
        // System.out.println(input);
        return input;
    }

    private void run() {
        Scanner scan = new Scanner(System.in);

        boolean exit = false;
        while(!exit) {
            // String input = getInput();
            String input = getScannerInput(scan);
            String[] commands = input.split(" ");
            switch (commands[0]) {
                case "list":
                    list();
                    break;
                case "add":
                    add(input.substring(commands[0].length()));
                    break;
                case "delete":
                    if (commands.length > 1) {
                        delete(Integer.parseInt(commands[1]) - 1);
                    } else {
                        System.out.println("Please include the number index of the item you want to delete");
                    }
                    break;
                case "exit":
                    exit = true;
                    System.out.println("Thank you for using the shopping cart.");
                    System.out.println("You have these remaining items:");
                    list();
                    break;
                default:
                    System.out.println(
                        """
                        Unrecognised command. Please enter:
                        list \t\tto show the items in your cart
                        add item \tto add the item to your cart
                        delete index \tto delete the number index item from your cart
                        exit \t\tto exit
                        """
                    );

            }
        }
        scan.close();
    }

    public static void main(String[] args) {
        ShopCart sc = new ShopCart();
        sc.run();
    }
}
