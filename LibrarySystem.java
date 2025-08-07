import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

class Book {
    private String id;
    private String title;
    private String author;
    private boolean available;

    public Book(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.available = true;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return available; }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return id + ". " + title + " by " + author;
    }
}

class User {
    private String id;
    private String name;
    private List<Book> borrowedBooks;
    private final int MAX_BOOKS = 3;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.borrowedBooks = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public List<Book> getBorrowedBooks() { return borrowedBooks; }

    public boolean canBorrow() {
        return borrowedBooks.size() < MAX_BOOKS;
    }

    public void addBook(Book book) {
        borrowedBooks.add(book);
    }

    public void removeBook(Book book) {
        borrowedBooks.remove(book);
    }

    @Override
    public String toString() {
        return id + "," + name;
    }
}

class Library {
    private List<Book> books;
    private List<User> users;
    private Scanner scanner;

    private static final String USER_FILE_PATH = "userdata.txt";
    private AtomicInteger userIdCounter = new AtomicInteger(0);

    public Library() {
        this.books = new ArrayList<>();
        this.users = new ArrayList<>();
        this.scanner = new Scanner(System.in);

        books.add(new Book("B001", "Java Programming", "James Gosling"));
        books.add(new Book("B002", "Data Structures", "Robert Sedgewick"));
        books.add(new Book("B003", "Algorithms", "Thomas Cormen"));
        books.add(new Book("B004", "Database Systems", "Ramez Elmasri"));
        books.add(new Book("B005", "Operating Systems", "Abraham Silberschatz"));

        loadUsersFromFile();
    }

    private void loadUsersFromFile() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(USER_FILE_PATH));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    User user = new User(parts[0], parts[1]);
                    users.add(user);
                    int currentId = Integer.parseInt(parts[0].substring(1));
                    if (currentId > userIdCounter.get()) {
                        userIdCounter.set(currentId);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("User data file not found or empty. Please create new users.");
        }
    }

    private void saveUsersToFile() {
        try (FileWriter writer = new FileWriter(USER_FILE_PATH)) {
            for (User user : users) {
                writer.write(user.toString() + "\n");
            }
            System.out.println("* User data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }

    private void createUser() {
        System.out.println("\n--- Create New User ---");
        System.out.print("Enter new user's name: ");
        String name = scanner.nextLine();

        String newUserId = "U" + String.format("%03d", userIdCounter.incrementAndGet());

        User newUser = new User(newUserId, name);
        users.add(newUser);

        System.out.println("* New user '" + name + "' created with ID: " + newUserId);
        saveUsersToFile();
    }

    public void showMenu() {
        System.out.println("\n=== Library Management System ===");
        System.out.println("1. Issue Book");
        System.out.println("2. Return Book");
        System.out.println("3. View Available Books");
        System.out.println("4. View User Books");
        System.out.println("5. Create New User");
        System.out.println("6. Exit");
        System.out.print("Choose option: ");
    }

    public void issueBookProcess() {
        if (users.isEmpty()) {
            System.out.println("No users found. Let's create one first.");
            createUser();
        }

        System.out.println("\n--- Available Books ---");
        showAvailableBooks();

        if (getAvailableBooks().isEmpty()) {
            System.out.println("No books available for issuing.");
            return;
        }

        System.out.print("Enter Book ID to issue: ");
        String bookId = scanner.nextLine();

        System.out.println("\n--- Users ---");
        showUsers();
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();

        issueBook(bookId, userId);
    }

    public void returnBookProcess() {
        System.out.println("\n--- Users with Books ---");
        showUsersWithBooks();
        if (users.stream().noneMatch(user -> !user.getBorrowedBooks().isEmpty())) {
            System.out.println("No users have borrowed books.");
            return;
        }

        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();

        User user = findUser(userId);
        if (user == null || user.getBorrowedBooks().isEmpty()) {
            System.out.println("User not found or has no books.");
            return;
        }

        System.out.println("\n--- " + user.getName() + "'s Books ---");
        for (Book book : user.getBorrowedBooks()) {
            System.out.println(book);
        }

        System.out.print("Enter Book ID to return: ");
        String bookId = scanner.nextLine();

        returnBook(bookId, userId);
    }

    private void showAvailableBooks() {
        for (Book book : books) {
            if (book.isAvailable()) {
                System.out.println(book);
            }
        }
    }

    private List<Book> getAvailableBooks() {
        List<Book> available = new ArrayList<>();
        for (Book book : books) {
            if (book.isAvailable()) {
                available.add(book);
            }
        }
        return available;
    }

    private void showUsers() {
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }
        for (User user : users) {
            System.out.println(user.getId() + ". " + user.getName());
        }
    }

    private void showUsersWithBooks() {
        for (User user : users) {
            if (!user.getBorrowedBooks().isEmpty()) {
                System.out.println(user.getId() + ". " + user.getName() + " (" + user.getBorrowedBooks().size() + " books)");
            }
        }
    }

    private Book findBook(String id) {
        for (Book book : books) {
            if (book.getId().equals(id)) {
                return book;
            }
        }
        return null;
    }

    private User findUser(String id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    private void issueBook(String bookId, String userId) {
        Book book = findBook(bookId);
        User user = findUser(userId);

        if (book == null) {
            System.out.println("Book not found!");
            return;
        }

        if (user == null) {
            System.out.println("User not found!");
            return;
        }

        if (!book.isAvailable()) {
            System.out.println("Book is already issued!");
            return;
        }

        if (!user.canBorrow()) {
            System.out.println("User has reached maximum book limit (3 books)!");
            return;
        }

        book.setAvailable(false);
        user.addBook(book);
        System.out.println("* Book '" + book.getTitle() + "' issued to " + user.getName());
    }

    private void returnBook(String bookId, String userId) {
        Book book = findBook(bookId);
        User user = findUser(userId);

        if (book == null) {
            System.out.println("Book not found!");
            return;
        }

        if (user == null) {
            System.out.println("User not found!");
            return;
        }

        if (!user.getBorrowedBooks().contains(book)) {
            System.out.println("User doesn't have this book!");
            return;
        }

        book.setAvailable(true);
        user.removeBook(book);
        System.out.println("* Book '" + book.getTitle() + "' returned by " + user.getName());
    }

    public void viewUserBooks() {
        System.out.println("\n--- Users ---");
        showUsers();
        if (users.isEmpty()) {
            return;
        }
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();

        User user = findUser(userId);
        if (user == null) {
            System.out.println("User not found!");
            return;
        }

        System.out.println("\n" + user.getName() + " has borrowed:");
        if (user.getBorrowedBooks().isEmpty()) {
            System.out.println("No books borrowed.");
        } else {
            for (Book book : user.getBorrowedBooks()) {
                System.out.println("- " + book);
            }
        }
    }

    public void run() {
        while (true) {
            showMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    issueBookProcess();
                    break;
                case "2":
                    returnBookProcess();
                    break;
                case "3":
                    System.out.println("\n--- Available Books ---");
                    showAvailableBooks();
                    break;
                case "4":
                    viewUserBooks();
                    break;
                case "5":
                    createUser();
                    break;
                case "6":
                    System.out.println("Thank you for using Library System!");
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }
}

public class LibrarySystem {
    public static void main(String[] args) {
        Library library = new Library();
        library.run();
    }
}
