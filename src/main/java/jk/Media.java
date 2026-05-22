package jk;

public abstract class Media {
    protected String id;
    protected String type;
    protected String title;
    protected boolean isAvailable;

    public Media(String id, String type, String title, boolean isAvailable) {
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

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public abstract String getInfo();
}
