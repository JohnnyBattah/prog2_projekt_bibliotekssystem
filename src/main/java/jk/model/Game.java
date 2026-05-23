package jk.model;

/**
 * Klassen Game representerar ett spel i bibliotekssystemets media-arvshierarki.
 * Den ärver från Media och innehåller gemensamma egenskaper som id, typ,
 * titel och tillgänglighet, men har också egna egenskaper som genre och
 * åldersgräns.
 * Klassen används av LibraryManager för att hantera spel som mediaobjekt.
 *
 * @author Johnny Battah
 * @version 1.0
 * @since 2026
 */
public class Game extends Media {
    /** Spelets genre. */
    private String genre;

    /** Spelets rekommenderade ålder eller åldersgräns. */
    private int age;

    /**
     * Skapar ett nytt Game-objekt med angivna värden.
     *
     * @param id          spelets id
     * @param title       spelets titel
     * @param isAvailable anger om spelet är tillgängligt för lån
     * @param genre       spelets genre
     * @param age         spelets rekommenderade ålder eller åldersgräns
     * @throws IllegalArgumentException om genren är tom eller om åldern är negativ
     */
    public Game(String id, String title, boolean isAvailable, String genre, int age) {
        super(id, "game", title, isAvailable);
        if (genre == null || genre.isBlank()) {
            throw new IllegalArgumentException("Genre får inte vara tom.");
        }
        if (age < 0) {
            throw new IllegalArgumentException("Ålder får inte vara negativ.");
        }

        this.genre = genre;
        this.age = age;
    }

    /**
     * Hämtar spelets genre.
     *
     * @return spelets genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Hämtar spelets rekommenderade ålder eller åldersgräns.
     *
     * @return spelets ålder eller åldersgräns
     */
    public int getAge() {
        return age;
    }

    /**
     * Anger en ny genre för spelet.
     *
     * @param genre spelets nya genre
     * @throws IllegalArgumentException om genren är tom
     */
    public void setGenre(String genre) {
        if (genre == null || genre.isBlank()) {
            throw new IllegalArgumentException("Genre får inte vara tom.");
        }
        this.genre = genre;
    }

    /**
     * Anger en ny rekommenderad ålder eller åldersgräns för spelet.
     *
     * @param age spelets nya ålder eller åldersgräns
     * @throws IllegalArgumentException om åldern är negativ
     */
    public void setAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Ålder får inte vara negativ.");
        }
        this.age = age;
    }

    /**
     * Returnerar en textbeskrivning av spelet.
     *
     * @return information om spelets id, typ, titel, genre, ålder och
     *         tillgänglighet
     */
    @Override
    public String getInfo() {
        return """
                --- Spel ---
                ID: %s
                Typ: %s
                Titel: %s
                Genre: %s
                Ålder: %d
                Tillgänglig: %b
                """.formatted(id, type, title, genre, age, isAvailable);
    }

}
