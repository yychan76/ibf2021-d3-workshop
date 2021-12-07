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

    private void run() throws IOException {
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
                case "users":
                    List<String> users = this.cartDB.listUsers();
                    if (users.size() > 0) {
                        System.out.println("Users who have saved cart items:");
                        for (String user : users) {
                            System.out.println(user);
                        }
                    } else {
                        System.out.println("There are no users who have saved cart items");
                    }
                    break;
                case "login":
                    if (commands.length > 1) {
                        String userName = commands[1];
                        this.cartDB.setUserName(userName);
                        List<String> savedItems = this.cartDB.load();
                        this.cart.setItems(savedItems);
                        System.out.println(userName + ", your cart contains the following items");
                        this.cart.list();
                    } else {
                        System.out.println("Please include your name");
                    }
                    break;
                case "save":
                    this.cartDB.save(this.cart.getItems());
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
                        users \t\tto list all users who have saved cart items
                        login yourname \tto load your previous saved items
                        list \t\tto show the items in your cart
                        add item \tto add the item to your cart
                        delete index \tto delete the number index item from your cart
                        save \t\tto save your cart items to file for future use
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
