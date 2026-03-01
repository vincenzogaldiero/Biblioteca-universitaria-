package biblioteca.gestionehome;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.input.MouseEvent;
import javafx.fxml.Initializable;


/**
 * @class HomeViewController
 * @brief Controller della schermata principale dell'applicazione Biblioteca.
 *
 * Gestisce la logica della Home, permettendo la navigazione verso le sezioni
 * Libri, Utenti e Prestiti. La classe è collegata alla vista FXML tramite
 * annotazioni {@link FXML} e rappresenta il punto centrale di accesso
 * alle funzionalità principali del sistema.
 */
public class HomeViewController implements Initializable{
 // -------------- MENU --------------
    @FXML
    private Label libriID;
    @FXML
    private Label utentiID;
    @FXML
    private Label prestitiID;
    
    // Pre-caricamento dei FXML per ridurre il lag al primo click
    private Parent libriRoot;
    private Parent utentiRoot;
    private Parent prestitiRoot;
    /**
     * @brief Inizializza il controller.
     *
     * Pre-carica i FXML delle sezioni Libri, Utenti e Prestiti
     * per ridurre il lag al primo click. Viene eseguito automaticamente
     * all'avvio della schermata.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        try {
        // Carica tutti i FXML all’avvio dell’app
            libriRoot = FXMLLoader.load(getClass().getResource("/view/LibroView.fxml"));
            utentiRoot = FXMLLoader.load(getClass().getResource("/view/UtentiView.fxml"));
            prestitiRoot = FXMLLoader.load(getClass().getResource("/view/PrestitiView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
      // ------------NAVIGAZIONE MENU ------------
    /** @brief Restituisce lo stage (finestra) attualmente associato alla vista.
     * Viene utilizzato principalmente per effettuare cambi di scena senza
     * ripetere codice in ogni metodo di navigazione, migliorando la pulizia
     * e la manutenibilità del controller.
     *
     * @return Lo {@link Stage} corrente della finestra in cui è caricata la vista
     */
    private Stage getStage(Label label) {
        return (Stage) label.getScene().getWindow();
    }
    /**
     * @brief Apre la sezione Libri.
     * 
     * @param event Evento del click generato dall'interazione dell'utente.
     *
     * @pre event != null
     */
@FXML
private void clickLibri(MouseEvent event) {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LibroView.fxml"));
        Stage stage = (Stage) ((Label) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
   
    /**
     * @brief Apre la sezione Utenti.
     * 
     * @param event Evento del click generato dall'interazione dell'utente.
     *
     * @pre event != null
     */
@FXML
private void clickUtenti(MouseEvent event) {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("/view/UtentiView.fxml"));
        Stage stage = (Stage) ((Label) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    /**
     * @brief Apre la sezione Prestiti.
     * 
     * @param event Evento del click generato dall'interazione dell'utente.
     *
     * @pre event != null
     */
@FXML
private void clickPrestiti(MouseEvent event) {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("/view/PrestitiView.fxml"));
        Stage stage = (Stage) ((Label) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}