
package biblioteca.gestioneeccezioni;

/**
 * @brief Eccezione sollevata quando un utente richiesto non è presente nell'archivio.
 * 
 * Questa eccezione estende {@link BibliotecaException} e viene utilizzata
 * per segnalare che l'utente richiesto non è stato trovato nella biblioteca.
 * 
 * Offre due costruttori:
 * - senza messaggio di dettaglio
 * - con messaggio di dettaglio personalizzato
 */
public class UtenteNonTrovatoException extends BibliotecaException{
    /**
     * @brief Costruttore di default.
     * 
     * Crea una nuova istanza di {@code UtenteNonTrovatoException} senza messaggio di dettaglio.
     * 
     * @post l'eccezione viene inizializzata senza messaggio
     */
    public UtenteNonTrovatoException() {
    }
    /**
     * @brief Costruttore con messaggio di dettaglio.
     * 
     * Crea una nuova istanza di {@code UtenteNonTrovatoException} con il messaggio di dettaglio specificato.
     * 
     * @param msg Il messaggio di dettaglio associato all'eccezione
     * @post l'eccezione viene inizializzata con il messaggio fornito
     */
    public UtenteNonTrovatoException(String msg) {
        super(msg);
    }
}
