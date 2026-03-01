package biblioteca.gestionelibri;

/**
 * @class Libro
 * @brief Rappresenta un libro del sistema bibliotecario
 * 
 * Questa classe contiene i dati del libro
 * Tutti i controlli sono stati affidati alla classe {@link LibriService}
 * 
 */
public class Libro implements Comparable<Libro>{
    
    /** Titolo del libro */
    private String titolo;
    /** Autore del libro */
    private String autore;
    /** Anno di pubblicazione del libro */
    private int annoPubblicazione;
    /** ISBN del libro */
    private String ISBN;      
    /** Copie Disponibili del libro */    
    private int copieDisponibili; 

    /** 
     * @brief Costruttore della classe Libro
     * @param titolo Titolo del libro
     * @param autore Autore del libro
     * @param annoPubblicazione Anno di pubblicazione del libro
     * @param ISBN ISBN del libro
     * @param copieDisponibili Numero di copie disponibili
     * 
     * @pre {@link LibriService} deve garantire tutti i controlli sui dati
     * 
     * @pre I dati passati devono essere validi (controllo delegato a LibriService)
     * @post Il libro è inizializzato
     * 
     */
    public Libro(String titolo, String autore, int annoPubblicazione, String ISBN, int copieDisponibili) {
         this.titolo=titolo;
         this.autore=autore;
         this.annoPubblicazione=annoPubblicazione;
         this.ISBN=ISBN;
         this.copieDisponibili=copieDisponibili;
    }
        public Libro(String titolo, String autore, int annoPubblicazione, String ISBN) {
         this.titolo=titolo;
         this.autore=autore;
         this.annoPubblicazione=annoPubblicazione;
         this.ISBN=ISBN;
    }

     /**
     * @return Il titolo del libro
     */
    public String getTitolo() {
        return titolo;  
    }
         /**
     * @return L'autore del libro
     */
    public String getAutore() {
        return autore;
    }
         /**
     * @return L'anno di pubblicazione del libro
     */
    public int getAnnoPubblicazione() {
        return annoPubblicazione;
    }
     /**
     * @return ISBN del libro
     */
    public String getISBN() {
        return ISBN;
    }
     /**
     * @return Le copie disponibili
     */
    public int getCopieDisponibili() {
        return copieDisponibili;
    }
     /**
     *@brief Imposta un nuovo titolo per il libro
     * 
     * @param titolo Nuovo titolo.
     * @pre titolo != null && !titolo.isEmpty()
     * @post Il titolo viene aggiornato
     */
    public void setTitolo(String titolo) {
        this.titolo=titolo;
    }
        /**
     *@brief Imposta un nuovo autore per il libro
     * 
     * @param autore Nuovo autore.
     * @pre autore != null && !autore.isEmpty()
     * @post L'autore viene aggiornato
     */
    public void setAutore(String autore) {
        this.autore=autore;
    }
        /**
     *@brief Imposta un nuovo anno di pubblicazione per il libro
     * 
     * @param annoPubblicazione Nuovo anno di pubblicazione.
     * @pre annoPubblicazione > 0 
     * @post L'anno di pubblicazione viene aggiornato
     */
    public void setAnnoPubblicazione(int annoPubblicazione) {
        this.annoPubblicazione=annoPubblicazione;
    }
     /**
     *@brief Imposta un nuovo ISBN per il libro
     * 
     * @param ISBN Nuovo ISBN.
     * @pre ISBN != null && !ISBN.isEmpty()
     * @post L'ISBN viene aggiornato
     */
    public void setISBN(String ISBN) {
        this.ISBN=ISBN;
    }
        /**
     *@brief Imposta un nuovo numero di copie disponibili per il libro
     * 
     * @param copieDisponibili Nuovo numero di copie disponibili.
     * @pre copieDisponibili >= 0 
     * @post Il numero di copie disponibili viene aggiornato
     */
    public void setCopieDisponibili(int copieDisponibili) {
        this.copieDisponibili = copieDisponibili;
    }
     /**
     *@return Una rappresentazione leggibile del libro
     */
    @Override 
    public String toString(){
        StringBuffer sb= new StringBuffer();
        sb.append("---->Libro \n").append("Titolo: ").append(titolo).append("Autore: ").append(autore)
                .append("Anno di pubblicazione: ").append(annoPubblicazione).append("ISBN: ").append(ISBN)
                .append("Copie disponibili: ").append(copieDisponibili);

        return sb.toString();
    }
    
     /**
     * @brief Due libri sono uguali se hanno lo stesso ISBN
     */
    @Override
    public boolean equals(Object o){ 
        if(o == null) return false;
        if(this==o) return true;
        if(this.getClass() != o.getClass()) return false;
        
        Libro l= (Libro)o;
        return l.ISBN.equals(this.ISBN);
    }
     /**
     * @brief hash coerente con equals(basato sull'ISBN)
     */
    @Override
    public int hashCode() {
        int code= ISBN == null ? 0 : ISBN.hashCode();
        return code;
    }
     /**
     * @brief Ordina i libri per titolo
     * @param o altro libro
     */
    @Override
    public int compareTo(Libro l){
        int cmp = this.titolo.compareTo(l.titolo);
        if (cmp != 0) return cmp;
        return this.ISBN.compareTo(l.ISBN);
    }

}