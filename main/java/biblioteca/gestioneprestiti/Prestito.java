package biblioteca.gestioneprestiti;

import biblioteca.gestioneutenti.Utente;
import biblioteca.gestionelibri.Libro;
import java.time.LocalDate;
/**
 * @class Prestito
 * @brief Rappresenta un prestito del sistema bibliotecario
 * 
 * Questa classe contiene solo i dati del prestito
 * Tutti i controlli sono stati affidati alla classe {@link PrestitoService}
 * 
 */
public class Prestito {
    /** Identificativo del prestito*/
    private final int id;
    /** dati dell'Utente*/
    private Utente utente;
    /** Dati del libro*/
    private Libro libro;
    /** data inizio prestito*/
    private LocalDate dataInizio;
    /** data fine prestito, restituzione libro*/
    private LocalDate dataFine;
    /** stato del prestito (attivo,ritardo,chiuso)*/
    private StatoPrestiti stato;
    
     /**
     * @brief Costruttore di caricamento da file della classe Prestito
     * @param id Identificativo del prestito
     * @param utente Dati dell'utente
     * @param libro Dati del libro
     * @param dataInizio Data di inizio prestito
     * @param dataFine Data di fine prestito, restituzione libro
     * @param stato lo stato del prestito(attivo,chiuso,ritardo)
     * 
     * @pre {@link PrestitoService} deve garantire tutti i controlli sui dati
     * @post Il prestito è preso da file
     */
    public Prestito(int id,Utente utente,Libro libro,LocalDate dataInizio,LocalDate dataFine, StatoPrestiti stato){
        this.id=id;
        this.utente=utente;
        this.libro=libro;
        this.dataInizio=dataInizio;
        this.dataFine=dataFine;
        this.stato=stato;
    }
    
     /**
     * @return I dati dell'utente
     */
    public Utente getUtente() {
        return utente;
    }
    
    /**
     *@brief Imposta un nuovo utente per il prestito
     * 
     * @param utente Nuovo utente.
     * 
     * @pre utente != null 
     * @post L'utente viene aggiornato
     */
    public void setUtente(Utente utente) {
        this.utente = utente;
    }
    
     /**
     * @return I dati del libro
     */
    public Libro getLibro() {
        return libro;
    }
     /**
     *@brief Imposta un nuovo libro per il prestito
     * 
     * @param libro Nuovo libro.
     * @pre libro != null
     * @post Il libro viene aggiornato
     */
    public void setLibro(Libro libro) {
        this.libro = libro;
    }
    /**
     * @return Data di inizio prestito
     */
    public LocalDate getDataInizio() {
        return dataInizio;
    }
     /**
     *@brief Imposta una nuova data di inizio prestito
     * 
     * @param dataInizio Nuova data di inizio.
     * @pre dataInizio != null 
     * @post La data di inizio prestito viene aggiornata
     */
    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }
    /**
     * @return Data di fine prestito
     */
    public LocalDate getDataFine() {
        return dataFine;
    }
     /**
     *@brief Imposta una nuova data fine prestito
     * 
     * @param dataFine Nuova data fine prestito.
     * @pre dataFine != null 
     * @post La data di fine prestito viene aggiornata
     */
    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }
    /**
     * @return Stato del prestito
     */
    public StatoPrestiti getStato() {
        return stato;
    }
     /**
     *@brief Imposta una nuovo stato del prestito
     * 
     * @param stato Nuovo stato del prestito
     * @pre stato != null 
     * @post Lo stato del prestito viene aggiornato
     */
    public void setStato(StatoPrestiti stato) {
        this.stato = stato;
    }
     /** @brief Restituisce il titolo del libro o stringa vuota se libro è null. */
    public String getTitolo() {
        return libro != null ? libro.getTitolo() : "";
    }
    /** @brief Restituisce l'autore del libro o stringa vuota se libro è null. */
    public String getAutori() {
        return libro != null ? libro.getAutore() : "";
    }
    /** @brief Restituisce l'ISBN del libro o stringa vuota se libro è null. */
    public String getISBN() {
        return libro != null ? libro.getISBN() : "";
    }
    /** @brief Restituisce l'anno di pubblicazione del libro o 0 se libro è null. */
    public int getAnno() {
        return libro != null ? libro.getAnnoPubblicazione() : 0;
    }
    /** @brief Restituisce il nome dell'utente o stringa vuota se utente è null. */
    public String getNome() {
        return utente != null ? utente.getNome() : "";
    }
    /** @brief Restituisce il cognome dell'utente o stringa vuota se utente è null. */
    public String getCognome() {
        return utente != null ? utente.getCognome() : "";
    }
    /** @brief Restituisce la matricola dell'utente o stringa vuota se utente è null. */
    public String getMatricola() {
        return utente != null ? utente.getMatricola() : "";
    }
    /** @brief Restituisce l'email dell'utente o stringa vuota se utente è null. */
    public String getEmail() {
        return utente != null ? utente.getEmail() : "";
    }
    /** @brief Restituisce l'id associato a questo oggetto. */
    public int getId() {
        return id;
    }
    }
