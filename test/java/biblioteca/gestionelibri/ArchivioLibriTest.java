package biblioteca.gestionelibri;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitari per la classe ArchivioLibri
 */
public class ArchivioLibriTest {

    private ArchivioLibri archivio;
    private Libro l1;
    private Libro l2;

    @BeforeEach
    public void setUp() {
        archivio = new ArchivioLibri("test_libri.csv");
        archivio.getLibri().clear();

        l1 = new Libro(
                "Il nome della rosa",
                "Umberto Eco",
                1980,
                "9788845245497",
                3
        );

        l2 = new Libro(
                "I promessi sposi",
                "Alessandro Manzoni",
                1827,
                "9788807900335",
                5
        );
    }

    @AfterEach
    public void tearDown() {
        archivio.getLibri().clear();

        File f = new File("test_libri.csv");
        if (f.exists()) {
            f.delete();
        }
    }

    // STATO INIZIALE

    @Test
    public void testArchivioInizialmenteVuoto() {
        assertNotNull(archivio.getLibri());
        assertTrue(archivio.getLibri().isEmpty());
    }

    // AGGIUNTA

    @Test
    public void testAggiungiLibro() {
        archivio.aggiungiLibro(l1);

        assertEquals(1, archivio.getLibri().size());
        assertTrue(archivio.getLibri().contains(l1));
    }

    @Test
    public void testAggiungiPiuLibri() {
        archivio.aggiungiLibro(l1);
        archivio.aggiungiLibro(l2);

        assertEquals(2, archivio.getLibri().size());
    }

    // RIMOZIONE
    @Test
    public void testRimuoviLibroPresente() {
        archivio.aggiungiLibro(l1);

        Libro rimosso = archivio.rimuoviLibro(l1);

        assertEquals(l1, rimosso);
        assertTrue(archivio.getLibri().isEmpty());
    }

    @Test
    public void testRimuoviLibroNonPresente() {
        Libro rimosso = archivio.rimuoviLibro(l1);
        assertNull(rimosso);
    }

    // RICERCA ISBN

    @Test
    public void testRicercaISBNPresente() {
        archivio.aggiungiLibro(l1);
        archivio.aggiungiLibro(l2);

        Libro trovato = archivio.ricercaISBN("9788845245497");

        assertNotNull(trovato);
        assertEquals(l1, trovato);
    }

    @Test
    public void testRicercaISBNInesistente() {
        Libro trovato = archivio.ricercaISBN("0000000000000");
        assertNull(trovato);
    }

    // RICERCA TITOLO

    @Test
    public void testRicercaTitolo() {
        archivio.aggiungiLibro(l1);
        archivio.aggiungiLibro(l2);

        Set<Libro> risultati = archivio.ricercaTitolo("Il nome della rosa");

        assertEquals(1, risultati.size());
        assertTrue(risultati.contains(l1));
    }

    @Test
    public void testRicercaTitoloInesistente() {
        archivio.aggiungiLibro(l1);

        Set<Libro> risultati = archivio.ricercaTitolo("Titolo inesistente");

        assertNotNull(risultati);
        assertTrue(risultati.isEmpty());
    }

    // RICERCA AUTORE

    @Test
    public void testRicercaAutore() {
        archivio.aggiungiLibro(l1);
        archivio.aggiungiLibro(l2);

        Set<Libro> risultati = archivio.ricercaAutore("Umberto Eco");

        assertEquals(1, risultati.size());
        assertTrue(risultati.contains(l1));
    }

    @Test
    public void testRicercaAutoreInesistente() {
        archivio.aggiungiLibro(l1);

        Set<Libro> risultati = archivio.ricercaAutore("Autore sconosciuto");

        assertNotNull(risultati);
        assertTrue(risultati.isEmpty());
    }

    // GET LIBRI

    @Test
    public void testGetLibri() {
        archivio.aggiungiLibro(l1);
        archivio.aggiungiLibro(l2);

        Set<Libro> libri = archivio.getLibri();

        assertEquals(2, libri.size());
        assertTrue(libri.contains(l1));
        assertTrue(libri.contains(l2));
    }

    // FILE I/O

    @Test
    public void testScriviELeggiDaFile() throws IOException {
        archivio.aggiungiLibro(l1);
        archivio.aggiungiLibro(l2);

        archivio.scriviSuFile("test_libri.csv");

        ArchivioLibri nuovoArchivio = new ArchivioLibri("test_libri.csv");

        assertEquals(2, nuovoArchivio.getLibri().size());
        assertNotNull(nuovoArchivio.ricercaISBN("9788845245497"));
        assertNotNull(nuovoArchivio.ricercaISBN("9788807900335"));
    }
}
