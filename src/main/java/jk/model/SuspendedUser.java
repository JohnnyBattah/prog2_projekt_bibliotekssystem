package jk.model;

/**
 * Klassen SuspendedUser representerar en avstängd användare i
 * bibliotekssystemet.
 * Den innehåller ett eget id för avstängningsposten samt customer_id,
 * som kopplar avstängningen till en användare på servern.
 * Klassen används för att avgöra om en användare får låna eller inte,
 * samt för att kunna lägga till, hämta, skriva ut och ta bort avstängningar.
 * Klassen implementerar Comparable<SuspendedUser> så att avstängda användare
 * kan sorteras alfabetiskt efter id vid utskrift i programmet.
 * Klassen används av LibraryManager för att kontrollera vilka användare
 * som inte får låna.
 * 
 * @author Johnny Battah
 * @version 1.0
 * @since 2026
 */

public class SuspendedUser implements Comparable<SuspendedUser> {
    /** Id för avstängningsposten. */
    private String id;

    /** Id för användaren som är avstängd. */
    private String customer_id;

    /**
     * Skapar ett nytt SuspendedUser-objekt med angivna värden.
     * 
     * @param id          Id för avstängningsposten
     * @param customer_id id för användaren som är avstängd
     * @throws IllegalArgumentException om customer_id är tomt
     */
    public SuspendedUser(String id, String customer_id) {
        if (id != null && id.isBlank()) {
            throw new IllegalArgumentException("Id får inte vara tomt.");
        }
        if (customer_id == null || customer_id.isBlank()) {
            throw new IllegalArgumentException("Användar id får inte vara tomt.");
        }

        this.id = id;
        this.customer_id = customer_id;
    }

    /**
     * Hämtar id för avstängningsposten.
     * 
     * @return id för avstängningsposten
     */
    public String getId() {
        return id;
    }

    /**
     * Hämtar id för den användare som är avstängd.
     * 
     * @return id för den användare som är avstängd
     */
    public String getCustomer_id() {
        return customer_id;
    }

    /**
     * Anger ett nytt användar-id för avstängningen.
     * 
     * @param customer_id id för användaren som är avstängd
     * @throws IllegalArgumentException om användar-id är tomt
     */
    public void setCustomer_id(String customer_id) {
        if (customer_id == null || customer_id.isBlank()) {
            throw new IllegalArgumentException("Användar id får inte vara tomt.");
        }
        this.customer_id = customer_id;
    }

    /**
     * Returnerar en textbeskrivning av den avstängda användaren.
     * 
     * @return information om avstängningspostens id och användarens id
     */
    public String getInfo() {
        return """
                --- Avstängd användare ---
                ID: %s
                Customer ID: %s
                """.formatted(id, customer_id);
    }

    /**
     * Jämför denna avstängda användare med en annan utifrån id i alfabetisk ordning.
     * 
     * @param other den andra avstängda användaren som jämförelsen görs med
     * @return ett negativt tal, 0 eller ett positivt tal beroende på
     *         sorteringsordningen
     */
    @Override
    public int compareTo(SuspendedUser other) {
        return this.id.compareToIgnoreCase(other.id);
    }
}
