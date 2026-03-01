package biblioteca.gestionelibri;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitari per la classe Libro
 */
public class LibroTest {

    private Libro libro1;
    private Libro libro2;
    private Libro libroStessoISBN;

    @BeforeEach
    public void setUp() {
        libro1 = new Libro(
                "Il nome della rosa",
                "Umberto Eco",
                1980,
                "9788845245497",
                3
        );

        libro2 = new Libro(
                "I promessi sposi",
                "Alessandro Manzoni",
                1827,
                "9788807900335",
                5
        );

        libroStessoISBN = new Libro(
                "Titolo diverso",
                "Altro autore",
                2000,
                "9788845245497",
                1
        );
    }

    // COSTRUTTORE E GETTER

    @Test
    public void testCostruttoreEGetter() {
        assertEquals("Il nome della rosa", libro1.getTitolo());
        assertEquals("Umberto Eco", libro1.getAutore());
        assertEquals(1980, libro1.getAnnoPubblicazione());
        assertEquals("9788845245497", libro1.getISBN());
        assertEquals(3, libro1.getCopieDisponibili());
    }

    // SETTER

    @Test
    public void testSetter() {
        libro1.setTitolo("Nuovo titolo");
        libro1.setAutore("Nuovo autore");
        libro1.setAnnoPubblicazione(1990);
        libro1.setISBN("1111111111111");
        libro1.setCopieDisponibili(10);

        assertEquals("Nuovo titolo", libro1.getTitolo());
        assertEquals("Nuovo autore", libro1.getAutore());
        assertEquals(1990, libro1.getAnnoPubblicazione());
        assertEquals("1111111111111", libro1.getISBN());
        assertEquals(10, libro1.getCopieDisponibili());
    }

    // EQUALS E HASHCODE

    @Test
    public void testEqualsStessoISBN() {
        assertEquals(libro1, libroStessoISBN);
    }

    @Test
    public void testEqualsDiversoISBN() {
        assertNotEquals(libro1, libro2);
    }

    @Test
    public void testHashCodeCoerenteConEquals() {
        assertEquals(libro1.hashCode(), libroStessoISBN.hashCode());
    }
    
    // COMPARE TO

    @Test
    public void testCompareToPerTitolo() {
        assertTrue(libro1.compareTo(libro2) > 0
                || libro1.compareTo(libro2) < 0);
    }

    @Test
    public void testCompareToStessoTitoloISBN() {
        Libro altro = new Libro(
                "Il nome della rosa",
                "Autore diverso",
                2020,
                "0000000000000",
                1
        );

        assertTrue(libro1.compareTo(altro) > 0);
    }

    // TOSTRING

    @Test
    public void testToString() {
        String s = libro1.toString();

        assertNotNull(s);
        assertTrue(s.contains("Libro"));
        assertTrue(s.contains("Il nome della rosa"));
        assertTrue(s.contains("Umberto Eco"));
    }
}
