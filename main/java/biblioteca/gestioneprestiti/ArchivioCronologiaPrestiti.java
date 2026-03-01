package biblioteca.gestioneprestiti;

import biblioteca.Archivio;
import biblioteca.gestionelibri.ArchivioLibri;
import biblioteca.gestioneutenti.Utente;
import biblioteca.gestionelibri.Libro;
import biblioteca.gestioneutenti.ArchivioUtenti;
import java.util.List;
import java.util.LinkedList;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
/**
 * @class ArchivioCronologiaPrestiti
 * @brief Gestisce la collezione della cronologia dei prestiti della biblioteca.
 *
 * Questa classe si occupa della sola gestione dei dati:
 * - memorizzazione della cronologia dei prestiti tramite {@link LinkedList} in ordine di inserimento;
 * - operazioni di aggiungi, rimuovi, ricerca, modifica e visualizza;
 * - salvataggio e caricamento da file.
 *
 * Tutti i controlli e le regole di business sono affidati alla classe {@link PrestitoService}.
 */
public class ArchivioCronologiaPrestiti implements Archivio{
    /** Lista dei prestiti in ordine di inserimento */
    private List<Prestito> cronologia;

    /** Archivio utenti collegato per eventuali riferimenti o validazioni */
    private ArchivioUtenti archivioUtenti;

    /** Archivio libri collegato per eventuali riferimenti o validazioni */
    private ArchivioLibri archivioLibri;
    /**
     * @brief Costruttore: inizializza una lista vuota e carica la cronologia da file.
     *
     * @param filename Nome del file CSV contenente la cronologia dei prestiti.
     * @param archivioUtenti Riferimento all'archivio utenti.
     * @param archivioLibri Riferimento all'archivio libri.
     *
     * @post La cronologia è inizializzata come {@link LinkedList} vuota,
     *       e i dati presenti nel file sono caricati correttamente.
     */
   public ArchivioCronologiaPrestiti(String filename, ArchivioUtenti archivioUtenti, ArchivioLibri archivioLibri){
        this.cronologia= new LinkedList<>();   
        this.archivioUtenti=archivioUtenti;
        this.archivioLibri=archivioLibri;

   
       try {
            leggiDaFile(filename);
       } catch (IOException e) {
            e.printStackTrace();
        }
       
   }
   
    /**
    * @brief Carica la cronologia dei prestiti da un file di testo
    * 
    * @param filename Nome del file da cui leggere
    * @throws IOException se si verificano errori di lettura
    * 
    * @pre filename!=null && !filename.isEmpty()
    * @post cronologia prestiti contiene la cronologia dei prestiti letti dal file 
    */
/**
 * @brief Carica la cronologia dei prestiti da un file di testo
 *
 * @param filename Nome del file da cui leggere
 * @throws IOException se si verificano errori di lettura
 *
 * @pre filename != null && !filename.isEmpty()
 * @post la cronologia contiene i prestiti letti dal file
 */
    @Override
    public void leggiDaFile(String filename) throws IOException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(filename)))) {

            if (scanner.hasNextLine()) scanner.nextLine(); // salta intestazione

            while (scanner.hasNextLine()) {

                String line = scanner.nextLine();
                if (line.trim().isEmpty()) continue;

                String[] parti = line.split(";");
                if (parti.length != 12) continue;

                int id = Integer.parseInt(parti[0]);

                String nome = parti[1];
                String cognome = parti[2];
                String email = parti[3];
                String matricola = parti[4];

                String titolo = parti[5];
                String autore = parti[6];
                int anno = Integer.parseInt(parti[7]);
                String isbn = parti[8];

                LocalDate dataInizio = LocalDate.parse(parti[9], formatter);
                LocalDate dataFine = LocalDate.parse(parti[10], formatter);
                StatoPrestiti stato = StatoPrestiti.valueOf(parti[11]);

                Utente u = archivioUtenti.ricercaMatricola(matricola);
                Libro l = archivioLibri.ricercaISBN(isbn);
                 
                if (u == null || l == null) {
                continue; // salta riga incoerente
                }  

                Prestito p = new Prestito(id, u, l, dataInizio, dataFine, stato);

                cronologia.add(p);
            }
        }
    }
    /**
    * @brief Salva la cronologia dei prestiti nel file specificato
    * 
    * @param filename Nome del file su cui scrivere
    * @throws IOException se si verificano errori di scrittura
    * 
    * @pre filename!=null && !filename.isEmpty()
    * @post il file contiene tutta la cronologia dei prestiti memorizzati
    */
    @Override
    public void scriviSuFile(String filename) throws IOException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(filename)))) {

            pw.println("ID;NOME;COGNOME;EMAIL;MATRICOLA;TITOLO;AUTORE;ANNO;ISBN;DATA_INIZIO;DATA_FINE;STATO");

            for (Prestito p : cronologia) {

                Utente u = p.getUtente();
                Libro l = p.getLibro();

                pw.println(
                    p.getId() + ";" +
                    u.getNome() + ";" +
                    u.getCognome() + ";" +
                    u.getEmail() + ";" +
                    u.getMatricola() + ";" +
                    l.getTitolo() + ";" +
                    l.getAutore() + ";" +
                    l.getAnnoPubblicazione() + ";" +
                    l.getISBN() + ";" +
                    p.getDataInizio().format(formatter) + ";" +
                    p.getDataFine().format(formatter) + ";" +
                    p.getStato().name()
                );
            }
        }
    }

   
    /**
    * @brief Aggiunge un nuovo prestito attivo all'archivio
    * Non effettua controlli vengono delegati a @see PrestitoService
    * 
    * @param p Prestito da aggiungere
    * 
    * @pre p!=null 
    * @post cronologia prestito contiene il prestito p
    */
   public void aggiungiPrestitoCronologia(Prestito p){
       cronologia.add(p);
   }   
    /**
     * @brief Cerca tutti i prestiti nella cronologia associati a un determinato utente.
     *
     * @param utente Utente da cercare (matricola valida).
     * @return Lista dei prestiti della cronologia relativi all'utente. La lista può essere vuota
     *         se l'utente non ha prestiti nella cronologia.
     *
     * @pre utente != null
     * @post Restituisce tutti i prestiti nella cronologia associati all'utente.
     */
   public List<Prestito> ricercaPrestitoUtenteCronologia(Utente utente){
        List <Prestito> risultati = new LinkedList<>();
   
        for(Prestito p : cronologia){
        if(p.getUtente().equals(utente)){
            risultati.add(p);
            }
        }
        return risultati;
   }
    /**
     * @brief Cerca tutti i prestiti nella cronologia associati a un determinato libro.
     *
     * @param libro Libro da cercare (ISBN valido).
     * @return Lista dei prestiti della cronologia relativi al libro. La lista può essere vuota
     *         se il libro non ha prestiti nella cronologia.
     *
     * @pre libro != null
     * @post Restituisce tutti i prestiti nella cronologia associati al libro.
     */
    public List<Prestito> ricercaPrestitoLibroCronologia(Libro libro){
        List<Prestito> risultati = new LinkedList<>();

        for(Prestito p : cronologia){
            if(p.getLibro().equals(libro)){
                risultati.add(p);
            }
        }
        return risultati;
     }
     /**
     * @brief Cerca un prestito specifico nella cronologia tramite utente e libro.
     *
     * @param utente Utente da cercare.
     * @param libro Libro da cercare.
     * @return Il prestito corrispondente se trovato, altrimenti null.
     *
     * @pre utente != null && libro != null
     * @post Restituisce il prestito nella cronologia associato a utente e libro se presente.
     */
    public Prestito ricercaPrestitoUtenteLibro(Utente utente, Libro libro) {
        for (Prestito p : cronologia) {
            if (p.getUtente().equals(utente) &&
                p.getLibro().equals(libro)) {
                return p;
            }
        }
        return null;
}
    
    /**
    * @brief Restituisce l'intero insieme deilla cronologia dei prestiti
    * @return Restituisce la lista dei prestiti in ordine di inserimento.
    */ 
   public List<Prestito> getCronologia(){ 
        return cronologia;
   }
   
}
