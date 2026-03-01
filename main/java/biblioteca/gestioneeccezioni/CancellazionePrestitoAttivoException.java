
package biblioteca.gestioneeccezioni;

/**
 * @brief Eccezione sollevata quando si tenta di cancellare un prestito che è ancora attivo.
 * 
 * Questa eccezione estende {@link BibliotecaException} e viene utilizzata
 * per segnalare che un prestito non può essere rimosso perché non è ancora stato restituito.
 * 
 * Offre due costruttori:
 * - senza messaggio di dettaglio
 * - con messaggio di dettaglio personalizzato
 */
public class CancellazionePrestitoAttivoException extends BibliotecaException{

    /**
     * @brief Costruttore di default.
     * 
     * Crea una nuova istanza di {@code CancellazionePrestitoAttivoException} senza messaggio di dettaglio.
     * 
     * @post l'eccezione viene inizializzata senza messaggio
     */
    public CancellazionePrestitoAttivoException() {
    }

    /**
     * @brief Costruttore con messaggio di dettaglio.
     * 
     * Crea una nuova istanza di {@code CancellazionePrestitoAttivoException} con il messaggio di dettaglio specificato.
     * 
     * @param msg Il messaggio di dettaglio associato all'eccezione
     * @post l'eccezione viene inizializzata con il messaggio fornito
     */
    public CancellazionePrestitoAttivoException(String msg) {
        super(msg);
    }
}
