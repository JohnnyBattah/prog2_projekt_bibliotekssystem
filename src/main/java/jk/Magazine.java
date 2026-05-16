package jk;

/**
 * Författare: Johnny Battah
 * Klassen Magazine representerar en tidning i bibliotekssystemet
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
        return "ID: " + id + ", Titel: " + title + ", Nummer: " + issueNumber + ", Kategori: " + category
                + ", Publiceringsår: " + publishedYear + ", Tillgänglig: " + isAvailable;
    }

}
