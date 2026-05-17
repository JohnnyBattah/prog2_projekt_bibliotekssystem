package jk;

/**
 * Författare: Johnny Battah
 * Klassen SuspendedUser representerar en avstängd användare i
 * bibliotekssystemet.
 * Den används för att avgöra om en kund får låna eller inte
 */

public class SuspendedUser{
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
}
