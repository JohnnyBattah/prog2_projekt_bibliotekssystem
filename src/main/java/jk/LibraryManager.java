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

    /**
     * Lägger till en bok i samlingen
     */
    public void addBook(Book book){
        books.add(book);
    }

    /**
     * Lägger till en tidning i samlingen
     */
    public void addMagazine(Magazine magazine){
        magazines.add(magazine);
    }

    /**
     * Lägger till en användare i samlingen
     */
    public void addUser(User user){
        users.add(user);
    }

    /**
     * Lägger till en avstängd användare i samlingen
     */
    public void addSuspendedUser(SuspendedUser suspendedUser){
        suspendedUsers.add(suspendedUser);
    }

    /**
     * skriver ut böcker sorterade på titel
     */
    public void printBooksSorted(){
        ArrayList<Book> sortedBooks = new ArrayList<>(books);
        Collections.sort(sortedBooks);

        if (sortedBooks.isEmpty()) {
            IO.println("Inga böcker finns.");
        } else {
            IO.println("=== Böcker sorterade på titel ===");
            for (Book book : sortedBooks){
                IO.println(book.getInfo());
            }
        }
    }
}
