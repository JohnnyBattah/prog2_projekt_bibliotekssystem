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
 * Klassen LibraryManager ansvarar för logiken i bibliotekssystemet.
 * Den hanterar hämtning, skapande, sökning och borttagning,
 * utlåning och filhantering för bibliotekets objekt.
 * Klassen används av Main, som skickar vidare användarens menyval
 * till rätt metod i denna klass.
 * 
 * @author Johnny Battah
 * @version 1.0
 * @since 2026
 */

public class LibraryManager {
    /** Lista som lagrar alla hämtade böcker. */
    private ArrayList<Book> books;

    /** Lista som lagrar alla hämtade tidningar. */
    private ArrayList<Magazine> magazines;

    /** Lista som lagrar alla hämtade användare. */
    private ArrayList<User> users;

    /** Lista som lagrar alla hämtade avstängda användare. */
    private ArrayList<SuspendedUser> suspendedUsers;

    /** Lista som lagrar all hämtad media, inklusive spel, filmer och musikalbum. */
    private ArrayList<Media> mediaItems;

    /** Lista som lagrar registrerade lån i programmet. */
    private ArrayList<Loan> loans;

    /** HashMap som används för snabb sökning av användare via e-postadress. */
    private Map<String, User> userMap;

    /** Set som innehåller id för användare som är avstängda från utlåning. */
    private Set<String> suspendedIdSet;

    /** Filnamn för lagring av lån på fil. */
    private final String loansFileName = "loans.json";

    /** Filnamn för lagring av media-arvshierarki på fil. */
    private final String mediaFileName = "media.json";

    /** Objekt som hanterar kommunikation med JSON-servern. */
    private LibraryApiClient apiClient;

    /** Gson-objekt som används för att omvandla Java-objekt till och från JSON. */
    private Gson gson;

    /**
     * Skapar ett nytt LibraryManager-objekt och initierar programmets
     * samlingar, hjälpsamlingar och objekt för JSON-hantering och
     * serverkommunikation.
     */
    public LibraryManager() {
        gson = new Gson();
        apiClient = new LibraryApiClient();

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
     * Hämtar alla böcker från servern och sparar dem i listan books.
     * 
     * @return true om böckerna hämtades korrekt, annars false
     */
    public boolean fetchBooks() {
        IO.println("Hämtar alla böcker...");

        String booksBody = apiClient.fetchAll("/books");

        if (booksBody == null) {
            IO.println("Fel vid hämtning av böcker.");
            return false;
        }

        Type bookListType = new TypeToken<ArrayList<Book>>() {
        }.getType();
        this.books = gson.fromJson(booksBody, bookListType);

        IO.println("Böcker hämtade från servern. Antal: " + books.size());
        return true;
    }

    /**
     * Hämtar en bok från servern med hjälp av id som användaren matar in.
     * 
     * @return true om boken hämtades korrekt, annars false
     */
    public boolean fetchOneBook() {
        String bookId = IO.readln("Ange bokens id: ").trim();

        if (bookId.isBlank()) {
            IO.println("Id får inte vara tomt.");
            return false;
        }

        String body = apiClient.fetchOne("/books", bookId);

        if (body == null) {
            IO.println("Ingen bok hittades med det id:t.");
            return false;
        }

        Book book = gson.fromJson(body, Book.class);
        IO.println("Bok hämtad från servern:");
        IO.println(book.getInfo());
        return true;
    }

    /**
     * Hämtar alla tidningar från servern och sparar dem i listan magazines.
     * 
     * @return true om tidningarna hämtades korrekt, annars false
     */
    public boolean fetchMagazines() {
        IO.println("Hämtar alla tidningar...");

        String magazinesBody = apiClient.fetchAll("/magazines");

        if (magazinesBody == null) {
            IO.println("Fel vid hämtning av tidningar.");
            return false;
        }

        Type magazineListType = new TypeToken<ArrayList<Magazine>>() {
        }.getType();
        this.magazines = gson.fromJson(magazinesBody, magazineListType);

        IO.println("Tidningar hämtade från servern. Antal: " + magazines.size());
        return true;
    }

    /**
     * Hämtar en tidning från servern med hjälp av id som användaren matar in.
     * 
     * @return true om tidningen hämtades korrekt, annars false
     */
    public boolean fetchOneMagazine() {
        String magazineId = IO.readln("Ange tidningens id: ").trim();

        if (magazineId.isBlank()) {
            IO.println("Id får inte vara tomt.");
            return false;
        }

        String body = apiClient.fetchOne("/magazines", magazineId);

        if (body == null) {
            IO.println("Ingen tidning hittades med det id:t.");
            return false;
        }

        Magazine magazine = gson.fromJson(body, Magazine.class);
        IO.println("Tidning hämtad från servern:");
        IO.println(magazine.getInfo());
        return true;
    }

    /**
     * Hämtar alla användare från servern och sparar dem i listan users.
     * Metoden uppdaterar också userMap för snabb sökning via e-postadress.
     * 
     * @return true om användarna hämtades korrekt, annars false
     */
    public boolean fetchUsers() {
        IO.println("Hämtar alla användare...");

        String usersBody = apiClient.fetchAll("/users");

        if (usersBody == null) {
            IO.println("Fel vid hämtning av användare.");
            return false;
        }

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
     * Hämtar en användare från servern med hjälp av id som användaren matar in.
     * 
     * @return true om användaren hämtades korrekt, annars false
     */
    public boolean fetchOneUser() {
        String userId = IO.readln("Ange användarens id: ").trim();

        if (userId.isBlank()) {
            IO.println("Id får inte vara tomt.");
            return false;
        }

        String body = apiClient.fetchOne("/users", userId);

        if (body == null) {
            IO.println("Ingen användare hittades med det id:t.");
            return false;
        }

        User user = gson.fromJson(body, User.class);
        IO.println("Användare hämtad från servern:");
        IO.println(user.getInfo());
        return true;
    }

    /**
     * Hämtar alla avstängda användare från servern och sparar dem i listan
     * suspendedUsers.
     * Metoden uppdaterar också suspendedIdSet för snabb kontroll av avstängda
     * användare.
     * 
     * @return true om de avstängda användarna hämtades korrekt, annars false
     */
    public boolean fetchSuspendedUsers() {
        IO.println("Hämtar alla avstängda användare...");

        String suspendedBody = apiClient.fetchAll("/suspended");

        if (suspendedBody == null) {
            IO.println("Fel vid hämtning av avstängda användare.");
            return false;
        }

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
     * Hämtar en avstängd användare från servern med hjälp av id som användaren
     * matar in.
     * 
     * @return true om den avstängda användaren hämtades korrekt, annars false
     */
    public boolean fetchOneSuspendedUser() {
        String suspendedId = IO.readln("Ange avstängningens id: ").trim();

        if (suspendedId.isBlank()) {
            IO.println("Id får inte vara tomt.");
            return false;
        }

        String body = apiClient.fetchOne("/suspended", suspendedId);

        if (body == null) {
            IO.println("Ingen avstängd användare hittades med det id:t.");
            return false;
        }

        SuspendedUser suspendedUser = gson.fromJson(body, SuspendedUser.class);
        IO.println("Avstängd användare hämtad från servern:");
        IO.println(suspendedUser.getInfo());
        return true;
    }

    /**
     * Hämtar all media från servern och omvandlar JSON-data till rätt subklasser
     * i media-arvshierarkin, till exempel Game, Movie och MusicAlbum.
     * 
     * @return true om media hämtades korrekt, annars false
     */
    public boolean fetchMedia() {
        IO.println("Hämtar all media...");

        String mediaBody = apiClient.fetchAll("/media");

        if (mediaBody == null) {
            IO.println("Fel vid hämtning av media.");
            return false;
        }

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

    /**
     * Hämtar ett mediaobjekt från servern med hjälp av id som användaren matar in.
     * Metoden avgör sedan rätt subklass utifrån objektets type-fält.
     * 
     * @return true om mediaobjektet hämtades korrekt, annars false
     */
    public boolean fetchOneMedia() {
        String mediaId = IO.readln("Ange mediets id: ").trim();

        if (mediaId.isBlank()) {
            IO.println("Id får inte vara tomt.");
            return false;
        }

        String body = apiClient.fetchOne("/media", mediaId);

        if (body == null) {
            IO.println("Ingen media hittades med det id:t.");
            return false;
        }

        JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
        String type = obj.get("type").getAsString();

        Media media = null;

        if (type.equalsIgnoreCase("game")) {
            media = gson.fromJson(obj, Game.class);
        } else if (type.equalsIgnoreCase("movie")) {
            media = gson.fromJson(obj, Movie.class);
        } else if (type.equalsIgnoreCase("music_album")) {
            media = gson.fromJson(obj, MusicAlbum.class);
        }

        if (media == null) {
            IO.println("Okänd mediatyp.");
            return false;
        }

        IO.println("Media hämtad från servern:");
        IO.println(media.getInfo());
        return true;
    }

    /******************
     *** Lägg till ****
     *****************/

    /**
     * Skapar en ny bok och skickar in den till servern.
     * Om serveranropet lyckas sparas boken också i den lokala samlingen.
     * 
     * @param title  bokens titel
     * @param author bokens författare
     * @param genre  bokens genre
     * @param pages  antal sidor i boken
     * @return true om boken sparades korrekt, annars false
     */
    public boolean addBookToServer(String title, String author, String genre, int pages) {
        Book newBook = new Book(null, title, true, author, genre, pages);
        String bookJson = gson.toJson(newBook);

        String responseBody = apiClient.post("/books", bookJson);

        if (responseBody == null) {
            IO.println("Fel vid skapande av bok.");
            return false;
        }

        Book savedBook = gson.fromJson(responseBody, Book.class);
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
     * Skapar en ny tidning och skickar den till servern.
     * Om serveranropet lyckas sparas tidningen också i den lokala samlingen.
     * 
     * @param title         tidningens titel
     * @param category      tidningens kategori
     * @param issueNumber   tidningens nummer
     * @param publishedYear tidningens publiceringsår
     * @return true om tidningen sparades korrekt, annars false
     */
    public boolean addMagazineToServer(String title, String category, int issueNumber,
            int publishedYear) {
        Magazine newServerMagazine = new Magazine(null, title, true, issueNumber, category, publishedYear);
        String magazineJson = gson.toJson(newServerMagazine);

        String responseBody = apiClient.post("/magazines", magazineJson);

        if (responseBody == null) {
            IO.println("Fel vid skapande av tidning.");
            return false;
        }

        Magazine savedMagazine = gson.fromJson(responseBody, Magazine.class);
        magazines.add(savedMagazine);

        IO.println("Tidningen lades till på servern och i den lokala samlingen.");
        return true;
    }

    /**
     * Läser in information om en tidning från användaren och lägger till den på
     * servern.
     * 
     * @return true om tidningen lades till korrekt, annars false
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
     * Skapar en ny användare och skickar den till servern.
     * Om serveranropet lyckas sparas användaren också i den lokala samlingen.
     * 
     * @param name  användarens namn
     * @param email användarens e-postadress
     * @return true om användaren sparades korrekt, annars false
     */
    public boolean addUserToServer(String name, String email) {
        User newUser = new User(null, name, email);
        String userJson = gson.toJson(newUser);

        String responseBody = apiClient.post("/users", userJson);

        if (responseBody == null) {
            IO.println("Fel vid skapande av användare.");
            return false;
        }

        User savedUser = gson.fromJson(responseBody, User.class);
        users.add(savedUser);
        userMap.put(savedUser.getEmail().toLowerCase(), savedUser);

        IO.println("Användaren lades till på servern och i den lokala samlingen.");
        return true;
    }

    /**
     * Läser in information om en användare från användaren och lägger till den på
     * servern.
     * 
     * @return true om användaren lades till korrekt, annars false
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
     * Skapar en ny avstängd användare och skickar den till servern.
     * Om serveranropet lyckas sparas posten också i den lokala samlingen.
     * 
     * @param customerId id för användaren som ska stängas av
     * @return true om den avstängda användaren sparades korrekt, annars false
     */
    public boolean addSuspendedUserToServer(String customerId) {
        SuspendedUser newSuspendedUser = new SuspendedUser(null, customerId);
        String suspendedJson = gson.toJson(newSuspendedUser);

        String responseBody = apiClient.post("/suspended", suspendedJson);

        if (responseBody == null) {
            IO.println("Fel vid skapande av avstängd användare.");
            return false;
        }

        SuspendedUser savedSuspendedUser = gson.fromJson(responseBody, SuspendedUser.class);
        suspendedUsers.add(savedSuspendedUser);
        suspendedIdSet.add(savedSuspendedUser.getCustomer_id());

        IO.println("Den avstängda användaren lades till på servern och i den lokala samlingen.");
        return true;
    }

    /**
     * Läser in ett användar-id och lägger till en avstängd användare på servern.
     * 
     * @return true om avstängningen skapades korrekt, annars false
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

    /**
     * Skapar ett nytt mediaobjekt och skickar det till servern.
     * Om serveranropet lyckas sparas mediaobjektet också i den lokala samlingen.
     *
     * @param media mediaobjektet som ska sparas
     * @return true om mediaobjektet sparades korrekt, annars false
     */
    public boolean addMediaToServer(Media media) {
        String mediaJson = gson.toJson(media);
        String responseBody = apiClient.post("/media", mediaJson);

        if (responseBody == null) {
            IO.println("Fel vid skapande av media.");
            return false;
        }

        if (media instanceof Game) {
            Game savedGame = gson.fromJson(responseBody, Game.class);
            mediaItems.add(savedGame);
        } else if (media instanceof Movie) {
            Movie savedMovie = gson.fromJson(responseBody, Movie.class);
            mediaItems.add(savedMovie);
        } else if (media instanceof MusicAlbum) {
            MusicAlbum savedMusicAlbum = gson.fromJson(responseBody, MusicAlbum.class);
            mediaItems.add(savedMusicAlbum);
        } else {
            IO.println("Okänd mediatyp.");
            return false;
        }

        IO.println("Media lades till på servern och i den lokala samlingen.");
        return true;
    }

    /**
     * Läser in information om ett spel från användaren och lägger till det på
     * servern.
     *
     * @return true om spelet lades till korrekt, annars false
     */
    public boolean addGame() {
        IO.println("Lägger till ett spel på servern...");
        String title = readRequiredText("Ange titel: ", "Titel");

        if (findMediaByTitle(title) != null) {
            IO.println("Det finns redan media med den titeln.");
            return false;
        }

        String genre = readRequiredText("Ange genre: ", "Genre");
        int age = readPositiveInt("Ange ålder: ", "Ålder");

        Game newGame = new Game(null, title, true, genre, age);
        return addMediaToServer(newGame);
    }

    /**
     * Läser in information om en film från användaren och lägger till den på
     * servern.
     *
     * @return true om filmen lades till korrekt, annars false
     */
    public boolean addMovie() {
        IO.println("Lägger till en film på servern...");
        String title = readRequiredText("Ange titel: ", "Titel");

        if (findMediaByTitle(title) != null) {
            IO.println("Det finns redan media med den titeln.");
            return false;
        }

        String genre = readRequiredText("Ange genre: ", "Genre");
        int minutes = readPositiveInt("Ange minuter: ", "Minuter");

        Movie newMovie = new Movie(null, title, true, genre, minutes);
        return addMediaToServer(newMovie);
    }

    /**
     * Läser in information om ett musikalbum från användaren och lägger till det på
     * servern.
     *
     * @return true om musikalbumet lades till korrekt, annars false
     */
    public boolean addMusicAlbum() {
        IO.println("Lägger till ett musikalbum på servern...");
        String title = readRequiredText("Ange titel: ", "Titel");

        if (findMediaByTitle(title) != null) {
            IO.println("Det finns redan media med den titeln.");
            return false;
        }

        String artist = readRequiredText("Ange artist: ", "Artist");

        MusicAlbum newMusicAlbum = new MusicAlbum(null, title, true, artist);
        return addMediaToServer(newMusicAlbum);
    }

    /******************
     **** Ta bort *****
     *****************/

    /**
     * Tar bort en bok från servern och den lokala samlingen med hjälp av titel.
     * 
     * @param title titeln på boken som ska tas bort
     * @return true om boken togs bort korrekt, annars false
     */
    public boolean deleteBookByTitleFromServer(String title) {
        Book bookToDelete = findBookByTitle(title);

        if (bookToDelete == null) {
            IO.println("Ingen bok hittades med den titeln.");
            return false;
        }

        boolean success = apiClient.delete("/books/" + bookToDelete.getId());

        if (!success) {
            IO.println("Fel vid borttagning av bok.");
            return false;
        }

        books.remove(bookToDelete);
        IO.println("Boken togs bort från servern och från den lokala samlingen.");
        return true;
    }

    /**
     * Läser in titel från användaren och tar bort en bok från servern.
     * 
     * @return true om boken togs bort korrekt, annars false
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
     * Tar bort en tidning från servern och från den lokala samlingen med hjälp av
     * titel.
     * 
     * @param title titeln på tidningen som ska tas bort
     * @return true om tidningen togs bort korrekt, annars false
     */
    public boolean deleteMagazineByTitleFromServer(String title) {
        Magazine magazineToDelete = findMagazineByTitle(title);

        if (magazineToDelete == null) {
            IO.println("Ingen tidning hittades med den titeln.");
            return false;
        }

        boolean success = apiClient.delete("/magazines/" + magazineToDelete.getId());

        if (!success) {
            IO.println("Fel vid borttagning av tidning.");
            return false;
        }

        magazines.remove(magazineToDelete);
        IO.println("Tidningen togs bort från servern och från den lokala samlingen.");
        return true;
    }

    /**
     * Läser in titel från användaren och tar bort en tidning från servern.
     * 
     * @return true om tidningen togs bort korrekt, annars false
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
     * Tar bort en användare från servern och från den lokala samlingen med hjälp av
     * e-post.
     * Om användaren också finns bland avstängda användare tas den posten bort
     * först.
     * 
     * @param email e-postadressen för användaren som ska tas bort
     * @return true om användaren togs bort korrekt, annars false
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

        boolean deleted = apiClient.delete("/users/" + userToDelete.getId());

        if (!deleted) {
            IO.println("Fel vid borttagning av användare.");
            return false;
        }

        users.remove(userToDelete);
        userMap.remove(userToDelete.getEmail().toLowerCase());
        IO.println("Användaren togs bort från servern och från den lokala samlingen.");
        return true;
    }

    /**
     * Läser in e-post från användaren och tar bort en användare från servern.
     * 
     * @return true om användaren togs bort korrekt, annars false
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
     * Tar bort en avstängd användare från servern och från den lokala listan med
     * hjälp av id.
     * 
     * @param id id för avstängningsposten som ska tas bort
     * @return true om den avstängda användaren togs bort korrekt, annars false
     */
    public boolean deleteSuspendedUserByIdFromServer(String id) {
        boolean deleted = apiClient.delete("/suspended/" + id);

        if (!deleted) {
            IO.println("Fel vid borttagning av avstängd användare.");
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
     * Läser in ett id från användaren och tar bort en avstängd användare från
     * servern.
     * 
     * @return true om den avstängda användaren togs bort korrekt, annars false
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
     * 
     * @param id id för avstängningsposten som ska tas bort
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
     * Tar bort ett mediaobjekt från servern och från den lokala samlingen med hjälp
     * av titel.
     * 
     * @param title titeln på mediaobjektet som ska tas bort
     * @return true om mediaobjektet togs bort korrekt, annars false
     */
    public boolean deleteMediaByTitleFromServer(String title) {
        Media mediaToDelete = findMediaByTitle(title);

        if (mediaToDelete == null) {
            IO.println("Ingen media hittades med den titeln.");
            return false;
        }

        boolean deleted = apiClient.delete("/media/" + mediaToDelete.getId());

        if (!deleted) {
            IO.println("Fel vid borttagning av media.");
            return false;
        }

        mediaItems.remove(mediaToDelete);
        IO.println("Media togs bort från servern och från den lokala samlingen.");
        return true;
    }

    /**
     * Läser in titel från användaren och tar bort ett mediaobjekt från servern.
     * 
     * @return true om mediaobjektet togs bort korrekt, annars false
     */
    public boolean deleteMedia() {
        IO.println("Tar bort media via titel...");

        if (mediaItems.isEmpty()) {
            IO.println("Ingen media är hämtad. Hämta media först.");
            return false;
        }

        String mediaTitleToDelete = readRequiredText("Ange titel: ", "Titel");

        return deleteMediaByTitleFromServer(mediaTitleToDelete);
    }

    /******************
     ** Sök/kontroll **
     *****************/

    /**
     * Kontrollerar om en boktitel redan finns i samlingen.
     * 
     * @param title titeln som ska kontrolleras
     * @return true om titeln redan finns, annars false
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
     * 
     * @param title titeln som ska kontrolleras
     * @return true om titeln redan finns, annars false
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
     * Kontrollerar om e-postadressen redan finns bland användarna.
     * 
     * @param email e-postadressen som ska kontrolleras
     * @return true om e-postadressen redan finns, annars false
     */
    public boolean emailExists(String email) {
        return userMap.containsKey(email.toLowerCase());
    }

    /**
     * Kontrollerar om en användares id redan finns bland de avstängda användarna.
     * 
     * @param customerId användarens id
     * @return true om användaren redan är avstängd, annars false
     */
    public boolean isUserAlreadySuspended(String customerId) {
        return suspendedIdSet.contains(customerId);
    }

    /**
     * Söker efter en bok med hjälp av titel.
     * 
     * @param title titeln som ska sökas efter
     * @return boken om den hittas, annars null
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
     * Läser in en titel från användaren och söker efter en bok.
     * 
     * @return true om boken hittades, annars false
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
     * Söker efter en tidning med hjälp av titel.
     * 
     * @param title titeln som ska sökas efter
     * @return tidningen om den hittas, annars null
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
     * Läser in titel från användaren och söker efter en tidning.
     * 
     * @return true om tidningen hittades, annars false
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
     * Söker efter en användare med hjälp av id.
     * 
     * @param id användarens id
     * @return användaren om den hittas, annars null
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
     * Söker efter en användare med hjälp av e-postadress.
     * 
     * @param email e-postadressen som ska sökas efter
     * @return användare om den hittas, annars null
     */
    public User findUserByEmail(String email) {
        return userMap.get(email.toLowerCase());
    }

    /**
     * Läser in en e-postadress från användaren och söker efter en användare.
     * 
     * @return true om användaren hittades, annars false
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

    /**
     * Söker efter ett mediaobjekt med hjälp av titel.
     * 
     * @param title titeln som ska sökas efter
     * @return mediaobjektet om det hittas, annars null
     */
    public Media findMediaByTitle(String title) {
        for (Media media : mediaItems) {
            if (media.getTitle().equalsIgnoreCase(title)) {
                return media;
            }
        }
        return null;
    }

    /**
     * Läser in en titel från användaren och söker efter ett mediaobjekt.
     * 
     * @return true om mediaobjektet hittades, annars false
     */
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
     * 
     * @param customerId användarens id
     * @return true om användaren inte är avstängd, annars false
     */
    public boolean canUserBorrow(String customerId) {
        return !suspendedIdSet.contains(customerId);
    }

    /**
     * Läser in användar-id från användaren och kontrollerar om användaren får låna.
     * 
     * @return true om kontrollen genomfördes, annars false
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

    /**
     * Lånar ut en bok till en användare och registrerar lånet i lånelistan och på
     * fil.
     * 
     * @return true om utlåningen lyckades, annars false
     */
    public boolean borrowBook() {
        IO.println("Låna bok...");

        if (!checkCollectionLoaded(books.isEmpty(), "Inga böcker är hämtade. Hämta böcker först.")) {
            return false;
        }

        User userToBorrow = getValidBorrowUser();
        if (userToBorrow == null) {
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

        if (!updateBookAvailabilityOnServer(bookToBorrow, false)) {
            IO.println("Kunde inte uppdatera bokens tillgänglighet på servern.");
            return false;
        }

        bookToBorrow.setIsAvailable(false);
        IO.println("Boken lånades ut.");
        createAndSaveLoan(userToBorrow.getId(), bookToBorrow.getId(), "book");
        return true;
    }

    /**
     * Tar emot en återlämnad bok, uppdaterar tillgängligheten och tar bort lånet.
     * 
     * @return true om återlämningen lyckades, annars false
     */
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

        if (!updateBookAvailabilityOnServer(bookToReturn, true)) {
            IO.println("Kunde inte uppdatera bokens tillgänglighet på servern.");
            return false;
        }

        loans.remove(loanToRemove);
        bookToReturn.setIsAvailable(true);

        IO.println("Boken har lämnats tillbaka.");
        saveLoansToFile();
        return true;
    }

    /**
     * Lånar ut en tidning till en användare och registrerar lånet i lånelistan och
     * på fil.
     * 
     * @return true om utlåningen lyckades, annars false
     */
    public boolean borrowMagazine() {
        IO.println("Låna tidning...");

        if (!checkCollectionLoaded(magazines.isEmpty(), "Inga tidningar är hämtade. Hämta tidningar först.")) {
            return false;
        }

        User userToBorrow = getValidBorrowUser();
        if (userToBorrow == null) {
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

        if (!updateMagazineAvailabilityOnServer(magazineToBorrow, false)) {
            IO.println("Kunde inte uppdatera tidningens tillgänglighet på servern.");
            return false;
        }

        magazineToBorrow.setIsAvailable(false);
        IO.println("Tidningen lånades ut.");
        createAndSaveLoan(userToBorrow.getId(), magazineToBorrow.getId(), "magazine");
        return true;
    }

    /**
     * Tar emot en återlämnad tidning, uppdaterar tillgängligheten och tar bort
     * lånet.
     * 
     * @return true om återlämningen lyckades, annars false
     */
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

        if (!updateMagazineAvailabilityOnServer(magazineToReturn, true)) {
            IO.println("Kunde inte uppdatera tidningens tillgänglighet på servern.");
            return false;
        }

        loans.remove(loanToRemove);
        magazineToReturn.setIsAvailable(true);

        IO.println("Tidningen har lämnats tillbaka.");
        saveLoansToFile();
        return true;
    }

    /**
     * Lånar ut ett mediaobjekt till en användare och registrerar lånet i lånelistan
     * och på fil.
     * 
     * @return true om utlåningen lyckades, annars false
     */
    public boolean borrowMedia() {
        IO.println("Låna media...");

        if (!checkCollectionLoaded(mediaItems.isEmpty(), "Ingen media är hämtad. Hämta media först.")) {
            return false;
        }

        User userToBorrow = getValidBorrowUser();
        if (userToBorrow == null) {
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

        if (!updateMediaAvailabilityOnServer(mediaToBorrow, false)) {
            IO.println("Kunde inte uppdatera mediets tillgänglighet på servern.");
            return false;
        }

        mediaToBorrow.setIsAvailable(false);
        IO.println("Mediet lånades ut.");
        createAndSaveLoan(userToBorrow.getId(), mediaToBorrow.getId(), "media");
        return true;
    }

    /**
     * Tar emot ett återlämnat mediaobjekt, uppdaterar tillgängligheten och tar bort
     * lånet.
     * 
     * @return true om återlämningen lyckades, annars false
     */
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

        if (!updateMediaAvailabilityOnServer(mediaToReturn, true)) {
            IO.println("Kunde inte uppdatera mediets tillgänglighet på servern.");
            return false;
        }

        loans.remove(loanToRemove);
        mediaToReturn.setIsAvailable(true);

        IO.println("Mediet har lämnats tillbaka.");
        saveLoansToFile();
        return true;
    }

    /**
     * Kontrollerar om ett objekt redan är utlånat.
     * 
     * @param itemId id för objektet som ska kontrolleras
     * @return true om objektet är utlånat, annars false
     */
    public boolean isItemLoaned(String itemId) {
        for (Loan loan : loans) {
            if (loan.getItemId().equalsIgnoreCase(itemId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Söker efter ett lån med hjälp av objektets id.
     * 
     * @param itemId id för objektet som lånet gäller
     * @return lånet om det hittas, annars null
     */
    public Loan findLoanByItemId(String itemId) {
        for (Loan loan : loans) {
            if (loan.getItemId().equalsIgnoreCase(itemId)) {
                return loan;
            }
        }
        return null;
    }

    /**
     * Hämtar titeln på det objekt som hör till ett visst lån.
     * 
     * @param loan lånet som ska kontrolleras
     * @return titeln på objektet, eller "Okänd titel" om objektet inte hittas
     */
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

    /**
     * Skriver ut alla registrerade lån med användarnamn, titel och objekttyp.
     */
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
     * Skriver ut alla böcker sorterade i alfabetisk ordning efter titel.
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
     * Skriver ut alla tidningar sorterade i alfabetisk ordning efter titel.
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
     * Skriver ut användare sorterade i alfabetisk ordning efter namn.
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
     * Skriver ut alla avstängda användare sorterade efter id.
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
     * Skriver ut all media i den lokala samlingen.
     */
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

    /**
     * Hämtar alla mediatitlar med hjälp av stream och returnerar dem i en lista.
     * 
     * @return en lista med alla mediatitlar
     */
    public ArrayList<String> getMediaTitlesStream() {
        return new ArrayList<>(
                mediaItems.stream()
                        .map(Media::getTitle)
                        .toList());
    }

    /**
     * Kontrollerar med hjälp av stream om en titel finns bland böcker, tidningar
     * eller media.
     * 
     * @param title titeln som ska kontrolleras
     * @return true om titeln finns, annars false
     */
    public boolean titleExistsStream(String title) {
        return books.stream().anyMatch(book -> book.matchesTitle(title))
                || magazines.stream().anyMatch(magazine -> magazine.matchesTitle(title))
                || mediaItems.stream().anyMatch(media -> media.matchesTitle(title));
    }

    /**
     * Hämtar alla utlånade böcker med hjälp av stream.
     * 
     * @return en lista med utlånade böcker
     */
    public ArrayList<Book> getBorrowedBooksStream() {
        return new ArrayList<>(
                books.stream()
                        .filter(book -> !book.getIsAvailable())
                        .toList());
    }

    /**
     * Hämtar all tillgänglig media med hjälp av stream.
     * 
     * @return en lista med tillgänglig media
     */
    public ArrayList<Media> getAvailableMediaStream() {
        return new ArrayList<>(
                mediaItems.stream()
                        .filter(Media::getIsAvailable)
                        .toList());
    }

    /**
     * Beräknar det totala antalet sidor för alla böcker med hjälp av stream.
     * 
     * @return summan av alla sidantal
     */
    public int getTotalPagesStream() {
        return books.stream()
                .mapToInt(Book::getPages)
                .sum();
    }

    /**
     * Skriver ut all media sorterad i alfabetisk ordning efter titel med hjälp av
     * stream.
     */
    public void printMediaSortedStream() {
        IO.println("Skriver ut media sorterad på titel...");

        mediaItems.stream()
                .sorted((m1, m2) -> m1.getTitle().compareToIgnoreCase(m2.getTitle()))
                .forEach(media -> IO.println(media.getInfo()));
    }

    /**
     * Skriver ut alla utlånade böcker med hjälp av stream.
     */
    public void printBorrowedBooksStream() {
        IO.println("Skriver ut utlånade böcker...");

        ArrayList<Book> borrowedBooks = getBorrowedBooksStream();

        if (borrowedBooks.isEmpty()) {
            IO.println("Det finns inga utlånade böcker.");
            return;
        }

        borrowedBooks.forEach(book -> IO.println(book.getInfo()));
    }

    /**
     * Skriver ut all tillgänglig media med hjälp av stream.
     */
    public void printAvailableMediaStream() {
        IO.println("Skriver ut tillgänglig media...");

        ArrayList<Media> availableMedia = getAvailableMediaStream();

        if (availableMedia.isEmpty()) {
            IO.println("Det finns ingen tillgänglig media.");
            return;
        }

        availableMedia.forEach(media -> IO.println(media.getInfo()));
    }

    /**
     * Skriver ut alla mediatitlar med hjälp av stream.
     */
    public void printMediaTitlesStream() {
        IO.println("Skriver ut alla mediatitlar...");

        ArrayList<String> titles = getMediaTitlesStream();

        if (titles.isEmpty()) {
            IO.println("Det finns ingen media.");
            return;
        }

        titles.forEach(title -> IO.println(title));
    }

    /**
     * Läser in en titel från användaren och kontrollerar med hjälp av stream om
     * titeln finns.
     */
    public void checkTitleExistsStreamInteractive() {
        IO.println("Kontrollerar om titel finns...");

        String title = readRequiredText("Ange titel: ", "Titel");

        if (titleExistsStream(title)) {
            IO.println("Titeln finns i systemet.");
        } else {
            IO.println("Titeln finns inte i systemet.");
        }
    }

    /**
     * Läser in en författare från användaren och skriver ut alla böcker av den
     * författaren
     * med hjälp av stream.
     */
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

    /**
     * Läser in en genre från användaren och skriver ut alla böcker i den genren
     * med hjälp av stream.
     */
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

    /**
     * Skriver ut alla böcker sorterade efter författare med hjälp av stream.
     */
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

    /**
     * Skriver ut alla böcker sorterade efter genre med hjälp av stream.
     */
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

    /**
     * Läser in en författare från användaren och räknar hur många böcker som finns
     * av den författaren med hjälp av stream.
     */
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

    /**
     * Skriver ut alla boktitlar med hjälp av stream.
     */
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

    /**
     * Skriver ut alla bokförfattare med hjälp av stream.
     */
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
     * Läser in text från användaren och fortsätter fråga tills ett icke-tomt värde
     * anges.
     * 
     * @param prompt    texten som visas för användaren
     * @param fieldName namnet på fältet som används i felmeddelandet
     * @return en ifylld textsträng utan inledande eller avslutande blanksteg
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
     * Läser in ett heltal från användaren och fortsätter fråga tills ett giltigt
     * positivt heltal anges.
     * 
     * @param prompt    texten som visas för användaren
     * @param fieldName namnet på fältet som används i felmeddelandet
     * @return ett heltal större än 0
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

    /**
     * Hämtar en giltig användare för utlåning.
     * Metoden kontrollerar att användaren finns och att användaren inte är
     * avstängd.
     * 
     * @return användaren om den får låna, annars null
     */
    private User getValidBorrowUser() {
        if (users.isEmpty()) {
            IO.println("Inga användare är hämtade. Hämta användare först.");
            return null;
        }

        String userId = readRequiredText("Ange användarens id: ", "Användarens id");
        User user = findUserById(userId);

        if (user == null) {
            IO.println("Ingen användare hittades med det id:t.");
            return null;
        }

        if (!canUserBorrow(userId)) {
            IO.println("Användaren är avstängd och får inte låna.");
            return null;
        }
        return user;
    }

    /**
     * Kontrollerar om en samling innehåller data innan en operation utförs.
     * 
     * @param isEmpty true om samlingen är tom, annars false
     * @param message meddelandet som ska skrivas ut om samlingen är tom
     * @return true om samlingen innehåller data, annars false
     */
    private boolean checkCollectionLoaded(boolean isEmpty, String message) {
        if (isEmpty) {
            IO.println(message);
            return false;
        }
        return true;
    }

    /**
     * Skapar ett nytt lån, lägger till det i lånelistan och sparar lånen till fil.
     * 
     * @param userId   id för användaren som lånar
     * @param itemId   id för objektet som lånas
     * @param itemType typen av objektet som lånas
     */
    private void createAndSaveLoan(String userId, String itemId, String itemType) {
        Loan newLoan = new Loan(userId, itemId, itemType);
        loans.add(newLoan);
        IO.println(newLoan.getInfo());
        saveLoansToFile();
    }

    /************************
     * Filhantering för lån *
     ***********************/

    /**
     * Sparar alla registrerade lån till en JSON-fil.
     */
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

    /**
     * Läser in lån från fil och uppdaterar tillgängligheten på utlånade objekt.
     */
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

    /**
     * Söker efter en bok med hjälp av id.
     * 
     * @param id bokens id
     * @return boken om den hittas, annars null
     */
    public Book findBookById(String id) {
        for (Book book : books) {
            if (book.getId().equalsIgnoreCase(id)) {
                return book;
            }
        }
        return null;
    }

    /**
     * Söker efter en tidning med hjälp av id.
     * 
     * @param id tidningens id
     * @return tidningen om den hittas, annars null
     */
    public Magazine findMagazineById(String id) {
        for (Magazine magazine : magazines) {
            if (magazine.getId().equalsIgnoreCase(id)) {
                return magazine;
            }
        }
        return null;
    }

    /**
     * Söker efter ett mediaobjekt med hjälp av id.
     * 
     * @param id mediaobjektets id
     * @return mediaobjektet om det hittas, annars null
     */
    public Media findMediaById(String id) {
        for (Media media : mediaItems) {
            if (media.getId().equalsIgnoreCase(id)) {
                return media;
            }
        }
        return null;
    }

    /**
     * Läser in all grunddata från servern och laddar därefter in lån från fil.
     */
    public void loadAllData() {
        fetchBooks();
        fetchMagazines();
        fetchUsers();
        fetchSuspendedUsers();
        fetchMedia();
        loadLoansFromFile();
    }

    /**
     * Uppdaterar en boks tillgänglighet på servern.
     * 
     * @param book            boken som ska uppdateras
     * @param newAvailability den nya tillgängligheten
     * @return true om uppdateringen lyckades, annars false
     */
    public boolean updateBookAvailabilityOnServer(Book book, boolean newAvailability) {
        Book updatedBook = new Book(
                book.getId(),
                book.getTitle(),
                newAvailability,
                book.getAuthor(),
                book.getGenre(),
                book.getPages());

        String jsonBody = gson.toJson(updatedBook);
        String responseBody = apiClient.put("/books/" + book.getId(), jsonBody);

        if (responseBody == null) {
            IO.println("Fel vid uppdatering av bok.");
            return false;
        }

        return true;
    }

    /**
     * Uppdaterar en tidnings tillgänglighet på servern
     * 
     * @param magazine        tidningen som ska uppdateras
     * @param newAvailability den nya tillgängligheten
     * @return true om uppdateringen lyckades, annars false
     */
    public boolean updateMagazineAvailabilityOnServer(Magazine magazine, boolean newAvailability) {
        Magazine updatedMagazine = new Magazine(
                magazine.getId(),
                magazine.getTitle(),
                newAvailability,
                magazine.getIssueNumber(),
                magazine.getCategory(),
                magazine.getPublishedYear());

        String jsonBody = gson.toJson(updatedMagazine);
        String responseBody = apiClient.put("/magazines/" + magazine.getId(), jsonBody);

        if (responseBody == null) {
            IO.println("Fel vid uppdatering av tidning.");
            return false;
        }

        return true;
    }

    /**
     * Uppdaterar ett mediaobjekts tillgänglighet på servern.
     * Metoden skapar rätt subklass beroende på om objektet är Game,
     * Movie eller MusicAlbum.
     * 
     * @param media           mediaobjektet som ska uppdateras
     * @param newAvailability den nya tillgängligheten
     * @return true om uppdateringen lyckades, annars false
     */
    public boolean updateMediaAvailabilityOnServer(Media media, boolean newAvailability) {
        Media updatedMedia = null;

        if (media instanceof Game game) {
            updatedMedia = new Game(
                    game.getId(),
                    game.getTitle(),
                    newAvailability,
                    game.getGenre(),
                    game.getAge());
        } else if (media instanceof Movie movie) {
            updatedMedia = new Movie(
                    movie.getId(),
                    movie.getTitle(),
                    newAvailability,
                    movie.getGenre(),
                    movie.getMinutes());
        } else if (media instanceof MusicAlbum musicAlbum) {
            updatedMedia = new MusicAlbum(
                    musicAlbum.getId(),
                    musicAlbum.getTitle(),
                    newAvailability,
                    musicAlbum.getArtist());
        }

        if (updatedMedia == null) {
            IO.println("Okänd mediatyp.");
            return false;
        }

        String jsonBody = gson.toJson(updatedMedia);
        String responseBody = apiClient.put("/media/" + media.getId(), jsonBody);

        if (responseBody == null) {
            IO.println("Fel vid uppdatering av media.");
            return false;
        }

        return true;
    }

    /************************
     ** Arvshierarki media **
     ***********************/

    /**
     * Sparar hela media-arvshierarkin till en JSON-fil.
     */
    public void saveMediaToFile() {
        try {
            Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
            String json = gsonPretty.toJson(mediaItems);
            Path filePath = Paths.get(mediaFileName);
            Files.writeString(filePath, json);
            IO.println("Media sparad till fil.");
        } catch (IOException e) {
            IO.println("Fel vid skrivning av media till fil: " + e.getMessage());
        }
    }

    /**
     * Läser in media från fil och omvandlar JSON-data till rätt subklasser
     * i media-arvshierarkin.
     */
    public void loadMediaFromFile() {
        try {
            Path filePath = Paths.get(mediaFileName);

            if (!Files.exists(filePath)) {
                IO.println("Ingen mediafil hittades.");
                return;
            }
            String jsonData = Files.readString(filePath);

            if (jsonData.isBlank()) {
                IO.println("Mediafilen är tom.");
                return;
            }

            JsonArray jsonArray = JsonParser.parseString(jsonData).getAsJsonArray();
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

            IO.println("Media inläst från fil. Antal: " + mediaItems.size());
        } catch (IOException e) {
            IO.println("Fel vid filinläsning av media: " + e.getMessage());
        }
    }
}