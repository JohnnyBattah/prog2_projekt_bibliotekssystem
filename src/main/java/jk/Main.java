package jk;

/**
 * Författare: Johnny Battah
 * Programmet är ett bibliotekssystem som hämtar, visar, skapar och tar bort
 * böcker, tidningar, användare och avstängda användare via en JSON-server. 
 * Programmet kan också söka användare via e-post och kontrollera om en användare får låna.
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
        Gson gson = new Gson(); // Omvandla mellan JSON och Java-objekt

        LibraryManager manager = new LibraryManager();

        boolean kör = true;

        while (kör) {
            IO.println("""

                        === HUVUDMENY ===
                        1. Hämta data
                        2. Skriv ut data
                        3. Sök och kontrollera
                        4. Lägg till
                        5. Ta bort
                        6. Avsluta
                    """);

            String input = IO.readln("Välj ett alternativ (1-6): ");
            int val;

            // Försöker omvandla användarens val till ett heltal
            try {
                val = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                IO.println("Felaktig inmatning. Ange ett nummer från menyn.");
                continue;
            }

            switch (val) {
                case 1:
                    boolean hämtaMeny = true;

                    while (hämtaMeny) {
                        IO.println("""

                                    === HÄMTA DATA ===
                                    1. Hämta böcker
                                    2. Hämta tidningar
                                    3. Hämta användare
                                    4. Hämta avstängda användare
                                    5. Tillbaka
                                """);

                        String hämtaInput = IO.readln("Välj ett alternativ (1-5): ");
                        int hämtaVal;

                        try {
                            hämtaVal = Integer.parseInt(hämtaInput);
                        } catch (NumberFormatException e) {
                            IO.println("Felaktig inmatning. Ange ett nummer från menyn.");
                            continue;
                        }

                        switch (hämtaVal) {
                            case 1:
                                IO.println("Hämtar alla böcker...");
                                manager.fetchBooks(baseURL, gson);
                                break;

                            case 2:
                                IO.println("Hämtar alla tidningar...");
                                manager.fetchMagazines(baseURL, gson);
                                break;

                            case 3:
                                IO.println("Hämtar alla användare...");
                                manager.fetchUsers(baseURL, gson);
                                break;

                            case 4:
                                IO.println("Hämtar alla avstängda användare...");
                                manager.fetchSuspendedUsers(baseURL, gson);
                                break;

                            case 5:
                                hämtaMeny = false;
                                break;

                            default:
                                IO.println("Ogiltigt val. Ange ett nummer från menyn.");
                        }
                    }
                    break;

                case 2:
                    boolean skrivUtMeny = true;
                    while (skrivUtMeny) {
                        IO.println("""

                                    === SKRIV UT DATA ===
                                    1. Skriv ut böcker
                                    2. Skriv ut tidningar
                                    3. Skriv ut användare
                                    4. Skriv ut avstängda användare
                                    5. Tillbaka
                                """);

                        String skrivUtInput = IO.readln("Välj ett alternativ (1-5): ");
                        int skrivUtVal;

                        try {
                            skrivUtVal = Integer.parseInt(skrivUtInput);
                        } catch (NumberFormatException e) {
                            IO.println("Felaktig inmatning. Ange ett nummer från menyn.");
                            continue;
                        }

                        switch (skrivUtVal) {
                            case 1:
                                IO.println("Skriver ut alla böcker...");
                                manager.printBooksSorted();
                                break;

                            case 2:
                                IO.println("Skriver ut alla tidningar...");
                                manager.printMagazinesSorted();
                                break;

                            case 3:
                                IO.println("Skriver ut alla användare...");
                                manager.printUsersSorted();
                                break;

                            case 4:
                                IO.println("Skriver ut alla avstängda användare...");
                                manager.printSuspendedUsersSorted();
                                break;

                            case 5:
                                skrivUtMeny = false;
                                break;

                            default:
                                IO.println("Ogiltigt val. Ange ett nummer från menyn.");
                        }
                    }
                    break;

                case 3:
                    boolean sökMeny = true;

                    while (sökMeny) {
                        IO.println("""

                                    === SÖK OCH KONTROLLERA ===
                                    1. Hitta användare via e-post
                                    2. Kontrollera om användare får låna
                                    3. Tillbaka
                                """);

                        String sökInput = IO.readln("Välj ett alternativ (1-3): ");
                        int sökVal;

                        try {
                            sökVal = Integer.parseInt(sökInput);
                        } catch (NumberFormatException e) {
                            IO.println("Felaktig inmatning. Ange ett nummer från menyn.");
                            continue;
                        }

                        switch (sökVal) {
                            case 1:
                                IO.println("Hitta användare via e-post...");
                                String email = IO.readln("Ange e-post: ");

                                User foundUser = manager.findUserByEmail(email);

                                if (foundUser == null) {
                                    IO.println("Ingen användare hittades med den e-postadressen.");
                                } else {
                                    IO.println("Användaren hittades:");
                                    IO.println(foundUser.getInfo());
                                }
                                break;

                            case 2:
                                IO.println("Kontrollerar om användare får låna...");
                                String customerIdToCheck = IO.readln("Ange användarens id: ");

                                if (manager.canUserBorrow(customerIdToCheck)) {
                                    IO.println("Användaren får låna.");
                                } else {
                                    IO.println("Användaren är avstängd och får inte låna.");
                                }
                                break;

                            case 3:
                                sökMeny = false;
                                break;

                            default:
                                IO.println("Ogiltigt val. Ange ett nummer från menyn.");
                        }
                    }
                    break;

                case 4:
                    boolean läggTillMeny = true;

                    while (läggTillMeny) {
                        IO.println("""

                                    === LÄGG TILL ===
                                    1. Lägg till användare på servern
                                    2. Lägg till bok på servern
                                    3. Lägg till tidning på servern
                                    4. Lägg till avstängd användare på servern
                                    5. Tillbaka
                                """);

                        String läggTillInput = IO.readln("Välj ett alternativ (1-5): ");
                        int läggTillVal;

                        try {
                            läggTillVal = Integer.parseInt(läggTillInput);
                        } catch (NumberFormatException e) {
                            IO.println("Felaktig inmatning. Ange ett nummer från menyn.");
                            continue;
                        }

                        switch (läggTillVal) {
                            case 1:
                                IO.println("Lägger till en användare på servern...");
                                String userName = IO.readln("Ange namn: ");
                                String userEmail = IO.readln("Ange e-post: ");

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

                                IO.println("Användaren lades till på servern och i den lokala samlingen.");
                                break;

                            case 2:
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

                                Book newServerBook = new Book(null, postBookTitle, true, postBookAuthor, postBookGenre,
                                        postBookPages);
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

                                IO.println("Boken lades till på servern och i den lokala samlingen.");
                                break;

                            case 3:
                                IO.println("Lägger till en tidning på servern...");
                                String postMagazineTitle = IO.readln("Ange titel: ");
                                String postMagazineCategory = IO.readln("Ange kategori: ");

                                int postIssueNumber;
                                try {
                                    postIssueNumber = Integer.parseInt(IO.readln("Ange nummer: "));
                                } catch (NumberFormatException e) {
                                    IO.println("Felaktigt nummer.");
                                    break;
                                }

                                int postPublishedYear;
                                try {
                                    postPublishedYear = Integer.parseInt(IO.readln("Ange publiceringsår: "));
                                } catch (NumberFormatException e) {
                                    IO.println("Felaktigt år.");
                                    break;
                                }

                                Magazine newServerMagazine = new Magazine(null, postMagazineTitle, true,
                                        postIssueNumber, postMagazineCategory, postPublishedYear);
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

                                IO.println("Tidningen lades till på servern och i den lokala samlingen.");
                                break;

                            case 4:
                                IO.println("Lägger till en avstängd användare på servern...");
                                String suspendedId = IO.readln("Ange id för avstängningen: ");
                                String customerId = IO.readln("Ange användarens id: ");

                                SuspendedUser newSuspendedUser = new SuspendedUser(suspendedId, customerId);
                                String suspendedJson = gson.toJson(newSuspendedUser);

                                HttpResponse<String> postSuspendedResponse;
                                try {
                                    postSuspendedResponse = Unirest.post(baseURL + "suspended")
                                            .header("Content-Type", "application/json")
                                            .body(suspendedJson)
                                            .asString();
                                } catch (UnirestException e) {
                                    IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
                                    break;
                                }

                                int postSuspendedStatus = postSuspendedResponse.getStatus();
                                if (postSuspendedStatus != 201 && postSuspendedStatus != 200) {
                                    IO.println("Fel vid skapande av avstängd användare. Statuskod: " + postSuspendedStatus);
                                    IO.println("Svar från servern: " + postSuspendedResponse.getBody());
                                    break;
                                }

                                SuspendedUser savedSuspendedUser = gson.fromJson(postSuspendedResponse.getBody(), SuspendedUser.class);
                                manager.addSuspendedUser(savedSuspendedUser);

                                IO.println("Den avstängda användaren lades till på servern och i den lokala samlingen.");
                                break;

                            case 5:
                                läggTillMeny = false;
                                break;

                            default:
                                IO.println("Ogiltigt val. Ange ett nummer från menyn.");
                        }
                    }
                    break;

                case 5:
                    boolean taBortMeny = true;

                    while (taBortMeny) {
                        IO.println("""

                                    === TA BORT ===
                                    1. Ta bort användare via e-post
                                    2. Ta bort bok via titel
                                    3. Ta bort tidning via titel
                                    4. Ta bort avstängd användare via id
                                    5. Tillbaka
                                """);

                        String taBortInput = IO.readln("Välj ett alternativ (1-5): ");
                        int taBortVal;

                        try {
                            taBortVal = Integer.parseInt(taBortInput);
                        } catch (NumberFormatException e) {
                            IO.println("Felaktig inmatning. Ange ett nummer från menyn.");
                            continue;
                        }

                        switch (taBortVal) {
                            case 1:
                                IO.println("Tar bort användare via e-post...");
                                String emailToDelete = IO.readln("Ange e-post för användare som ska tas bort: ");

                                User userToDelete = manager.findUserByEmail(emailToDelete);

                                if (userToDelete == null) {
                                    IO.println("Ingen användare hittades med den e-postadressen.");
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
                                IO.println("Användaren togs bort från servern och från den lokala samlingen.");
                                break;

                            case 2:
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

                            case 3:
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
                                    deleteMagazineResponse = Unirest.delete(baseURL + "magazines/" + magazineIdToDelete)
                                            .asString();
                                } catch (UnirestException e) {
                                    IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
                                    break;
                                }

                                int deleteMagazineStatus = deleteMagazineResponse
                                        .getStatus();
                                if (deleteMagazineStatus != 200 && deleteMagazineStatus != 204) {
                                    IO.println("Fel vid borttagning av tidning. Statuskod: " + deleteMagazineStatus);
                                    IO.println("Svar från servern: " + deleteMagazineResponse.getBody());
                                    break;
                                }

                                manager.removeMagazine(magazineToDelete);
                                IO.println("Tidningen togs bort från servern och från den lokala samlingen.");
                                break;

                            case 4:
                                IO.println("Tar bort avstängd användare via id...");
                                String suspendedIdToDelete = IO.readln("Ange id på avstängningen som ska tas bort: ");

                                HttpResponse<String> deleteSuspendedResponse;
                                try {
                                    deleteSuspendedResponse = Unirest
                                            .delete(baseURL + "suspended/" + suspendedIdToDelete)
                                            .asString();
                                } catch (UnirestException e) {
                                    IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
                                    break;
                                }

                                int deleteSuspendedStatus = deleteSuspendedResponse
                                        .getStatus();
                                if (deleteSuspendedStatus != 200 && deleteSuspendedStatus != 204) {
                                    IO.println("Fel vid borttagning av avstängd användare. Statuskod: "
                                            + deleteSuspendedStatus);
                                    IO.println("Svar från servern: " + deleteSuspendedResponse.getBody());
                                    break;
                                }

                                manager.removeSuspendedUserById(suspendedIdToDelete);
                                IO.println(
                                        "Den avstängda användaren togs bort från servern och från den lokala samlingen.");
                                break;

                            case 5:
                                taBortMeny = false;
                                break;

                            default:
                                IO.println("Ogiltigt val. Ange ett nummer från menyn.");
                        }
                    }
                    break;

                case 6:
                    kör = false;
                    IO.println("Programmet avslutas.");
                    break;

                default:
                    IO.println("Ogiltigt val. Ange ett nummer från menyn.");

            }
        }

        Unirest.shutDown(); // Stänger Unirest när programmet är klart

    }
}