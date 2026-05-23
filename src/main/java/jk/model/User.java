package jk.model;

/**
 * Klassen User representerar en användare i bibliotekssystemet.
 * Den innehåller information om en kund i form av id, namn och e-postadress.
 * User-objekt används bland annat när programmet hämtar, skapar, söker
 * och tar bort användare från servern.
 * Klassen implementerar Comparable<User> så att användare kan sorteras
 * alfabetiskt efter namn vid utskrift i programmet.
 * Klassen används av LibraryManager vid hämtnings, skapande, sökning
 * och borttagning av användare.
 * 
 * @author Johnny Battah
 * @version 1.0
 * @since 2026
 */

public class User implements Comparable<User> {
    /** Användarens id. */
    private String id;

    /** Användarens namn. */
    private String name;

    /** Användarens e-postadress. */
    private String email;

    /**
     * Skapar ett nytt User-objekt med angivna värden.
     * 
     * @param id användarens id
     * @param name användarens namn
     * @param email användare e-postadress
     * @throws IllegalArgumentException om namn eller e-postadress är tomma
     */
    public User(String id, String name, String email) {
        if (id != null && id.isBlank()) { throw new IllegalArgumentException("Id får inte vara tomt.");}
        if (name == null || name.isBlank()) { throw new IllegalArgumentException("Namn får inte vara tomt.");}
        if (email == null || email.isBlank()) { throw new IllegalArgumentException("E-post får inte vara tom.");}

        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * Hämtar användarens id.
     * 
     * @return användarens id
     */
    public String getId() {
        return id;
    }

    /**
     * Hämtar användarens namn.
     * 
     * @return användarens namn
     */
    public String getName() {
        return name;
    }

    /**
     * Hämtar användarens e-postadress.
     * 
     * @return användarens e-postadress
     */
    public String getEmail() {
        return email;
    }

    /**
     * Anger ett nytt namn för användaren.
     * 
     * @param name använadrens nya namn
     * @throws IllegalArgumentException om namnet är tomt
     */
    public void setName(String name) {
        if (name == null || name.isBlank()) { throw new IllegalArgumentException("Namn får inte vara tomt.");}
        this.name = name;
    }

    /**
     * Anger en ny e-postadress för användaren.
     * 
     * @param name använadrens nya e-postadress
     * @throws IllegalArgumentException om e-postadressen är tom
     */
    public void setEmail(String email) {
        if (email == null || email.isBlank()) { throw new IllegalArgumentException("E-post får inte vara tom.");}
        this.email = email;
    }

    /**
     * Retunerar en textbeskrivning av användaren.
     * 
     * @return information om användarens id, namn och e-postadress
     */
    public String getInfo() {
        return """
                --- Användare ---
                ID: %s
                Namn: %s
                E-post: %s
                """.formatted(id, name, email);
    }

    /**
     * Jämför denna användare med en annan användare utifrån namn i alfabetisk ordning.
     * 
     * @param other den andra användaren som jämförelsen görs med
     * @return ett negativt tal, 0 eller ett positivt tal beroende på sorteringsordningen
     */
    @Override
    public int compareTo(User other) {
        return this.name.compareToIgnoreCase(other.name);
    }

}
