package jk.model;

import jk.interfaces.Searchable;

/**
 * Klassen Media är en abstrakt basklass för mediaobjekt i bibliotekssystemet.
 * Den innehåller gemensamma egenskaper som id, typ, titel och tillgänglighet.
 * Klassen används som superklass för Game, Movie och MusicAlbum.
 *
 * @author Johnny Battah
 * @version 1.0
 * @since 2026
 */
public abstract class Media implements Searchable {
    /** Mediaobjektets id. */
    protected String id;

    /** Mediaobjektets typ, till exempel game, movie eller music_album. */
    protected String type;

    /** Mediaobjektets titel. */
    protected String title;

    /** Anger om mediaobjektet är tillgängligt för lån. */
    protected boolean isAvailable;

    /**
     * Skapar ett nytt Media-objekt med gemensamma värden.
     *
     * @param id          mediaobjektets id
     * @param type        mediaobjektets typ
     * @param title       mediaobjektets titel
     * @param isAvailable anger om mediaobjektet är tillgängligt för lån
     * @throws IllegalArgumentException om type eller title är tomma
     */
    public Media(String id, String type, String title, boolean isAvailable) {
        if (id != null && id.isBlank()) {
            throw new IllegalArgumentException("Id får inte vara tomt.");
        }
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Typ får inte vara tom.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Titel får inte vara tom.");
        }

        this.id = id;
        this.type = type;
        this.title = title;
        this.isAvailable = isAvailable;
    }

    /**
     * Hämtar mediaobjektets id.
     *
     * @return mediaobjektets id
     */
    public String getId() {
        return id;
    }

    /**
     * Hämtar mediaobjektets typ.
     *
     * @return mediaobjektets typ
     */
    public String getType() {
        return type;
    }

    /**
     * Hämtar mediaobjektets titel.
     *
     * @return mediaobjektets titel
     */
    public String getTitle() {
        return title;
    }

    /**
     * Hämtar om mediaobjektet är tillgängligt för lån.
     *
     * @return true om objektet är tillgängligt, annars false
     */
    public boolean getIsAvailable() {
        return isAvailable;
    }

    /**
     * Anger en ny titel för mediaobjektet.
     *
     * @param title mediaobjektets nya titel
     * @throws IllegalArgumentException om titeln är tom
     */
    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Titel får inte vara tom.");
        }
        this.title = title;
    }

    /**
     * Anger om mediaobjektet är tillgängligt för lån eller inte.
     *
     * @param isAvailable den nya tillgängligheten
     */
    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    /**
     * Returnerar en textbeskrivning av mediaobjektet.
     *
     * @return information om mediaobjektet
     */
    public abstract String getInfo();

    /**
     * Kontrollerar om mediaobjektets titel matchar en given titel.
     *
     * @param title titeln som ska jämföras med
     * @return true om titlarna matchar, annars false
     */
    @Override
    public boolean matchesTitle(String title) {
        return this.title.equalsIgnoreCase(title);
    }
}
