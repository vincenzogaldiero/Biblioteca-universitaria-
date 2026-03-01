
package biblioteca.gestioneeccezioni;

/**
 * @brief Eccezione sollevata quando si tenta di prestare un libro che non è disponibile.
 * 
 * Questa eccezione estende {@link BibliotecaException} e viene utilizzata
 * per segnalare che il libro richiesto non può essere prestato perché:
 * - ha numero di copie disponibili = 0
 * 
 * Offre due costruttori:
 * - senza messaggio di dettaglio
 * - con messaggio di dettaglio personalizzato
 */
public class LibroNonDisponibileException extends BibliotecaException{
    /**
     * @brief Costruttore di default.
     * 
     * Crea una nuova istanza di {@code LibroNonDisponibileException} senza messaggio di dettaglio.
     * 
     * @post l'eccezione viene inizializzata senza messaggio
     */
    public LibroNonDisponibileException() {
    }
    /**
     * @brief Costruttore con messaggio di dettaglio.
     * 
     * Crea una nuova istanza di {@code LibroNonDisponibileException} con il messaggio di dettaglio specificato.
     * 
     * @param msg Il messaggio di dettaglio associato all'eccezione
     * @post l'eccezione viene inizializzata con il messaggio fornito
     */
    public LibroNonDisponibileException(String msg) {
        super(msg);
    }
}
