package biblioteca.gestionelibri;

import biblioteca.Archivio;
import java.util.TreeSet;
import java.util.Set;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

/**
 * @class ArchivioLibri
 * @brief Gestione della collezione dei libri della biblioteca.
 *
 * La classe si occupa esclusivamente della gestione dei dati relativi ai libri:
 * - memorizzazione dei libri tramite {@link TreeSet}, che garantisce
 *   l'ordinamento naturale definito in {@link Libro};
 * - operazioni di aggiunta, rimozione, ricerca, modifica e visualizzazione;
 * - caricamento e salvataggio dei dati su file.
 *
 * Tutti i controlli di validità e la logica applicativa sono demandati
 * alla classe {@link LibriService}.
 */
public class ArchivioLibri implements Archivio{
    /** Insieme ordinato dei libri presenti in archivio. */
    private Set<Libro> libri;
   
    /**
     * @brief Costruttore dell'archivio libri.
     *
     * Inizializza la struttura dati interna come {@link TreeSet} vuoto
     * e carica i libri dal file specificato.
     *
     * @param filename Nome del file da cui caricare i dati dei libri.
     *
     * @post {@code libri} è inizializzato come {@link TreeSet}.
     * @post Se il file esiste, i libri sono caricati in memoria.
     */
public ArchivioLibri(String filename) {
    this.libri = new TreeSet<>();
    try {
        leggiDaFile(filename);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
   
/**
 * @brief Carica i libri dal file specificato.
 *
 * Il formato previsto per ogni riga (dopo l'intestazione) è:
 * titolo;autore;annoPubblicazione;isbn;copieDisponibili
 *
 * La prima riga del file viene ignorata poiché contiene l'intestazione.
 *
 * @param filename Nome del file da cui leggere i dati.
 * @throws IOException Se si verificano errori di lettura.
 *
 * @pre filename != null && !filename.isEmpty()
 * @post L'archivio contiene tutti i libri presenti nel file.
 */
   @Override
   public void leggiDaFile(String filename)throws IOException{
       try (Scanner scanner = new Scanner(new BufferedReader( new FileReader(filename)))) {

        //  Salta la prima riga (intestazione)
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        //Lettura righe successive
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.trim().isEmpty()) continue;

            String[] parti = line.split(";");
            if (parti.length < 5) continue;

            String titolo = parti[0];
            String autore = parti[1];
            int anno = Integer.parseInt(parti[2]);
            String isbn = parti[3];
            int copie = Integer.parseInt(parti[4]);

            Libro l = new Libro(titolo, autore, anno, isbn, copie);
            libri.add(l);
            }
       }
   }
/**
 * @brief Salva tutti i libri sul file specificato.
 *
 * Ogni libro viene scritto in una riga nel formato:
 * titolo;autore;annoPubblicazione;isbn;copieDisponibili
 *
 * La prima riga del file contiene l'intestazione con i nomi dei campi.
 *
 * @param filename Nome del file su cui salvare i dati.
 * @throws IOException Se si verificano errori di scrittura.
 *
 * @pre filename != null && !filename.isEmpty()
 * @post Il file contiene l'intestazione e tutti i libri memorizzati nell'archivio.
 */
   @Override
   public void scriviSuFile(String filename) throws IOException{
       try(PrintWriter pw = new PrintWriter( new BufferedWriter ( new FileWriter (filename)))){
           pw.println("TITOLO;AUTORE;ANNODIPUBBLICAZIONE;ISBN;COPIEDISPONIBILI");
           for(Libro l: libri){
               pw.println(
                       l.getTitolo() + ";" +
                       l.getAutore() + ";" +
                       l.getAnnoPubblicazione() + ";" + 
                       l.getISBN() + ";" +
                       l.getCopieDisponibili()
                    );
            }   
        }
   }      
    /**
    * @brief Aggiunge un nuovo libro all'archivio
    * Non effettua controlli, che vengono delegati a {@link LibriService}.    * 
    * @param l Libro da aggiungere
    * 
    * @pre l!=null 
    * @post libri contiene il libro l
    */
   public void aggiungiLibro(Libro l){
       libri.add(l);
   }
      /**
    * @brief Rimuove un libro dall'archivio
    * 
    * @param l libro da rimuovere
    * @return Il libro che è stato rimosso oppure null se non presente
    * 
    * @pre l !=null
    * @post se presente, il libro viene rimosso dall'archivio.
    */
   public Libro rimuoviLibro(Libro l){
    boolean r = libri.remove(l);
    return r ? l : null;
   }
    /**
    * @brief Cerca tutti i libri che hanno un determinato titolo
    * 
    * @param titolo Titolo da cercare
    * @return Insieme dei libri con quel titolo oppure un insieme vuoto se nessuno viene trovato
    * 
    * @pre titolo!= null && !titolo.isEmpty()
    * @post restituisce l'insieme dei libri corrispondenti
    */
   public Set<Libro> ricercaTitolo(String titolo){
     Set<Libro> risultato = new TreeSet<>();
    
    for (Libro l : libri) {
        if (l.getTitolo().equalsIgnoreCase(titolo)) {
            risultato.add(l);
        }
    }
    
    return risultato;
   }
    /**
    * @brief Cerca se esiste un libro con un determinato ISBN
    * 
    * @param ISBN ISBN da cercare
    * @return il libro con l'ISBN dato oppure null in caso di libro non trovato
    * 
    * @pre ISBN!= null && !ISBN.isEmpty()
    * @post restituisce l'insieme dei libri corrispondenti
    */
   public Libro ricercaISBN(String ISBN){ 
        for (Libro l : libri) {
            if (l.getISBN().equalsIgnoreCase(ISBN)) {
                return l;
            }
        }
    return null;
   }
    /**
    * @brief Cerca se esiste un libro con un determinato autore.
    * 
    * @param autore Autore da cercare
    * @return  l'insieme dei libri con l'autore dato oppure un insieme vuoto in caso di libro non trovato
    * 
    * @pre autore!= null && !autore.isEmpty()
    * @post restituisce l'insieme dei libri corrispondenti
    */
   public Set<Libro> ricercaAutore(String autore){   
     Set<Libro> risultato = new TreeSet<>();

    for (Libro l : libri) {
        if (l.getAutore().equalsIgnoreCase(autore)) {
            risultato.add(l);
        }
    }

    return risultato;
   }
    /**
    * @brief Restituisce l'intero insieme dei libri
    * @return Set ordinato dei libri
    */
   public Set<Libro> getLibri(){ 
        return libri;
   }
  
}

