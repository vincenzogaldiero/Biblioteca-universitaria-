package biblioteca.gestioneutenti;

import biblioteca.gestioneeccezioni.DuplicatoException;
import biblioteca.gestioneeccezioni.ValidazioneException;
import biblioteca.gestioneeccezioni.UtenteNonTrovatoException;
import biblioteca.gestioneeccezioni.CancellazionePrestitoAttivoException;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

/**
 * @class UtentiService
 * @brief Service che esegue la validazione dei dati degli utenti.
 *
 * Effettua i controlli su:
 * - nome e cognome non vuoti
 * - email non vuota e con formato valido
 * - matricola non vuota, univoca e con formato valido
 * - massimo 3 prestiti attivi
 *
 * Le operazioni vengono delegate a ArchivioUtenti.
 */

public class UtentiService {
    /**
     * Archivio degli utenti su cui operare
     */
    private ArchivioUtenti archivioUtenti;
    /**
     * @brief Costruttore del Service 
     * @param archivioUtenti archivio degli utenti da usare come dati
     * 
     * @pre archivioUtenti != null
     * @post this.archivioUtenti ==archivioUtenti
     */
    public UtentiService(ArchivioUtenti archivioUtenti){
        this.archivioUtenti = archivioUtenti;
    }
/**
     * @brief Aggiunge un nuovo utente all'archivio dopo aver effettuato tutti i controlli di validità.
     *
     * Verifica che tutti i campi dell'utente siano corretti:
     * - nome e cognome non vuoti,
     * - matricola composta da 10 cifre numeriche,
     * - email valida e istituzionale (termina con {@code unisa.it}),
     * - nessun duplicato di matricola già presente nell'archivio.
     * 
     * Se tutti i controlli sono superati, l'utente viene aggiunto
     * all'archivio e l'archivio viene salvato su file.
     *
     * @param u Utente da registrare.
     *
     * @throws ValidazioneException Se uno o più campi dell'utente non sono validi.
     * @throws DuplicatoException Se esiste già un utente con la stessa matricola.
     *
     * @pre {@code u != null}
     * @post L'utente {@code u} è stato aggiunto all'archivio
     *       e l'archivio aggiornato è stato salvato su file {@code utenti.csv}.
     */
    public void registraUtente(Utente u) throws ValidazioneException, DuplicatoException{
    if(u.getNome() == null || u.getNome().trim().isEmpty()){
        throw new ValidazioneException("Nome non valido");
    }
    if(u.getCognome() == null || u.getCognome().trim().isEmpty()){
        throw new ValidazioneException("cognome non valido");
    }
    String matricola = u.getMatricola();

    if (matricola == null || matricola.isEmpty()) {
        throw new ValidazioneException("La matricola non può essere vuota");
    }

    // Controllo che ci siano solo numeri
    if (!matricola.matches("\\d+")) {
        throw new ValidazioneException("La matricola deve contenere solo cifre");
    }

    // Controllo la lunghezza esatta
    if (matricola.length() != 10) {
        throw new ValidazioneException("La matricola deve essere composta da 10 cifre");
    }
    if(u.getEmail() == null || u.getEmail().trim().isEmpty()){
        throw new ValidazioneException("email non valida");
    }
    if(!u.getEmail().endsWith("unisa.it")){
        throw new ValidazioneException("l'email deve essere istituzionale");
    }
    
    //Controllo duplicato
    Utente u1 = archivioUtenti.ricercaMatricola(u.getMatricola());
    if(u1 != null){
        throw new DuplicatoException("Esiste già un utente con questa matricola");
    }
    
    //Aggiunta all'archivio
    archivioUtenti.aggiungiUtente(u);
        try {
         archivioUtenti.scriviSuFile("utenti.csv");
        } catch(IOException e) {
         e.printStackTrace();
        }
    }
    /**
     * @brief Elimina un utente esistente dall'archivio.
     *
     * Controlla che l'utente non abbia prestiti attivi prima di rimuoverlo.
     * Se ci sono prestiti attivi, viene sollevata un'eccezione.
     * Dopo la rimozione, l'archivio aggiornato viene salvato su file.
     *
     * @param u Utente da eliminare.
     * @return L'utente eliminato dall'archivio, oppure {@code null} se non presente.
     *
     * @throws CancellazionePrestitoAttivoException Se l'utente ha prestiti attivi.
     *
     * @pre {@code u != null}
     * @pre L'utente non deve avere prestiti attivi.
     * @post Se non vengono sollevate eccezioni, l'utente {@code u} è rimosso dall'archivio
     *       e l'archivio aggiornato è salvato su file {@code utenti.csv}.
     */
    public Utente eliminaUtente(Utente u) throws CancellazionePrestitoAttivoException{
    if(u.getPrestitiAttivi()!= null && !u.getPrestitiAttivi().isEmpty()){
        throw new CancellazionePrestitoAttivoException("impossibile eliminare l'utente, ha ancora" + u.getPrestitiAttivi().size() + "prestiti attivi");
    }
    
    //Rimozione dall'archivio
        try {
         archivioUtenti.scriviSuFile("utenti.csv");
        } catch(IOException e) {
         e.printStackTrace();
        }
    return archivioUtenti.rimuoviUtente(u);
    }

    /**
     * @brief Esegue una ricerca di utenti in base a cognome o matricola.
     *
     * La ricerca segue la seguente priorità:
     * - Se è stata inserita la matricola, viene effettuata la ricerca per matricola.
     * - Altrimenti, se è stato inserito il cognome, viene effettuata la ricerca per cognome.
     *
     * @param cognome Cognome da cercare (può essere vuoto).
     * @param matricola Matricola da cercare (può essere vuota).
     *
     * @return L'insieme degli utenti trovati. Può contenere un solo utente
     *         se la ricerca è stata effettuata per matricola.
     *
     * @throws ValidazioneException Se tutti i campi sono vuoti o non validi.
     * @throws UtenteNonTrovatoException Se nessun utente corrisponde ai criteri.
     *
     * @pre Almeno uno tra cognome o matricola deve essere compilato.
     * @post Restituisce l'insieme degli utenti trovati secondo il criterio di ricerca applicato.
     */
    public Set<Utente> ricercaUtente(String cognome, String matricola) throws ValidazioneException, UtenteNonTrovatoException{
     cognome = cognome == null ? "" : cognome.trim();
     matricola = matricola == null ? "" : matricola.trim();
     
      if (cognome.isEmpty() && matricola.isEmpty()) {
        throw new ValidazioneException("Inserire almeno un campo per la ricerca.");
    }
     
     //PRIORITÀ 1: Matricola
     if(!matricola.isEmpty()){
         Utente trovato = archivioUtenti.ricercaMatricola(matricola);
         if(trovato == null){
             throw new UtenteNonTrovatoException("Nessun utente con questa matricola.");
         }
         return Collections.singleton(trovato);
     }
     
     // PRIORITÀ 2: Cognome
     Set<Utente> trovati = archivioUtenti.ricercaCognome(cognome);
     if (trovati == null || trovati.isEmpty()){
         throw new UtenteNonTrovatoException("nessun utente trovato con questo cognome.");
     }
     
     return trovati;
    }
    /**
     * @brief Modifica un utente già esistente nell'archivio.
     *
     * Applica le modifiche presenti nell'oggetto {@code utente} passato
     * come parametro, dopo aver effettuato tutti i controlli di validità:
     * - nome e cognome non vuoti,
     * - email valida e istituzionale (termina con {@code @unisa.it}),
     * - matricola composta da 10 cifre,
     * - nessun duplicato di matricola già presente nell'archivio
     *   (controllo effettuato solo se la matricola è stata modificata).
     *
     * Dopo la validazione, l'utente modificato viene salvato nell'archivio
     * e l'archivio viene scritto su file.
     *
     * @param utente L'utente già modificato (tramite tabella) da validare e salvare.
     * @param matricolaOriginale La matricola originale dell'utente prima della modifica.
     *
     * @throws ValidazioneException Se uno o più campi dell'utente non sono validi.
     * @throws DuplicatoException Se un altro utente con la stessa matricola esiste già.
     *
     * @pre {@code utente != null}
     * @post Le modifiche dell'utente vengono salvate nell'archivio se valide
     *       e l'archivio aggiornato viene scritto su file {@code utenti.csv}.
     post Le modificge vengono salvate nell'archivio, se valide.
     */  
public void modificaUtente(Utente utente, String matricolaOriginale) 
        throws ValidazioneException, DuplicatoException {

    if (utente == null) {
        throw new ValidazioneException("L'utente non può essere nullo.");
    }

    // Validazioni dei campi
    if (utente.getNome() == null || utente.getNome().trim().isEmpty())
        throw new ValidazioneException("Il nome non può essere vuoto");

    if (utente.getCognome() == null || utente.getCognome().trim().isEmpty())
        throw new ValidazioneException("Il cognome non può essere vuoto");

    if (utente.getEmail() == null || utente.getEmail().trim().isEmpty())
        throw new ValidazioneException("L'email non può essere vuota");

    if (!utente.getEmail().endsWith("unisa.it"))
        throw new ValidazioneException("L'email deve terminare con @unisa.it");

    String matricola = utente.getMatricola();
    if (matricola == null || !matricola.matches("\\d{10}"))
        throw new ValidazioneException("La matricola deve essere composta da 10 cifre");

    // Controllo duplicato SOLO se la matricola è stata modificata
    if (!matricola.equals(matricolaOriginale)) {
        for (Utente u : archivioUtenti.getUtenti()) {
            if (u != utente && u.getMatricola().equals(matricola)) {
                throw new DuplicatoException("Esiste già un utente con questa matricola");
            }
        }
    }

    // Nessuna rimozione, l'oggetto nella JTable è già aggiornato

    // Salva su file
    try {
        archivioUtenti.scriviSuFile("utenti.csv");
    } catch (IOException e) {
        e.printStackTrace();
    }
}




    
    /**
     * @brief Restituisce l'insieme completo degli utenti
     * @return Set ordinato degli utenti
     * 
     * @post restituisce una copia dell'archivio
     */  
   public Set<Utente> visualizzaUtenti(){ 
        return archivioUtenti.getUtenti();
   }
    
    
}
