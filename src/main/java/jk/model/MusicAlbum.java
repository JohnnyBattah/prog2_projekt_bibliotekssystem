package jk.model;

/**
 * Klassen MusicAlbum representerar ett musikalbum i bibliotekssystemets
 * media-arvshierarki.
 * Den ärver från Media och innehåller gemensamma egenskaper som id, typ,
 * titel och tillgänglighet, men har också en egen egenskap för artist.
 * Klassen används av LibraryManager för att hantera musikalbum som mediaobjekt.
 *
 * @author Johnny Battah
 * @version 1.0
 * @since 2026
 */
public class MusicAlbum extends Media {
    /** Namnet på artisten för musikalbumet. */
    private String artist;

    /**
     * Skapar ett nytt MusicAlbum-objekt med angivna värden.
     *
     * @param id          musikalbumets id
     * @param title       musikalbumets titel
     * @param isAvailable anger om musikalbumet är tillgängligt för lån
     * @param artist      namnet på artisten
     * @throws IllegalArgumentException om artist är tom
     */
    public MusicAlbum(String id, String title, boolean isAvailable, String artist) {
        super(id, "music_album", title, isAvailable);
        if (artist == null || artist.isBlank()) {
            throw new IllegalArgumentException("Artist får inte vara tom.");
        }

        this.artist = artist;
    }

    /**
     * Hämtar namnet på artisten för musikalbumet.
     *
     * @return artistens namn
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Anger en ny artist för musikalbumet.
     *
     * @param artist artistens nya namn
     * @throws IllegalArgumentException om artist är tom
     */
    public void setArtist(String artist) {
        if (artist == null || artist.isBlank()) {
            throw new IllegalArgumentException("Artist får inte vara tom.");
        }
        this.artist = artist;
    }

    /**
     * Returnerar en textbeskrivning av musikalbumet.
     *
     * @return information om musikalbumets id, typ, titel, artist och
     *         tillgänglighet
     */
    @Override
    public String getInfo() {
        return """
                --- Musikalbum ---
                ID: %s
                Typ: %s
                Titel: %s
                Artist: %s
                Tillgänglig: %b
                """.formatted(id, type, title, artist, isAvailable);
    }
}
