
package biblioteca.gestioneeccezioni;
/**
 * @brief Eccezione sollevata quando un libro non è presente nell'archivio.
 * 
 * Questa eccezione estende {@link BibliotecaException} e viene utilizzata
 * per segnalare che il libro richiesto non è stato trovato in biblioteca.
 * 
 * Offre due costruttori:
 * - senza messaggio di dettaglio
 * - con messaggio di dettaglio personalizzato
 */
public class LibroNonTrovatoException extends BibliotecaException{
    /**
     * @brief Costruttore di default.
     * 
     * Crea una nuova istanza di {@code LibroNonTrovatoException} senza messaggio di dettaglio.
     * 
     * @post l'eccezione viene inizializzata senza messaggio
     */
    public LibroNonTrovatoException() {
    }
    /**
     * @brief Costruttore con messaggio di dettaglio.
     * 
     * Crea una nuova istanza di {@code LibroNonTrovatoException} con il messaggio di dettaglio specificato.
     * 
     * @param msg Il messaggio di dettaglio associato all'eccezione
     * @post l'eccezione viene inizializzata con il messaggio fornito
     */
    public LibroNonTrovatoException(String msg) {
        super(msg);
    }
}
