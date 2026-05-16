package jk;

/**
 * Författare Johnny Battah
 * Klassen LibraryItem är basklassen för saker som finns i biblioteket
 */

public class LibraryItem {
    protected String id;
    protected String title;
    protected boolean isAvailable;

    public LibraryItem(String id, String title, boolean isAvailable) {
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
        this.title = title;
    }

    public String getInfo() {
        return "ID: " + id + ", Titel: " + title + ", Tillgänglig: " + isAvailable;
    }
}
