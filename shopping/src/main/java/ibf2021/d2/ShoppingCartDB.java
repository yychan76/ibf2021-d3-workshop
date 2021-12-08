package ibf2021.d2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShoppingCartDB {
    private static final String DEFAULT_DB_ROOT_FOLDER = "db";
    private static final String DEFAULT_DB_FILE_EXTENSION = ".db";
    private String dbRoot;
    private String userName;

    public ShoppingCartDB() throws IOException {
        this.dbRoot = initDBFolder(DEFAULT_DB_ROOT_FOLDER);
    }

    public ShoppingCartDB(String dbRoot) throws IOException {
        this.dbRoot = initDBFolder(dbRoot);
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String initDBFolder(String folderName) throws IOException {
        Path path = Paths.get(folderName);
        if(!Files.exists(path)) {
            folderName = Files.createDirectories(path).toString();
        }
        System.out.println("Using database root folder: " + folderName);
        return folderName;
    }

    public List<String> listUsers() throws IOException {
        try(Stream<Path> stream = Files.list(Paths.get(this.dbRoot))) {
            return stream
                .filter(file -> !Files.isDirectory(file))
                .map(Path::getFileName)
                .map(Path::toString)
                .map(filename -> removeFileExtension(filename, true))
                .collect(Collectors.toList());
        }
    }

    private Path getDBFile() throws IOException {
        Path dbFile = Paths.get(this.dbRoot, this.userName.toLowerCase() + DEFAULT_DB_FILE_EXTENSION);
        if (!Files.exists(dbFile)) {
            Files.createFile(dbFile);
        }
        return dbFile;
    }

    public List<String> load() throws IOException {
        if (this.userName == null) {
            System.out.println("User not logged in. Please enter: \nlogin yourname");
            return new ArrayList<String>();
        }
        Path dbFile = getDBFile();
        return Files.readAllLines(dbFile);
    }

    public void save(List<String> cartItems) throws IOException {
        if (this.userName == null) {
            System.out.println("User not logged in. Please enter: \nlogin yourname");
            return;
        }
        Path dbFile = getDBFile();
        System.out.println("Writing to database file: " + dbFile.toAbsolutePath().toString());
        Files.write(dbFile, cartItems);
        System.out.println("Your cart has been saved");
    }

    // from https://www.baeldung.com/java-filename-without-extension
    public static String removeFileExtension(String filename, boolean removeAllExtensions) {
        if (filename == null || filename.isEmpty()) {
            return filename;
        }

        String extPattern = "(?<!^)[.]" + (removeAllExtensions ? ".*" : "[^.]*$");
        return filename.replaceAll(extPattern, "");
    }

    public static void main(String[] args) throws IOException {
        ShoppingCartDB cartDB = new ShoppingCartDB();
        List<String> items = new ArrayList<String>( Arrays.asList("apple", "orange", "pear"));
        cartDB.setUserName("John");
        cartDB.save(items);
        System.out.println(cartDB.load());
        System.out.println(cartDB.listUsers());
    }

}
