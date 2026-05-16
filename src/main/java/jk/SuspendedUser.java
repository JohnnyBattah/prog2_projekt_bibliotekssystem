package jk;

/**
 * Författare: Johnny Battah
 * Klassen SuspendedUser representerar en avstängd användare i
 * bibliotekssystemet.
 * Den används för att avgöra om en kund får låna eller inte
 */

public class SuspendedUser {
    private String id;
    private String userId;
    private String reason;

    public SuspendedUser(String id, String userId, String reason) {
        this.id = id;
        this.userId = userId;
        this.reason = reason;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getInfo() {
        return "ID: " + id + ", UserID: " + userId + ", Orsak: " + reason;
    }
}
