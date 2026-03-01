
package biblioteca.gestioneeccezioni;

/**
 * @brief Eccezione sollevata quando i dati inseriti non sono validi.
 * 
 * Questa eccezione estende {@link BibliotecaException} e viene utilizzata
 * per segnalare errori di validazione dei campi inseriti dall'utente.
 * 
 * Può essere lanciata, ad esempio, quando:
 * - un campo obbligatorio è vuoto
 * - un formato non è corretto (es. email, matricola, ISBN)
 * 
 * Offre due costruttori:
 * - senza messaggio di dettaglio
 * - con messaggio di dettaglio personalizzato
 */
public class ValidazioneException extends BibliotecaException{
    /**
     * @brief Costruttore di default.
     * 
     * Crea una nuova istanza di {@code ValidazioneException} senza messaggio di dettaglio.
     * 
     * @post l'eccezione viene inizializzata senza messaggio
     */
    public ValidazioneException() {
    }
    /**
     * @brief Costruttore con messaggio di dettaglio.
     * 
     * Crea una nuova istanza di {@code ValidazioneException} con il messaggio di dettaglio specificato.
     * 
     * @param msg Il messaggio di dettaglio associato all'eccezione
     * @post l'eccezione viene inizializzata con il messaggio fornito
     */
    public ValidazioneException(String msg) {
        super(msg);
    }
}
