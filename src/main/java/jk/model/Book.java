package jk.model;

/**
 * Klassen Book representerar en bok i bibliotekssystemet.
 * Den ärver från LibraryItem och innehåller gemensamma egenskaper
 * som id, titel och tillgänglighet, men har också egna egenskaper som
 * författare, genre och antal sidor.
 * Klassen implementerar Comparable<Book> så att böcker kan sorteras
 * alfabetiskt efter titel vid utskrift i programmet.
 * Klassen används av LibraryManager för att hantera böcker i programmet.
 * 
 * @author Johnny Battah
 * @version 1.0
 * @since 2026
 */

public class Book extends LibraryItem implements Comparable<Book> {
    /** Bokens författare. */
    private String author;

    /** Bokens genre. */
    private String genre;

    /** Antal sidor i boken. */
    private int pages;

    /**
     * Skapar ett nytt Book-objekt med angivna värden.
     * 
     * @param id          bokens id
     * @param title       bokens titel
     * @param isAvailable anger om boken är tillgänglig för lån
     * @param author      bokens författare
     * @param genre       bokens genre
     * @param pages       antal sidor i boken
     * @throws IllegalArgumentException om title, author eller genre är tomma
     *                                  eller om pages är mindre än eller lika med 0
     */
    public Book(String id, String title, boolean isAvailable, String author, String genre, int pages) {
        super(id, title, isAvailable);
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Författare får inte vara tom.");
        }
        if (genre == null || genre.isBlank()) {
            throw new IllegalArgumentException("Genre får inte vara tom.");
        }
        if (pages <= 0) {
            throw new IllegalArgumentException("Antal sidor måste vara större än 0.");
        }

        this.author = author;
        this.genre = genre;
        this.pages = pages;
    }

    /**
     * Hämtar bokens författare.
     * 
     * @return bokens författare
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Hämtar bokens genre.
     * 
     * @return bokens genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Hämtar antal sidor i boken.
     * 
     * @return antal sidor i boken
     */
    public int getPages() {
        return pages;
    }

    /**
     * Anger en ny författare för boken.
     * 
     * @param author bokens nya författare
     * @throws IllegalArgumentException om författaren är tom
     */
    public void setAuthor(String author) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Författare får inte vara tom.");
        }
        this.author = author;
    }

    /**
     * Anger en ny genre för boken.
     * 
     * @param genre bokens nya genre
     * @throws IllegalArgumentException om genren är tom
     */
    public void setGenre(String genre) {
        if (genre == null || genre.isBlank()) {
            throw new IllegalArgumentException("Genre får inte vara tom.");
        }
        this.genre = genre;
    }

    /**
     * Anger ett nytt sidantal för boken.
     * 
     * @param pages bokens nya antal sidor
     * @throws IllegalArgumentException om antal sidor är mindre än eller lika med 0
     */
    public void setPages(int pages) {
        if (pages <= 0) {
            throw new IllegalArgumentException("Antal sidor måste vara större än 0.");
        }
        this.pages = pages;
    }

    /**
     * Jämför denna bok med en annan bok utifrån titel i alfabetisk ordning.
     * 
     * @param other den andra boken som jämförelsen görs med
     * @return ett negativt tal, 0 eller ett positivt tal beroende på
     *         sorteringsordningen
     */
    @Override
    public int compareTo(Book other) {
        return this.title.compareToIgnoreCase(other.title);
    }

    /**
     * Returnerar en textbeskrivning av boken.
     * 
     * @return information om bokens id, titel, författare, genre, sidantal och
     *         tillgänglighet
     */
    @Override
    public String getInfo() {
        return """
                --- Bok ---
                ID: %s
                Titel: %s
                Författare: %s
                Genre: %s
                Sidor: %d
                Tillgänglig: %b
                """.formatted(id, title, author, genre, pages, isAvailable);
    }

}
