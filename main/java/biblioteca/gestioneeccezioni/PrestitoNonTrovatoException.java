
package biblioteca.gestioneeccezioni;

/**
 * @brief Eccezione sollevata quando un prestito richiesto non è presente nell'archivio.
 * 
 * Questa eccezione estende {@link BibliotecaException} e viene utilizzata
 * per segnalare che il prestito richiesto non è stato trovato.
 * 
 * Offre due costruttori:
 * - senza messaggio di dettaglio
 * - con messaggio di dettaglio personalizzato
 */
public class PrestitoNonTrovatoException extends BibliotecaException{
    /**
     * @brief Costruttore di default.
     * 
     * Crea una nuova istanza di {@code PrestitoNonTrovatoException} senza messaggio di dettaglio.
     * 
     * @post l'eccezione viene inizializzata senza messaggio
     */
    public PrestitoNonTrovatoException() {
    }
    /**
     * @brief Costruttore con messaggio di dettaglio.
     * 
     * Crea una nuova istanza di {@code PrestitoNonTrovatoException} con il messaggio di dettaglio specificato.
     * 
     * @param msg Il messaggio di dettaglio associato all'eccezione
     * @post l'eccezione viene inizializzata con il messaggio fornito
     */
    public PrestitoNonTrovatoException(String msg) {
        super(msg);
    }
}
