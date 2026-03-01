package biblioteca.gestioneprestiti;

import biblioteca.gestionelibri.*;
import biblioteca.gestioneutenti.*;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitari per ArchivioCronologiaPrestiti
 */
public class ArchivioCronologiaPrestitiTest {

    private ArchivioCronologiaPrestiti archivio;
    private ArchivioUtenti archivioUtenti;
    private ArchivioLibri archivioLibri;

    private Utente utente;
    private Libro libro;
    private Prestito prestito;

    @BeforeEach
    public void setUp() {
        archivioUtenti = new ArchivioUtenti("test_utenti.csv");
        archivioLibri = new ArchivioLibri("test_libri.csv");

        archivioUtenti.getUtenti().clear();
        archivioLibri.getLibri().clear();

        archivio = new ArchivioCronologiaPrestiti(
                "test_cronologia.csv",
                archivioUtenti,
                archivioLibri
        );

        archivio.getCronologia().clear();

        utente = new Utente(
                "Mario",
                "Rossi",
                "1234567890",
                "mario@unisa.it"
        );

        libro = new Libro(
                "Il nome della rosa",
                "Umberto Eco",
                1980,
                "ISBN123456",
                2
        );

        archivioUtenti.aggiungiUtente(utente);
        archivioLibri.aggiungiLibro(libro);

        prestito = new Prestito(
                1,
                utente,
                libro,
                LocalDate.now().minusDays(10),
                LocalDate.now().minusDays(3),
                StatoPrestiti.CHIUSO
        );
    }

    // AGGIUNTA

    @Test
    public void testAggiungiPrestitoCronologia() {
        archivio.aggiungiPrestitoCronologia(prestito);

        assertEquals(1, archivio.getCronologia().size());
        assertTrue(archivio.getCronologia().contains(prestito));
    }
    // RICERCA

    @Test
    public void testRicercaPrestitoUtenteCronologia() {
        archivio.aggiungiPrestitoCronologia(prestito);

        List<Prestito> risultati =
                archivio.ricercaPrestitoUtenteCronologia(utente);

        assertEquals(1, risultati.size());
        assertEquals(prestito, risultati.get(0));
    }

    @Test
    public void testRicercaPrestitoLibroCronologia() {
        archivio.aggiungiPrestitoCronologia(prestito);

        List<Prestito> risultati =
                archivio.ricercaPrestitoLibroCronologia(libro);

        assertEquals(1, risultati.size());
        assertEquals(prestito, risultati.get(0));
    }

    @Test
    public void testRicercaPrestitoUtenteLibroCronologia() {
        archivio.aggiungiPrestitoCronologia(prestito);

        Prestito trovato =
                archivio.ricercaPrestitoUtenteLibro(utente, libro);

        assertNotNull(trovato);
        assertEquals(prestito, trovato);
    }

    @Test
    public void testRicercaPrestitoUtenteLibroNonTrovato() {
        Prestito trovato =
                archivio.ricercaPrestitoUtenteLibro(utente, libro);

        assertNull(trovato);
    }

    // VISUALIZZA

    @Test
    public void testGetCronologia() {
        archivio.aggiungiPrestitoCronologia(prestito);

        List<Prestito> cronologia = archivio.getCronologia();

        assertEquals(1, cronologia.size());
    }
}
