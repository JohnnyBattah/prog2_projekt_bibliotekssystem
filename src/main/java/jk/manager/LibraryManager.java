package jk.manager;

import java.util.ArrayList;
import java.util.Collections;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jk.model.Book;
import jk.model.Game;
import jk.model.Loan;
import jk.model.Magazine;
import jk.model.Media;
import jk.model.Movie;
import jk.model.MusicAlbum;
import jk.model.SuspendedUser;
import jk.model.User;

import java.lang.reflect.Type;

import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.HttpResponse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import com.google.gson.GsonBuilder;

/**
 * Författare: Johnny Battah
 * Klassen LibraryManager ansvarar för logiken i bibliotekssystemet.
 * Den hanterar hämtning, skapande, sökning och borttagning av
 * böcker, tidningar, användare och avstängda användare via JSON-servern.
 * Klassen lagrar också data lokalt i samlingar och använder Map och Set
 * för att effektivt kunna hitta användare via e-post och kontrollera om
 * en användare är avstängd.
 * LibraryManager används av Main, som skickar vidare användarens menyval
 * till rätt metod i denna klass.
 */

public class LibraryManager {
    private ArrayList<Book> books;
    private ArrayList<Magazine> magazines;
    private ArrayList<User> users;
    private ArrayList<SuspendedUser> suspendedUsers;
    private ArrayList<Media> mediaItems;
    private ArrayList<Loan> loans;

    private Map<String, User> userMap;
    private Set<String> suspendedIdSet;

    private final String loansFileName = "loans.json";

    private String baseURL;
    private Gson gson;

    public LibraryManager() {
        baseURL = "http://localhost:3000/";
        gson = new Gson();

        books = new ArrayList<>();
        magazines = new ArrayList<>();
        users = new ArrayList<>();
        suspendedUsers = new ArrayList<>();
        mediaItems = new ArrayList<>();
        loans = new ArrayList<>();
        userMap = new HashMap<>();
        suspendedIdSet = new HashSet<>();
    }

    /******************
     ****** Hämta ******
     *****************/

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

    public boolean fetchMedia() {
        IO.println("Hämtar all media...");

        HttpResponse<String> mediaResponse;
        try {
            mediaResponse = Unirest.get(baseURL + "/media").asString();
        } catch (UnirestException e) {
            IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
            return false;
        }

        int mediaStatus = mediaResponse.getStatus();
        if (mediaStatus != 200) {
            IO.println("Fel från servern vid hämtning av media. Statuskod: " + mediaStatus);
            return false;
        }

        String mediaBody = mediaResponse.getBody();
        JsonArray jsonArray = JsonParser.parseString(mediaBody).getAsJsonArray();

        mediaItems.clear();

        for (JsonElement element : jsonArray) {
            JsonObject obj = element.getAsJsonObject();
            String type = obj.get("type").getAsString();

            if (type.equalsIgnoreCase("game")) {
                Game game = gson.fromJson(obj, Game.class);
                mediaItems.add(game);
            } else if (type.equalsIgnoreCase("movie")) {
                Movie movie = gson.fromJson(obj, Movie.class);
                mediaItems.add(movie);
            } else if (type.equalsIgnoreCase("music_album")) {
                MusicAlbum musicAlbum = gson.fromJson(obj, MusicAlbum.class);
                mediaItems.add(musicAlbum);
            }
        }

        IO.println("Media hämtad från servern. Antal: " + mediaItems.size());
        return true;
    }

    /******************
     *** Lägg till ****
     *****************/

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
        String postBookTitle = readRequiredText("Ange titel: ", "Titel");

        if (bookTitleExists(postBookTitle)) {
            IO.println("Det finns redan en bok med den titeln.");
            return false;
        }

        String postBookAuthor = readRequiredText("Ange författare: ", "Författare");
        String postBookGenre = readRequiredText("Ange genre: ", "Genre");

        int postBookPages = readPositiveInt("Ange antal sidor: ", "Antal sidor");

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
        String postMagazineTitle = readRequiredText("Ange titel: ", "Titel");

        if (magazineTitleExists(postMagazineTitle)) {
            IO.println("Det finns redan en tidning med den titeln.");
            return false;
        }

        String postMagazineCategory = readRequiredText("Ange kategori: ", "Kategori");
        int postIssueNumber = readPositiveInt("Ange nummer: ", "Nummer");
        int postPublishedYear = readPositiveInt("Ange publiceringsår: ", "Publiceringsår");

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
        String userName = readRequiredText("Ange namn: ", "Namn");
        String userEmail = readRequiredText("Ange e-post: ", "E-post");

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

        String customerId = readRequiredText("Ange användarens id: ", "Användarens id");

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

    /******************
     **** Ta bort *****
     *****************/

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

        if (books.isEmpty()) {
            IO.println("Inga böcker är hämtade. Hämta böcker först.");
            return false;
        }

        String titleToDelete = readRequiredText("Ange titel: ", "Titel");
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

        if (magazines.isEmpty()) {
            IO.println("Inga tidningar är hämtade. Hämta tidningar först.");
            return false;
        }

        String magazineTitleToDelete = readRequiredText("Ange titel: ", "Titel");

        return deleteMagazineByTitleFromServer(magazineTitleToDelete);
    }

    /**
     * Tar bort en användare från servern och lokalt med hjälp av e-post.
     * Om användaren också finns bland avstängda användare tas den posten bort
     * först.
     */
    public boolean deleteUserByEmailFromServer(String email) {
        User userToDelete = findUserByEmail(email);

        if (userToDelete == null) {
            IO.println("Ingen användare hittades med den e-postadressen.");
            return false;
        }

        String userIdToDelete = userToDelete.getId();

        String suspendedIdToDelete = null;

        for (SuspendedUser suspendedUser : suspendedUsers) {
            if (suspendedUser.getCustomer_id().equalsIgnoreCase(userIdToDelete)) {
                suspendedIdToDelete = suspendedUser.getId();
                break;
            }
        }

        if (suspendedIdToDelete != null) {
            if (!deleteSuspendedUserByIdFromServer(suspendedIdToDelete)) {
                IO.println("Kunde inte ta bort användarens avstängning.");
                return false;
            }
        }

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

        String emailToDelete = readRequiredText("Ange e-post för användare som ska tas bort: ", "E-post");

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

        if (suspendedUsers.isEmpty()) {
            IO.println("Inga avstängda användare är hämtade. Hämta avstängda användare först.");
            return false;
        }

        String suspendedIdToDelete = readRequiredText("Ange id: ", "Id");

        return deleteSuspendedUserByIdFromServer(suspendedIdToDelete);
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

    /******************
     ** Sök/kontroll **
     *****************/

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
     * Läser in titel och söker efter en bok.
     */
    public boolean findBookByTitleInteractive() {
        IO.println("Hitta bok via titel...");

        if (books.isEmpty()) {
            IO.println("Inga böcker är hämtade. Hämta böcker först.");
            return false;
        }

        String title = readRequiredText("Ange titel: ", "Titel");

        Book foundBook = findBookByTitle(title);

        if (foundBook == null) {
            IO.println("Ingen bok hittades med den titeln.");
            return false;
        } else {
            IO.println("Boken hittades:");
            IO.println(foundBook.getInfo());
        }
        return true;
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
     * Läser in titel och söker efter en tidning.
     */
    public boolean findMagazineByTitleInteractive() {
        IO.println("Hitta tidning via titel...");

        if (magazines.isEmpty()) {
            IO.println("Inga tidningar är hämtade. Hämta tidningar först.");
            return false;
        }

        String title = readRequiredText("Ange titel: ", "Titel");

        Magazine foundMagazine = findMagazineByTitle(title);

        if (foundMagazine == null) {
            IO.println("Ingen tidning hittades med den titeln.");
            return false;
        } else {
            IO.println("Tidningen hittades:");
            IO.println(foundMagazine.getInfo());
        }
        return true;
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

        String email = readRequiredText("Ange e-post: ", "E-post");

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

    public Media findMediaByTitle(String title) {
        for (Media media : mediaItems) {
            if (media.getTitle().equalsIgnoreCase(title)) {
                return media;
            }
        }
        return null;
    }

    public boolean findMediaByTitleInteractive() {
        IO.println("Hitta media via titel...");

        if (mediaItems.isEmpty()) {
            IO.println("Ingen media är hämtad. Hämta media först.");
            return false;
        }

        String title = readRequiredText("Ange titel: ", "Titel");
        Media foundMedia = findMediaByTitle(title);

        if (foundMedia == null) {
            IO.println("Ingen media hittades med den titeln.");
            return false;
        }

        IO.println("Media hittades:");
        IO.println(foundMedia.getInfo());
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

        String customerIdToCheck = readRequiredText("Ange användarens id: ", "Användarens id");

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

    /******************
     ****** Lån *******
     *****************/

    public boolean borrowBook() {
        IO.println("Låna bok...");

        if (users.isEmpty()) {
            IO.println("Inga användare är hämtade. Hämta användare först.");
            return false;
        }

        if (books.isEmpty()) {
            IO.println("Inga böcker är hämtade. Hämta böcker först.");
            return false;
        }

        String userId = readRequiredText("Ange användarens id: ", "Användarens id");
        User userToBorrow = findUserById(userId);

        if (userToBorrow == null) {
            IO.println("Ingen användare hittades med det id:t.");
            return false;
        }

        if (!canUserBorrow(userId)) {
            IO.println("Användaren är avstängd och får inte låna.");
            return false;
        }

        String bookTitle = readRequiredText("Ange bokens titel: ", "Titel");
        Book bookToBorrow = findBookByTitle(bookTitle);

        if (bookToBorrow == null) {
            IO.println("Ingen bok hittades med den titeln.");
            return false;
        }

        if (!bookToBorrow.getIsAvailable()) {
            IO.println("Boken är inte tillgänglig.");
            return false;
        }

        if (isItemLoaned(bookToBorrow.getId())) {
            IO.println("Boken är redan utlånad.");
            return false;
        }

        Loan newLoan = new Loan(userId, bookToBorrow.getId(), "book");
        loans.add(newLoan);
        bookToBorrow.setIsAvailable(false);

        IO.println("Boken lånades ut.");
        IO.println(newLoan.getInfo());
        saveLoansToFile();
        return true;
    }

    public boolean returnBook() {
        IO.println("Lämna tillbaka bok...");

        if (books.isEmpty()) {
            IO.println("Inga böcker är hämtade. Hämta böcker först.");
            return false;
        }

        if (loans.isEmpty()) {
            IO.println("Det finns inga registrerade lån.");
            return false;
        }

        String bookTitle = readRequiredText("Ange bokens titel: ", "Titel");
        Book bookToReturn = findBookByTitle(bookTitle);

        if (bookToReturn == null) {
            IO.println("Ingen bok hittades med den titeln.");
            return false;
        }

        Loan loanToRemove = findLoanByItemId(bookToReturn.getId());

        if (loanToRemove == null) {
            IO.println("Boken är inte utlånad.");
            return false;
        }

        loans.remove(loanToRemove);
        bookToReturn.setIsAvailable(true);

        IO.println("Boken har lämnats tillbaka.");
        saveLoansToFile();
        return true;
    }

    public boolean borrowMagazine() {
        IO.println("Låna tidning...");

        if (users.isEmpty()) {
            IO.println("Inga användare är hämtade. Hämta användare först.");
            return false;
        }

        if (magazines.isEmpty()) {
            IO.println("Inga tidningar är hämtade. Hämta tidningar först.");
            return false;
        }

        String userId = readRequiredText("Ange användarens id: ", "Användarens id");
        User userToBorrow = findUserById(userId);

        if (userToBorrow == null) {
            IO.println("Ingen användare hittades med det id:t.");
            return false;
        }

        if (!canUserBorrow(userId)) {
            IO.println("Användaren är avstängd och får inte låna.");
            return false;
        }

        String magazineTitle = readRequiredText("Ange tidningens titel: ", "Titel");
        Magazine magazineToBorrow = findMagazineByTitle(magazineTitle);

        if (magazineToBorrow == null) {
            IO.println("Ingen tidning hittades med den titeln.");
            return false;
        }

        if (!magazineToBorrow.getIsAvailable()) {
            IO.println("Tidningen är inte tillgänglig.");
            return false;
        }

        if (isItemLoaned(magazineToBorrow.getId())) {
            IO.println("Tidningen är redan utlånad.");
            return false;
        }

        Loan newLoan = new Loan(userId, magazineToBorrow.getId(), "magazine");
        loans.add(newLoan);
        magazineToBorrow.setIsAvailable(false);

        IO.println("Tidningen lånades ut.");
        IO.println(newLoan.getInfo());
        saveLoansToFile();
        return true;
    }

    public boolean returnMagazine() {
        IO.println("Lämna tillbaka tidning...");

        if (magazines.isEmpty()) {
            IO.println("Inga tidningar är hämtade. Hämta tidningar först.");
            return false;
        }

        if (loans.isEmpty()) {
            IO.println("Det finns inga registrerade lån.");
            return false;
        }

        String magazineTitle = readRequiredText("Ange tidningens titel: ", "Titel");
        Magazine magazineToReturn = findMagazineByTitle(magazineTitle);

        if (magazineToReturn == null) {
            IO.println("Ingen tidning hittades med den titeln.");
            return false;
        }

        Loan loanToRemove = findLoanByItemId(magazineToReturn.getId());

        if (loanToRemove == null) {
            IO.println("Tidningen är inte utlånad.");
            return false;
        }

        loans.remove(loanToRemove);
        magazineToReturn.setIsAvailable(true);

        IO.println("Tidningen har lämnats tillbaka.");
        saveLoansToFile();
        return true;
    }

    public boolean borrowMedia() {
        IO.println("Låna media...");

        if (users.isEmpty()) {
            IO.println("Inga användare är hämtade. Hämta användare först.");
            return false;
        }

        if (mediaItems.isEmpty()) {
            IO.println("Ingen media är hämtad. Hämta media först.");
            return false;
        }

        String userId = readRequiredText("Ange användarens id: ", "Användarens id");
        User userToBorrow = findUserById(userId);

        if (userToBorrow == null) {
            IO.println("Ingen användare hittades med det id:t.");
            return false;
        }

        if (!canUserBorrow(userId)) {
            IO.println("Användaren är avstängd och får inte låna.");
            return false;
        }

        String mediaTitle = readRequiredText("Ange mediets titel: ", "Titel");
        Media mediaToBorrow = findMediaByTitle(mediaTitle);

        if (mediaToBorrow == null) {
            IO.println("Ingen media hittades med den titeln.");
            return false;
        }

        if (!mediaToBorrow.getIsAvailable()) {
            IO.println("Mediet är inte tillgängligt.");
            return false;
        }

        if (isItemLoaned(mediaToBorrow.getId())) {
            IO.println("Mediet är redan utlånat.");
            return false;
        }

        Loan newLoan = new Loan(userId, mediaToBorrow.getId(), "media");
        loans.add(newLoan);
        mediaToBorrow.setIsAvailable(false);

        IO.println("Mediet lånades ut.");
        IO.println(newLoan.getInfo());
        saveLoansToFile();
        return true;
    }

    public boolean returnMedia() {
        IO.println("Lämna tillbaka media...");

        if (mediaItems.isEmpty()) {
            IO.println("Ingen media är hämtad. Hämta media först.");
            return false;
        }

        if (loans.isEmpty()) {
            IO.println("Det finns inga registrerade lån.");
            return false;
        }

        String mediaTitle = readRequiredText("Ange mediets titel: ", "Titel");
        Media mediaToReturn = findMediaByTitle(mediaTitle);

        if (mediaToReturn == null) {
            IO.println("Ingen media hittades med den titeln.");
            return false;
        }

        Loan loanToRemove = findLoanByItemId(mediaToReturn.getId());

        if (loanToRemove == null) {
            IO.println("Mediet är inte utlånat.");
            return false;
        }

        loans.remove(loanToRemove);
        mediaToReturn.setIsAvailable(true);

        IO.println("Mediet har lämnats tillbaka.");
        saveLoansToFile();
        return true;
    }

    public boolean isItemLoaned(String itemId) {
        for (Loan loan : loans) {
            if (loan.getItemId().equalsIgnoreCase(itemId)) {
                return true;
            }
        }
        return false;
    }

    public Loan findLoanByItemId(String itemId) {
        for (Loan loan : loans) {
            if (loan.getItemId().equalsIgnoreCase(itemId)) {
                return loan;
            }
        }
        return null;
    }

    public String findItemTitleByLoan(Loan loan) {
        if (loan.getItemType().equalsIgnoreCase("book")) {
            for (Book book : books) {
                if (book.getId().equalsIgnoreCase(loan.getItemId())) {
                    return book.getTitle();
                }
            }
        } else if (loan.getItemType().equalsIgnoreCase("magazine")) {
            for (Magazine magazine : magazines) {
                if (magazine.getId().equalsIgnoreCase(loan.getItemId())) {
                    return magazine.getTitle();
                }
            }
        } else if (loan.getItemType().equalsIgnoreCase("media")) {
            for (Media media : mediaItems) {
                if (media.getId().equalsIgnoreCase(loan.getItemId())) {
                    return media.getTitle();
                }
            }
        }

        return "Okänd titel";
    }

    public void printLoans() {
        IO.println("Skriver ut alla lån...");

        if (loans.isEmpty()) {
            IO.println("Det finns inga registrerade lån.");
            return;
        }

        for (Loan loan : loans) {
            User user = findUserById(loan.getUserId());
            String userName;
            if (user != null) {
                userName = user.getName();
            } else {
                userName = "Okänd användare";
            }

            String itemTitle = findItemTitleByLoan(loan);

            IO.println("Användare: " + userName + ", Titel: " + itemTitle + ", Typ: " + loan.getItemType());
        }
    }

    /**********************
     * Vanliga utskrifter *
     *********************/

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

    public void printMedia() {
        IO.println("Skriver ut all media...");

        if (mediaItems.isEmpty()) {
            IO.println("Ingen media finns.");
            return;
        }

        for (Media media : mediaItems) {
            IO.println(media.getInfo());
        }
    }

    /******************
     **** Streams *****
     *****************/

    public ArrayList<String> getMediaTitlesStream() {
        return new ArrayList<>(
                mediaItems.stream()
                        .map(Media::getTitle)
                        .toList());
    }

    public boolean titleExistsStream(String title) {
        return books.stream().anyMatch(book -> book.matchesTitle(title))
                || magazines.stream().anyMatch(magazine -> magazine.matchesTitle(title))
                || mediaItems.stream().anyMatch(media -> media.matchesTitle(title));
    }

    public ArrayList<Book> getBorrowedBooksStream() {
        return new ArrayList<>(
                books.stream()
                        .filter(book -> !book.getIsAvailable())
                        .toList());
    }

    public ArrayList<Media> getAvailableMediaStream() {
        return new ArrayList<>(
                mediaItems.stream()
                        .filter(Media::getIsAvailable)
                        .toList());
    }

    public int getTotalPagesStream() {
        return books.stream()
                .mapToInt(Book::getPages)
                .sum();
    }

    public void printMediaSortedStream() {
        IO.println("Skriver ut media sorterad på titel...");

        mediaItems.stream()
                .sorted((m1, m2) -> m1.getTitle().compareToIgnoreCase(m2.getTitle()))
                .forEach(media -> IO.println(media.getInfo()));
    }

    public void printBorrowedBooksStream() {
        IO.println("Skriver ut utlånade böcker...");

        ArrayList<Book> borrowedBooks = getBorrowedBooksStream();

        if (borrowedBooks.isEmpty()) {
            IO.println("Det finns inga utlånade böcker.");
            return;
        }

        borrowedBooks.forEach(book -> IO.println(book.getInfo()));
    }

    public void printAvailableMediaStream() {
        IO.println("Skriver ut tillgänglig media...");

        ArrayList<Media> availableMedia = getAvailableMediaStream();

        if (availableMedia.isEmpty()) {
            IO.println("Det finns ingen tillgänglig media.");
            return;
        }

        availableMedia.forEach(media -> IO.println(media.getInfo()));
    }

    public void printMediaTitlesStream() {
        IO.println("Skriver ut alla mediatitlar...");

        ArrayList<String> titles = getMediaTitlesStream();

        if (titles.isEmpty()) {
            IO.println("Det finns ingen media.");
            return;
        }

        titles.forEach(title -> IO.println(title));
    }

    public void checkTitleExistsStreamInteractive() {
        IO.println("Kontrollerar om titel finns...");

        String title = readRequiredText("Ange titel: ", "Titel");

        if (titleExistsStream(title)) {
            IO.println("Titeln finns i systemet.");
        } else {
            IO.println("Titeln finns inte i systemet.");
        }
    }

    public void printBooksByAuthorStreamInteractive() {
        IO.println("Filtrerar böcker efter författare...");

        if (books.isEmpty()) {
            IO.println("Inga böcker är hämtade.");
            return;
        }

        String author = readRequiredText("Ange författare: ", "Författare");

        ArrayList<Book> filteredBooks = new ArrayList<>(
                books.stream()
                        .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                        .toList());

        if (filteredBooks.isEmpty()) {
            IO.println("Inga böcker hittades av den författaren.");
            return;
        }

        filteredBooks.forEach(book -> IO.println(book.getInfo()));
    }

    public void printBooksByGenreStreamInteractive() {
        IO.println("Filtrerar böcker efter genre...");

        if (books.isEmpty()) {
            IO.println("Inga böcker är hämtade.");
            return;
        }

        String genre = readRequiredText("Ange genre: ", "Genre");

        ArrayList<Book> filteredBooks = new ArrayList<>(
                books.stream()
                        .filter(book -> book.getGenre().equalsIgnoreCase(genre))
                        .toList());

        if (filteredBooks.isEmpty()) {
            IO.println("Inga böcker hittades i den genren.");
            return;
        }

        filteredBooks.forEach(book -> IO.println(book.getInfo()));
    }

    public void printBooksSortedByAuthorStream() {
        IO.println("Skriver ut böcker sorterade efter författaren...");

        if (books.isEmpty()) {
            IO.println("Inga böcker är hämtade.");
            return;
        }

        books.stream()
                .sorted((b1, b2) -> b1.getAuthor().compareToIgnoreCase(b2.getAuthor()))
                .forEach(book -> IO.println(book.getInfo()));
    }

    public void printBooksSortedByGenreStream() {
        IO.println("Skriver ut böcker sorterade efter genre...");

        if (books.isEmpty()) {
            IO.println("Inga böcker är hämtade.");
            return;
        }

        books.stream()
                .sorted((b1, b2) -> b1.getGenre().compareToIgnoreCase(b2.getGenre()))
                .forEach(book -> IO.println(book.getInfo()));
    }

    public void countBooksByAuthorStreamInteractive() {
        IO.println("Räknar böcker av en författare...");

        if (books.isEmpty()) {
            IO.println("Inga böcker är hämtade.");
            return;
        }

        String author = readRequiredText("Ange författaren: ", "Författare");

        long count = books.stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .count();

        IO.println("Antal böcker av " + author + ": " + count);
    }

    public void printBookTitlesStream() {
        IO.println("Skriver ut alla boktitlar...");

        if (books.isEmpty()) {
            IO.println("Inga böcker är hämtade.");
            return;
        }

        books.stream()
                .map(Book::getTitle)
                .forEach(title -> IO.println(title));
    }

    public void printBookAuthorsStream() {
        IO.println("Skriver ut alla bokförfattare...");

        if (books.isEmpty()) {
            IO.println("Inga böcker är hämtade.");
            return;
        }

        books.stream()
                .map(Book::getAuthor)
                .forEach(author -> IO.println(author));
    }

    /************************
     * Privata hjälpmetoder *
     ***********************/

    /**
     * Läser in text och fortsätter fråga tills användaren skrivit något som inte är
     * tomt.
     */
    private String readRequiredText(String prompt, String fieldName) {
        while (true) {
            String input = IO.readln(prompt).trim();

            if (input.isBlank()) {
                IO.println(fieldName + " får inte vara tomt.");
            } else {
                return input;
            }
        }
    }

    /**
     * Läser in ett heltal och fortsätter fråga tills användaren skriver ett giltigt
     * tal större än 0.
     */
    private int readPositiveInt(String prompt, String fieldName) {
        while (true) {
            String input = IO.readln(prompt).trim();

            try {
                int value = Integer.parseInt(input);

                if (value <= 0) {
                    IO.println(fieldName + " måste vara större än 0.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                IO.println(fieldName + " måste vara ett heltal.");
            }
        }
    }

    /************************
     * Filhantering för lån *
     ***********************/

    public void saveLoansToFile() {
        try {
            Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
            String json = gsonPretty.toJson(loans);
            Path filePath = Paths.get(loansFileName);
            Files.writeString(filePath, json);
            IO.println("Lån sparade till fil.");
        } catch (IOException e) {
            IO.println("Fel vid skrivande till fil: " + e.getMessage());
        }
    }

    public void loadLoansFromFile() {
        try {
            Path filePath = Paths.get(loansFileName);

            if (!Files.exists(filePath)) {
                IO.println("Ingen lånefil hittades. Startar med tom lånelista.");
                return;
            }

            String jsonData = Files.readString(filePath);

            if (jsonData.isBlank()) {
                IO.println("Lånefilen är tom. Startar med tom lånelista.");
                return;
            }

            Type loanListType = new TypeToken<ArrayList<Loan>>() {
            }.getType();
            ArrayList<Loan> loadedLoans = gson.fromJson(jsonData, loanListType);

            if (loadedLoans != null) {
                loans = loadedLoans;
            }

            for (Loan loan : loans) {
                if (loan.getItemType().equalsIgnoreCase("book")) {
                    Book book = findBookById(loan.getItemId());
                    if (book != null) {
                        book.setIsAvailable(false);
                    }
                } else if (loan.getItemType().equalsIgnoreCase("magazine")) {
                    Magazine magazine = findMagazineById(loan.getItemId());
                    if (magazine != null) {
                        magazine.setIsAvailable(false);
                    }
                } else if (loan.getItemType().equalsIgnoreCase("media")) {
                    Media media = findMediaById(loan.getItemId());
                    if (media != null) {
                        media.setIsAvailable(false);
                    }
                }
            }
            IO.println("Lån inlästa från fil. Antal: " + loans.size());
        } catch (IOException e) {
            IO.println("Fel vid filinläsning: " + e.getMessage());
        }
    }

    public Book findBookById(String id) {
        for (Book book : books) {
            if (book.getId().equalsIgnoreCase(id)) {
                return book;
            }
        }
        return null;
    }

    public Magazine findMagazineById(String id) {
        for (Magazine magazine : magazines) {
            if (magazine.getId().equalsIgnoreCase(id)) {
                return magazine;
            }
        }
        return null;
    }

    public Media findMediaById(String id) {
        for (Media media : mediaItems) {
            if (media.getId().equalsIgnoreCase(id)) {
                return media;
            }
        }
        return null;
    }

    public void loadAllData() {
        fetchBooks();
        fetchMagazines();
        fetchUsers();
        fetchSuspendedUsers();
        fetchMedia();
        loadLoansFromFile();
    }

}