package biblioteca.gestioneprestiti;

import biblioteca.gestionelibri.*;
import biblioteca.gestioneutenti.*;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitari per ArchivioPrestitiAttivi
 */
public class ArchivioPrestitiAttiviTest {

    private ArchivioPrestitiAttivi archivio;
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

        archivio = new ArchivioPrestitiAttivi(
                "test_prestiti.csv",
                archivioUtenti,
                archivioLibri
        );

        archivio.getPrestitiAttivi().clear();

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
                3
        );

        archivioUtenti.aggiungiUtente(utente);
        archivioLibri.aggiungiLibro(libro);

        prestito = new Prestito(
                1,
                utente,
                libro,
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                StatoPrestiti.ATTIVO
        );
    }

    // AGGIUNTA / RIMOZIONE

    @Test
    public void testAggiungiPrestitoAttivo() {
        archivio.aggiungiPrestitoAttivo(prestito);

        assertEquals(1, archivio.getPrestitiAttivi().size());
        assertTrue(archivio.getPrestitiAttivi().contains(prestito));
    }

    @Test
    public void testRimuoviPrestitoAttivoPresente() {
        archivio.aggiungiPrestitoAttivo(prestito);

        Prestito rimosso = archivio.rimuoviPrestitoAttivo(prestito);

        assertEquals(prestito, rimosso);
        assertTrue(archivio.getPrestitiAttivi().isEmpty());
    }

    @Test
    public void testRimuoviPrestitoAttivoNonPresente() {
        Prestito rimosso = archivio.rimuoviPrestitoAttivo(prestito);

        assertNull(rimosso);
    }

    // RICERCA

    @Test
    public void testRicercaPrestitoAttivoUtenteLibro() {
        archivio.aggiungiPrestitoAttivo(prestito);

        Prestito trovato =
                archivio.ricercaPrestitoAttivo(utente, libro);

        assertNotNull(trovato);
        assertEquals(prestito, trovato);
    }

    @Test
    public void testRicercaPrestitoAttivoUtente() {
        archivio.aggiungiPrestitoAttivo(prestito);

        List<Prestito> risultati =
                archivio.ricercaPrestitoAttivoUtente(utente);

        assertEquals(1, risultati.size());
    }

    @Test
    public void testRicercaPrestitoAttivoLibro() {
        archivio.aggiungiPrestitoAttivo(prestito);

        List<Prestito> risultati =
                archivio.ricercaPrestitoAttivoLibro(libro);

        assertEquals(1, risultati.size());
    }

    // CONTEGGI E CONTROLLI

    @Test
    public void testContaPrestitiAttiviUtente() {
        archivio.aggiungiPrestitoAttivo(prestito);

        int count = archivio.contaPrestitiAttiviUtente(utente);

        assertEquals(1, count);
    }

    @Test
    public void testEsistePrestitoAttivoLibro() {
        archivio.aggiungiPrestitoAttivo(prestito);

        assertTrue(archivio.esistePrestitoAttivoLibro(libro));
    }

    @Test
    public void testNonEsistePrestitoAttivoLibro() {
        assertFalse(archivio.esistePrestitoAttivoLibro(libro));
    }
    
    //VISUALIZZAZIONE
    
    @Test
    public void testVisualizzaPrestitiAttiviConUnPrestito() {
    archivio.aggiungiPrestitoAttivo(prestito);

    List<Prestito> lista = archivio.getPrestitiAttivi();

    assertNotNull(lista);
    assertEquals(1, lista.size());
    assertEquals(prestito, lista.get(0));
    }

    @Test
    public void testVisualizzaPrestitiAttiviOrdineInserimento() {
        Prestito prestito2 = new Prestito(
                2,
                utente,
                libro,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(8),
                StatoPrestiti.ATTIVO
        );

        archivio.aggiungiPrestitoAttivo(prestito);
        archivio.aggiungiPrestitoAttivo(prestito2);

        List<Prestito> lista = archivio.getPrestitiAttivi();

        assertEquals(2, lista.size());
        assertEquals(prestito, lista.get(0));
        assertEquals(prestito2, lista.get(1));
    }

    @Test
    public void testVisualizzaPrestitiAttiviVuoto() {
        List<Prestito> lista = archivio.getPrestitiAttivi();

        assertNotNull(lista);
        assertTrue(lista.isEmpty());
    }

    // ID

    @Test
    public void testGeneraNuovoIdIncrementale() {
        int id1 = archivio.generaNuovoId();
        int id2 = archivio.generaNuovoId();

        assertEquals(id1 + 1, id2);
    }
}
