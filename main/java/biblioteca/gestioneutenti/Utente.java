package biblioteca.gestioneutenti;

import biblioteca.gestioneprestiti.Prestito;
import java.util.List;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
/**
 * @class Utente
 * @brief Rappresenta un utente del sistema bibliotecario
 * 
 * Questa classe contiene i dati dell'utente 
 * e le operazioni per gestire la lista dei prestiti attivi.
 * Tutti i controlli sono stati affidati alla classe {@see UtenteService}
 * 
 */
public class Utente implements Comparable<Utente>{
    
    /** Nome dell'utente */
    private String nome;
    
    /** Cognome dell'utente */
    private String cognome;
    
    /** Matricola univoca dell'utente */
    private String matricola;
    
    /** Email dell'utente */
    private String email;
    
    /** Lista dei prestiti attivi dell'utente */
    private List<Prestito> prestitiAttivi;
    
    /** 
     * @brief Costruttore della classe Utente
     * @param nome Nome dell'utente
     * @param cognome Cognome dell'utente
     * @param matricola Matricola univoca dell'utente
     * @param email Email dell'utente
     * 
     * @pre Tutti i controlli sono stati affidati alla classe {@link UtentiService}.
     * @post L'utente è inizializzato con lista dei prestiti vuota
     * 
     */
    public Utente(String nome,String cognome, String matricola, String email){
        this.nome=nome;
        this.cognome=cognome;
        this.matricola=matricola;
        this.email=email;
        this.prestitiAttivi = new ArrayList<>(); 
    }

    /**
     * @return Il nome dell'utente
     */
    public String getNome() {
        return nome;
    }
    
    /**
     *@brief Imposta un nuovo nome per l'utente
     * 
     * @param nome Nuovo nome.
     * @pre nome != null && !nome.isEmpty()
     * @post Il nome viene aggiornato
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

     /**
     * @return Il cognome dell'utente
     */
    public String getCognome() {
        return cognome;
    }

     /**
     *@brief Imposta un nuovo cognome
     * 
     * @param cognome Nuovo cognome
     * @pre cognome !=null && !cognome.isEmpty() 
     * @post Il cognome viene aggiornato
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * @return La matricola dell'utente
     */
    public String getMatricola() {
        return matricola;
    }

    /**
     *@brief Imposta una nuova matricola
     * 
     * @param matricola Nuova matricola.
     * @pre matricola > 0; l’univocità è garantita da UtentiService
     * @post La matricola viene aggiornata
     */
    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

        /**
     * @return L'email dell'utente
     */
    public String getEmail() {
        return email;
    }

     /**
     *@brief Imposta una nuova email 
     * 
     * @param email Nuova email. 
     * @pre email!= null && !email.isEmpty()
     * @post L'email viene aggiornata
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * @brief Aggiunge un prestito 
     * @param p Prestito da aggiungere
     * 
     * @pre prestitiAttivi.size()<3
     * @post prestitiAttivi contiene p
     */
    public void aggiungiPrestitoAttivo(Prestito p){
        prestitiAttivi.add(p);
    }
    /**
     * @brief Rimuove un prestito attivo
     * @param p Prestito da rimuovere
     * 
     * @pre !prestitiAttivi.isEmpty()
     * @post prestitiAttivi non contiene più p
     */
    public void rimuoviPrestitoAttivo(Prestito p){
        prestitiAttivi.remove(p);

    }
    /**
     * @return lista dei prestiti attivi
     */
    public List<Prestito> getPrestitiAttivi() {
        return prestitiAttivi;
    }
    
     /**
     * @brief Restituisce i titoli dei libri presenti nei prestiti attivi.
     *
     * Se non ci sono prestiti attivi, restituisce la stringa {@code "Nessuno"}.
     * Altrimenti, concatena i titoli separati da una virgola e uno spazio.
     *
     * @return Una stringa contenente i titoli dei libri in prestito attivo,
     *         oppure {@code "Nessuno"} se la lista è vuota o nulla.
     */
    public String getPrestitiTitolo(){
     if (prestitiAttivi == null || prestitiAttivi.isEmpty()) {
        return "Nessuno";
    }
    StringBuffer sb = new StringBuffer();
    for(Prestito p : prestitiAttivi){
        sb.append(p.getLibro().getTitolo()).append(", ");
    }
    // Rimuovi l’ultima virgola + spazio
    sb.setLength(sb.length() - 2);
    return sb.toString();
    }
    
     /**
     * @brief Restituisce le date di fine dei prestiti attivi.
     *
     * Se non ci sono prestiti attivi, restituisce la stringa {@code "Nessuna"}.
     * Altrimenti, concatena le date formattate come {@code "dd/MM/yyyy"},
     * separate da una virgola e uno spazio.
     *
     * @return Una stringa contenente le date di fine dei prestiti attivi,
     *         oppure {@code "Nessuna"} se la lista è vuota o nulla.
     */
    public String getPrestitiDataRest(){
        if (prestitiAttivi == null || prestitiAttivi.isEmpty()) {
        return "Nessuna";
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    StringBuilder sb = new StringBuilder();

    for (Prestito p : prestitiAttivi) {
        sb.append(p.getDataFine().format(formatter))
          .append(", ");
    }

    // Rimuove ", " finale
    sb.setLength(sb.length() - 2);

    return sb.toString();
    }
    
    /**
     *@return Una rappresentazione leggibile dell'utente
     */
    @Override
    public String toString(){
        StringBuffer sb= new StringBuffer();
        sb.append("---->Utente \n").append("Nome: ").append(nome).append("Cognome: ").append(cognome)
            .append("Matricola: ").append(matricola).append("Email: ").append(email)
                .append("Lista di libri presi in prestito: ");
        
        if (prestitiAttivi.size()==0)
            sb.append("Nessun libro in prestito.");
        
        for(Prestito p : prestitiAttivi){
            sb.append(p.getLibro().getTitolo());
        }
        return sb.toString();
    }
    
    /**
     * @brief Due utenti sono uguali se hanno la stessa matricola
     */
    @Override
    public boolean equals(Object o){
       
       if(o == null) return false; 
       if (this == o) return true;
       if (this.getClass() != o.getClass()) return false;
       Utente u = (Utente) o;
       return this.matricola.equals(u.matricola); 
        
    }
    /**
     * @brief hash coerente con equals(basato sulla matricola)
     */
    @Override
    public int hashCode(){
        int code= matricola == null ? 0 : matricola.hashCode();
        return code;
    }
    /**
     * @brief Ordina per cognome, a parità di cognome, per nome.
     * @param u1 altro utente
     */
@Override
public int compareTo(Utente u1) {

    int cmp = this.cognome.compareToIgnoreCase(u1.cognome);
    if (cmp != 0) return cmp;

    cmp = this.nome.compareToIgnoreCase(u1.nome);
    if (cmp != 0) return cmp;

    return this.matricola.compareTo(u1.matricola); // <-- risolve il problema
}
    
    
    
}
