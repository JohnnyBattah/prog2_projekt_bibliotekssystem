package jk;

/**
 * Författare: Johnny Battah
 * Programmet är ett bibliotekssystem som hämtar, visar, skapar och tar bort
 * böcker, tidningar, användare och avstängda användare via en JSON-server. 
 * Programmet kan också söka användare via e-post och kontrollera om en användare får låna.
 */

import com.google.gson.Gson;
import kong.unirest.Unirest;

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

                                if (manager.getUsers().isEmpty()) {
                                    IO.println("Inga användare är hämtade. Hämta användare först.");
                                    break;
                                }

                                String email = IO.readln("Ange e-post: ");

                                if (email.isBlank()) {
                                    IO.println("E-post får inte vara tom.");
                                    break;
                                }

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

                                if (manager.getUsers().isEmpty()) {
                                    IO.println("Inga användare är hämtade. Hämta användare först.");
                                    break;
                                }

                                String customerIdToCheck = IO.readln("Ange användarens id: ");

                                if (customerIdToCheck.isBlank()) {
                                    IO.println("Id får inte vara tomt.");
                                    break;
                                }

                                User userToCheck = manager.findUserById(customerIdToCheck);

                                if (userToCheck == null) {
                                    IO.println("Ingen användare hittades med det id:t.");
                                } else if (manager.canUserBorrow(customerIdToCheck)) {
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
                                if (userName.isBlank()) {
                                    IO.println("Namn får inte vara tomt.");
                                    break;
                                }
                                String userEmail = IO.readln("Ange e-post: ");
                                if (userEmail.isBlank()) {
                                    IO.println("E-post får inte vara tom.");
                                    break;
                                }

                                if (manager.emailExists(userEmail)) {
                                    IO.println("Det finns redan en användare med den e-postadressen.");
                                    break;
                                }

                                manager.addUserToServer(baseURL, gson, userName, userEmail);
                                break;

                            case 2:
                                IO.println("Lägger till en bok på servern...");

                                String postBookTitle = IO.readln("Ange titel: ");
                                if (postBookTitle.isBlank()) {
                                    IO.println("Titel får inte vara tom.");
                                    break;
                                }

                                String postBookAuthor = IO.readln("Ange författare: ");
                                if (postBookAuthor.isBlank()) {
                                    IO.println("Författare får inte vara tom.");
                                    break;
                                }

                                String postBookGenre = IO.readln("Ange genre: ");
                                if (postBookGenre.isBlank()) {
                                    IO.println("Genre får inte vara tom.");
                                    break;
                                }

                                int postBookPages;
                                try {
                                    postBookPages = Integer.parseInt(IO.readln("Ange antal sidor: "));
                                } catch (NumberFormatException e) {
                                    IO.println("Felaktigt antal sidor.");
                                    break;
                                }

                                if (postBookPages <= 0) {
                                    IO.println("Antal sidor måste vara större än 0.");
                                    break;
                                }

                                if (manager.bookTitleExists(postBookTitle)) {
                                    IO.println("Det finns redan en bok med denna titel");
                                    break;
                                }

                                manager.addBookToServer(baseURL, gson, postBookTitle, postBookAuthor, postBookGenre, postBookPages);
                                break;

                            case 3:
                                IO.println("Lägger till en tidning på servern...");
                                String postMagazineTitle = IO.readln("Ange titel: ");
                                if (postMagazineTitle.isBlank()) {
                                    IO.println("Titel får inte vara tom.");
                                    break;
                                }

                                String postMagazineCategory = IO.readln("Ange kategori: ");
                                if (postMagazineCategory.isBlank()) {
                                    IO.println("Kategori får inte vara tom.");
                                    break;
                                }

                                int postIssueNumber;
                                try {
                                    postIssueNumber = Integer.parseInt(IO.readln("Ange nummer: "));
                                } catch (NumberFormatException e) {
                                    IO.println("Felaktigt nummer.");
                                    break;
                                }

                                if (postIssueNumber <= 0) {
                                    IO.println("Nummer måste vara större än 0.");
                                    break;
                                }

                                int postPublishedYear;
                                try {
                                    postPublishedYear = Integer.parseInt(IO.readln("Ange publiceringsår: "));
                                } catch (NumberFormatException e) {
                                    IO.println("Felaktigt år.");
                                    break;
                                }

                                if (postPublishedYear <= 0) {
                                    IO.println("Publiceringsår måste vara större än 0.");
                                    break;
                                }

                                if (manager.magazineTitleExists(postMagazineTitle)) {
                                    IO.println("Det finns redan en tidning med denna titel.");
                                    break;
                                }

                                manager.addMagazineToServer(baseURL, gson, postMagazineTitle, postMagazineCategory,
                                        postIssueNumber, postPublishedYear);
                                break;

                            case 4:
                                IO.println("Lägger till en avstängd användare på servern...");
                                
                                if (manager.getUsers().isEmpty()) {
                                    IO.println("Inga användare är hämtade. Hämta användare först.");
                                    break;
                                }

                                String suspendedId = IO.readln("Ange id för avstängningen: ");
                                if (suspendedId.isBlank()) {
                                    IO.println("Id får inte vara tomt.");
                                    break;
                                }

                                String customerId = IO.readln("Ange användarens id: ");
                                if (customerId.isBlank()) {
                                    IO.println("Användarens id får inte vara tomt.");
                                    break;
                                }

                                User userToSuspend = manager.findUserById(customerId);

                                if (userToSuspend == null) {
                                    IO.println("Ingen användare hittades med det id:t.");
                                    break;
                                }

                                if (manager.isUserAlreadySuspended(customerId)) {
                                    IO.println("Användaren är redan avstängd.");
                                    break;
                                }

                                manager.addSuspendedUserToServer(baseURL, gson, suspendedId, customerId);
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

                                if (manager.getUsers().isEmpty()) {
                                    IO.println("Inga användare är hämtade. Hämta användare först.");
                                    break;
                                }

                                String emailToDelete = IO.readln("Ange e-post för användare som ska tas bort: ");

                                if (emailToDelete.isBlank()) {
                                    IO.println("E-post får inte vara tom.");
                                    break;
                                }

                                manager.deleteUserByEmailFromServer(baseURL, emailToDelete);
                                break;

                            case 2:
                                IO.println("Tar bort bok via titel...");

                                if (manager.getBooks().isEmpty()) {
                                    IO.println("Inga böcker är hämtade. Hämta böcker först.");
                                    break;
                                }

                                String titleToDelete = IO.readln("Ange titel på boken som ska tas bort: ");

                                if (titleToDelete.isBlank()) {
                                    IO.println("Titel får inte vara tom.");
                                    break;
                                }

                                manager.deleteBookByTitleFromServer(baseURL, titleToDelete);
                                break;

                            case 3:
                                IO.println("Tar bort tidning via titel...");

                                if (manager.getMagazines().isEmpty()) {
                                    IO.println("Inga tidningar är hämtade. Hämta tidningar först.");
                                    break;
                                }

                                String magazineTitleToDelete = IO.readln("Ange titel på tidningen som ska tas bort: ");

                                if (magazineTitleToDelete.isBlank()) {
                                    IO.println("Titel får inte vara tom.");
                                    break;
                                }

                                manager.deleteMagazineByTitleFromServer(baseURL, magazineTitleToDelete);
                                break;

                            case 4:
                                IO.println("Tar bort avstängd användare via id...");

                                if (manager.getSuspendedUsers().isEmpty()) {
                                    IO.println("Inga avstängda användare är hämtade. Hämta avstängda användare först.");
                                    break;
                                }

                                String suspendedIdToDelete = IO.readln("Ange id på avstängningen som ska tas bort: ");

                                if (suspendedIdToDelete.isBlank()) {
                                    IO.println("Id får inte vara tomt.");
                                    break;
                                }

                                manager.deleteSuspendedUserByIdFromServer(baseURL, suspendedIdToDelete);
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