package jk.model;

/**
 * Författare: Johnny Battah
 * Klassen User representerar en användare i bibliotekssystemet.
 * Den innehåller information om en kund i form av id, namn och e-postadress.
 * User-objekt används bland annat när programmet hämtar, skapar, söker
 * och tar bort användare från servern.
 * Klassen implementerar Comparable<User> så att användare kan sorteras
 * alfabetiskt efter namn vid utskrift i programmet.
 */

public class User implements Comparable<User> {
    private String id;
    private String name;
    private String email;

    public User(String id, String name, String email) {
        if (id != null && id.isBlank()) { throw new IllegalArgumentException("Id får inte vara tomt.");}
        if (name == null || name.isBlank()) { throw new IllegalArgumentException("Namn får inte vara tomt.");}
        if (email == null || email.isBlank()) { throw new IllegalArgumentException("E-post får inte vara tom.");}

        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) { throw new IllegalArgumentException("Namn får inte vara tomt.");}
        this.name = name;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) { throw new IllegalArgumentException("E-post får inte vara tom.");}
        this.email = email;
    }

    public String getInfo() {
        return """
                --- Användare ---
                ID: %s
                Namn: %s
                E-post: %s
                """.formatted(id, name, email);
    }

    @Override
    public int compareTo(User other) {
        return this.name.compareToIgnoreCase(other.name);
    }

}
