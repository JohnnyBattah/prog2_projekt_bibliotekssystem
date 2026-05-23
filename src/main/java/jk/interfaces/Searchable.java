package jk.interfaces;

/**
 * Interfacet Searchable används för objekt som kan jämföras mot en titel.
 * Det används i bibliotekssystemet för att kunna kontrollera om en titel
 * matchar ett visst objekt.
 *
 * @author Johnny Battah
 * @version 1.0
 * @since 2026
 */
public interface Searchable {
    
    /**
     * Kontrollerar om objektets titel matchar en given titel.
     *
     * @param title titeln som ska jämföras med
     * @return true om titlarna matchar, annars false
     */
    boolean matchesTitle(String title);
}
