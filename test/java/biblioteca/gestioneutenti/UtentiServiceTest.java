package biblioteca.gestioneutenti;

import biblioteca.gestioneeccezioni.*;
import java.util.Set;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitari per la classe UtentiService
 */
public class UtentiServiceTest {

    private ArchivioUtenti archivio;
    private UtentiService service;
    private Utente utenteValido;

    @BeforeEach
    public void setUp() {
        archivio = new ArchivioUtenti("test_utenti_service.csv");
        archivio.getUtenti().clear();

        service = new UtentiService(archivio);

        utenteValido = new Utente(
                "Mario",
                "Rossi",
                "1234567890",
                "mario.rossi@unisa.it"
        );
    }

    @AfterEach
    public void tearDown() {
        archivio.getUtenti().clear();
    }

    // REGISTRA UTENTE

    @Test
    public void testRegistraUtenteValido() throws Exception {
        service.registraUtente(utenteValido);

        assertEquals(1, archivio.getUtenti().size());
        assertNotNull(archivio.ricercaMatricola("1234567890"));
    }

    @Test
    public void testRegistraUtenteNomeVuoto() {
        Utente u = new Utente("", "Rossi", "1234567890", "a@unisa.it");

        assertThrows(ValidazioneException.class, () -> {
            service.registraUtente(u);
        });
    }

    @Test
    public void testRegistraUtenteEmailNonIstituzionale() {
        Utente u = new Utente("Mario", "Rossi", "1234567890", "mario@gmail.com");

        assertThrows(ValidazioneException.class, () -> {
            service.registraUtente(u);
        });
    }

    @Test
    public void testRegistraUtenteMatricolaFormatoErrato() {
        Utente u = new Utente("Mario", "Rossi", "ABC123", "a@unisa.it");

        assertThrows(ValidazioneException.class, () -> {
            service.registraUtente(u);
        });
    }

    @Test
    public void testRegistraUtenteMatricolaDuplicata() throws Exception {
        service.registraUtente(utenteValido);

        Utente duplicato = new Utente(
                "Altro",
                "Nome",
                "1234567890",
                "altro@unisa.it"
        );

        assertThrows(DuplicatoException.class, () -> {
            service.registraUtente(duplicato);
        });
    }

    // ELIMINA UTENTE

    @Test
    public void testEliminaUtenteSenzaPrestiti() throws Exception {
        service.registraUtente(utenteValido);

        Utente eliminato = service.eliminaUtente(utenteValido);

        assertEquals(utenteValido, eliminato);
        assertTrue(archivio.getUtenti().isEmpty());
    }

    @Test
    public void testEliminaUtenteConPrestitiAttivi() throws Exception {
        service.registraUtente(utenteValido);

        // simuliamo la presenza di un prestito attivo
        utenteValido.getPrestitiAttivi().add(null);

        assertThrows(CancellazionePrestitoAttivoException.class, () -> {
            service.eliminaUtente(utenteValido);
        });
    }

    // RICERCA UTENTE

    @Test
    public void testRicercaUtentePerMatricola() throws Exception {
        service.registraUtente(utenteValido);

        Set<Utente> risultati = service.ricercaUtente("", "1234567890");

        assertEquals(1, risultati.size());
        assertTrue(risultati.contains(utenteValido));
    }

    @Test
    public void testRicercaUtentePerCognome() throws Exception {
        service.registraUtente(utenteValido);

        Set<Utente> risultati = service.ricercaUtente("Rossi", "");

        assertEquals(1, risultati.size());
    }

    @Test
    public void testRicercaUtenteCampiVuoti() {
        assertThrows(ValidazioneException.class, () -> {
            service.ricercaUtente("", "");
        });
    }

    @Test
    public void testRicercaUtenteNonTrovato() throws Exception {
        service.registraUtente(utenteValido);

        assertThrows(UtenteNonTrovatoException.class, () -> {
            service.ricercaUtente("Bianchi", "");
        });
    }

    // MODIFICA UTENTE

    @Test
    public void testModificaUtenteValida() throws Exception {
        service.registraUtente(utenteValido);

        utenteValido.setEmail("nuova@email@unisa.it");

        service.modificaUtente(utenteValido, "1234567890");

        assertEquals(
                "nuova@email@unisa.it",
                archivio.ricercaMatricola("1234567890").getEmail()
        );
    }

    @Test
    public void testModificaUtenteMatricolaDuplicata() throws Exception {
        service.registraUtente(utenteValido);

        Utente altro = new Utente(
                "Anna",
                "Bianchi",
                "0987654321",
                "anna@unisa.it"
        );
        service.registraUtente(altro);

        altro.setMatricola("1234567890");

        assertThrows(DuplicatoException.class, () -> {
            service.modificaUtente(altro, "0987654321");
        });
    }

    // VISUALIZZA UTENTI

    @Test
    public void testVisualizzaUtenti() throws Exception {
        service.registraUtente(utenteValido);

        Set<Utente> utenti = service.visualizzaUtenti();

        assertEquals(1, utenti.size());
        assertTrue(utenti.contains(utenteValido));
    }
}
