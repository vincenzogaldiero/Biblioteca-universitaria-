package biblioteca.gestioneutenti;

import biblioteca.Archivio;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.TreeSet;
import java.util.Set;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * @class ArchivioUtenti
 * @brief Gestisce la collezione degli utenti della biblioteca.
 *
 * Questa classe si occupa esclusivamente della gestione dei dati degli utenti:
 * - memorizzazione degli utenti tramite {@link TreeSet} per garantire ordinamento naturale
 *   secondo il confronto definito in {@link Utente};
 * - operazioni di aggiunta, rimozione, ricerca, modifica e visualizzazione;
 * - salvataggio e caricamento degli utenti da file.
 *
 * Tutti i controlli e le validazioni sono delegati alla classe {@link UtentiService}.
 */
    public class ArchivioUtenti implements Archivio{
        /** Insieme ordinato degli utenti */
       private Set<Utente> utenti;
    /**
     * @brief Costruttore: inizializza un archivio vuoto e lo popola da file.
     *
     * @param filename Nome del file da cui leggere gli utenti.
     *
     * @post L'insieme {@link #utenti} è inizializzato come nuovo {@link TreeSet}.
     * @post Se il file esiste e può essere letto, {@link #utenti} contiene gli utenti letti dal file.
     */
    public ArchivioUtenti(String filename) {
        this.utenti = new TreeSet<>();

        try {
            leggiDaFile(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/**
 * @brief Carica gli utenti dal file specificato.
 *
 * Il formato previsto per ogni riga (dopo l'intestazione) è:
 * nome;cognome;matricola;email
 *
 * La prima riga del file viene ignorata poiché contiene l'intestazione.
 *
 * @param filename Nome del file da cui leggere i dati.
 * @throws IOException Se si verificano errori di lettura.
 *
 * @pre filename != null && !filename.isEmpty()
 * @post L'archivio contiene tutti gli utenti presenti nel file.
 */
   @Override
    public void leggiDaFile(String filename) throws IOException{
       
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
            if (parti.length < 4) continue;

            String nome = parti[0];
            String cognome = parti[1];
            String matricola = (parti[2]);
            String email = parti[3];
            
            Utente u = new Utente(nome, cognome, matricola, email);
            utenti.add(u);
            
            }
       }
    
   }
/**
 * @brief Salva tutti gli utenti sul file specificato.
 *
 * Ogni libro viene scritto in una riga nel formato:
 * nome;cognome;email;matricola;
 *
 * La prima riga del file contiene l'intestazione con i nomi dei campi.
 *
 * @param filename Nome del file su cui salvare i dati.
 * @throws IOException Se si verificano errori di scrittura.
 *
 * @pre filename != null && !filename.isEmpty()
 * @post Il file contiene l'intestazione e tutti gli utenti memorizzati nell'archivio.
 */
   @Override
   public void scriviSuFile(String filename) throws IOException{
       try(PrintWriter pw = new PrintWriter( new BufferedWriter ( new FileWriter (filename)))){
           pw.println("NOME;COGNOME;MATRICOLA;EMAIL");
           for(Utente u: utenti){
               pw.println(
                       u.getNome() + ";" +
                       u.getCognome() + ";" +
                       u.getMatricola() + ";" + 
                       u.getEmail()
                    );
            }   
        }
   } 
   /**
    * @brief Aggiunge un nuovo utente all'archivio
    * Non effettua controlli, che vengono delegati a {@link UtentiService}.
    * 
    * @param u Utente da aggiungere
    * 
    * @pre u!=null 
    * @post utenti contiene l'utente u
    */
   public void aggiungiUtente(Utente u){
       utenti.add(u);
   }
   
   /**
    * @brief Rimuove un utente dall'archivio
    * 
    * @param u utente da rimuovere
    * @return L'utente che è stato rimosso oppure null se non presente
    * 
    * @pre u !=null
    * @pre l'utente non deve avere prestiti attivi(verifica delegata alla classe {@link UtentiService}).
    * @post se presente, l'utente viene rimosso dall'archivio.
    */
   public Utente rimuoviUtente(Utente u){
        boolean rimosso = utenti.remove(u);
        return rimosso ? u : null;
   }
   
   /**
    * @brief Cerca tutti gli utenti che hanno un determinato cognome.
    * 
    * @param cognome Cognome da cercare
    * @return Insieme degli utenti con quel cognome oppure un insieme vuoto se nessuno trovato.
    * 
    * @pre cognome!= null && !cognome.isEmpty()
    * @post restituisce l'insieme degli utenti corrispondenti.
    */
   public Set<Utente> ricercaCognome(String cognome){
        Set<Utente> risultato = new TreeSet<>();
   
        for(Utente u : utenti){
            if(u.getCognome().equals(cognome)){
                risultato.add(u);
            }
   }
   
   return risultato;
   }
      /**
    * @brief Cerca se esiste un utente con una determinata matricola.
    * 
    * @param matricola Matricola da cercare
    * @return Torna l'utente con la matricola data oppure null in caso di utente non trovato
    * 
    * @pre matricola >0 
    * @post restituisce l'utente corrispondente
    */
   public Utente ricercaMatricola(String matricola){
        for(Utente u : utenti){
            if(u.getMatricola().equals(matricola)){
                return u;
            }
        }
        return null;
   }
   
   /**
    * @brief Restituisce l'intero insieme degli utenti
    * @return Set ordinato degli utenti
    */
   public Set<Utente> getUtenti(){ 
       return utenti;
   }
}
