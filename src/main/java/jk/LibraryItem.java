package jk;

/**
 * Författare: Johnny Battah
 * Klassen LibraryItem är en basklass för bibliotekets medier.
 * Den innehåller gemensam information för objekt som kan finnas i biblioteket,
 * till exempel id, titel och om objektet är tillgängligt eller inte.
 * Klassen används som superklass för Book och Magazine för att undvika
 * duplicerad kod och samla gemensamma egenskaper på ett ställe.
 */

public abstract class LibraryItem implements Searchable {
    protected String id;
    protected String title;
    protected boolean isAvailable;

    public LibraryItem(String id, String title, boolean isAvailable) {
        if (id != null && id.isBlank()) { throw new IllegalArgumentException("Id får inte vara tomt.");}
        if (title == null || title.isBlank()) { throw new IllegalArgumentException("Titel får inte vara tom.");}

        this.id = id;
        this.title = title;
        this.isAvailable = isAvailable;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void setTitle(String title) {
        if (title == null || title.isBlank()) { throw new IllegalArgumentException("Titel får inte vara tom.");}
        this.title = title;
    }

    public abstract String getInfo();

    @Override
    public boolean matchesTitle(String title) {
        return this.title.equalsIgnoreCase(title);
    }
}
