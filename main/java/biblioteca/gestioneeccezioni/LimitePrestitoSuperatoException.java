
package biblioteca.gestioneeccezioni;
/**
 * @brief Eccezione sollevata quando un utente supera il limite massimo di prestiti attivi.
 * 
 * Questa eccezione estende {@link BibliotecaException} e viene utilizzata
 * per segnalare che un utente ha già raggiunto il numero massimo di prestiti consentiti.
 * 
 * Attualmente, il limite di prestiti attivi per utente è 3.
 * 
 * Offre due costruttori:
 * - senza messaggio di dettaglio
 * - con messaggio di dettaglio personalizzato
 */
public class LimitePrestitoSuperatoException extends BibliotecaException{
    /**
     * @brief Costruttore di default.
     * 
     * Crea una nuova istanza di {@code LimitePrestitoSuperatoException} senza messaggio di dettaglio.
     * 
     * @post l'eccezione viene inizializzata senza messaggio
     */
    public LimitePrestitoSuperatoException() {
    }
    /**
     * @brief Costruttore con messaggio di dettaglio.
     * 
     * Crea una nuova istanza di {@code LimitePrestitoSuperatoException} con il messaggio di dettaglio specificato.
     * 
     * @param msg Il messaggio di dettaglio associato all'eccezione
     * @post l'eccezione viene inizializzata con il messaggio fornito
     */
    public LimitePrestitoSuperatoException(String msg) {
        super(msg);
    }
}
