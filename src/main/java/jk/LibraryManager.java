package jk;

import java.util.ArrayList;
import java.util.Collections;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.HttpResponse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Författare: Johnny Battah
 * Klassen LibraryManager ansvarar för att lagra och hantera böcker,
 * tidningar, användare och avstängda användare i bibliotekssystemet.
 * Klassen används för sökning, sortering och kontroll av lånerätt.
 */

public class LibraryManager {
    private ArrayList<Book> books;
    private ArrayList<Magazine> magazines;
    private ArrayList<User> users;
    private ArrayList<SuspendedUser> suspendedUsers;

    private Map<String, User> userMap;
    private Set<String> suspendedIdSet;

    private String baseURL;
    private Gson gson;

    public LibraryManager() {
        // skoladress: http://10.151.168.5:3137/
        // hemadress: http://localhost:3000/
        baseURL = "http://localhost:3000/";
        gson = new Gson();

        books = new ArrayList<>();
        magazines = new ArrayList<>();
        users = new ArrayList<>();
        suspendedUsers = new ArrayList<>();
        userMap = new HashMap<>();
        suspendedIdSet = new HashSet<>();
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

    /**
     * Hämtar alla böcker från servern och sparar dem i listan.
     */
    public boolean fetchBooks() {
        IO.println("Hämtar alla böcker...");

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

    /**
     * Hämtar en bok från servern med hjälp av id.
     */
    public boolean fetchOneBook() {
        String bookId = IO.readln("Ange bokens id: ").trim();

        if (bookId.isBlank()) {
            IO.println("Id får inte vara tomt.");
            return false;
        }

        HttpResponse<String> response;

        try {
            response = Unirest.get(baseURL + "/books/" + bookId).asString();
        } catch (UnirestException e) {
            IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
            return false;
        }

        if (response.getStatus() != 200) {
            IO.println("Ingen bok hittades med det id:t. Statuskod: " + response.getStatus());
            return false;
        }

        Book book = gson.fromJson(response.getBody(), Book.class);
        IO.println("Bok hämtad från servern:");
        IO.println(book.getInfo());
        return true;
    }

    /**
     * Hämtar alla tidningar från servern och sparar dem i listan.
     */
    public boolean fetchMagazines() {
        IO.println("Hämtar alla tidningar...");
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

    /**
     * Hämtar en tidning från servern med hjälp av id.
     */
    public boolean fetchOneMagazine() {
        String magazineId = IO.readln("Ange tidningens id: ").trim();

        if (magazineId.isBlank()) {
            IO.println("Id får inte vara tomt.");
            return false;
        }

        HttpResponse<String> response;

        try {
            response = Unirest.get(baseURL + "/magazines/" + magazineId).asString();
        } catch (UnirestException e) {
            IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
            return false;
        }

        if (response.getStatus() != 200) {
            IO.println("Ingen tidning hittades med det id:t. Statuskod: " + response.getStatus());
            return false;
        }

        Magazine magazine = gson.fromJson(response.getBody(), Magazine.class);
        IO.println("Tidning hämtad från servern:");
        IO.println(magazine.getInfo());
        return true;
    }

    /**
     * Hämtar alla användare från servern och sparar dem i listan.
     */
    public boolean fetchUsers() {
        IO.println("Hämtar alla användare...");
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

        userMap.clear();
        for (User user : users) {
            userMap.put(user.getEmail().toLowerCase(), user);
        }

        IO.println("Användare hämtade från servern. Antal: " + users.size());
        return true;
    }

    /**
     * Hämtar en användare från servern med hjälp av id.
     */
    public boolean fetchOneUser() {
        String userId = IO.readln("Ange användarens id: ").trim();

        if (userId.isBlank()) {
            IO.println("Id får inte vara tomt.");
            return false;
        }

        HttpResponse<String> response;

        try {
            response = Unirest.get(baseURL + "/users/" + userId).asString();
        } catch (UnirestException e) {
            IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
            return false;
        }

        if (response.getStatus() != 200) {
            IO.println("Ingen användare hittades med det id:t. Statuskod: " + response.getStatus());
            return false;
        }

        User user = gson.fromJson(response.getBody(), User.class);
        IO.println("Användare hämtad från servern:");
        IO.println(user.getInfo());
        return true;
    }

    /**
     * Hämtar alla avstängda användare från servern och sparar dem i listan.
     */
    public boolean fetchSuspendedUsers() {
        IO.println("Hämtar alla avstängda användare...");
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

        suspendedIdSet.clear();
        for (SuspendedUser suspendedUser : suspendedUsers) {
            suspendedIdSet.add(suspendedUser.getCustomer_id());
        }

        IO.println("Avstängda användare hämtade från servern. Antal: " + suspendedUsers.size());
        return true;
    }

    /**
     * Hämtar en avstängd användare från servern med hjälp av id.
     */
    public boolean fetchOneSuspendedUser() {
        String suspendedId = IO.readln("Ange avstängningens id: ").trim();

        if (suspendedId.isBlank()) {
            IO.println("Id får inte vara tomt.");
            return false;
        }

        HttpResponse<String> response;

        try {
            response = Unirest.get(baseURL + "/suspended/" + suspendedId).asString();
        } catch (UnirestException e) {
            IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
            return false;
        }

        if (response.getStatus() != 200) {
            IO.println("Ingen avstängd användare hittades med det id:t. Statuskod: " + response.getStatus());
            return false;
        }

        SuspendedUser suspendedUser = gson.fromJson(response.getBody(), SuspendedUser.class);
        IO.println("Avstängd användare hämtad från servern:");
        IO.println(suspendedUser.getInfo());
        return true;
    }

    /**
     * Lägger till en ny bok på servern och sparar den lokalt i listan.
     */
    public boolean addBookToServer(String title, String author, String genre, int pages) {
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

    /**
     * Läser in information om en bok och lägger till den på servern.
     */
    public boolean addBook() {
        IO.println("Lägger till en bok på servern...");
        String postBookTitle = IO.readln("Ange titel: ").trim();
        if (postBookTitle.isBlank()) {
            IO.println("Titel får inte vara tom.");
            return false;
        }

        if (bookTitleExists(postBookTitle)) {
            IO.println("Det finns redan en bok med den titeln.");
            return false;
        }

        String postBookAuthor = IO.readln("Ange författare: ").trim();
        if (postBookAuthor.isBlank()) {
            IO.println("Författare får inte vara tom.");
            return false;
        }

        String postBookGenre = IO.readln("Ange genre: ").trim();
        if (postBookGenre.isBlank()) {
            IO.println("Genre får inte vara tom.");
            return false;
        }

        int postBookPages;
        try {
            postBookPages = Integer.parseInt(IO.readln("Ange antal sidor: ").trim());
        } catch (NumberFormatException e) {
            IO.println("Felaktigt antal sidor.");
            return false;
        }

        if (postBookPages <= 0) {
            IO.println("Antal sidor måste vara större än 0.");
            return false;
        }

        return addBookToServer(postBookTitle, postBookAuthor, postBookGenre, postBookPages);
    }

    /**
     * Lägger till en ny tidning på servern och sparar den lokalt i listan.
     */
    public boolean addMagazineToServer(String title, String category, int issueNumber,
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

    /**
     * Läser in information om en tidning och lägger till den på servern.
     */
    public boolean addMagazine() {
        IO.println("Lägger till en tidning på servern...");
        String postMagazineTitle = IO.readln("Ange titel: ").trim();
        if (postMagazineTitle.isBlank()) {
            IO.println("Titel får inte vara tom.");
            return false;
        }

        if (magazineTitleExists(postMagazineTitle)) {
            IO.println("Det finns redan en tidning med den titeln.");
            return false;
        }

        String postMagazineCategory = IO.readln("Ange kategori: ").trim();
        if (postMagazineCategory.isBlank()) {
            IO.println("Kategori får inte vara tom.");
            return false;
        }

        int postIssueNumber;
        try {
            postIssueNumber = Integer.parseInt(IO.readln("Ange nummer: ").trim());
        } catch (NumberFormatException e) {
            IO.println("Felaktigt nummer.");
            return false;
        }

        if (postIssueNumber <= 0) {
            IO.println("Nummer måste vara större än 0.");
            return false;
        }

        int postPublishedYear;
        try {
            postPublishedYear = Integer.parseInt(IO.readln("Ange publiceringsår: ").trim());
        } catch (NumberFormatException e) {
            IO.println("Felaktigt år.");
            return false;
        }

        if (postPublishedYear <= 0) {
            IO.println("Publiceringsår måste vara större än 0.");
            return false;
        }

        return addMagazineToServer(postMagazineTitle, postMagazineCategory, postIssueNumber, postPublishedYear);
    }

    /**
     * Lägger till en ny användare på servern och sparar den lokalt i listan.
     */
    public boolean addUserToServer(String name, String email) {
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
        userMap.put(savedUser.getEmail().toLowerCase(), savedUser);

        IO.println("Användaren lades till på servern och i den lokala samlingen.");
        return true;
    }

    /**
     * Läser in information om en användare och lägger till den på servern.
     */
    public boolean addUser() {
        IO.println("Lägger till en användare på servern...");
        String userName = IO.readln("Ange namn: ").trim();
        if (userName.isBlank()) {
            IO.println("Namn får inte vara tomt.");
            return false;
        }
        String userEmail = IO.readln("Ange e-post: ").trim();
        if (userEmail.isBlank()) {
            IO.println("E-post får inte vara tom.");
            return false;
        }

        if (emailExists(userEmail)) {
            IO.println("Det finns redan en användare med den e-postadressen.");
            return false;
        }

        return addUserToServer(userName, userEmail);
    }

    /**
     * Lägger till en ny avstängd användare på servern och sparar den lokalt i
     * listan.
     */
    public boolean addSuspendedUserToServer(String customerId) {
        SuspendedUser newSuspendedUser = new SuspendedUser(null, customerId);
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
        suspendedIdSet.add(savedSuspendedUser.getCustomer_id());

        IO.println("Den avstängda användaren lades till på servern och i den lokala samlingen.");
        return true;
    }

    /**
     * Läser in ett användar-id och lägger till en avstängd användare på servern.
     */
    public boolean addSuspendedUser() {
        IO.println("Lägger till en avstängd användare på servern...");

        if (users.isEmpty()) {
            IO.println("Inga användare är hämtade. Hämta användare först.");
            return false;
        }

        String customerId = IO.readln("Ange användarens id: ").trim();
        if (customerId.isBlank()) {
            IO.println("Användarens id får inte vara tomt.");
            return false;
        }

        User userToSuspend = findUserById(customerId);

        if (userToSuspend == null) {
            IO.println("Ingen användare hittades med det id:t.");
            return false;
        }

        if (isUserAlreadySuspended(customerId)) {
            IO.println("Användaren är redan avstängd.");
            return false;
        }

        return addSuspendedUserToServer(customerId);
    }

    /**
     * Tar bort en bok från servern och lokalt med hjälp av titel.
     */
    public boolean deleteBookByTitleFromServer(String title) {
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

        if (deleteBookStatus == 404) {
            IO.println("Ingen bok hittades med det id:t.");
            return false;
        }

        if (deleteBookStatus != 200 && deleteBookStatus != 204) {
            IO.println("Fel vid borttagning av bok. Statuskod: " + deleteBookStatus);
            IO.println("Svar från servern: " + deleteBookResponse.getBody());
            return false;
        }

        books.remove(bookToDelete);
        IO.println("Boken togs bort från servern och från den lokala samlingen.");
        return true;
    }

    /**
     * Läser in titel och tar bort en bok från servern.
     */
    public boolean deleteBook() {
        IO.println("Tar bort bok via titel...");

        if (getBooks().isEmpty()) {
            IO.println("Inga böcker är hämtade. Hämta böcker först.");
            return false;
        }

        String titleToDelete = IO.readln("Ange titel på boken som ska tas bort: ").trim();

        if (titleToDelete.isBlank()) {
            IO.println("Titel får inte vara tom.");
            return false;
        }

        return deleteBookByTitleFromServer(titleToDelete);
    }

    /**
     * Tar bort en tidning från servern och lokalt med hjälp av titel.
     */
    public boolean deleteMagazineByTitleFromServer(String title) {
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

        if (deleteMagazineStatus == 404) {
            IO.println("Ingen tidning hittades med det id:t.");
            return false;
        }

        if (deleteMagazineStatus != 200 && deleteMagazineStatus != 204) {
            IO.println("Fel vid borttagning av tidning. Statuskod: " + deleteMagazineStatus);
            IO.println("Svar från servern: " + deleteMagazineResponse.getBody());
            return false;
        }

        magazines.remove(magazineToDelete);
        IO.println("Tidningen togs bort från servern och från den lokala samlingen.");
        return true;
    }

    /**
     * Läser in titel och tar bort en tidning från servern.
     */
    public boolean deleteMagazine() {
        IO.println("Tar bort tidning via titel...");

        if (getMagazines().isEmpty()) {
            IO.println("Inga tidningar är hämtade. Hämta tidningar först.");
            return false;
        }

        String magazineTitleToDelete = IO.readln("Ange titel på tidningen som ska tas bort: ").trim();

        if (magazineTitleToDelete.isBlank()) {
            IO.println("Titel får inte vara tom.");
            return false;
        }

        return deleteMagazineByTitleFromServer(magazineTitleToDelete);
    }

    /**
     * Tar bort en användare från servern och lokalt med hjälp av e-post.
     */
    public boolean deleteUserByEmailFromServer(String email) {
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
        if (deleteUserStatus == 404) {
            IO.println("Ingen användare hittades med det id:t.");
            return false;
        }

        if (deleteUserStatus != 200 && deleteUserStatus != 204) {
            IO.println("Fel vid borttagning av användare. Statuskod: " + deleteUserStatus);
            IO.println("Svar från servern: " + deleteUserResponse.getBody());
            return false;
        }

        users.remove(userToDelete);
        userMap.remove(userToDelete.getEmail().toLowerCase());
        IO.println("Användaren togs bort från servern och från den lokala samlingen.");
        return true;
    }

    /**
     * Läser in e-post och tar bort en användare från servern.
     */
    public boolean deleteUser() {
        IO.println("Tar bort användare via e-post...");

        if (users.isEmpty()) {
            IO.println("Inga användare är hämtade. Hämta användare först.");
            return false;
        }

        String emailToDelete = IO.readln("Ange e-post för användare som ska tas bort: ").trim();

        if (emailToDelete.isBlank()) {
            IO.println("E-post får inte vara tom.");
            return false;
        }

        return deleteUserByEmailFromServer(emailToDelete);
    }

    /**
     * Tar bort en avstängd användare från servern och lokalt med hjälp av id.
     */
    public boolean deleteSuspendedUserByIdFromServer(String id) {
        HttpResponse<String> deleteSuspendedResponse;
        try {
            deleteSuspendedResponse = Unirest.delete(baseURL + "suspended/" + id).asString();
        } catch (UnirestException e) {
            IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
            return false;
        }

        int deleteSuspendedStatus = deleteSuspendedResponse
                .getStatus();

        if (deleteSuspendedStatus == 404) {
            IO.println("Ingen avstängd användare hittades med det id:t.");
            return false;
        }

        if (deleteSuspendedStatus != 200 && deleteSuspendedStatus != 204) {
            IO.println("Fel vid borttagning av avstängd användare. Statuskod: " + deleteSuspendedStatus);
            return false;
        }

        removeSuspendedUserById(id);

        suspendedIdSet.clear();
        for (SuspendedUser suspendedUser : suspendedUsers) {
            suspendedIdSet.add(suspendedUser.getCustomer_id());
        }
        IO.println("Den avstängda användaren togs bort från servern och från den lokala samlingen.");

        return true;
    }

    /**
     * Läser in ett id och tar bort en avstängd användare från servern.
     */
    public boolean deleteSuspendedUser() {
        IO.println("Tar bort avstängd användare via id...");

        if (getSuspendedUsers().isEmpty()) {
            IO.println("Inga avstängda användare är hämtade. Hämta avstängda användare först.");
            return false;
        }

        String suspendedIdToDelete = IO.readln("Ange id på avstängningen som ska tas bort: ").trim();

        if (suspendedIdToDelete.isBlank()) {
            IO.println("Id får inte vara tomt.");
            return false;
        }

        return deleteSuspendedUserByIdFromServer(suspendedIdToDelete);
    }

    /**
     * Kontrollerar om en boktitel redan finns i samlingen.
     */
    public boolean bookTitleExists(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Kontrollerar om en tidningstitel redan finns i samlingen.
     */
    public boolean magazineTitleExists(String title) {
        for (Magazine magazine : magazines) {
            if (magazine.getTitle().equalsIgnoreCase(title)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Kontrollerar om e-postadressen redan finns bland användarna via HashMap.
     */
    public boolean emailExists(String email) {
        return userMap.containsKey(email.toLowerCase());
    }

    /**
     * Kontrollerar om en användares id redan finns bland de avstängda användarna.
     */
    public boolean isUserAlreadySuspended(String customerId) {
        return suspendedIdSet.contains(customerId);
    }

    /**
     * Lägger till en bok i samlingen.
     */
    public void addBook(Book book) {
        books.add(book);
    }

    /**
     * Lägger till en tidning i samlingen.
     */
    public void addMagazine(Magazine magazine) {
        magazines.add(magazine);
    }

    /**
     * Lägger till en användare i samlingen.
     */
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Lägger till en avstängd användare i samlingen.
     */
    public void addSuspendedUser(SuspendedUser suspendedUser) {
        suspendedUsers.add(suspendedUser);
    }

    /**
     * Tar bort en bok ur den lokala listan.
     */
    public void removeBook(Book book) {
        books.remove(book);
    }

    /**
     * Tar bort en tidning ur den lokala listan.
     */
    public void removeMagazine(Magazine magazine) {
        magazines.remove(magazine);
    }

    /**
     * Tar bort en användare ur den lokala listan.
     */
    public void removeUser(User user) {
        users.remove(user);
    }

    /**
     * Tar bort en avstängd användare ur den lokala listan med hjälp av id.
     */
    public void removeSuspendedUserById(String id) {
        for (int i = 0; i < suspendedUsers.size(); i++) {
            if (suspendedUsers.get(i).getId().equalsIgnoreCase(id)) {
                suspendedUsers.remove(i);
                return;
            }
        }
    }

    /**
     * Skriver ut böcker sorterade på titel.
     */
    public void printBooksSorted() {
        IO.println("Skriver ut alla böcker...");
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
     * Skriver ut tidningar sorterade på titel.
     */
    public void printMagazinesSorted() {
        IO.println("Skriver ut alla tidningar...");
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
     * Skriver ut användare sorterade på namn.
     */
    public void printUsersSorted() {
        IO.println("Skriver ut alla användare...");
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
     * Skriver ut avstängda användare sorterade på id.
     */
    public void printSuspendedUsersSorted() {
        IO.println("Skriver ut alla avstängda användare...");
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
     * Hittar en användare med hjälp av id.
     */
    public User findUserById(String id) {
        for (User user : users) {
            if (user.getId().equalsIgnoreCase(id)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Hittar en användare med hjälp av e-post via HashMap.
     */
    public User findUserByEmail(String email) {
        return userMap.get(email.toLowerCase());
    }

    /**
     * Läser in e-post och söker efter en användare.
     */
    public boolean findUserByEmailInteractive() {
        IO.println("Hitta användare via e-post...");

        if (users.isEmpty()) {
            IO.println("Inga användare är hämtade. Hämta användare först.");
            return false;
        }

        String email = IO.readln("Ange e-post: ").trim();

        if (email.isBlank()) {
            IO.println("E-post får inte vara tom.");
            return false;
        }

        User foundUser = findUserByEmail(email);

        if (foundUser == null) {
            IO.println("Ingen användare hittades med den e-postadressen.");
            return false;
        } else {
            IO.println("Användaren hittades:");
            IO.println(foundUser.getInfo());
        }
        return true;
    }

    /**
     * Avgör om en användare får låna eller inte.
     * Returnerar false om användarens id finns bland de avstängda användarna.
     */
    public boolean canUserBorrow(String customerId) {
        return !suspendedIdSet.contains(customerId);
    }

    /**
     * Läser in användar-id och kontrollerar om användaren får låna.
     */
    public boolean checkUserBorrow() {
        IO.println("Kontrollerar om användare får låna...");

        if (users.isEmpty()) {
            IO.println("Inga användare är hämtade. Hämta användare först.");
            return false;
        }

        String customerIdToCheck = IO.readln("Ange användarens id: ").trim();

        if (customerIdToCheck.isBlank()) {
            IO.println("Id får inte vara tomt.");
            return false;
        }

        User userToCheck = findUserById(customerIdToCheck);

        if (userToCheck == null) {
            IO.println("Ingen användare hittades med det id:t.");
            return false;
        } else if (canUserBorrow(customerIdToCheck)) {
            IO.println("Användaren får låna.");
        } else {
            IO.println("Användaren är avstängd och får inte låna.");
        }
        return true;
    }

    /**
     * Kontrollerar
     */
}
