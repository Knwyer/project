import java.io.*;
import java.util.*;

// Author class
class Author implements Serializable {
    private String name;
    private String biography;

    public Author(String name, String biography) {
        this.name = name;
        this.biography = biography;
    }

    public String getName() {
        return name;
    }

    public String getBiography() {
        return biography;
    }
}

// Book class
class Book implements Serializable {
    private String title;
    private String isbn;
    private List<Author> authors;
    private int publicationYear;
    private boolean availabilityStatus = true;

    public Book(String title, String isbn, List<Author> authors, int publicationYear) {
        this.title = title;
        this.isbn = isbn;
        this.authors = authors;
        this.publicationYear = publicationYear;
    }

    public String getTitle() {
        return title;
    }

    public boolean isAvailable() {
        return availabilityStatus;
    }

    public void changeAvailabilityStatus(boolean status) {
        this.availabilityStatus = status;
    }

    public String getBookInfo() {
        return title + " (" + publicationYear + ") - ISBN: " + isbn;
    }
}

// Abstract User class
abstract class User implements Serializable {
    protected int id;
    protected String name;
    protected String email;
    protected String userType;

    public User(int id, String name, String email, String userType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.userType = userType;
    }

    public abstract void register();
    public abstract void login();
}

// Reader class
class Reader extends User {
    public Reader(int id, String name, String email) {
        super(id, name, email, "Reader");
    }

    @Override
    public void register() {
        System.out.println("Reader registered.");
    }

    @Override
    public void login() {
        System.out.println("Reader logged in.");
    }

    public void borrowBook(Book book) throws Exception {
        if (book.isAvailable()) {
            book.changeAvailabilityStatus(false);
            System.out.println("Book '" + book.getTitle() + "' borrowed successfully.");
        } else {
            throw new Exception("The book is not available.");
        }
    }
}

// Librarian class
class Librarian extends User {
    public Librarian(int id, String name, String email) {
        super(id, name, email, "Librarian");
    }

    @Override
    public void register() {
        System.out.println("Librarian registered.");
    }

    @Override
    public void login() {
        System.out.println("Librarian logged in.");
    }

    public void addBook(Book book, List<Book> libraryBooks) {
        libraryBooks.add(book);
        System.out.println("Book '" + book.getTitle() + "' added to library.");
    }

    public void editBook(Book book, String newTitle) {
        book.changeAvailabilityStatus(newTitle.equals("Available"));
        System.out.println("Book title updated to '" + newTitle + "'.");
    }

    public void deleteBook(Book book, List<Book> libraryBooks) {
        libraryBooks.remove(book);
        System.out.println("Book '" + book.getTitle() + "' removed from library.");
    }
}

// Loan class
class Loan implements Serializable {
    private Book book;
    private Reader reader;
    private Date loanDate;
    private Date returnDate;

    public Loan(Book book, Reader reader) {
        this.book = book;
        this.reader = reader;
        this.loanDate = new Date();
    }

    public void returnBook() {
        this.returnDate = new Date();
        book.changeAvailabilityStatus(true);
        System.out.println("Book '" + book.getTitle() + "' returned by reader '" + reader.name + "'.");
    }
}

// Library class
class Library implements Serializable {
    private List<Book> books = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<Loan> loans = new ArrayList<>();

    public List<Book> getBooks() {
        return books;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public Book searchBook(String title) throws Exception {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        throw new Exception("Book not found.");
    }

    public void generateReport() {
        System.out.println("Library Report:");
        System.out.println("Total Books: " + books.size());
        System.out.println("Total Users: " + users.size());
        System.out.println("Total Loans: " + loans.size());
    }
}

// Main program
public class Main {
    public static void main(String[] args) {
        try {
            Library library = new Library();
            Reader reader = new Reader(1, "Alice", "alice@example.com");
            Librarian librarian = new Librarian(2, "Bob", "bob@example.com");

            library.getUsers().add(reader);
            library.getUsers().add(librarian);

            Book book1 = new Book("Java Programming", "1234567890", new ArrayList<>(), 2024);
            librarian.addBook(book1, library.getBooks());

            reader.borrowBook(book1);

            Loan loan = new Loan(book1, reader);
            library.getLoans().add(loan);

            loan.returnBook();

            library.generateReport();

            // Save library data to a file
            saveLibraryData(library, "library.dat");
            System.out.println("Library data saved.");

            // Load library data from a file
            Library loadedLibrary = loadLibraryData("library.dat");
            System.out.println("Library data loaded. Total books: " + loadedLibrary.getBooks().size());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void saveLibraryData(Library library, String fileName) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(library);
        }
    }

    public static Library loadLibraryData(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (Library) ois.readObject();
        }
    }
}

