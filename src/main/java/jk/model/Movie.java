package jk.model;

public class Movie extends Media {
    private String genre;
    private int minutes;

    public Movie(String id, String title, boolean isAvailable, String genre, int minutes) {
        super(id, "movie", title, isAvailable);
        if (genre == null || genre.isBlank()) { throw new IllegalArgumentException("Genre får inte vara tom.");}
        if (minutes <= 0) { throw new IllegalArgumentException("Antal minuter måste vara större än 0.");}

        this.genre = genre;
        this.minutes = minutes;
    }

    public String getGenre() {
        return genre;
    }

    public int getMinutes() {
        return minutes;
    }

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
