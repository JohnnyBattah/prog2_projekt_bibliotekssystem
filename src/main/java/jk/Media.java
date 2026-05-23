package jk;

public abstract class Media implements Searchable {
    protected String id;
    protected String type;
    protected String title;
    protected boolean isAvailable;

    public Media(String id, String type, String title, boolean isAvailable) {
        if (id != null && id.isBlank()) { throw new IllegalArgumentException("Id får inte vara tomt.");}
        if (type == null || type.isBlank()) { throw new IllegalArgumentException("Typ får inte vara tom.");}
        if (title == null || title.isBlank()) { throw new IllegalArgumentException("Titel får inte vara tom.");}

        this.id = id;
        this.type = type;
        this.title = title;
        this.isAvailable = isAvailable;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setTitle(String title) {
        if (title == null || title.isBlank()) { throw new IllegalArgumentException("Titel får inte vara tom.");}
        this.title = title;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public abstract String getInfo();

    @Override
    public boolean matchesTitle(String title) {
        return this.title.equalsIgnoreCase(title);
    }
}
