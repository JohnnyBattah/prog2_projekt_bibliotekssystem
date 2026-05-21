package jk;

/**
 * Författare: Johnny Battah
 * Klassen SuspendedUser representerar en avstängd användare i bibliotekssystemet.
 * Den innehåller ett eget id för avstängningsposten samt customer_id,
 * som kopplar avstängningen till en användare på servern.
 * Klassen används för att avgöra om en användare får låna eller inte,
 * samt för att kunna lägga till, hämta, skriva ut och ta bort avstängningar.
 * Klassen implementerar Comparable<SuspendedUser> så att avstängda användare 
 * kan sorteras alfabetiskt efter id vid utskrift i programmet.
 */

public class SuspendedUser implements Comparable<SuspendedUser>{
    private String id;
    private String customer_id;

    public SuspendedUser(String id, String customer_id) {
        this.id = id;
        this.customer_id = customer_id;
    }

    public String getId() {
        return id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public String getInfo() {
        return "ID: " + id + ", Customer ID: " + customer_id;
    }

    @Override
    public int compareTo(SuspendedUser other){
        return this.id.compareToIgnoreCase(other.id);
    }
}
