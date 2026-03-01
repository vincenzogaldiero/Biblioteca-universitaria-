
package biblioteca.gestioneeccezioni;

/**
 * @brief Eccezione sollevata quando si tenta di aggiungere un elemento già presente nell'archivio.
 * 
 * Questa eccezione estende {@link BibliotecaException} e viene utilizzata
 * per segnalare che un utente o un libro è già registrato nella biblioteca.
 * 
 * È una classe generica per duplicati e può essere lanciata in contesti diversi:
 * - aggiunta di un utente già esistente
 * - aggiunta di un libro già presente
 * 
 * Offre due costruttori:
 * - senza messaggio di dettaglio
 * - con messaggio di dettaglio personalizzato
 */
public class DuplicatoException extends BibliotecaException{

     /**
     * @brief Costruttore di default.
     * 
     * Crea una nuova istanza di {@code DuplicatoException} senza messaggio di dettaglio.
     * 
     * @post l'eccezione viene inizializzata senza messaggio
     */
    public DuplicatoException() {
    }

    /**
     * @brief Costruttore con messaggio di dettaglio.
     * 
     * Crea una nuova istanza di {@code DuplicatoException} con il messaggio di dettaglio specificato.
     * 
     * @param msg Il messaggio di dettaglio associato all'eccezione
     * @post l'eccezione viene inizializzata con il messaggio fornito
     */
    public DuplicatoException(String msg) {
        super(msg);
    }
}
