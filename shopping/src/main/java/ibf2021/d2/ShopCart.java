package ibf2021.d2;

import java.io.Console;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ShopCart {
    private ShoppingCartDB cartDB;
    private Cart cart;

    public ShopCart() {
        this.cart = new Cart();
    }

    public ShopCart(ShoppingCartDB cartDB) {
        this.cartDB = cartDB;
        this.cart = new Cart();
    }

    public Cart getCart() {
        return this.cart;
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
                    this.cart.list();
                    break;
                case "add":
                    this.cart.add(input.substring(commands[0].length()));
                    break;
                case "delete":
                    if (commands.length > 1) {
                        this.cart.delete(Integer.parseInt(commands[1]) - 1);
                    } else {
                        System.out.println("Please include the number index of the item you want to delete");
                    }
                    break;
                case "exit":
                    exit = true;
                    System.out.println("Thank you for using the shopping cart.");
                    System.out.println("You have these remaining items:");
                    this.cart.list();
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



    public static void main(String[] args) throws IOException {
        ShoppingCartDB cartDB;
        if (args.length > 0) {
            // using db folder specified in argument
            cartDB = new ShoppingCartDB(args[0]);
        } else {
            // using default db folder
            cartDB = new ShoppingCartDB();
        }

        ShopCart sc = new ShopCart(cartDB);
        sc.run();
    }
}
