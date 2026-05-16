package jk;

/**
 * Författare: Johnny Battah
 * Klassen Book representerar en bok i bibliotekssystemet
 */

public class Book extends LibraryItem implements Comparable<Book> {
    private String author;
    private String genre;
    private int pages;

    public Book(String id, String title, boolean isAvailable, String author, String genre, int pages) {
        super(id, title, isAvailable);
        this.author = author;
        this.genre = genre;
        this.pages = pages;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public int getPages() {
        return pages;
    }

    @Override
    public int compareTo(Book other) {
        return this.title.compareTo(other.title);
    }

    @Override
    public String getInfo() {
        return "ID: " + id + ", Titel: " + title + ", Författare: " + author + ", Genre: " + genre + ", Sidor: " + pages
                + ", Tillgänglig: " + isAvailable;
    }

}
