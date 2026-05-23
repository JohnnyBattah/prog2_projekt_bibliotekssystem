package jk.model;

/**
 * Författare: Johnny Battah
 * Klassen Book representerar en bok i bibliotekssystemet.
 * Den ärver från LibraryItem och innehåller därför gemensamma egenskaper
 * som id, titel och tillgänglighet, men har också egna egenskaper som
 * författare, genre och antal sidor.
 * Klassen implementerar Comparable<Book> så att böcker kan sorteras
 * alfabetiskt efter titel vid utskrift i programmet.
 */

public class Book extends LibraryItem implements Comparable<Book> {
    private String author;
    private String genre;
    private int pages;

    /**
     * Skapar ett nytt Book-objekt med angivna värden
     * 
     * @param id bokens id
     * @param title bokens titel
     * @param isAvailable anger om boken är tillgänglig för lån.
     * @param author bokens författare
     * @param genre bokens genre
     * @param pages antal sidor i boken
     * @throws IllegalArgumentEXception om title, author eller genre är tomma
     *         eller om pages är mindre än eller lika med 0
     */
    public Book(String id, String title, boolean isAvailable, String author, String genre, int pages) {
        super(id, title, isAvailable);
        if (author == null || author.isBlank()) { throw new IllegalArgumentException("Författare får inte vara tom.");}
        if (genre == null || genre.isBlank()) { throw new IllegalArgumentException("Genre får inte vara tom.");}
        if (pages <= 0) { throw new IllegalArgumentException("Antal sidor måste vara större än 0.");}
        
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

    public void setAuthor(String author) {
        if (author == null || author.isBlank()) { throw new IllegalArgumentException("Författare får inte vara tom.");}
        this.author = author;
    }

    public void setGenre(String genre) {
        if (genre == null || genre.isBlank()) { throw new IllegalArgumentException("Genre får inte vara tom.");}
        this.genre = genre;
    }

    public void setPages(int pages) {
        if (pages <= 0) { throw new IllegalArgumentException("Antal sidor måste vara större än 0.");}
        this.pages = pages;
    }

    @Override
    public int compareTo(Book other) {
        return this.title.compareToIgnoreCase(other.title);
    }

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
