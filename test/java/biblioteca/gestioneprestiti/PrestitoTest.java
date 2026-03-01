package biblioteca.gestioneprestiti;

import biblioteca.gestioneutenti.Utente;
import biblioteca.gestionelibri.Libro;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitari per la classe Prestito
 */
public class PrestitoTest {

    private Utente utente;
    private Libro libro;
    private Prestito prestito;
    private LocalDate inizio;
    private LocalDate fine;

    @BeforeEach
    public void setUp() {
        utente = new Utente(
                "Mario",
                "Rossi",
                "1234567890",
                "mario.rossi@unisa.it"
        );

        libro = new Libro(
                "Il nome della rosa",
                "Umberto Eco",
                1980,
                "1234567890123",
                2
        );

        inizio = LocalDate.now();
        fine = inizio.plusDays(7);

        prestito = new Prestito(
                1,
                utente,
                libro,
                inizio,
                fine,
                StatoPrestiti.ATTIVO
        );
    }

    //COSTRUTTORE 

    @Test
    public void testCostruttore() {
        assertEquals(1, prestito.getId());
        assertEquals(utente, prestito.getUtente());
        assertEquals(libro, prestito.getLibro());
        assertEquals(inizio, prestito.getDataInizio());
        assertEquals(fine, prestito.getDataFine());
        assertEquals(StatoPrestiti.ATTIVO, prestito.getStato());
    }

    //  GETTER E SETTER 

    @Test
    public void testSetUtente() {
        Utente nuovo = new Utente("Anna", "Bianchi", "0987654321", "anna@unisa.it");
        prestito.setUtente(nuovo);
        assertEquals(nuovo, prestito.getUtente());
    }

    @Test
    public void testSetLibro() {
        Libro nuovo = new Libro("1984", "George Orwell", 1949, "1111111111", 1);
        prestito.setLibro(nuovo);
        assertEquals(nuovo, prestito.getLibro());
    }

    @Test
    public void testSetDate() {
        LocalDate nuovaFine = LocalDate.now().plusDays(14);
        prestito.setDataFine(nuovaFine);
        assertEquals(nuovaFine, prestito.getDataFine());
    }

    @Test
    public void testSetStato() {
        prestito.setStato(StatoPrestiti.CHIUSO);
        assertEquals(StatoPrestiti.CHIUSO, prestito.getStato());
    }

    // METODI DI SUPPORTO

    @Test
    public void testGetDatiLibro() {
        assertEquals("Il nome della rosa", prestito.getTitolo());
        assertEquals("Umberto Eco", prestito.getAutori());
        assertEquals("1234567890123", prestito.getISBN());
        assertEquals(1980, prestito.getAnno());
    }

    @Test
    public void testGetDatiUtente() {
        assertEquals("Mario", prestito.getNome());
        assertEquals("Rossi", prestito.getCognome());
        assertEquals("1234567890", prestito.getMatricola());
        assertEquals("mario.rossi@unisa.it", prestito.getEmail());
    }

    @Test
    public void testGetDatiLibroNull() {
        prestito.setLibro(null);

        assertEquals("", prestito.getTitolo());
        assertEquals("", prestito.getAutori());
        assertEquals("", prestito.getISBN());
        assertEquals(0, prestito.getAnno());
    }

    @Test
    public void testGetDatiUtenteNull() {
        prestito.setUtente(null);

        assertEquals("", prestito.getNome());
        assertEquals("", prestito.getCognome());
        assertEquals("", prestito.getMatricola());
        assertEquals("", prestito.getEmail());
    }
}
