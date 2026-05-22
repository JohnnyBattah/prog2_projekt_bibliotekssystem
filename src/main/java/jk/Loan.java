package jk;

public class Loan {
    private String userId;
    private String itemId;
    private String itemType;

    public Loan(String userId, String itemId, String itemType) {
        this.userId = userId;
        this.itemId = itemId;
        this.itemType = itemType;
    }

    public String getUserId() {
        return userId;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getInfo() {
        return """
                --- Lån ---
                User ID: %s
                Item ID: %s
                Item Type: %s
                """.formatted(userId, itemId, itemType);
    }
}
