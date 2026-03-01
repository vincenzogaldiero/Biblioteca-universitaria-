package biblioteca.gestioneprestiti;

import biblioteca.gestioneeccezioni.PrestitoNonTrovatoException;
import biblioteca.gestioneeccezioni.ValidazioneException;
import biblioteca.gestioneeccezioni.LimitePrestitoSuperatoException;
import biblioteca.gestioneeccezioni.LibroNonDisponibileException;
import biblioteca.gestionelibri.ArchivioLibri;
import biblioteca.gestionelibri.Libro;
import biblioteca.gestioneutenti.ArchivioUtenti;
import biblioteca.gestioneutenti.Utente;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Collections;
/**
 * @class PrestitiService
 * @brief Service che esegue la validazione dei dati dei prestiti.
 *
 * Effettua i controlli su:
 * - utente, libro, dataInizio, dataFine e lo stato
 * - l'utente non ha superato il limite di Prestiti consetito (minore di 3) 
 * - il numero di copie non è minore di 0 
 * Le operazioni vengono delegate a ArchivioPrestitiAttivi e ArchivioCronologiaPrestiti.
 */
public class PrestitiService {
    /** Archivio dei libri su cui operare */
    private ArchivioLibri archivioLibri;
     /** Archivio degli utenti su cui operare */
    private ArchivioUtenti archivioUtenti;
     /** Archivio dei prestiti attivi su cui operare */
    private ArchivioPrestitiAttivi archivioPrestitiAttivi;
     /** Archivio della cornologia dei prestiti su cui operare */
    private ArchivioCronologiaPrestiti archivioCronologia;
    
    /**
     * @brief Costruttore del Service 
     * @param archivioLibri archivio dei libri da usare come dati
     * @param archivioUtenti archivio degli utenti da usare come dati
     * @param archivioPrestitiAttivi archivio dei prestiti attivi da usare come dati
     * @param archivioCronologia archivio della cronologia dei prestiti da usare come dati
     * 
     * @pre archivioLibri != null, archivioUtenti != null,archivioPrestitiAttivi!= null, archivioCronologia != null
     * @post this.archivioLibri==archivioLibri, this.archivioUtenti==archivioUtenti, this.archivioPrestitiAttivi==archivioPrestitiAttivi, this.archivioCronologia==archivioCronologia
     */
    public PrestitiService(ArchivioLibri archivioLibri, ArchivioUtenti archivioUtenti, ArchivioPrestitiAttivi archivioPrestitiAttivi, ArchivioCronologiaPrestiti archivioCronologia) {
        this.archivioLibri = archivioLibri;
        this.archivioUtenti = archivioUtenti;
        this.archivioPrestitiAttivi = archivioPrestitiAttivi;
        this.archivioCronologia = archivioCronologia;
    }
/**
 * @brief Aggiunge un nuovo prestito attivo effettuando i dovuti controlli.
 *
 * Questo metodo verifica:
 * - la validità dei parametri forniti;
 * - l'esistenza dell'utente e del libro;
 * - che l'utente non abbia superato il limite massimo di 3 prestiti attivi;
 * - che il libro abbia almeno una copia disponibile.
 * 
 * In caso di esito positivo, il prestito viene registrato come ATTIVO,
 * il numero di copie disponibili del libro viene decrementato.
 * e le modifiche vengono salvate nei file CSV corrispondenti (prestitiAttivi.csv, libri.csv, utenti.csv).
 * 
 * @param isbn ISBN del libro da prestare (non nullo e non vuoto)
 * @param matricola Matricola dell'utente (non nulla e valida)
 * @param dataFine Data prevista di restituzione del libro (non nulla, futura)
 *
 * @throws ValidazioneException Se i dati forniti non sono validi
 * @throws LibroNonDisponibileException Se il libro non ha copie disponibili
 * @throws LimitePrestitoSuperatoException Se l'utente ha già 3 prestiti attivi
 *
 * @pre isbn != null && !isbn.isEmpty()
 * @pre matricola != null && !matricola.isEmpty()
 * @pre dataFine != null && dataFine > LocalDate.now()
 * @post Viene aggiunto un nuovo prestito attivo all'archivio dei prestiti,
 *       il numero di copie disponibili del libro è decrementato di 1.
 *       e tutte le modifiche sono salvate su file.
 */
public void registraPrestito(String isbn, String matricola, LocalDate dataFine)
        throws ValidazioneException, LibroNonDisponibileException, LimitePrestitoSuperatoException {

    if (isbn == null || isbn.trim().isEmpty() || matricola == null || matricola.trim().isEmpty() || dataFine == null) {
        throw new ValidazioneException("Dati del prestito non validi.");
    }
        LocalDate oggi = LocalDate.now();


    if (dataFine.isBefore(oggi)) {
        throw new ValidazioneException(
                "La data di restituzione non può essere nel passato."
        );
    }
    if (dataFine.isEqual(oggi)) {
        throw new ValidazioneException(
                "La restituzione deve avvenire almeno il giorno successivo."
        );
    }

    Libro libro = archivioLibri.ricercaISBN(isbn);
    if (libro == null) {
        throw new ValidazioneException(
            "Nessun libro trovato con questo ISBN."
        );
    }
    Utente utente = archivioUtenti.ricercaMatricola(matricola);
        if (utente == null) {
        throw new ValidazioneException(
            "Nessun utente trovato con questa matricola."
        );
    }

    // Controllo limite massimo prestiti
    if (archivioPrestitiAttivi.contaPrestitiAttiviUtente(utente) >= 3) {
        throw new LimitePrestitoSuperatoException(
                "L'utente ha già raggiunto il numero massimo di prestiti consentiti."
        );
    }

    // Controllo disponibilità libro
    if (libro.getCopieDisponibili() <= 0) {
        throw new LibroNonDisponibileException("Libro non disponibile.");
    }
    
    // Genera un nuovo ID rogressivo e univoco per il prestito attivo
    int id = archivioPrestitiAttivi.generaNuovoId();
    
    Prestito p = new Prestito(
            id,
            utente,
            libro,
            oggi,
            dataFine,
            StatoPrestiti.ATTIVO
    );

    libro.setCopieDisponibili(libro.getCopieDisponibili() - 1);
    archivioPrestitiAttivi.aggiungiPrestitoAttivo(p);
    Utente u = p.getUtente();
    u.aggiungiPrestitoAttivo(p);
    
    try {
        archivioPrestitiAttivi.scriviSuFile("prestitiAttivi.csv");
        archivioLibri.scriviSuFile("libri.csv");
        archivioUtenti.scriviSuFile("utenti.csv");
    } catch(IOException e) {
        e.printStackTrace();
    }
}
/**
 * @brief Elimina un prestito attivo esistente (restituzione del libro).
 *
 * Il metodo rimuove il prestito dall'archivio dei prestiti attivi,
 * incrementa il numero di copie disponibili del libro e
 * inserisce il prestito nella cronologia impostando lo stato a CHIUSO.
 * Tutte le modifiche vengono salvate nei rispettivi file CSV:
 * prestitiAttivi.csv, libri.csv, utenti.csv e cronologia.csv.
 *
 * @param p Prestito attivo da eliminare
 * @return Il prestito eliminato oppure null se non presente
 *
 * @throws ValidazioneException Se il prestito fornito è nullo
 *
 * @pre p != null
 * @post se presente, il prestito viene rimosso dai prestiti attivi,
 *       aggiunto alla cronologia, aggiornato lo stato a CHIUSO
 *       e tutte le modifiche sono salvate su file.
 */
    public Prestito eliminaPrestitoAttivo(Prestito p) throws ValidazioneException {

        if (p == null) {
            throw new ValidazioneException("Prestito nullo.");
        }

        Prestito rimosso = archivioPrestitiAttivi.rimuoviPrestitoAttivo(p);

        if (rimosso != null) {

            // recupero il libro VERO dall'archivio
            Libro libroArchivio = archivioLibri.ricercaISBN(
                    rimosso.getLibro().getISBN()
            );

            if (libroArchivio == null) {
                throw new IllegalStateException(
                    "Libro non presente in ArchivioLibri"
                );
            }

            // incremento copie SUL LIBRO GIUSTO
            libroArchivio.setCopieDisponibili(
                    libroArchivio.getCopieDisponibili() + 1
            );

            rimosso.setStato(StatoPrestiti.CHIUSO);
            archivioCronologia.aggiungiPrestitoCronologia(rimosso);
            Utente u = rimosso.getUtente();
            u.rimuoviPrestitoAttivo(rimosso);
        }

        try {
            archivioPrestitiAttivi.scriviSuFile("prestitiAttivi.csv");
            archivioLibri.scriviSuFile("libri.csv");
            archivioUtenti.scriviSuFile("utenti.csv");
            archivioCronologia.scriviSuFile("cronologia.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rimosso;
    }

/**
 * @brief Restituisce l'elenco completo dei prestiti attivi.
 *
 * Il metodo aggiorna lo stato dei prestiti verificando se sono in ritardo:
 * eventuali prestiti scaduti vengono contrassegnati con {@link StatoPrestiti#RITARDO}.
 *
 * @return Lista dei prestiti attivi presenti in archivio
 *
 * @post la lista restituita rappresenta lo stato corrente
 *       dell'archivio dei prestiti attivi, con i prestiti eventualmente aggiornati a RITARDO.
 */
public List<Prestito> visualizzaPrestitiAttivi() {
    for (Prestito p : archivioPrestitiAttivi.getPrestitiAttivi()) {
        if (prestitoInRitardo(p)) {
            p.setStato(StatoPrestiti.RITARDO);
        }
    }
    return archivioPrestitiAttivi.getPrestitiAttivi();
}
    /**
     * @brief Aggiunge un nuovo prestito alla cronologia.
     *
     * Questo metodo **accetta solo prestiti chiusi** (stato CHIUSO) e viene
     * richiamato internamente da {@link #eliminaPrestitoAttivo(Prestito)} 
     * quando un prestito viene restituito e spostato dalla lista dei prestiti attivi
     * alla cronologia.
     *
     * @param p Prestito da registrare (deve essere chiuso)
     * @throws ValidazioneException se i dati non sono validi o il prestito non è chiuso
     * 
     * @pre p != null && p.getStato() == StatoPrestiti.CHIUSO
     * @post il prestito viene aggiunto all'archivio della cronologia
     */
       public void registraPrestitoCronologia(Prestito p) throws ValidazioneException{
        
        if (p == null) {
            throw new ValidazioneException("Prestito nullo.");
        }
        if (p.getStato() != StatoPrestiti.CHIUSO) {
            throw new ValidazioneException("Solo prestiti chiusi possono essere registrati nella cronologia.");
        }
        archivioCronologia.aggiungiPrestitoCronologia(p);
   }
     /**
    * @brief Restituisce l'insieme completo della cronologia dei prestiti
    * @return lista dei prestiti attivi
    */ 
   public List<Prestito> visualizzaCronologia(){ 
        return archivioCronologia.getCronologia();
   }
  /**
  * @brief Ricerca prestiti attivi filtrando per matricola e/o ISBN.
  *
  * Il metodo restituisce i prestiti attivi corrispondenti ai criteri forniti.
  * La ricerca può essere effettuata su:
  * - entrambe matricola e ISBN,
  * - solo matricola,
  * - solo ISBN.
  *
  * @param matricola Matricola dell'utente da cercare (può essere vuota)
  * @param isbn ISBN del libro da cercare (può essere vuoto)
  * @return Lista dei prestiti attivi trovati
  * @throws ValidazioneException Se i dati inseriti non sono validi (matricola/ISBN)
  * @throws PrestitoNonTrovatoException Se non viene trovato alcun prestito corrispondente
  *
  * @pre Almeno uno tra matricola o ISBN deve essere fornito
  * @post Viene restituita la lista dei prestiti attivi corrispondenti ai criteri
  */
   public List<Prestito> ricercaPrestitiAttivi(String matricola, String isbn) throws ValidazioneException, PrestitoNonTrovatoException {
    return ricercaPrestitiInterna(matricola, isbn, false);
    }
    /**
     * @brief Ricerca prestiti nella cronologia filtrando per matricola e/o ISBN.
     *
     * Il metodo restituisce i prestiti chiusi presenti nella cronologia corrispondenti ai criteri forniti.
     * La ricerca può essere effettuata su:
     * - entrambe matricola e ISBN,
     * - solo matricola,
     * - solo ISBN.
     *
     * @param matricola Matricola dell'utente da cercare (può essere vuota)
     * @param isbn ISBN del libro da cercare (può essere vuoto)
     * @return Lista dei prestiti chiusi trovati
     * @throws ValidazioneException Se i dati inseriti non sono validi (matricola/ISBN)
     * @throws PrestitoNonTrovatoException Se non viene trovato alcun prestito corrispondente
     *
     * @pre Almeno uno tra matricola o ISBN deve essere fornito
     * @post Viene restituita la lista dei prestiti chiusi corrispondenti ai criteri
     */
   public List<Prestito> ricercaPrestitiCronologia(String matricola, String isbn)
        throws ValidazioneException, PrestitoNonTrovatoException {
    return ricercaPrestitiInterna(matricola, isbn, true);
}
      /**
      * @brief Funzione interna che esegue la ricerca dei prestiti, sia attivi che chiusi.
      *
      * Il metodo gestisce tutti i casi di ricerca:
      * - matricola + ISBN
      * - solo matricola
      * - solo ISBN
      * 
      * A seconda del flag cronologia:
      * - false → ricerca nell'archivio dei prestiti attivi
      * - true → ricerca nell'archivio dei prestiti chiusi (cronologia)
      *
      * @param matricola Matricola dell'utente da cercare (può essere vuota)
      * @param isbn ISBN del libro da cercare (può essere vuoto)
      * @param cronologia true se la ricerca è nella cronologia, false se nei prestiti attivi
      * @return Lista dei prestiti trovati in base ai criteri
      * @throws ValidazioneException Se matricola o ISBN non sono validi
      * @throws PrestitoNonTrovatoException Se non viene trovato alcun prestito corrispondente
      *
      * @pre Almeno uno tra matricola o ISBN deve essere fornito
      * @post Restituisce la lista dei prestiti trovati nell'archivio corretto
      */
    private List<Prestito> ricercaPrestitiInterna(
            String matricola,
            String isbn,
            boolean cronologia)
            throws ValidazioneException, PrestitoNonTrovatoException {

        matricola = matricola == null ? "" : matricola.trim();
        isbn = isbn == null ? "" : isbn.trim();

        // CASO: entrambi i campi vuoti → VALIDAZIONE
        if (matricola.isEmpty() && isbn.isEmpty()) {
            throw new ValidazioneException("Inserire una matricola oppure un ISBN.");
        }

        // CASO 1: matricola + ISBN
        if (!matricola.isEmpty() && !isbn.isEmpty()) {
            Utente u = archivioUtenti.ricercaMatricola(matricola);
            Libro l = archivioLibri.ricercaISBN(isbn);

            if (u == null || l == null) {
                throw new PrestitoNonTrovatoException("Utente o libro inesistente.");
            }

            Prestito trovato = cronologia
                    ? archivioCronologia.ricercaPrestitoUtenteLibro(u, l)
                    : archivioPrestitiAttivi.ricercaPrestitoAttivo(u, l);

            if (trovato == null) {
                throw new PrestitoNonTrovatoException("Nessun prestito trovato per questo utente e libro.");
            }

            return Collections.singletonList(trovato);
        }

        // CASO 2: solo matricola
        if (!matricola.isEmpty()) {
            Utente u = archivioUtenti.ricercaMatricola(matricola);

            if (u == null) {
                throw new PrestitoNonTrovatoException("Utente inesistente.");
            }

            List<Prestito> risultati = cronologia
                    ? archivioCronologia.ricercaPrestitoUtenteCronologia(u)
                    : archivioPrestitiAttivi.ricercaPrestitoAttivoUtente(u);

            if (risultati.isEmpty()) {
                throw new PrestitoNonTrovatoException("Nessun prestito trovato per questo utente.");
            }
            return risultati;
        }

        // CASO 3: solo ISBN
        if (!isbn.isEmpty()) {
            Libro l = archivioLibri.ricercaISBN(isbn);

            if (l == null) {
                throw new PrestitoNonTrovatoException("Libro inesistente.");
            }

            List<Prestito> risultati = cronologia
                    ? archivioCronologia.ricercaPrestitoLibroCronologia(l)
                    : archivioPrestitiAttivi.ricercaPrestitoAttivoLibro(l);

            if (risultati.isEmpty()) {
                throw new PrestitoNonTrovatoException("Nessun prestito trovato per questo libro.");
            }
            return risultati;
        }

        // Questo punto non dovrebbe mai essere raggiunto
        throw new PrestitoNonTrovatoException("Prestito non trovato.");
    }

    /**
     * @brief Verifica se un prestito attivo è in ritardo.
     *
     * Un prestito è considerato in ritardo se:
     * - lo stato è {@link StatoPrestiti#ATTIVO}
     * - la data corrente è successiva alla data di fine prestito.
     *
     * @param p Prestito da controllare (non nullo)
     * @return true se il prestito è attivo e in ritardo, false altrimenti
     *
     * @pre p != null
     * @post Restituisce lo stato di ritardo del prestito attivo
     */
    public boolean prestitoInRitardo(Prestito p) {

         if (p == null) return false;

         if (p.getStato() != StatoPrestiti.ATTIVO) return false;

    return LocalDate.now().isAfter(p.getDataFine());
    }

}
