package jk;

public class Movie extends Media {
    private String genre;
    private int minutes;

    public Movie(String id, String title, boolean isAvailable, String genre, int minutes) {
        super(id, "movie", title, isAvailable);
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
        return "ID: " + id + ", Typ: " + type + ", Titel: " + title + ", Genre: " + genre + ", Minuter: " + minutes
                + ", Tillgänglig: " + isAvailable;
    }
}
