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
