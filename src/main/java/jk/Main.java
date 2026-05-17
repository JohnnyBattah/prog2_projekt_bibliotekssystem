package jk;

/**
 * Författare: Johnny Battah
 * Programmet är ett enkelt bibliotekssystem som hämtar, visar, skapar och tar bort
 * böcker, tidningar, användare och avstängda användare via en JSON-server. 
 * Programmet kan också söka användare via email och kontrollera om en användare får låna.
 */

// Gson objekt som vi behöver
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

// Importera Type för att hjälpa json att omvandla data
import java.lang.reflect.Type;

// UniREST objekt som vi behöver
import kong.unirest.Unirest;
import kong.unirest.HttpResponse;
import kong.unirest.UnirestException;

// ArrayList för att lagra objekt
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // skoladress: http://10.151.168.5:3137/
        // hemadress: http://localhost:3000/

        String baseURL = "http://localhost:3000/"; // Basadress till servern
        Gson gson = new Gson(); // JSON --> Java-objekt

        LibraryManager manager = new LibraryManager();

        boolean kör = true;

        // Programmet körs tills användaren väljer att avsluta
        while (kör) {
            // To do - undermenyer samt flytta kod till manager
            IO.println("""

                        === MENY ===
                        1. Hämta böcker
                        2. Hämta tidningar
                        3. Skriv ut böcker
                        4. Skriv ut tidningar
                        5. Lägg till bok
                        6. Lägg till tidning
                        7. Hämta användare
                        8. Skriv ut användare
                        9. Hitta användare via email
                        10. Hämta avstängda användare
                        11. Kontrollera om användare får låna
                        12. Lägg till användare på server
                        13. Lägg till bok på servern
                        14. Lägg till tidning på servern
                        15. Ta bort användare via email
                        16. Ta bort bok via titel
                        17. Ta bort tidning via titel
                        18. Ta bort avstängd användare via id
                        19. Avsluta
                    """);

            String input = IO.readln("Välj ett alternativ (1-19): ");
            int val;

            // Försöker omvandla användarens val till ett heltal
            try {
                val = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                IO.println("Felaktig inmatning. Ange en siffra");
                continue;
            }

            switch (val) {
                case 1:
                    IO.println("Hämtar alla böcker...");
                    HttpResponse<String> booksResponse;
                    try {
                        // Hämtar böcker från servern
                        booksResponse = Unirest.get(baseURL + "/books").asString();
                    } catch (UnirestException e) {
                        IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
                        IO.println();
                        break;
                    }

                    int bookStatus = booksResponse.getStatus();
                    if (bookStatus != 200) {
                        IO.println("Fel från servern vid hämtning av böcker. Statuskod: " + bookStatus);
                        IO.println();
                        break;
                    }

                    // JSON-texten från servern
                    String booksBody = booksResponse.getBody();

                    // Gör om JSON till en lista av Book-objekt
                    Type bookListType = new TypeToken<ArrayList<Book>>() {
                    }.getType();
                    manager.setBooks(gson.fromJson(booksBody, bookListType));

                    IO.println("Böcker hämtade från servern. Antal: " + manager.getBooks().size());
                    break;

                case 2:
                    IO.println("Hämtar alla tidningar...");
                    HttpResponse<String> magazinesResponse;
                    try {
                        // Hämtar tidningar från server
                        magazinesResponse = Unirest.get(baseURL + "/magazines").asString();
                    } catch (UnirestException e) {
                        IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
                        break;
                    }

                    int magazineStatus = magazinesResponse.getStatus();
                    if (magazineStatus != 200) {
                        IO.println("Fel från servern vid hämtning av tidningar. Statuskod: " + magazineStatus);
                        break;
                    }

                    // JSON-texten från servern
                    String magazinesBody = magazinesResponse.getBody();

                    // Gör om JSON till en lista av Magazine-objekt
                    Type magazineListType = new TypeToken<ArrayList<Magazine>>() {
                    }.getType();
                    manager.setMagazines(gson.fromJson(magazinesBody, magazineListType));

                    IO.println("Tidningar hämtade från servern. Antal: " + manager.getMagazines().size());
                    break;

                case 3:
                    IO.println("Skriver ut alla böcker...");
                    manager.printBooksSorted();
                    break;

                case 4:
                    IO.println("Skriver ut alla tidningar...");
                    manager.printMagazinesSorted();
                    break;

                case 5:
                    IO.println("Lägger till en bok...");
                    String bookId = IO.readln("Ange Id: ");
                    String bookTitle = IO.readln("Ange titel: ");
                    String bookAuthor = IO.readln("Ange författare: ");
                    String bookGenre = IO.readln("Ange genre: ");
                    int pages;
                    try {
                        pages = Integer.parseInt(IO.readln("Ange antal sidor: "));
                    } catch (NumberFormatException e) {
                        IO.println("Felaktigt antal sidor");
                        break;
                    }

                    // Skapar ett nytt bokobjekt och sparar lokalt
                    Book newBook = new Book(bookId, bookTitle, true, bookAuthor, bookGenre, pages);
                    manager.addBook(newBook);
                    IO.println("Boken lades till lokalt i samlingen");
                    break;

                case 6:
                    IO.println("Lägger till en tidning...");
                    String magazineId = IO.readln("Ange Id: ");
                    String magazineTitle = IO.readln("Ange titel: ");

                    int issueNumber;
                    try {
                        issueNumber = Integer.parseInt(IO.readln("Ange nummer: "));
                    } catch (NumberFormatException e) {
                        IO.println("Felaktigt nummer");
                        break;
                    }

                    String category = IO.readln("Ange kategori: ");

                    int publishedYear;
                    try {
                        publishedYear = Integer.parseInt(IO.readln("Ange publiceringsår: "));
                    } catch (NumberFormatException e) {
                        IO.println("Felaktigt år");
                        break;
                    }

                    // Skapar ett nytt tidningsobjekt och sparar lokalt
                    Magazine newMagazine = new Magazine(magazineId, magazineTitle, true, issueNumber, category,
                            publishedYear);
                    manager.addMagazine(newMagazine);
                    IO.println("Tidningen lades till lokalt i samlingen");
                    break;

                case 7:
                    IO.println("Hämtar alla användare...");
                    HttpResponse<String> usersResponse;
                    try {
                        usersResponse = Unirest.get(baseURL + "/users").asString();
                    } catch (UnirestException e) {
                        IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
                        break;
                    }

                    int userStatus = usersResponse.getStatus();
                    if (userStatus != 200) {
                        IO.println("Fel från servern vid hämtning av användare. Statuskod: " + userStatus);
                        break;
                    }

                    String usersBody = usersResponse.getBody();
                    Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
                    manager.setUsers(gson.fromJson(usersBody, userListType));

                    IO.println("Användare hämtade från servern. Antal: " + manager.getUsers().size());
                    break;

                case 8:
                    IO.println("Skriver ut alla användare...");
                    manager.printUsersSorted();
                    break;

                case 9:
                    IO.println("Hitta användare via email...");
                    String email = IO.readln("Ange email: ");

                    User foundUser = manager.findUserByEmail(email);

                    if (foundUser == null) {
                        IO.println("Ingen användare hittades med den email-adressen");
                    } else {
                        IO.println("Användaren hittad");
                        IO.println(foundUser.getInfo());
                    }
                    break;

                case 10: 
                    IO.println("Hämtar alla avstängda användare...");
                    HttpResponse<String> suspendedResponse;
                    try {
                        suspendedResponse = Unirest.get(baseURL + "/suspended").asString();
                    } catch (UnirestException e) {
                        IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
                        break;
                    }

                    int suspendedStatus = suspendedResponse.getStatus();
                    if (suspendedStatus != 200) {
                        IO.println("Fel vid hämtning av avstängda användare. Statuskod: " + suspendedStatus);
                        break;
                    }

                    String suspendedBody = suspendedResponse.getBody();
                    Type suspendedListType = new TypeToken<ArrayList<SuspendedUser>>() {}.getType();
                    manager.setSuspendedUsers(gson.fromJson(suspendedBody, suspendedListType));

                    IO.println("Avstängda användare hämtade från servern. Antal: " + manager.getSuspendedUsers().size());
                    break;

                case 11:
                    IO.println("Kontrollerar om användare får låna...");
                    String customerIdToCheck = IO.readln("Ange användarens id: ");

                    if (manager.canUserBorrow(customerIdToCheck)) {
                        IO.println("Användaren får låna");
                    } else {
                        IO.println("Användaren är avstängd och får inte låna.");
                    }
                    break;

                case 12:
                    IO.println("Lägger till en användare på servern...");
                    String userName = IO.readln("Ange namn: ");
                    String userEmail = IO.readln("Ange email: ");

                    User newUser = new User(null, userName, userEmail);
                    String userJson = gson.toJson(newUser);

                    HttpResponse<String> postUserResponse;
                    try {
                        postUserResponse = Unirest.post(baseURL + "users")
                                .header("Content-Type", "application/json")
                                .body(userJson)
                                .asString();
                    } catch (UnirestException e) {
                        IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
                        break;
                    }

                    int postUserStatus = postUserResponse.getStatus();
                    if (postUserStatus != 201 && postUserStatus != 200) {
                        IO.println("Fel vid skapande av användare. Statuskod: " + postUserStatus);
                        IO.println("Svar från servern: " + postUserResponse.getBody());
                        break;
                    }

                    User savedUser = gson.fromJson(postUserResponse.getBody(), User.class);
                    manager.addUser(savedUser);

                    IO.println("Användaren lades till på servern");
                    break;

                case 13: 
                    IO.println("Lägger till en bok på servern...");
                    String postBookTitle = IO.readln("Ange titel: ");
                    String postBookAuthor = IO.readln("Ange författare: ");
                    String postBookGenre = IO.readln("Ange genre: ");
                    int postBookPages;
                    try {
                        postBookPages = Integer.parseInt(IO.readln("Ange antal sidor: "));
                    } catch (NumberFormatException e) {
                        IO.println("Felaktigt antal sidor.");
                        break;
                    }

                    Book newServerBook = new Book(null, postBookTitle, true, postBookAuthor, postBookGenre, postBookPages);
                    String bookJson = gson.toJson(newServerBook);

                    HttpResponse<String> postBookResponse;
                    try {
                        postBookResponse = Unirest.post(baseURL + "books")
                                .header("Content-Type", "application/json")
                                .body(bookJson)
                                .asString();
                    } catch (UnirestException e) {
                        IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
                        break;
                    }

                    int postBookStatus = postBookResponse.getStatus();
                    if (postBookStatus != 201 && postBookStatus != 200) {
                        IO.println("Fel vid skapande av bok. Statuskod: " + postBookStatus);
                        IO.println("Svar från servern: " + postBookResponse.getBody());
                        break;
                    }

                    Book savedBook = gson.fromJson(postBookResponse.getBody(), Book.class);
                    manager.addBook(savedBook);

                    IO.println("Boken lades till på servern");
                    break;

                case 14:
                    IO.println("Lägger till en tidning på servern...");
                    String postMagazineTitle = IO.readln("Ange titel: ");
                    String postMagazineCategory = IO.readln("Ange kategori: ");
                    
                    int postIssueNumber;
                    try {
                        postIssueNumber = Integer.parseInt(IO.readln("Ange nummer: "));
                    } catch (NumberFormatException e) {
                        IO.println("Felaktigt nummer");
                        break;
                    }

                    int postPublishedYear;
                    try {
                        postPublishedYear = Integer.parseInt(IO.readln("Ange publiceringsår: "));
                    } catch (NumberFormatException e) {
                        IO.println("Felaktigt år.");
                        break;
                    }

                    Magazine newServerMagazine = new Magazine(null, postMagazineTitle, true, postIssueNumber, postMagazineCategory, postPublishedYear);
                    String magazineJson = gson.toJson(newServerMagazine);

                    HttpResponse<String> postMagazineResponse;
                    try {
                        postMagazineResponse = Unirest.post(baseURL + "magazines")
                                .header("Content-Type", "application/json")
                                .body(magazineJson)
                                .asString();
                    } catch (UnirestException e) {
                        IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
                        break;
                    }

                    int postMagazineStatus = postMagazineResponse.getStatus();
                    if (postMagazineStatus != 201 && postMagazineStatus != 200) {
                        IO.println("Fel vid skapande av tidning. Statuskod: " + postMagazineStatus);
                        IO.println("Svar från servern: " + postMagazineResponse.getBody());
                        break;
                    }

                    Magazine savedMagazine = gson.fromJson(postMagazineResponse.getBody(), Magazine.class);
                    manager.addMagazine(savedMagazine);

                    IO.println("Tidningen lades till på servern");
                    break;

                case 15:
                    IO.println("Tar bort användare via email...");
                    String emailToDelete = IO.readln("Ange email för användare som ska tas bort: ");

                    User userToDelete = manager.findUserByEmail(emailToDelete);

                    if (userToDelete == null) {
                        IO.println("Ingen användare hittades med den email-adressen.");
                        break;
                    }

                    String userIdToDelete = userToDelete.getId();

                    HttpResponse<String> deleteUserResponse;
                    try {
                        deleteUserResponse = Unirest.delete(baseURL + "users/" + userIdToDelete).asString();
                    } catch (UnirestException e) {
                        IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
                        break;
                    }

                    int deleteUserStatus = deleteUserResponse.getStatus();
                    if (deleteUserStatus != 200 && deleteUserStatus != 204) {
                        IO.println("Fel vid borttagning av användare. Statuskod: " + deleteUserStatus);
                        IO.println("Svar från servern: " + deleteUserResponse.getBody());
                        break;
                    }

                    manager.removeUser(userToDelete);
                    IO.println("Användaren togs bort från servern och från den lokala samlingen");
                    break;

                case 16:
                    IO.println("Tar bort bok via titel...");
                    String titleToDelete = IO.readln("Ange titel på boken som ska tas bort: ");

                    Book bookToDelete = manager.findBookByTitle(titleToDelete);

                    if (bookToDelete == null) {
                        IO.println("Ingen bok hittades med den titeln.");
                        break;
                    }

                    String bookIdToDelete = bookToDelete.getId();

                    HttpResponse<String> deleteBookResponse;
                    try {
                        deleteBookResponse = Unirest.delete(baseURL + "books/" + bookIdToDelete).asString();
                    } catch (UnirestException e) {
                        IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
                        break;
                    }

                    int deleteBookStatus = deleteBookResponse.getStatus();
                    if (deleteBookStatus != 200 && deleteBookStatus != 204) {
                        IO.println("Fel vid borttagning av bok. Statuskod: " + deleteBookStatus);
                        IO.println("Svar från servern: " + deleteBookResponse.getBody());
                        break;
                    }

                    manager.removeBook(bookToDelete);
                    IO.println("Boken togs bort från servern och från den lokala samlingen.");
                    break;

                case 17:
                    IO.println("Tar bort tidning via titel...");
                    String magazineTitleToDelete = IO.readln("Ange titel på tidningen som ska tas bort: ");

                    Magazine magazineToDelete = manager.findMagazineByTitle(magazineTitleToDelete);

                    if (magazineToDelete == null) {
                        IO.println("Ingen tidning hittades med den titeln.");
                        break;
                    }

                    String magazineIdToDelete = magazineToDelete.getId();

                    HttpResponse<String> deleteMagazineResponse;
                    try {
                        deleteMagazineResponse = Unirest.delete(baseURL + "magazines/" + magazineIdToDelete).asString();
                    } catch (UnirestException e) {
                        IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
                        break;
                    }

                    int deleteMagazineStatus = deleteMagazineResponse.getStatus();
                    if (deleteMagazineStatus != 200 && deleteMagazineStatus != 204) {
                        IO.println("Fel vid borttagning av tidning. Statuskod: " + deleteMagazineStatus);
                        IO.println("Svar från servern: " + deleteMagazineResponse.getBody());
                        break;
                    }

                    manager.removeMagazine(magazineToDelete);
                    IO.println("Tidningen togs bort från servern och från den lokala samlingen.");
                    break;

                case 18:
                    IO.println("Tar bort avstängd användare via id...");
                    String suspendedIdToDelete = IO.readln("Ange id på avstängningen som ska tas bort: ");

                    HttpResponse<String> deleteSuspendedResponse;
                    try {
                        deleteSuspendedResponse = Unirest.delete(baseURL + "suspended/" + suspendedIdToDelete).asString();
                    } catch (UnirestException e) {
                        IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
                        break;
                    }

                    int deleteSuspendedStatus = deleteSuspendedResponse.getStatus();
                    if (deleteSuspendedStatus != 200 && deleteSuspendedStatus != 204) {
                        IO.println("Fel vid borttagning av avstängd användare. Statuskod: " + deleteSuspendedStatus);
                        IO.println("Svar från servern: " + deleteSuspendedResponse.getBody());
                        break;
                    }

                    manager.removeSuspendedUserById(suspendedIdToDelete);
                    IO.println("Den avstängda användaren togs bort från servern och från den lokala samlingen.");
                    break;

                case 19:
                    kör = false;
                    IO.println("Programmet avslutas");
                    break;

                default:
                    IO.println("Ogiltigt val");
            }
        }

        Unirest.shutDown(); // Stänger Unirest när programmet är klart

    }
}