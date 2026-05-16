package jk;

/**
 * Författare: Johnny Battah
 * Programmet är ett enkelt bibliotekssystem på E nivå
 * Det kan hämta böcker och tidningar från en server, visa dem, 
 * och lägga till Nya böcker/tidningar lokalt i Arraylist
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
                        12. Avsluta
                    """);

            String input = IO.readln("Välj ett alternativ (1-12): ");
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
                    IO.print("Hämtar alla avstängda användare...");
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

                    IO.println("Avstängda användare hämtad från servern. Antal: " + manager.getSuspendedUsers().size());
                    break;

                case 11:
                    IO.println("Kontrollerar om användare får låna...");
                    String userIdToCheck = IO.readln("Ange användarens id: ");

                    if (manager.canUserBorrow(userIdToCheck)) {
                        IO.println("Användaren får låna");
                    } else {
                        IO.println("användaren är avstängd och får inte låna.");
                    }
                    break;

                case 12:
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