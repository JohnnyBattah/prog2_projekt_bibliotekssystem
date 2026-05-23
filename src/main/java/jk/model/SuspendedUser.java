package jk.model;

/**
 * Författare: Johnny Battah
 * Klassen SuspendedUser representerar en avstängd användare i
 * bibliotekssystemet.
 * Den innehåller ett eget id för avstängningsposten samt customer_id,
 * som kopplar avstängningen till en användare på servern.
 * Klassen används för att avgöra om en användare får låna eller inte,
 * samt för att kunna lägga till, hämta, skriva ut och ta bort avstängningar.
 * Klassen implementerar Comparable<SuspendedUser> så att avstängda användare
 * kan sorteras alfabetiskt efter id vid utskrift i programmet.
 */

public class SuspendedUser implements Comparable<SuspendedUser> {
    private String id;
    private String customer_id;

    public SuspendedUser(String id, String customer_id) {
        if (id != null && id.isBlank()) { throw new IllegalArgumentException("Id får inte vara tomt.");}
        if (customer_id == null || customer_id.isBlank()) { throw new IllegalArgumentException("Användar id får inte vara tomt.");}

        this.id = id;
        this.customer_id = customer_id;
    }

    public String getId() {
        return id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        if (customer_id == null || customer_id.isBlank()) { throw new IllegalArgumentException("Användar id får inte vara tomt.");}
        this.customer_id = customer_id;
    }

    public String getInfo() {
        return """
                --- Avstängd användare ---
                ID: %s
                Customer ID: %s
                """.formatted(id, customer_id);
    }

    @Override
    public int compareTo(SuspendedUser other) {
        return this.id.compareToIgnoreCase(other.id);
    }
}
