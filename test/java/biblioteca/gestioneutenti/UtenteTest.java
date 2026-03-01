package biblioteca.gestioneutenti;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitari per la classe Utente
 */
public class UtenteTest {

    private Utente utente;

    @BeforeEach
    public void setUp() {
        utente = new Utente(
                "Mario",
                "Rossi",
                "100",
                "mario.rossi@email.it"
        );
    }

    @AfterEach
    public void tearDown() {
        utente = null;
    }

    // COSTRUTTORE E STATO INIZIALE

    @Test
    public void testCostruttoreInizializzaCorrettamente() {
        assertNotNull(utente);
        assertNotNull(utente.getPrestitiAttivi());
        assertTrue(utente.getPrestitiAttivi().isEmpty());
    }

    // GETTER

    @Test
    public void testGetter() {
        assertEquals("Mario", utente.getNome());
        assertEquals("Rossi", utente.getCognome());
        assertEquals("100", utente.getMatricola());
        assertEquals("mario.rossi@email.it", utente.getEmail());
    }

    // SETTER
    @Test
    public void testSetterNome() {
        utente.setNome("Luigi");
        assertEquals("Luigi", utente.getNome());
    }

    @Test
    public void testSetterCognome() {
        utente.setCognome("Verdi");
        assertEquals("Verdi", utente.getCognome());
    }

    @Test
    public void testSetterMatricola() {
        utente.setMatricola("200");
        assertEquals("200", utente.getMatricola());
    }

    @Test
    public void testSetterEmail() {
        utente.setEmail("luigi.verdi@email.it");
        assertEquals("luigi.verdi@email.it", utente.getEmail());
    }

    // EQUALS

    @Test
    public void testEqualsStessaMatricola() {
        Utente stesso = new Utente(
                "Altro",
                "Nome",
                "100",
                "altro@email.it"
        );
        assertEquals(utente, stesso);
    }

    @Test
    public void testEqualsMatricolaDiversa() {
        Utente diverso = new Utente(
                "Mario",
                "Rossi",
                "999",
                "m@email.it"
        );
        assertNotEquals(utente, diverso);
    }

    @Test
    public void testEqualsStessoOggetto() {
        assertEquals(utente, utente);
    }

    @Test
    public void testEqualsNull() {
        assertNotEquals(utente, null);
    }

    @Test
    public void testEqualsTipoDiverso() {
        assertNotEquals(utente, "non un utente");
    }

    // HASHCODE

    @Test
    public void testHashCode() {
        Utente stesso = new Utente(
                "Altro",
                "Nome",
                "100",
                "altro@email.it"
        );
        assertEquals(utente.hashCode(), stesso.hashCode());
    }

    // COMPARE TO

    @Test
    public void testCompareToPerCognome() {
        Utente bianchi = new Utente("Anna", "Bianchi", "050", "a@email.it");
        assertTrue(bianchi.compareTo(utente) < 0); // Bianchi < Rossi
    }

    @Test
    public void testCompareToPerNome() {
        Utente rossiLuca = new Utente("Luca", "Rossi", "150", "l@email.it");
        assertTrue(rossiLuca.compareTo(utente) < 0); // Luca < Mario
    }

    @Test
    public void testCompareToPerMatricola() {
        Utente rossiMario200 = new Utente("Mario", "Rossi", "200", "m@email.it");
        assertTrue(utente.compareTo(rossiMario200) < 0); // 100 < 200
    }

    @Test
    public void testCompareToUguaglianza() {
        Utente stesso = new Utente("Mario", "Rossi", "100", "x@email.it");
        assertEquals(0, utente.compareTo(stesso));
    }

    // PRESTITI (CASI VUOTI)

    @Test
    public void testPrestitiTitoloVuoto() {
        assertEquals("Nessuno", utente.getPrestitiTitolo());
    }

    @Test
    public void testPrestitiDataRestVuota() {
        assertEquals("Nessuna", utente.getPrestitiDataRest());
    }

    @Test
    public void testToStringContieneInformazioniPrincipali() {
        String s = utente.toString();

        assertNotNull(s);
        assertTrue(s.contains("Mario"));
        assertTrue(s.contains("Rossi"));
        assertTrue(s.contains("100"));
        assertTrue(s.contains("mario.rossi@email.it"));
    }

}
