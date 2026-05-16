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

    public LibraryManager(){
        books = new ArrayList<>();
        magazines = new ArrayList<>();
        users = new ArrayList<>();
        suspendedUsers = new ArrayList<>();
    }

    


}
