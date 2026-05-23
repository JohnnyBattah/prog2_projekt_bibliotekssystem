package jk.model;

/**
 * Klassen Loan representerar ett lån i bibliotekssystemet.
 * Den registrerar vilken användare som har lånat vilket objekt
 * och vilken typ av objekt lånet gäller.
 * Klassen används av LibraryManager för att hantera utlåning,
 * återlämning och filhantering av lån.
 *
 * @author Johnny Battah
 * @version 1.0
 * @since 2026
 */
public class Loan {
    /** Id för användaren som har lånat objektet. */
    private String userId;

    /** Id för objektet som är utlånat. */
    private String itemId;

    /** Typ av objekt som är utlånat, till exempel book, magazine eller media. */
    private String itemType;

    /**
     * Skapar ett nytt Loan-objekt med angivna värden.
     *
     * @param userId   id för användaren som lånar
     * @param itemId   id för objektet som lånas
     * @param itemType typen av objekt som lånas
     * @throws IllegalArgumentException om userId, itemId eller itemType är tomma
     */
    public Loan(String userId, String itemId, String itemType) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User id får inte vara tomt.");
        }
        if (itemId == null || itemId.isBlank()) {
            throw new IllegalArgumentException("Item id får inte vara tomt.");
        }
        if (itemType == null || itemType.isBlank()) {
            throw new IllegalArgumentException("Item type får inte vara tomt.");
        }

        this.userId = userId;
        this.itemId = itemId;
        this.itemType = itemType;
    }

    /**
     * Hämtar id för användaren som har lånat objektet.
     *
     * @return användarens id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Hämtar id för objektet som är utlånat.
     *
     * @return objektets id
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * Hämtar typen av objekt som är utlånat.
     *
     * @return objektets typ
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * Anger ett nytt användar-id för lånet.
     *
     * @param userId id för användaren som lånar
     * @throws IllegalArgumentException om användar-id är tomt
     */
    public void setUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User id får inte vara tomt.");
        }
        this.userId = userId;
    }

    /**
     * Anger ett nytt objekt-id för lånet.
     *
     * @param itemId id för objektet som lånas
     * @throws IllegalArgumentException om objekt-id är tomt
     */
    public void setItemId(String itemId) {
        if (itemId == null || itemId.isBlank()) {
            throw new IllegalArgumentException("Item id får inte vara tomt.");
        }
        this.itemId = itemId;
    }

    /**
     * Anger en ny objekttyp för lånet.
     *
     * @param itemType typen av objekt som lånas
     * @throws IllegalArgumentException om objekttypen är tom
     */
    public void setItemType(String itemType) {
        if (itemType == null || itemType.isBlank()) {
            throw new IllegalArgumentException("Item type får inte vara tomt.");
        }
        this.itemType = itemType;
    }

    /**
     * Returnerar en textbeskrivning av lånet.
     *
     * @return information om användar-id, objekt-id och objekttyp
     */
    public String getInfo() {
        return """
                --- Lån ---
                User ID: %s
                Item ID: %s
                Item Type: %s
                """.formatted(userId, itemId, itemType);
    }
}
