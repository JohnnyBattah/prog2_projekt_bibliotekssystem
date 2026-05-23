package jk.model;

import jk.interfaces.Searchable;

/**
 * Klassen LibraryItem är en abstrakt basklass för bibliotekets tryckta objekt.
 * Den innehåller gemensamma egenskaper som id, titel och tillgänglighet.
 * Klassen används som superklass för Book och Magazine.
 *
 * @author Johnny Battah
 * @version 1.0
 * @since 2026
 */

public abstract class LibraryItem implements Searchable {
    /** Objektets id. */
    protected String id;

    /** Objektets titel. */
    protected String title;

    /** Anger om objektet är tillgängligt för lån. */
    protected boolean isAvailable;

    /**
     * Skapar ett nytt LibraryItem-objekt med gemensamma värden.
     *
     * @param id          objektets id
     * @param title       objektets titel
     * @param isAvailable anger om objektet är tillgängligt för lån
     * @throws IllegalArgumentException om titeln är tom
     */
    public LibraryItem(String id, String title, boolean isAvailable) {
        if (id != null && id.isBlank()) {
            throw new IllegalArgumentException("Id får inte vara tomt.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Titel får inte vara tom.");
        }

        this.id = id;
        this.title = title;
        this.isAvailable = isAvailable;
    }

    /**
     * Hämtar objektets id.
     *
     * @return objektets id
     */
    public String getId() {
        return id;
    }

    /**
     * Hämtar objektets titel.
     *
     * @return objektets titel
     */
    public String getTitle() {
        return title;
    }

    /**
     * Hämtar om objektet är tillgängligt för lån.
     *
     * @return true om objektet är tillgängligt, annars false
     */
    public boolean getIsAvailable() {
        return isAvailable;
    }

    /**
     * Anger om objektet är tillgängligt för lån eller inte.
     *
     * @param isAvailable den nya tillgängligheten
     */
    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    /**
     * Anger en ny titel för objektet.
     *
     * @param title objektets nya titel
     * @throws IllegalArgumentException om titeln är tom
     */
    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Titel får inte vara tom.");
        }
        this.title = title;
    }

    /**
     * Returnerar en textbeskrivning av objektet.
     *
     * @return information om objektet
     */
    public abstract String getInfo();

    /**
     * Kontrollerar om objektets titel matchar en given titel.
     *
     * @param title titeln som ska jämföras med
     * @return true om titlarna matchar, annars false
     */
    @Override
    public boolean matchesTitle(String title) {
        return this.title.equalsIgnoreCase(title);
    }
}
