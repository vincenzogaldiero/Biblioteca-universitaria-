package biblioteca.gestioneutenti;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitari per la classe ArchivioUtenti
 */
public class ArchivioUtentiTest {

    private ArchivioUtenti archivio;
    private Utente u1;
    private Utente u2;

    @BeforeEach
    public void setUp() {
        archivio = new ArchivioUtenti("test_utenti.csv");

        u1 = new Utente("Mario", "Rossi", "100", "mario@email.it");
        u2 = new Utente("Anna", "Bianchi", "200", "anna@email.it");

        archivio.getUtenti().clear();
    }

    @AfterEach
    public void tearDown() {
        archivio.getUtenti().clear();

        File f = new File("test_utenti.csv");
        if (f.exists()) {
            f.delete();
        }
    }

    // STATO INIZIALE

    @Test
    public void testArchivioInizialmenteVuoto() {
        assertNotNull(archivio.getUtenti());
        assertTrue(archivio.getUtenti().isEmpty());
    }

    // AGGIUNTA UTENTI
    
    @Test
    public void testAggiungiUtente() {
        archivio.aggiungiUtente(u1);

        assertEquals(1, archivio.getUtenti().size());
        assertTrue(archivio.getUtenti().contains(u1));
    }

    @Test
    public void testAggiungiPiuUtenti() {
        archivio.aggiungiUtente(u1);
        archivio.aggiungiUtente(u2);

        assertEquals(2, archivio.getUtenti().size());
    }

    // RIMOZIONE

    @Test
    public void testRimuoviUtentePresente() {
        archivio.aggiungiUtente(u1);

        Utente rimosso = archivio.rimuoviUtente(u1);

        assertEquals(u1, rimosso);
        assertTrue(archivio.getUtenti().isEmpty());
    }

    @Test
    public void testRimuoviUtenteNonPresente() {
        Utente rimosso = archivio.rimuoviUtente(u1);
        assertNull(rimosso);
    }

    // RICERCA

    @Test
    public void testRicercaMatricolaEsistente() {
        archivio.aggiungiUtente(u1);
        archivio.aggiungiUtente(u2);

        Utente trovato = archivio.ricercaMatricola("100");

        assertNotNull(trovato);
        assertEquals(u1, trovato);
    }

    @Test
    public void testRicercaMatricolaInesistente() {
        Utente trovato = archivio.ricercaMatricola("999");
        assertNull(trovato);
    }

    @Test
    public void testRicercaCognome() {
        archivio.aggiungiUtente(u1);
        archivio.aggiungiUtente(u2);

        Set<Utente> risultati = archivio.ricercaCognome("Rossi");

        assertEquals(1, risultati.size());
        assertTrue(risultati.contains(u1));
    }

    @Test
    public void testRicercaCognomeInesistente() {
        archivio.aggiungiUtente(u1);

        Set<Utente> risultati = archivio.ricercaCognome("Verdi");

        assertNotNull(risultati);
        assertTrue(risultati.isEmpty());
    }

    // GET UTENTI

    @Test
    public void testGetUtenti() {
        archivio.aggiungiUtente(u1);
        archivio.aggiungiUtente(u2);

        Set<Utente> utenti = archivio.getUtenti();

        assertEquals(2, utenti.size());
        assertTrue(utenti.contains(u1));
        assertTrue(utenti.contains(u2));
    }

    // FILE I/O

    @Test
    public void testScriviELeggiDaFile() throws IOException {
        archivio.aggiungiUtente(u1);
        archivio.aggiungiUtente(u2);

        archivio.scriviSuFile("test_utenti.csv");

        ArchivioUtenti nuovoArchivio = new ArchivioUtenti("test_utenti.csv");

        assertEquals(2, nuovoArchivio.getUtenti().size());
        assertNotNull(nuovoArchivio.ricercaMatricola("100"));
        assertNotNull(nuovoArchivio.ricercaMatricola("200"));
    }
}
