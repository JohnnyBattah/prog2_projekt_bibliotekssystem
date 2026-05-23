package jk.model;

public class Loan {
    private String userId;
    private String itemId;
    private String itemType;

    public Loan(String userId, String itemId, String itemType) {
        if (userId == null || userId.isBlank()) { throw new IllegalArgumentException("User id får inte vara tomt.");}
        if (itemId == null || itemId.isBlank()) { throw new IllegalArgumentException("Item id får inte vara tomt.");}
        if (itemType == null || itemType.isBlank()) { throw new IllegalArgumentException("Item type får inte vara tomt.");}

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
        if (userId == null || userId.isBlank()) { throw new IllegalArgumentException("User id får inte vara tomt.");}
        this.userId = userId;
    }

    public void setItemId(String itemId) {
        if (itemId == null || itemId.isBlank()) { throw new IllegalArgumentException("Item id får inte vara tomt.");}
        this.itemId = itemId;
    }

    public void setItemType(String itemType) {
        if (itemType == null || itemType.isBlank()) { throw new IllegalArgumentException("Item type får inte vara tomt.");}
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
