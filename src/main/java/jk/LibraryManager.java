package jk;


import java.util.ArrayList;
import java.util.Collections;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.HttpResponse;

/**
 * Författare: Johnny Battah
 * Klassen LibraryManager ansvarar för att lagra och hantera böcker,
 * tidningar, användare och avstängda användare i bibliotekssystemet.
 * Klassen används för sökning, sortering och kontroll av lånerätt
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

    public ArrayList<Book> getBooks() {
        return books;
    }

    public ArrayList<Magazine> getMagazines() {
        return magazines;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<SuspendedUser> getSuspendedUsers() {
        return suspendedUsers;
    }

    public boolean fetchBooks(String baseURL, Gson gson) {
        HttpResponse<String> booksResponse;
        try {
            booksResponse = Unirest.get(baseURL + "/books").asString();
        } catch (UnirestException e) {
            IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
            return false;
        }

        int bookStatus = booksResponse.getStatus();
        if (bookStatus != 200) {
            IO.println("Fel från servern vid hämtning av böcker. Statuskod: " + bookStatus);
            return false;
        }

        String booksBody = booksResponse.getBody();
        Type bookListType = new TypeToken<ArrayList<Book>>() {
        }.getType();
        this.books = gson.fromJson(booksBody, bookListType);

        IO.println("Böcker hämtade från servern. Antal: " + books.size());
        return true;
    }

    public boolean fetchMagazines(String baseURL, Gson gson) {
        HttpResponse<String> magazinesResponse;
        try {
            magazinesResponse = Unirest.get(baseURL + "/magazines").asString();
        } catch (UnirestException e) {
            IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
            return false;
        }

        int magazineStatus = magazinesResponse.getStatus();
        if (magazineStatus != 200) {
            IO.println("Fel från servern vid hämtning av tidningar. Statuskod: " + magazineStatus);
            return false;
        }

        String magazinesBody = magazinesResponse.getBody();
        Type magazineListType = new TypeToken<ArrayList<Magazine>>() {
        }.getType();
        this.magazines = gson.fromJson(magazinesBody, magazineListType);

        IO.println("Tidningar hämtade från servern. Antal: " + magazines.size());
        return true;
    }

    public boolean fetchUsers(String baseURL, Gson gson) {
        HttpResponse<String> usersResponse;

        try {
            usersResponse = Unirest.get(baseURL + "/users").asString();
        } catch (UnirestException e) {
            IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
            return false;
        }

        int userStatus = usersResponse.getStatus();
        if (userStatus != 200) {
            IO.println("Fel från servern vid hämtning av användare. Statuskod: " + userStatus);
            return false;
        }

        String usersBody = usersResponse.getBody();
        Type userListType = new TypeToken<ArrayList<User>>() {
        }.getType();
        this.users = gson.fromJson(usersBody, userListType);

        IO.println("Användare hämtade från servern. Antal: " + users.size());
        return true;
    }

    public boolean fetchSuspendedUsers(String baseURL, Gson gson) {
        HttpResponse<String> suspendedResponse;
        try {
            suspendedResponse = Unirest.get(baseURL + "/suspended").asString();
        } catch (UnirestException e) {
            IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
            return false;
        }

        int suspendedStatus = suspendedResponse.getStatus();
        if (suspendedStatus != 200) {
            IO.println("Fel vid hämtning av avstängda användare. Statuskod: " + suspendedStatus);
            return false;
        }

        String suspendedBody = suspendedResponse.getBody();
        Type suspendedListType = new TypeToken<ArrayList<SuspendedUser>>() {
        }.getType();
        this.suspendedUsers = gson.fromJson(suspendedBody, suspendedListType);

        IO.println("Avstängda användare hämtade från servern. Antal: " + suspendedUsers.size());
        return true;
    }

    public boolean addUserToServer(String baseURL, Gson gson, String name, String email) {
        User newUser = new User(null, name, email);
        String userJson = gson.toJson(newUser);

        HttpResponse<String> postUserResponse;
        try {
            postUserResponse = Unirest.post(baseURL + "users")
                    .header("Content-Type", "application/json")
                    .body(userJson)
                    .asString();
        } catch (UnirestException e) {
            IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
            return false;
        }

        int postUserStatus = postUserResponse.getStatus();
        if (postUserStatus != 201 && postUserStatus != 200) {
            IO.println("Fel vid skapande av användare. Statuskod: " + postUserStatus);
            IO.println("Svar från servern: " + postUserResponse.getBody());
            return false;
        }

        User savedUser = gson.fromJson(postUserResponse.getBody(), User.class);
        users.add(savedUser);

        IO.println("Användaren lades till på servern och i den lokala samlingen.");
        return true;
    }

    public boolean addBookToServer(String baseURL, Gson gson, String title, String author, String genre, int pages) {
        Book newBook = new Book(null, title, true, author, genre, pages);
        String bookJson = gson.toJson(newBook);

        HttpResponse<String> postBookResponse;
        try {
            postBookResponse = Unirest.post(baseURL + "books")
                    .header("Content-Type", "application/json")
                    .body(bookJson)
                    .asString();
        } catch (UnirestException e) {
            IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
            return false;
        }

        int postBookStatus = postBookResponse.getStatus();
        if (postBookStatus != 201 && postBookStatus != 200) {
            IO.println("Fel vid skapande av bok. Statuskod: " + postBookStatus);
            IO.println("Svar från servern: " + postBookResponse.getBody());
            return false;
        }

        Book savedBook = gson.fromJson(postBookResponse.getBody(), Book.class);
        books.add(savedBook);

        IO.println("Boken lades till på servern och i den lokala samlingen.");
        return true;
    }

    public boolean addMagazineToServer(String baseURL, Gson gson, String title, String category, int issueNumber,
            int publishedYear) {
        Magazine newServerMagazine = new Magazine(null, title, true, issueNumber, category, publishedYear);
        String magazineJson = gson.toJson(newServerMagazine);

        HttpResponse<String> postMagazineResponse;
        try {
            postMagazineResponse = Unirest.post(baseURL + "magazines")
                    .header("Content-Type", "application/json")
                    .body(magazineJson)
                    .asString();
        } catch (UnirestException e) {
            IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
            return false;
        }

        int postMagazineStatus = postMagazineResponse.getStatus();
        if (postMagazineStatus != 201 && postMagazineStatus != 200) {
            IO.println("Fel vid skapande av tidning. Statuskod: " + postMagazineStatus);
            IO.println("Svar från servern: " + postMagazineResponse.getBody());
            return false;
        }

        Magazine savedMagazine = gson.fromJson(postMagazineResponse.getBody(), Magazine.class);
        magazines.add(savedMagazine);

        IO.println("Tidningen lades till på servern och i den lokala samlingen.");
        return true;
    }

    public boolean addSuspendedUserToServer(String baseURL, Gson gson, String id, String customerId) {
        SuspendedUser newSuspendedUser = new SuspendedUser(id, customerId);
        String suspendedJson = gson.toJson(newSuspendedUser);

        HttpResponse<String> postSuspendedResponse;
        try {
            postSuspendedResponse = Unirest.post(baseURL + "suspended")
                    .header("Content-Type", "application/json")
                    .body(suspendedJson)
                    .asString();
        } catch (UnirestException e) {
            IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
            return false;
        }

        int postSuspendedStatus = postSuspendedResponse.getStatus();
        if (postSuspendedStatus != 201 && postSuspendedStatus != 200) {
            IO.println("Fel vid skapande av avstängd användare. Statuskod: " + postSuspendedStatus);
            IO.println("Svar från servern: " + postSuspendedResponse.getBody());
            return false;
        }

        SuspendedUser savedSuspendedUser = gson.fromJson(postSuspendedResponse.getBody(), SuspendedUser.class);
        suspendedUsers.add(savedSuspendedUser);

        IO.println("Den avstängda användaren lades till på servern och i den lokala samlingen.");
        return true;
    }

    public boolean deleteUserByEmailFromServer(String baseURL, String email) {
        User userToDelete = findUserByEmail(email);

        if (userToDelete == null) {
            IO.println("Ingen användare hittades med den e-postadressen.");
            return false;
        }

        String userIdToDelete = userToDelete.getId();

        HttpResponse<String> deleteUserResponse;
        try {
            deleteUserResponse = Unirest.delete(baseURL + "users/" + userIdToDelete).asString();
        } catch (UnirestException e) {
            IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
            return false;
        }

        int deleteUserStatus = deleteUserResponse.getStatus();
        if (deleteUserStatus != 200 && deleteUserStatus != 204) {
            IO.println("Fel vid borttagning av användare. Statuskod: " + deleteUserStatus);
            IO.println("Svar från servern: " + deleteUserResponse.getBody());
            return false;
        }

        users.remove(userToDelete);
        IO.println("Användaren togs bort från servern och från den lokala samlingen.");
        return true;
    }

    public boolean deleteBookByTitleFromServer(String baseURL, String title) {
        Book bookToDelete = findBookByTitle(title);

        if (bookToDelete == null) {
            IO.println("Ingen bok hittades med den titeln.");
            return false;
        }

        String bookIdToDelete = bookToDelete.getId();

        HttpResponse<String> deleteBookResponse;
        try {
            deleteBookResponse = Unirest.delete(baseURL + "books/" + bookIdToDelete).asString();
        } catch (UnirestException e) {
            IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
            return false;
        }

        int deleteBookStatus = deleteBookResponse.getStatus();
        if (deleteBookStatus != 200 && deleteBookStatus != 204) {
            IO.println("Fel vid borttagning av bok. Statuskod: " + deleteBookStatus);
            IO.println("Svar från servern: " + deleteBookResponse.getBody());
            return false;
        }

        books.remove(bookToDelete);
        IO.println("Boken togs bort från servern och från den lokala samlingen.");
        return true;
    }

    public boolean deleteMagazineByTitleFromServer(String baseURL, String title) {
        Magazine magazineToDelete = findMagazineByTitle(title);

        if (magazineToDelete == null) {
            IO.println("Ingen tidning hittades med den titeln.");
            return false;
        }

        String magazineIdToDelete = magazineToDelete.getId();

        HttpResponse<String> deleteMagazineResponse;
        try {
            deleteMagazineResponse = Unirest.delete(baseURL + "magazines/" + magazineIdToDelete)
                    .asString();
        } catch (UnirestException e) {
            IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
            return false;
        }

        int deleteMagazineStatus = deleteMagazineResponse
                .getStatus();
        if (deleteMagazineStatus != 200 && deleteMagazineStatus != 204) {
            IO.println("Fel vid borttagning av tidning. Statuskod: " + deleteMagazineStatus);
            IO.println("Svar från servern: " + deleteMagazineResponse.getBody());
            return false;
        }

        magazines.remove(magazineToDelete);
        IO.println("Tidningen togs bort från servern och från den lokala samlingen.");
        return true;
    }

    public boolean deleteSuspendedUserByIdFromServer(String baseURL, String id) {
        HttpResponse<String> deleteSuspendedResponse;
        try {
            deleteSuspendedResponse = Unirest.delete(baseURL + "suspended/" + id).asString();
        } catch (UnirestException e) {
            IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
            return false;
        }

        int deleteSuspendedStatus = deleteSuspendedResponse
                .getStatus();
        if (deleteSuspendedStatus != 200 && deleteSuspendedStatus != 204) {
            IO.println("Fel vid borttagning av avstängd användare. Statuskod: " + deleteSuspendedStatus);
            IO.println("Svar från servern: " + deleteSuspendedResponse.getBody());
            return false;
        }

        removeSuspendedUserById(id);
        IO.println("Den avstängda användaren togs bort från servern och från den lokala samlingen.");

        return true;
    }

    /**
     * Hittar en användare med hjälp av id.
     */
    public User findUserById(String id){
        for (User user : users) {
            if (user.getId().equalsIgnoreCase(id)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Kontrollerar om e-postadressen redan finns bland användarna.
     */
    public boolean emailExists(String email){
        for (User user : users){
            if (user.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Kontrollerar om en användare redan finns i listan över avstängda användare.
     */
    public boolean isUserAlreadySuspended(String customerId){
        for (SuspendedUser suspendedUser : suspendedUsers){
            if (suspendedUser.getCustomer_id().equalsIgnoreCase(customerId)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Kontrollerar om en boktitel redan finns i samlingen.
     */
    public boolean bookTitleExists(String title){
        for (Book book : books){
            if (book.getTitle().equalsIgnoreCase(title)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Kontrollerar om en tidningstitel redan finns i samlingen.
     */
    public boolean magazineTitleExists(String title){
        for (Magazine magazine : magazines){
            if (magazine.getTitle().equalsIgnoreCase(title)) {
                return true;
            }
        }
        return false;
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

    public void removeBook(Book book) {
        books.remove(book);
    }

    public void removeMagazine(Magazine magazine) {
        magazines.remove(magazine);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public void removeSuspendedUserById(String id) {
        for (int i = 0; i < suspendedUsers.size(); i++) {
            if (suspendedUsers.get(i).getId().equalsIgnoreCase(id)) {
                suspendedUsers.remove(i);
                return;
            }
        }
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

    public void printSuspendedUsersSorted() {
        ArrayList<SuspendedUser> sortedSuspendedUsers = new ArrayList<>(suspendedUsers);
        Collections.sort(sortedSuspendedUsers);

        if (sortedSuspendedUsers.isEmpty()) {
            IO.println("Inga avstängda användare finns.");
        } else {
            IO.println("=== Avstängda användare ===");
            for (SuspendedUser suspendedUser : sortedSuspendedUsers) {
                IO.println(suspendedUser.getInfo());
            }
        }
    }

    /**
     * Hittar en bok med hjälp av titel.
     */
    public Book findBookByTitle(String title) {
        for (Book book : books) {
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
        for (Magazine magazine : magazines) {
            if (magazine.getTitle().equalsIgnoreCase(title)) {
                return magazine;
            }
        }
        return null;
    }

    /**
     * Hittar en användare med hjälp av email.
     */
    public User findUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Avgör om en användare får låna eller inte.
     * Returnerar false om användaren finns i listan över avstängda
     */
    public boolean canUserBorrow(String customerId) {
        for (SuspendedUser suspendedUser : suspendedUsers) {
            if (suspendedUser.getCustomer_id().equalsIgnoreCase(customerId)) {
                return false;
            }
        }
        return true;
    }

}
