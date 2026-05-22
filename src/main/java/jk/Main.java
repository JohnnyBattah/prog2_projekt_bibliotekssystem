package jk;

/**
 * Författare: Johnny Battah
 * Programmet är ett bibliotekssystem som kommunicerar med en JSON-server.
 * Användaren kan via menyer hämta, skriva ut, skapa, söka och ta bort
 * böcker, tidningar, användare och avstängda användare.
 * Programmet kan också söka böcker och tidningar via titel, söka användare
 * via e-post och kontrollera om en användare får låna eller inte.
 * Main ansvarar för menyerna och skickar vidare användarens val till
 * LibraryManager där själva logiken finns.
 */

import kong.unirest.Unirest;

public class Main {
    public static void main(String[] args) {
        // skoladress: http://10.151.168.5:3137/
        // hemadress: http://localhost:3000/

        LibraryManager manager = new LibraryManager();

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
                        9. Avsluta
                    """);

            String input = IO.readln("Välj ett alternativ (1-9): ");
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

                                    === HÄMTA DATA (ALLA) ===
                                    1. Hämta böcker
                                    2. Hämta tidningar
                                    3. Hämta användare
                                    4. Hämta avstängda användare
                                    5. Hämta media
                                    6. Tillbaka
                                """);

                        String hämtaInput = IO.readln("Välj ett alternativ (1-6): ");
                        int hämtaVal;

                        try {
                            hämtaVal = Integer.parseInt(hämtaInput);
                        } catch (NumberFormatException e) {
                            IO.println("Felaktig inmatning. Ange ett nummer från menyn.");
                            continue;
                        }

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
                                    5. Tillbaka
                                """);

                        String hämtaEnInput = IO.readln("Välj ett alternativ (1-5): ");
                        int hämtaEnVal;

                        try {
                            hämtaEnVal = Integer.parseInt(hämtaEnInput);
                        } catch (NumberFormatException e) {
                            IO.println("Felaktig inmatning. Ange ett nummer från menyn.");
                            continue;
                        }

                        switch (hämtaEnVal) {
                            case 1 -> manager.fetchOneBook();
                            case 2 -> manager.fetchOneMagazine();
                            case 3 -> manager.fetchOneUser();
                            case 4 -> manager.fetchOneSuspendedUser();
                            case 5 -> hämtaEnMeny = false;
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

                        String skrivUtInput = IO.readln("Välj ett alternativ (1-7): ");
                        int skrivUtVal;

                        try {
                            skrivUtVal = Integer.parseInt(skrivUtInput);
                        } catch (NumberFormatException e) {
                            IO.println("Felaktig inmatning. Ange ett nummer från menyn.");
                            continue;
                        }

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

                        String sökInput = IO.readln("Välj ett alternativ (1-6): ");
                        int sökVal;

                        try {
                            sökVal = Integer.parseInt(sökInput);
                        } catch (NumberFormatException e) {
                            IO.println("Felaktig inmatning. Ange ett nummer från menyn.");
                            continue;
                        }

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

                        String läggTillInput = IO.readln("Välj ett alternativ (1-5): ");
                        int läggTillVal;

                        try {
                            läggTillVal = Integer.parseInt(läggTillInput);
                        } catch (NumberFormatException e) {
                            IO.println("Felaktig inmatning. Ange ett nummer från menyn.");
                            continue;
                        }

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
                            case 1 -> manager.deleteBook();
                            case 2 -> manager.deleteMagazine();
                            case 3 -> manager.deleteUser();
                            case 4 -> manager.deleteSuspendedUser();
                            case 5 -> taBortMeny = false;
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

                        String lånInput = IO.readln("Välj ett alternativ (1-7): ");
                        int lånVal;

                        try {
                            lånVal = Integer.parseInt(lånInput);
                        } catch (NumberFormatException e) {
                            IO.println("Felaktig inmatning. Ange ett nummer från menyn.");
                            continue;
                        }

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

                        String streamInput = IO.readln("Välj ett alternativ (1-4): ");
                        int streamVal;

                        try {
                            streamVal = Integer.parseInt(streamInput);
                        } catch (NumberFormatException e) {
                            IO.println("Felaktig inmatning. Ange ett nummer från menyn.");
                            continue;
                        }

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

                                    String filterInput = IO.readln("Välj ett alternativ (1-5): ");
                                    int filterVal;

                                    try {
                                        filterVal = Integer.parseInt(filterInput);
                                    } catch (NumberFormatException e) {
                                        IO.println("Felaktig inmatning. Ange ett nummer från menyn.");
                                        continue;
                                    }

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

                                    String sorteringInput = IO.readln("Välj ett alternativ (1-4): ");
                                    int sorteringVal;

                                    try {
                                        sorteringVal = Integer.parseInt(sorteringInput);
                                    } catch (NumberFormatException e) {
                                        IO.println("Felaktig inmatning. Ange ett nummer från menyn.");
                                        continue;
                                    }

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

                                    String statistikInput = IO.readln("Välj ett alternativ (1-7): ");
                                    int statistikVal;

                                    try {
                                        statistikVal = Integer.parseInt(statistikInput);
                                    } catch (NumberFormatException e) {
                                        IO.println("Felaktig inmatning. Ange ett nummer från menyn.");
                                        continue;
                                    }

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