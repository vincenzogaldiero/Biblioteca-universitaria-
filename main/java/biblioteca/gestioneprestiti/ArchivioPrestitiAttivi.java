package biblioteca.gestioneprestiti;

import biblioteca.Archivio;
import biblioteca.gestionelibri.Libro;
import biblioteca.gestioneutenti.Utente;
import biblioteca.gestionelibri.ArchivioLibri;
import biblioteca.gestioneutenti.ArchivioUtenti;
import java.util.List;
import java.util.LinkedList;
import java.io.IOException;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.format.DateTimeFormatter;

import java.time.LocalDate;
/**
 * @class ArchivioPrestitiAttivi
 * @brief Gestisce la collezione dei prestiti attivi della biblioteca.
 *
 * Questa classe si occupa della sola gestione dei dati:
 * - memorizzazione dei prestiti attivi tramite {@link LinkedList} in ordine di inserimento;
 * - operazioni di aggiungi, rimuovi, ricerca, modifica e visualizza;
 * - salvataggio e caricamento da file.
 *
 * Tutti i controlli e le regole di business sono affidati alla classe {@link PrestitoService}.
 */
public class ArchivioPrestitiAttivi implements Archivio{
    /** Lista in ordine di inserimento dei prestiti attivi */
    private List<Prestito> prestitiAttivi;

    /** ID da assegnare al prossimo prestito creato */
    private int prossimoId;

    /** Archivio utenti collegato per validazioni e riferimenti */
    private ArchivioUtenti archivioUtenti;

    /** Archivio libri collegato per validazioni e riferimenti */
    private ArchivioLibri archivioLibri;
    
    /**
     * @brief Costruttore: inizializza una lista vuota e carica i dati da file.
     *
     * @param filename Nome del file CSV contenente i prestiti attivi.
     * @param archivioUtenti Riferimento all'archivio utenti.
     * @param archivioLibri Riferimento all'archivio libri.
     *
     * @post La lista dei prestiti è inizializzata come {@link LinkedList} vuota,
     *       i dati presenti nel file sono caricati e l'ID successivo è aggiornato.
     */
   public ArchivioPrestitiAttivi(String filename, ArchivioUtenti archivioUtenti, ArchivioLibri archivioLibri){
        this.prestitiAttivi= new LinkedList<>();
        prossimoId = 1;
        this.archivioUtenti = archivioUtenti;
        this.archivioLibri = archivioLibri;
        
        try {
            leggiDaFile(filename);
            aggiornaProssimoId();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
   }
    /**
    * @brief Carica i prestiti attivi da un file di testo
    * 
    * @param filename Nome del file da cui leggere
    * @throws IOException se si verificano errori di lettura
    * 
    * @pre filename!=null && !filename.isEmpty()
    * @post prestiti attivi contiene i prestiti attivi letti dal file 
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

                prestitiAttivi.add(p);
                u.aggiungiPrestitoAttivo(p);
            }
        }
    }
    /**
    * @brief Salva i prestiti attivi nel file specificato
    * 
    * @param filename Nome del file su cui scrivere
    * @throws IOException se si verificano errori di scrittura
    * 
    * @pre filename!=null && !filename.isEmpty()
    * @post il file contiene tutti i prestiti attivi memorizzati
    */
    @Override
    public void scriviSuFile(String filename) throws IOException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(filename)))) {

            pw.println("ID;NOME;COGNOME;EMAIL;MATRICOLA;TITOLO;AUTORE;ANNO;ISBN;DATA_INIZIO;DATA_FINE;STATO");

            for (Prestito p : prestitiAttivi) {

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
    * Non effettua controlli vengono delegati a {@link PrestitoService}
    * 
    * @param p Prestito da aggiungere
    * 
    * @pre p!=null 
    * @post prestitiAttivi contiene il prestito p
    */
   public void aggiungiPrestitoAttivo(Prestito p){
       prestitiAttivi.add(p);
   }
    /**
    * @brief Rimuove un prestito attivo dall'archivio
    * 
    * @param p prestito attivo da rimuovere
    * @return Il prestito attivo che è stato rimosso oppure null se non presente
    * 
    * @pre p !=null
    * @post se presente, il prestito attivo viene rimosso dall'archivio.
    */
   public Prestito rimuoviPrestitoAttivo(Prestito p){
        boolean rimosso = prestitiAttivi.remove(p);
        return rimosso ? p : null;
   }
    /**
     * @brief Cerca un prestito attivo specifico tramite utente e libro.
     *
     * Verifica nella lista dei prestiti attivi se esiste un prestito che corrisponde
     * all'utente e al libro forniti.
     *
     * @param utente Utente da cercare (matricola valida).
     * @param libro Libro da cercare (ISBN valido).
     * @return Il prestito attivo corrispondente, oppure null se non trovato.
     *
     * @pre utente != null && libro != null
     * @post Restituisce il prestito attivo corrispondente all'utente e libro se presente.
     */
   public Prestito ricercaPrestitoAttivo(Utente utente, Libro libro){
        for(Prestito p : prestitiAttivi){
            if(p.getUtente().equals(utente) && p.getLibro().equals(libro)){
                return p;
            }
        }
        return null;
   }
      /**
      * @brief Cerca tutti i prestiti attivi di un determinato utente.
      *
      * @param utente Utente di cui cercare i prestiti attivi.
      * @return Lista dei prestiti attivi dell'utente. La lista può essere vuota se l'utente
      *         non ha prestiti attivi.
      *
      * @pre utente != null
      * @post Restituisce tutti i prestiti attivi associati all'utente.
      */
    public List<Prestito> ricercaPrestitoAttivoUtente(Utente utente){
        List <Prestito> risultati = new LinkedList<>();

        for(Prestito p : prestitiAttivi){
           if(p.getUtente().equals(utente)){
               risultati.add(p);
           }
        }
        return risultati;
    }
    /**
     * @brief Cerca tutti i prestiti attivi di un determinato libro.
     *
     * @param libro Libro di cui cercare i prestiti attivi.
     * @return Lista dei prestiti attivi associati al libro. La lista può essere vuota se
     *         il libro non ha prestiti attivi.
     *
     * @pre libro != null
     * @post Restituisce tutti i prestiti attivi associati al libro.
     */
    public List<Prestito> ricercaPrestitoAttivoLibro(Libro libro){
        List<Prestito> risultati = new LinkedList<>();

        for(Prestito p : prestitiAttivi){
            if(p.getLibro().equals(libro)){
                risultati.add(p);
            }
        }
        return risultati;
     }
      
    /**
    * @brief Restituisce l'intero insieme dei prestiti attivi
    * @return lista dei prestiti attivi in ordine di inserimento    
    */
   public List<Prestito> getPrestitiAttivi(){ 
        return prestitiAttivi;
   }
   public int contaPrestitiAttiviUtente(Utente utente) {
        int count = 0;

        for (Prestito p : prestitiAttivi) {
            if (p.getUtente().getMatricola().equals(utente.getMatricola())) {
                count++;
            }
        }

        return count;
}
    /**
    * @brief Aggiorna l'ID del prossimo prestito da assegnare.
    *
    * Scansiona tutti i prestiti attivi e imposta {@link prossimoId} come
    * il massimo ID trovato più uno.
    *
    * @post {@link prossimoId} è aggiornato al valore corretto per il prossimo prestito.
    */
   private void aggiornaProssimoId() {
    int maxId = 0;

    for (Prestito p : prestitiAttivi) {
        if (p.getId() > maxId) {
            maxId = p.getId();
        }
    }

    prossimoId = maxId + 1;
    }
    /**
    * @brief Aggiorna l'ID del prossimo prestito da assegnare.
    *
    * Scansiona tutti i prestiti attivi e imposta {@link prossimoId} come
    * il massimo ID trovato più uno.
    *
    * @post {@link prossimoId} è aggiornato al valore corretto per il prossimo prestito.
    */
   public int generaNuovoId() {
        return prossimoId++;
    }
    /**
    * @brief Verifica se esiste un prestito attivo per un determinato libro.
    *
    * @param l Libro da controllare.
    * @return true se il libro è attualmente preso in prestito, false altrimenti.
    *
    * @pre l != null
    * @post Restituisce true se esiste almeno un prestito attivo per il libro specificato.
    */
   public boolean esistePrestitoAttivoLibro(Libro l) {
        for (Prestito p : prestitiAttivi) {
            if (p.getLibro().getISBN().equals(l.getISBN())) {
                return true;
            }
        }
        return false;
}
}
