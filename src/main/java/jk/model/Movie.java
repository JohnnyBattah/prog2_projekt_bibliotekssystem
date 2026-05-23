package jk.model;

/**
 * Klassen Movie representerar en film i bibliotekssystemets media-arvshierarki.
 * Den ärver från Media och innehåller gemensamma egenskaper som id, typ,
 * titel och tillgänglighet, men har också egna egenskaper som genre och
 * speltid.
 * Klassen används av LibraryManager för att hantera filmer som mediaobjekt.
 *
 * @author Johnny Battah
 * @version 1.0
 * @since 2026
 */
public class Movie extends Media {
    /** Filmens genre. */
    private String genre;

    /** Filmens längd i minuter. */
    private int minutes;

    /**
     * Skapar ett nytt Movie-objekt med angivna värden.
     *
     * @param id          filmens id
     * @param title       filmens titel
     * @param isAvailable anger om filmen är tillgänglig för lån
     * @param genre       filmens genre
     * @param minutes     filmens längd i minuter
     * @throws IllegalArgumentException om genren är tom eller om antalet minuter
     *                                  är mindre än eller lika med 0
     */
    public Movie(String id, String title, boolean isAvailable, String genre, int minutes) {
        super(id, "movie", title, isAvailable);
        if (genre == null || genre.isBlank()) {
            throw new IllegalArgumentException("Genre får inte vara tom.");
        }
        if (minutes <= 0) {
            throw new IllegalArgumentException("Antal minuter måste vara större än 0.");
        }

        this.genre = genre;
        this.minutes = minutes;
    }

    /**
     * Hämtar filmens genre.
     *
     * @return filmens genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Hämtar filmens längd i minuter.
     *
     * @return filmens längd i minuter
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Anger en ny genre för filmen.
     *
     * @param genre filmens nya genre
     * @throws IllegalArgumentException om genren är tom
     */
    public void setGenre(String genre) {
        if (genre == null || genre.isBlank()) {
            throw new IllegalArgumentException("Genre får inte vara tom.");
        }
        this.genre = genre;
    }

    /**
     * Anger en ny längd i minuter för filmen.
     *
     * @param minutes filmens nya längd i minuter
     * @throws IllegalArgumentException om antalet minuter är mindre än eller lika
     *                                  med 0
     */
    public void setMinutes(int minutes) {
        if (minutes <= 0) {
            throw new IllegalArgumentException("Antal minuter måste vara större än 0.");
        }
        this.minutes = minutes;
    }

    /**
     * Returnerar en textbeskrivning av filmen.
     *
     * @return information om filmens id, typ, titel, genre, längd och
     *         tillgänglighet
     */
    @Override
    public String getInfo() {
        return """
                --- Film ---
                ID: %s
                Typ: %s
                Titel: %s
                Genre: %s
                Minuter: %d
                Tillgänglig: %b
                """.formatted(id, type, title, genre, minutes, isAvailable);
    }
}
