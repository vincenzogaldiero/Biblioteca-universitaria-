package biblioteca;

import java.io.IOException;

/**
 * @interface Archivio
 * @brief Interfaccia per la gestione della persistenza dei dati.
 *
 * Definisce le operazioni fondamentali per leggere e scrivere i dati
 * di un archivio su file. Le classi che implementano questa interfaccia
 * devono fornire un meccanismo concreto di persistenza.
 */
public interface Archivio {

    /**
     * @brief Carica i dati dell'archivio da un file di testo.
     *
     * Legge il contenuto del file indicato e popola la struttura dati
     * dell’archivio. L’implementazione deve definire il formato dei dati.
     *
     * @param filename Percorso del file da cui leggere i dati.
     * @throws IOException Se si verifica un errore di I/O durante la lettura.
     *
     * @pre filename != null && !filename.isEmpty()
     * @post I dati dell’archivio sono aggiornati con le informazioni contenute nel file.
     */
    void leggiDaFile(String filename) throws IOException;

    /**
     * @brief Salva i dati dell'archivio su un file di testo.
     *
     * Scrive sul file specificato lo stato corrente dell’archivio.
     * L’implementazione deve garantire che il formato sia coerente
     * con quello utilizzato per la lettura.
     *
     * @param filename Percorso del file su cui salvare i dati.
     * @throws IOException Se si verifica un errore di I/O durante la scrittura.
     *
     * @pre filename != null && !filename.isEmpty()
     * @post Il file contiene una copia persistente dei dati dell’archivio.
     */
    void scriviSuFile(String filename) throws IOException;
}
