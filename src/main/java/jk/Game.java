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
        return "ID: " + id + ", Typ: " + type + ", Titel: " + title + ", Genre: " + genre + ", Ålder: " + age
                + ", Tillgänglig: " + isAvailable;
    }

}
