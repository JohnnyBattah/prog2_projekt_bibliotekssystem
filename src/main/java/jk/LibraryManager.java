package jk;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Författare: Johnny Battah
 * Klassen LibraryManager ansvarar för att lagra och hämta böcker,
 * tidningar, användare och avstängda användare i bibliotekssystemet.
 * Klassen används för sökning, sortering och kontroll av Lånerätt
 */

public class LibraryManager {
    private ArrayList<Book> books;
    private ArrayList<Magazine> magazines;
    private ArrayList<User> users;
    private ArrayList<SuspendedUser> suspendedUsers;

    public LibraryManager() {
        books = new ArrayList<>();
        magazines = new ArrayList<>();
        users = new ArrayList<>();
        suspendedUsers = new ArrayList<>();
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    public void setMagazines(ArrayList<Magazine> magazines) {
        this.magazines = magazines;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void setSuspendedUsers(ArrayList<SuspendedUser> suspendedUsers) {
        this.suspendedUsers = suspendedUsers;
    }

    public ArrayList<Book> getBooks(){
        return books;
    }
    
    public ArrayList<Magazine> getMagazines(){
        return magazines;
    }

    public ArrayList<User> getUsers(){
        return users;
    }

    public ArrayList<SuspendedUser> getSuspendedUsers(){
        return suspendedUsers;
    }

    /**
     * Lägger till en bok i samlingen
     */
    public void addBook(Book book) {
        books.add(book);
    }

    /**
     * Lägger till en tidning i samlingen
     */
    public void addMagazine(Magazine magazine) {
        magazines.add(magazine);
    }

    /**
     * Lägger till en användare i samlingen
     */
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Lägger till en avstängd användare i samlingen
     */
    public void addSuspendedUser(SuspendedUser suspendedUser) {
        suspendedUsers.add(suspendedUser);
    }

    /**
     * skriver ut böcker sorterade på titel
     */
    public void printBooksSorted() {
        ArrayList<Book> sortedBooks = new ArrayList<>(books);
        Collections.sort(sortedBooks);

        if (sortedBooks.isEmpty()) {
            IO.println("Inga böcker finns.");
        } else {
            IO.println("=== Böcker sorterade på titel ===");
            for (Book book : sortedBooks) {
                IO.println(book.getInfo());
            }
        }
    }

    /**
     * skriver ut tidningar sorterade på titel
     */
    public void printMagazinesSorted() {
        ArrayList<Magazine> sortedMagazines = new ArrayList<>(magazines);
        Collections.sort(sortedMagazines);

        if (sortedMagazines.isEmpty()) {
            IO.println("Inga tidningar finns.");
        } else {
            IO.println("=== Tidningar sorterade på titel ===");
            for (Magazine magazine : sortedMagazines) {
                IO.println(magazine.getInfo());
            }
        }
    }

    /**
     * skriver ut användare sorterade på namn
     */
    public void printUsersSorted() {
        ArrayList<User> sortedUsers = new ArrayList<>(users);
        Collections.sort(sortedUsers);

        if (sortedUsers.isEmpty()) {
            IO.println("Inga användare finns.");
        } else {
            IO.println("=== Användare sorterade på namn ===");
            for (User user : sortedUsers) {
                IO.println(user.getInfo());
            }
        }
    }

    /**
     * Hittar en bok med hjälp av titel.
     */
    public Book findBookByTitle(String title) {
        for (Book book : books){
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }

    /**
     * Hittar en tidning med hjälp av titel.
     */
    public Magazine findMagazineByTitle(String title) {
        for (Magazine magazine : magazines){
            if (magazine.getTitle().equalsIgnoreCase(title)) {
                return magazine;
            }
        }
        return null;
    }

    /**
     * Hittar en användare med hjälp av email.
     */
    public User findUserByTitle(String email) {
        for (User user : users){
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Avgör om en användare får låna eller inte.
     * Retunerar false om användaren finns i listan över avstängda
     */
    public boolean canUserBorrow(String userId){
        for (SuspendedUser suspendedUser : suspendedUsers){
            if (suspendedUser.getId().equalsIgnoreCase(userId)) {
                return false;
            }
        }
        return true;
    }

}
