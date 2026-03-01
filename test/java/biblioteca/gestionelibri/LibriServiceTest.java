package biblioteca.gestionelibri;

import biblioteca.gestioneeccezioni.*;
import biblioteca.gestioneprestiti.ArchivioPrestitiAttivi;
import biblioteca.gestioneprestiti.Prestito;
import biblioteca.gestioneutenti.ArchivioUtenti;
import java.util.Set;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import biblioteca.gestioneprestiti.StatoPrestiti;


/**
 * Test unitari per la classe LibriService
 */
public class LibriServiceTest {

    private ArchivioLibri archivioLibri;
    private ArchivioPrestitiAttivi archivioPrestiti;
    private LibriService service;
    private Libro libroValido;

    @BeforeEach
    public void setUp() {

        archivioLibri = new ArchivioLibri("test_libri.csv");
        archivioLibri.getLibri().clear();

        ArchivioUtenti archivioUtenti = new ArchivioUtenti("test_utenti.csv");
        archivioUtenti.getUtenti().clear();

        archivioPrestiti = new ArchivioPrestitiAttivi(
                "test_prestiti_attivi.csv",
                archivioUtenti,
                archivioLibri
        );
        archivioPrestiti.getPrestitiAttivi().clear();

        service = new LibriService(archivioLibri, archivioPrestiti);

        libroValido = new Libro(
                "Il nome della rosa",
                "Umberto Eco",
                1980,
                "1234567890",
                3
        );
    }

    @AfterEach
    public void tearDown() {
        archivioLibri.getLibri().clear();
        archivioPrestiti.getPrestitiAttivi().clear();
    }

    // REGISTRA LIBRO

    @Test
    public void testRegistraLibroValido() throws Exception {
        service.registraLibro(libroValido);

        assertEquals(1, archivioLibri.getLibri().size());
        assertNotNull(archivioLibri.ricercaISBN("1234567890"));
    }

    @Test
    public void testRegistraLibroTitoloNonValido() {
        Libro l = new Libro("", "Eco", 1980, "1234567890", 2);

        assertThrows(ValidazioneException.class, () ->
                service.registraLibro(l)
        );
    }

    @Test
    public void testRegistraLibroAnnoFuturo() {
        Libro l = new Libro(
                "Titolo",
                "Autore",
                3000,
                "1234567890",
                1
        );

        assertThrows(ValidazioneException.class, () ->
                service.registraLibro(l)
        );
    }

    @Test
    public void testRegistraLibroISBNDuplicato() throws Exception {
        service.registraLibro(libroValido);

        Libro duplicato = new Libro(
                "Altro libro",
                "Altro autore",
                1990,
                "1234567890",
                2
        );

        assertThrows(DuplicatoException.class, () ->
                service.registraLibro(duplicato)
        );
    }

    // ELIMINA LIBRO

    @Test
    public void testEliminaLibroSenzaPrestiti() throws Exception {
        service.registraLibro(libroValido);

        Libro eliminato = service.eliminaLibro(libroValido);

        assertEquals(libroValido, eliminato);
        assertTrue(archivioLibri.getLibri().isEmpty());
    }

    @Test
    public void testEliminaLibroConPrestitoAttivo() throws Exception {
        service.registraLibro(libroValido);

        Prestito prestito = new Prestito(
                1,
                null, // utente non rilevante per questo test
                libroValido,
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                StatoPrestiti.ATTIVO
        );

        archivioPrestiti.aggiungiPrestitoAttivo(prestito);

        assertThrows(CancellazionePrestitoAttivoException.class, () ->
                service.eliminaLibro(libroValido)
        );
    }

    // RICERCA

    @Test
    public void testRicercaLibroPerISBN() throws Exception {
        service.registraLibro(libroValido);

        Set<Libro> risultati = service.ricercaLibro("", "", "1234567890");

        assertEquals(1, risultati.size());
        assertTrue(risultati.contains(libroValido));
    }

    @Test
    public void testRicercaLibroPerTitolo() throws Exception {
        service.registraLibro(libroValido);

        Set<Libro> risultati = service.ricercaLibro("Il nome della rosa", "", "");

        assertEquals(1, risultati.size());
    }

    @Test
    public void testRicercaLibroCampiVuoti() {
        assertThrows(ValidazioneException.class, () ->
                service.ricercaLibro("", "", "")
        );
    }

    @Test
    public void testRicercaLibroNonTrovato() throws Exception {
        service.registraLibro(libroValido);

        assertThrows(LibroNonTrovatoException.class, () ->
                service.ricercaLibro("", "", "9999999999")
        );
    }

    // MODIFICA LIBRO

    @Test
    public void testModificaLibroValida() throws Exception {
        service.registraLibro(libroValido);

        libroValido.setCopieDisponibili(5);

        service.modificaLibro(libroValido, "1234567890");

        assertEquals(
                5,
                archivioLibri.ricercaISBN("1234567890").getCopieDisponibili()
        );
    }

    @Test
    public void testModificaLibroISBNDuplicato() throws Exception {
        service.registraLibro(libroValido);

        Libro altro = new Libro(
                "Altro libro",
                "Altro autore",
                2000,
                "0987654321",
                1
        );
        service.registraLibro(altro);

        altro.setISBN("1234567890");

        assertThrows(DuplicatoException.class, () ->
                service.modificaLibro(altro, "0987654321")
        );
    }

    // VISUALIZZA

    @Test
    public void testVisualizzaLibri() throws Exception {
        service.registraLibro(libroValido);

        Set<Libro> libri = service.visualizzaLibri();

        assertEquals(1, libri.size());
        assertTrue(libri.contains(libroValido));
    }
}
