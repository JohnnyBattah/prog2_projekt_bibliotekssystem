package jk.main;

import jk.manager.LibraryManager;
import kong.unirest.Unirest;

/**
 * Programmet är ett bibliotekssystem som kommunicerar med en JSON-server.
 * Användaren kan via menyer hämta, skriva ut, skapa, söka och ta bort
 * böcker, tidningar, användare, avstängda användare och media.
 * Programmet kan också hantera lån, återlämning, streams och filhantering.
 * Main ansvarar för menyerna och skickar vidare användarens val till
 * LibraryManager där själva programmets logik finns.
 * 
 * @author Johnny Battah
 * @version 1.0
 * @since 2026
 */

public class Main {
    /**
     * Startar programmet, läser in data och visar huvudmenyn.
     * 
     * @param args eventuella argument från kommandoraden
     */
    public static void main(String[] args) {
        LibraryManager manager = new LibraryManager();
        manager.loadAllData();

        boolean kör = true;

        while (kör) {
            IO.println("""

                        === HUVUDMENY ===
                        1. Hämta data (alla)
                        2. Hämta data (en via id)
                        3. Skriv ut data
                        4. Sök och kontrollera
                        5. Lägg till
                        6. Ta bort
                        7. Lån och återlämning
                        8. Streams och statistik
                        9. Filhantering media
                        10. Avsluta
                    """);

            int val = readMenuChoice("Välj ett alternativ (1-10): ");

            switch (val) {
                case 1:
                    boolean hämtaMeny = true;

                    while (hämtaMeny) {
                        IO.println("""

                                    === HÄMTA DATA (ALLA) ===
                                    1. Hämta böcker
                                    2. Hämta tidningar
                                    3. Hämta användare
                                    4. Hämta avstängda användare
                                    5. Hämta media
                                    6. Tillbaka
                                """);

                        int hämtaVal = readMenuChoice("Välj ett alternativ (1-6): ");

                        switch (hämtaVal) {
                            case 1 -> manager.fetchBooks();
                            case 2 -> manager.fetchMagazines();
                            case 3 -> manager.fetchUsers();
                            case 4 -> manager.fetchSuspendedUsers();
                            case 5 -> manager.fetchMedia();
                            case 6 -> hämtaMeny = false;
                            default -> IO.println("Ogiltigt val. Ange ett nummer från menyn.");
                        }
                    }
                    break;

                case 2:
                    boolean hämtaEnMeny = true;
                    while (hämtaEnMeny) {
                        IO.println("""

                                    === HÄMTA DATA (EN VIA ID) ===
                                    1. Hämta en bok
                                    2. Hämta en tidning
                                    3. Hämta en användare
                                    4. Hämta en avstängd användare
                                    5. Hämta media
                                    6. Tillbaka
                                """);

                        int hämtaEnVal = readMenuChoice("Välj ett alternativ (1-6): ");

                        switch (hämtaEnVal) {
                            case 1 -> manager.fetchOneBook();
                            case 2 -> manager.fetchOneMagazine();
                            case 3 -> manager.fetchOneUser();
                            case 4 -> manager.fetchOneSuspendedUser();
                            case 5 -> manager.fetchOneMedia();
                            case 6 -> hämtaEnMeny = false;
                            default -> IO.println("Ogiltigt val. Ange ett nummer från menyn.");
                        }
                    }
                    break;

                case 3:
                    boolean skrivUtMeny = true;
                    while (skrivUtMeny) {
                        IO.println("""

                                    === SKRIV UT DATA ===
                                    1. Skriv ut böcker
                                    2. Skriv ut tidningar
                                    3. Skriv ut användare
                                    4. Skriv ut avstängda användare
                                    5. Skriv ut media
                                    6. Skriv ut lån
                                    7. Tillbaka
                                """);

                        int skrivUtVal = readMenuChoice("Välj ett alternativ (1-7): ");

                        switch (skrivUtVal) {
                            case 1 -> manager.printBooksSorted();
                            case 2 -> manager.printMagazinesSorted();
                            case 3 -> manager.printUsersSorted();
                            case 4 -> manager.printSuspendedUsersSorted();
                            case 5 -> manager.printMedia();
                            case 6 -> manager.printLoans();
                            case 7 -> skrivUtMeny = false;
                            default -> IO.println("Ogiltigt val. Ange ett nummer från menyn.");
                        }
                    }
                    break;

                case 4:
                    boolean sökMeny = true;

                    while (sökMeny) {
                        IO.println("""

                                    === SÖK OCH KONTROLLERA ===
                                    1. Hitta bok via titel
                                    2. Hitta tidning via titel
                                    3. Hitta användare via e-post
                                    4. Hitta media via titel
                                    5. Kontrollera om användare får låna
                                    6. Tillbaka
                                """);

                        int sökVal = readMenuChoice("Välj ett alternativ (1-6): ");

                        switch (sökVal) {
                            case 1 -> manager.findBookByTitleInteractive();
                            case 2 -> manager.findMagazineByTitleInteractive();
                            case 3 -> manager.findUserByEmailInteractive();
                            case 4 -> manager.findMediaByTitleInteractive();
                            case 5 -> manager.checkUserBorrow();
                            case 6 -> sökMeny = false;
                            default -> IO.println("Ogiltigt val. Ange ett nummer från menyn.");
                        }
                    }
                    break;

                case 5:
                    boolean läggTillMeny = true;

                    while (läggTillMeny) {
                        IO.println("""

                                    === LÄGG TILL ===
                                    1. Lägg till bok på servern
                                    2. Lägg till tidning på servern
                                    3. Lägg till användare på servern
                                    4. Lägg till avstängd användare på servern
                                    5. Tillbaka
                                """);

                        int läggTillVal = readMenuChoice("Välj ett alternativ (1-5): ");

                        switch (läggTillVal) {
                            case 1 -> manager.addBook();
                            case 2 -> manager.addMagazine();
                            case 3 -> manager.addUser();
                            case 4 -> manager.addSuspendedUser();
                            case 5 -> läggTillMeny = false;
                            default -> IO.println("Ogiltigt val. Ange ett nummer från menyn.");
                        }
                    }
                    break;

                case 6:
                    boolean taBortMeny = true;

                    while (taBortMeny) {
                        IO.println("""

                                    === TA BORT ===
                                    1. Ta bort bok via titel
                                    2. Ta bort tidning via titel
                                    3. Ta bort användare via e-post
                                    4. Ta bort avstängd användare via id
                                    5. Ta bort media via titel
                                    6. Tillbaka
                                """);

                        int taBortVal = readMenuChoice("Välj ett alternativ (1-6): ");

                        switch (taBortVal) {
                            case 1 -> manager.deleteBook();
                            case 2 -> manager.deleteMagazine();
                            case 3 -> manager.deleteUser();
                            case 4 -> manager.deleteSuspendedUser();
                            case 5 -> manager.deleteMedia();
                            case 6 -> taBortMeny = false;
                            default -> IO.println("Ogiltigt val. Ange ett nummer från menyn.");
                        }
                    }
                    break;

                case 7:
                    boolean lånMeny = true;

                    while (lånMeny) {
                        IO.println("""

                                    === LÅN OCH ÅTERLÄMNING ===
                                    1. Låna bok
                                    2. Lämna tillbaka bok
                                    3. Låna tidning
                                    4. Lämna tillbaka tidning
                                    5. Låna media
                                    6. Lämna tillbaka media
                                    7. Tillbaka
                                """);

                        int lånVal = readMenuChoice("Välj ett alternativ (1-7): ");

                        switch (lånVal) {
                            case 1 -> manager.borrowBook();
                            case 2 -> manager.returnBook();
                            case 3 -> manager.borrowMagazine();
                            case 4 -> manager.returnMagazine();
                            case 5 -> manager.borrowMedia();
                            case 6 -> manager.returnMedia();
                            case 7 -> lånMeny = false;
                            default -> IO.println("Ogiltigt val. Ange ett nummer från menyn.");
                        }
                    }
                    break;

                case 8:
                    boolean streamMeny = true;

                    while (streamMeny) {
                        IO.println("""

                                    === STREAMS ===
                                    1. Filtrering
                                    2. Sortering
                                    3. Statistik och map
                                    4. Tillbaka
                                """);

                        int streamVal = readMenuChoice("Välj ett alternativ (1-4): ");

                        switch (streamVal) {
                            case 1:
                                boolean filterMeny = true;

                                while (filterMeny) {
                                    IO.println("""

                                                === STREAMS - FILTRERING ===
                                                1. Filtrera böcker efter författare
                                                2. Filtrera böcker efter genre
                                                3. Visa utlånade böcker
                                                4. Visa tillgänglig media
                                                5. Tillbaka
                                            """);

                                    int filterVal = readMenuChoice("Välj ett alternativ (1-5): ");

                                    switch (filterVal) {
                                        case 1 -> manager.printBooksByAuthorStreamInteractive();
                                        case 2 -> manager.printBooksByGenreStreamInteractive();
                                        case 3 -> manager.printBorrowedBooksStream();
                                        case 4 -> manager.printAvailableMediaStream();
                                        case 5 -> filterMeny = false;
                                        default -> IO.println("Ogiltigt val. Ange ett nummer från menyn.");
                                    }
                                }
                                break;

                            case 2:
                                boolean sorteringsMeny = true;

                                while (sorteringsMeny) {
                                    IO.println("""

                                                === STREAMS - SORTERING ===
                                                1. Sortera böcker efter författare
                                                2. Sortera böcker efter genre
                                                3. Sortera media efter titel
                                                4. Tillbaka
                                            """);

                                    int sorteringVal = readMenuChoice("Välj ett alternativ (1-4): ");

                                    switch (sorteringVal) {
                                        case 1 -> manager.printBooksSortedByAuthorStream();
                                        case 2 -> manager.printBooksSortedByGenreStream();
                                        case 3 -> manager.printMediaSortedStream();
                                        case 4 -> sorteringsMeny = false;
                                        default -> IO.println("Ogiltigt val. Ange ett nummer från menyn.");
                                    }
                                }
                                break;

                            case 3:
                                boolean statistikMeny = true;

                                while (statistikMeny) {
                                    IO.println("""

                                                === STREAMS - STATISTIK OCH MAP ===
                                                1. Räkna antal böcker av en författare
                                                2. Visa alla boktitlar
                                                3. Visa alla bokförfattare
                                                4. Visa alla mediatitlar
                                                5. Kontrollera om en titel finns
                                                6. Visa totalt antal sidor i böcker
                                                7. Tillbaka
                                            """);

                                    int statistikVal = readMenuChoice("Välj ett alternativ (1-7): ");

                                    switch (statistikVal) {
                                        case 1 -> manager.countBooksByAuthorStreamInteractive();
                                        case 2 -> manager.printBookTitlesStream();
                                        case 3 -> manager.printBookAuthorsStream();
                                        case 4 -> manager.printMediaTitlesStream();
                                        case 5 -> manager.checkTitleExistsStreamInteractive();
                                        case 6 -> IO.println("Totalt antal sidor: " + manager.getTotalPagesStream());
                                        case 7 -> statistikMeny = false;
                                        default -> IO.println("Ogiltigt val. Ange ett nummer från menyn.");
                                    }
                                }
                                break;

                            case 4:
                                streamMeny = false;
                                break;

                            default:
                                IO.println("Ogiltigt val. Ange ett nummer från menyn.");
                        }
                    }
                    break;

                case 9:
                    boolean mediaFilMeny = true;

                    while (mediaFilMeny) {
                        IO.println("""

                                    === FILHANTERING MENY ===
                                    1. Spara media till fil
                                    2. Läs media från fil
                                    3. Tillbaka
                                """);

                        int mediaFilVal = readMenuChoice("Välj ett alternativ (1-3): ");

                        switch (mediaFilVal) {
                            case 1 -> manager.saveMediaToFile();
                            case 2 -> manager.loadMediaFromFile();
                            case 3 -> mediaFilMeny = false;
                            default -> IO.println("Ogiltigt val. Ange ett nummer från menyn.");
                        }
                    }
                    break;

                case 10:
                    kör = false;
                    IO.println("Programmet avslutas.");
                    break;

                default:
                    IO.println("Ogiltigt val. Ange ett nummer från menyn.");
            }
        }
        Unirest.shutDown(); // Stänger Unirest när programmet är klart
    }

    /**
     * Läser in ett menyval från användaren och fortsätter fråga tills en giltigt
     * heltal anges.
     * 
     * @param prompt texten som visas för användaren
     * @return användarens menyval som heltal
     */
    private static int readMenuChoice(String prompt) {
        while (true) {
            String input = IO.readln(prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                IO.println("Felaktig inmatning. Ange ett nummer från menyn.");
            }
        }
    }
}