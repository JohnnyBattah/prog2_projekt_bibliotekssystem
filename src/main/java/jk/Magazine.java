package jk;

/**
 * Författare: Johnny Battah
 * Klassen Magazine representerar en tidning i bibliotekssystemet.
 * Den ärver från LibraryItem och innehåller därför gemensamma egenskaper
 * som id, titel och tillgänglighet, men har också egna egenskaper som
 * nummer, kategori och publiceringsår.
 * Klassen implementerar Comparable<Magazine> så att tidningar kan sorteras
 * alfabetiskt efter titel vid utskrift i programmet.
 */

public class Magazine extends LibraryItem implements Comparable<Magazine> {
    private int issueNumber;
    private String category;
    private int publishedYear;

    public Magazine(String id, String title, boolean isAvailable, int issueNumber, String category, int publishedYear) {
        super(id, title, isAvailable);
        if (issueNumber <= 0) { throw new IllegalArgumentException("Nummer måste vara större än 0.");}
        if (category == null || category.isBlank()) { throw new IllegalArgumentException("Ketegori får inte vara tomt.");}
        if (publishedYear <= 0) { throw new IllegalArgumentException("Publiceringsår måste vara större än 0.");}
        this.issueNumber = issueNumber;
        this.category = category;
        this.publishedYear = publishedYear;
    }

    public int getIssueNumber() {
        return issueNumber;
    }

    public String getCategory() {
        return category;
    }

    public int getPublishedYear() {
        return publishedYear;
    }

    @Override
    public int compareTo(Magazine other) {
        return this.title.compareToIgnoreCase(other.title);
    }

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
