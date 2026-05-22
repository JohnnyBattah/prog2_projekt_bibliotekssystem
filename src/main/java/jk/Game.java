package jk;

public class Game extends Media {
    private String genre;
    private int age;

    public Game(String id, String title, boolean isAvailable, String genre, int age) {
        super(id, "game", title, isAvailable);
        this.genre = genre;
        this.age = age;
    }

    public String getGenre() {
        return genre;
    }

    public int getAge() {
        return age;
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
