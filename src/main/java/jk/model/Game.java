package jk.model;

public class Game extends Media {
    private String genre;
    private int age;

    public Game(String id, String title, boolean isAvailable, String genre, int age) {
        super(id, "game", title, isAvailable);
        if (genre == null || genre.isBlank()) { throw new IllegalArgumentException("Genre får inte vara tom.");}
        if (age < 0) { throw new IllegalArgumentException("Ålder får inte vara negativ.");}

        this.genre = genre;
        this.age = age;
    }

    public String getGenre() {
        return genre;
    }

    public int getAge() {
        return age;
    }

    public void setGenre(String genre) {
        if (genre == null || genre.isBlank()) { throw new IllegalArgumentException("Genre får inte vara tom.");}
        this.genre = genre;
    }

    public void setAge(int age) {
        if (age < 0) { throw new IllegalArgumentException("Ålder får inte vara negativ.");}
        this.age = age;
    }

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
