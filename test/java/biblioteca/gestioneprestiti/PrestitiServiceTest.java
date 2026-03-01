package biblioteca.gestioneprestiti;

import biblioteca.gestionelibri.*;
import biblioteca.gestioneutenti.*;
import biblioteca.gestioneeccezioni.*;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitari e di integrazione per PrestitiService
 */
public class PrestitiServiceTest {

    private ArchivioLibri archivioLibri;
    private ArchivioUtenti archivioUtenti;
    private ArchivioPrestitiAttivi archivioPrestitiAttivi;
    private ArchivioCronologiaPrestiti archivioCronologia;

    private PrestitiService service;

    private Libro libro;
    private Utente utente;

    @BeforeEach
    public void setUp() {
        archivioLibri = new ArchivioLibri("test_libri.csv");
        archivioUtenti = new ArchivioUtenti("test_utenti.csv");
        archivioPrestitiAttivi = new ArchivioPrestitiAttivi(
                "test_prestiti.csv",
                archivioUtenti,
                archivioLibri
        );
        archivioCronologia = new ArchivioCronologiaPrestiti(
                "test_cronologia.csv",
                archivioUtenti,
                archivioLibri
        );

        archivioLibri.getLibri().clear();
        archivioUtenti.getUtenti().clear();
        archivioPrestitiAttivi.getPrestitiAttivi().clear();
        archivioCronologia.getCronologia().clear();

        service = new PrestitiService(
                archivioLibri,
                archivioUtenti,
                archivioPrestitiAttivi,
                archivioCronologia
        );

        libro = new Libro(
                "Il nome della rosa",
                "Umberto Eco",
                1980,
                "1234567890",
                2
        );

        utente = new Utente(
                "Mario",
                "Rossi",
                "1111111111",
                "mario@unisa.it"
        );

        archivioLibri.aggiungiLibro(libro);
        archivioUtenti.aggiungiUtente(utente);
    }

    // REGISTRA PRESTITO

    @Test
    public void testRegistraPrestitoValido() throws Exception {
        service.registraPrestito(
                libro.getISBN(),
                utente.getMatricola(),
                LocalDate.now().plusDays(7)
        );

        assertEquals(1, archivioPrestitiAttivi.getPrestitiAttivi().size());
        assertEquals(1, utente.getPrestitiAttivi().size());
        assertEquals(1, libro.getCopieDisponibili());
    }

    @Test
    public void testRegistraPrestitoDataPassata() {
        assertThrows(ValidazioneException.class, () ->
            service.registraPrestito(
                libro.getISBN(),
                utente.getMatricola(),
                LocalDate.now().minusDays(1)
            )
        );
    }

    @Test
    public void testRegistraPrestitoLibroNonDisponibile() throws Exception {
        libro.setCopieDisponibili(0);

        assertThrows(LibroNonDisponibileException.class, () ->
            service.registraPrestito(
                libro.getISBN(),
                utente.getMatricola(),
                LocalDate.now().plusDays(5)
            )
        );
    }

    @Test
    public void testRegistraPrestitoLimiteSuperato() throws Exception {
        for (int i = 0; i < 3; i++) {
            Libro l = new Libro(
                    "Libro " + i,
                    "Autore",
                    2000,
                    "ISBN000000" + i,
                    1
            );
            archivioLibri.aggiungiLibro(l);
            service.registraPrestito(
                    l.getISBN(),
                    utente.getMatricola(),
                    LocalDate.now().plusDays(10)
            );
        }

        assertThrows(LimitePrestitoSuperatoException.class, () ->
            service.registraPrestito(
                libro.getISBN(),
                utente.getMatricola(),
                LocalDate.now().plusDays(10)
            )
        );
    }

    // ELIMINA PRESTITO ATTIVO

    @Test
    public void testEliminaPrestitoAttivo() throws Exception {
        service.registraPrestito(
                libro.getISBN(),
                utente.getMatricola(),
                LocalDate.now().plusDays(7)
        );

        Prestito p = archivioPrestitiAttivi.getPrestitiAttivi().get(0);

        Prestito eliminato = service.eliminaPrestitoAttivo(p);

        assertEquals(StatoPrestiti.CHIUSO, eliminato.getStato());
        assertTrue(archivioPrestitiAttivi.getPrestitiAttivi().isEmpty());
        assertEquals(1, archivioCronologia.getCronologia().size());
        assertEquals(2, libro.getCopieDisponibili());
    }

    @Test
    public void testEliminaPrestitoNullo() {
        assertThrows(ValidazioneException.class, () ->
            service.eliminaPrestitoAttivo(null)
        );
    }

    // VISUALIZZA PRESTITI (RITARDO)

    @Test
    public void testPrestitoInRitardo() throws Exception {

        // registro prestito con data FUTURA
        service.registraPrestito(
            libro.getISBN(),
            utente.getMatricola(),
            LocalDate.now().plusDays(5)
        );

        Prestito p = archivioPrestitiAttivi.getPrestitiAttivi().get(0);

        // forzo il ritardo
        p.setDataFine(LocalDate.now().minusDays(2));

        // verifico che venga riconosciuto come in ritardo
        assertTrue(service.prestitoInRitardo(p));
    }

    // RICERCA PRESTITI

    @Test
    public void testRicercaPrestitoPerMatricola() throws Exception {
        service.registraPrestito(
                libro.getISBN(),
                utente.getMatricola(),
                LocalDate.now().plusDays(5)
        );

        List<Prestito> risultati =
                service.ricercaPrestitiAttivi(utente.getMatricola(), "");

        assertEquals(1, risultati.size());
    }

    @Test
    public void testRicercaPrestitoNonTrovato() {
        assertThrows(PrestitoNonTrovatoException.class, () ->
            service.ricercaPrestitiAttivi("999", "")
        );
    }

    @Test
    public void testRicercaPrestitoCampiVuoti() {
        assertThrows(ValidazioneException.class, () ->
            service.ricercaPrestitiAttivi("", "")
        );
    }
}
