package jk.model;

/**
 * Klassen Magazine representerar en tidning i bibliotekssystemet.
 * Den ärver från LibraryItem och innehåller gemensamma egenskaper
 * som id, titel och tillgänglighet, men har också egna egenskaper som
 * nummer, kategori och publiceringsår.
 * Klassen implementerar Comparable<Magazine> så att tidningar kan sorteras
 * alfabetiskt efter titel vid utskrift i programmet.
 * Klassen används av LibraryManager för att hantera tidningar i programmet.
 * 
 * @author Johnny Battah
 * @version 1.0
 * @since 2026
 */

public class Magazine extends LibraryItem implements Comparable<Magazine> {
    /** Tidningens nummer. */
    private int issueNumber;

    /** Tidningens kategori. */
    private String category;

    /** Tidningens publiceringsår. */
    private int publishedYear;

    /**
     * Skapar ett nytt Magazine-objekt med angivna värden.
     * 
     * @param id            tidningens id
     * @param title         tidningens titel
     * @param isAvailable   anger om tidningen är tillgänglig för lån
     * @param issueNumber   tidningens nummer
     * @param category      tidningens kategori
     * @param publishedYear tidningens publiceringsår
     * @throws IllegalArgumentException om nummer eller publiceringsår är mindre än
     *                                  eller lika med 0, eller om kategorin är tom
     */
    public Magazine(String id, String title, boolean isAvailable, int issueNumber, String category, int publishedYear) {
        super(id, title, isAvailable);
        if (issueNumber <= 0) {
            throw new IllegalArgumentException("Nummer måste vara större än 0.");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Kategori får inte vara tom.");
        }
        if (publishedYear <= 0) {
            throw new IllegalArgumentException("Publiceringsår måste vara större än 0.");
        }
        this.issueNumber = issueNumber;
        this.category = category;
        this.publishedYear = publishedYear;
    }

    /**
     * Hämtar tidningens nummer.
     * 
     * @return tidningens nummer
     */
    public int getIssueNumber() {
        return issueNumber;
    }

    /**
     * Hämtar tidningens kategori.
     * 
     * @return tidningens kategori
     */
    public String getCategory() {
        return category;
    }

    /**
     * Hämtar tidningens publiceringsår.
     * 
     * @return tidningens publiceringsår
     */
    public int getPublishedYear() {
        return publishedYear;
    }

    /**
     * Anger ett nytt nummer för tidningen.
     * 
     * @param issueNumber tidningens nya nummer
     * @throws IllegalArgumentException om numret är mindre än eller lika med 0
     */
    public void setIssueNumber(int issueNumber) {
        if (issueNumber <= 0) {
            throw new IllegalArgumentException("Nummer måste vara större än 0.");
        }
        this.issueNumber = issueNumber;
    }

    /**
     * Anger en ny kategori för tidningen.
     * 
     * @param category tidningens nya kategori
     * @throws IllegalArgumentException om kategorin är tom
     */
    public void setCategory(String category) {
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Kategori får inte vara tom.");
        }
        this.category = category;
    }

    /**
     * Anger ett nytt publiceringår för tidningen.
     * 
     * @param publishedYear tidningens nya publiceringsår
     * @throws IllegalArgumentException om publiceringsåret är mindre än eller lika
     *                                  med 0
     */
    public void setPublishedYear(int publishedYear) {
        if (publishedYear <= 0) {
            throw new IllegalArgumentException("Publiceringsår måste vara större än 0.");
        }
        this.publishedYear = publishedYear;
    }

    /**
     * Jämför denna tidning med en annan tidning utifrån titel i alfabetisk ordning.
     * 
     * @param other den andra tidningen som jämförelsen görs med
     * @return ett negativt tal, 0 eller ett positivt tal beroende på
     *         sorteringsordningen
     */
    @Override
    public int compareTo(Magazine other) {
        return this.title.compareToIgnoreCase(other.title);
    }

    /**
     * Returnerar en textbeskrivning av tidningen.
     * 
     * @return information om tidningens id, titel, nummer, kategori,
     *         publiceringsår och tillgänglighet
     */
    @Override
    public String getInfo() {
        return """
                --- Tidning ---
                ID: %s
                Titel: %s
                Nummer: %d
                Kategori: %s
                Publiceringsår: %d
                Tillgänglig: %b
                """.formatted(id, title, issueNumber, category, publishedYear, isAvailable);
    }

}
