
package biblioteca.gestioneeccezioni;

/**
 * @brief Classe base astratta per tutte le eccezioni della biblioteca.
 * 
 * Questa classe estende {@link Exception} e serve come superclasse
 * per tutte le eccezioni personalizzate relative alla gestione della biblioteca.
 * 
 * Offre due costruttori:
 * - senza messaggio di dettaglio
 * - con messaggio di dettaglio
 * 
 * @author Utente
 */
public abstract class BibliotecaException extends Exception{

    /**
     * @brief Costruttore di default.
     * 
     * Crea una nuova istanza di {@code BibliotecaException} senza messaggio di dettaglio.
     * 
     * @post l'eccezione viene inizializzata senza messaggio
     */
    public BibliotecaException() {
    }

   /**
     * @brief Costruttore con messaggio di dettaglio.
     * 
     * Crea una nuova istanza di {@code BibliotecaException} con il messaggio di dettaglio specificato.
     * 
     * @param msg Il messaggio di dettaglio associato all'eccezione
     * @post l'eccezione viene inizializzata con il messaggio fornito
     */
    public BibliotecaException(String msg) {
        super(msg);
    }
}
