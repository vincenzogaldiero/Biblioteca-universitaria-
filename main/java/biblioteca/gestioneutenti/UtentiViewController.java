package biblioteca.gestioneutenti;

import biblioteca.Main;
import biblioteca.gestioneeccezioni.BibliotecaException;
import biblioteca.gestioneeccezioni.CancellazionePrestitoAttivoException;
import biblioteca.gestioneeccezioni.DuplicatoException;
import biblioteca.gestioneeccezioni.UtenteNonTrovatoException;
import biblioteca.gestioneeccezioni.ValidazioneException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.collections.FXCollections;
/**
 * @class UtentiViewController
 * @brief Controller della vista FXML dedicata alla gestione degli utenti.
 *
 * Questa classe gestisce l'interazione dell'interfaccia grafica con:
 * - inserimento di un nuovo utente
 * - ricerca utenti
 * - rimozione utenti
 * - visualizzazione dell’elenco degli utenti
 *
 * Tutti i controlli sono stati affidati alla classe {@link UtentiService}.
 *
 */
public class UtentiViewController implements Initializable{
    // ------------------ MENU ----------------------
    @FXML
    private Label homeID;
    @FXML
    private Label libriID;
    @FXML
    private Label prestitiID;
    // ------------------ CAMPI INPUT ---------------
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtCognome;
    @FXML
    private TextField txtMatricola;
    @FXML
    private TextField txtEmail;
    // --------------- BOTTONI --------------------
    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button viewButton;
    // -------------- TABELLA -----------------
    @FXML
    private TableView<Utente> utenteTable;
    @FXML
    private TableColumn<Utente, String> colNome;
    @FXML
    private TableColumn<Utente, String> colCognome;
    @FXML
    private TableColumn<Utente, String> colMatricola;
    @FXML
    private TableColumn<Utente, String> colEmail;
    @FXML
    private TableColumn<Utente, String> colPrestiti;
    @FXML
    private TableColumn<Utente, LocalDate> colDataRest;
  // ------------- LOGICA --------------
    
    /** Service per la gestione degli utenti */
    private UtentiService utenteService;
    
    /** Lista osservabile degli utenti per la TableView */
    private ObservableList<Utente> listaUtenti;
    
    /** Memorizza la matricola originale durante l'editing in-place */
    private String matricolaOriginale;

    /**
     * @brief Metodo di inizializzazione del controller utenti.
     *
     * Viene eseguito automaticamente all'avvio della schermata.
     * Si occupa di:
     * - inizializzare il {@link UtentiService} e la {@link ObservableList} degli utenti;
     * - popolare la {@link TableView} con i dati correnti;
     * - rendere le colonne editabili e impostare le cell factory e value factory;
     * - configurare i binding dei pulsanti (aggiungi, elimina, ricerca) in base
     *   allo stato dei campi di input e della selezione nella tabella.
     *
     * @post La tabella è popolata con gli utenti attuali e i pulsanti sono abilitati/disabilitati
     *       correttamente in base ai dati inseriti nei campi di input.
     */
@Override
public void initialize(URL url, ResourceBundle rb) {
    // Inizializzazione service e lista
    utenteService = new UtentiService(Main.archivioUtenti);
    listaUtenti = FXCollections.observableArrayList();
    listaUtenti.setAll(utenteService.visualizzaUtenti());

    // La tabella deve essere editabile
    utenteTable.setEditable(true);

    // CELL FACTORY
    colNome.setCellFactory(TextFieldTableCell.forTableColumn());
    colCognome.setCellFactory(TextFieldTableCell.forTableColumn());
    colEmail.setCellFactory(TextFieldTableCell.forTableColumn());
    colMatricola.setCellFactory(TextFieldTableCell.forTableColumn());

    // VALUE FACTORY
    colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
    colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
    colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    colPrestiti.setCellValueFactory(new PropertyValueFactory<>("prestitiTitolo"));
    colDataRest.setCellValueFactory(new PropertyValueFactory<>("prestitiDataRest"));
    colMatricola.setCellValueFactory(new PropertyValueFactory<>("matricola"));
    
    // Imposta dati nella tabella
    utenteTable.setItems(listaUtenti);

    // BOTTONI
    searchButton.disableProperty().bind(
        txtCognome.textProperty().isEmpty()
        .and(txtMatricola.textProperty().isEmpty())
    );

    addButton.disableProperty().bind(
        txtNome.textProperty().isEmpty()
        .or(txtCognome.textProperty().isEmpty())
        .or(txtMatricola.textProperty().isEmpty())
        .or(txtEmail.textProperty().isEmpty())
    );

    removeButton.disableProperty().bind(
        utenteTable.getSelectionModel().selectedItemProperty().isNull()
    );

    // Resize proporzionale e minWidth
    utenteTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    colNome.prefWidthProperty().bind(utenteTable.widthProperty().multiply(0.15));
    colCognome.prefWidthProperty().bind(utenteTable.widthProperty().multiply(0.15));
    colMatricola.prefWidthProperty().bind(utenteTable.widthProperty().multiply(0.10));
    colEmail.prefWidthProperty().bind(utenteTable.widthProperty().multiply(0.20));
    colPrestiti.prefWidthProperty().bind(utenteTable.widthProperty().multiply(0.25));
    colDataRest.prefWidthProperty().bind(utenteTable.widthProperty().multiply(0.15));

    colNome.setMinWidth(80);
    colCognome.setMinWidth(80);
    colMatricola.setMinWidth(60);
    colEmail.setMinWidth(100);
    colPrestiti.setMinWidth(120);
    colDataRest.setMinWidth(80);
}
 
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
    
        // ------------NAVIGAZIONE MENU ------------
    
    /**
     * @brief Apre la sezione Home.
     * 
     * @param event Evento del click generato dall'interazione dell'utente.
     *
     * @pre event != null
     */
@FXML
private void clickHome(MouseEvent event) {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("/view/HomeView.fxml"));
        Stage stage = (Stage) ((Label) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    } catch (IOException e) {
        e.printStackTrace();
    }
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
    //------------OPERAZIONI UTENTI ------------
    
     /**
     * @brief Aggiunge un nuovo utente utilizzando i dati inseriti nei campi testo.
     * 
     * @pre Tutti i campi del nuovo utente devono essere compilati correttamente.
     * @post Se i dati sono validi e non duplicati, l'utente viene aggiunto all'archivio,
     *       la tabella viene aggiornata e i campi di input vengono svuotati.
     * 
     * @throws ValidazioneException Se uno o più campi del nuovo utente non sono validi.
     * @throws DuplicatoException Se l'utente esiste già nell'archivio.
     */
    @FXML
    private void onAggiungiUtente(ActionEvent event) {
        
         try {
        // Creazione oggetto libro
        Utente nuovo = new Utente(
            txtNome.getText(),
            txtCognome.getText(),
            txtMatricola.getText(),
            txtEmail.getText()
        );

        // Aggiunta tramite service
        utenteService.registraUtente(nuovo);

        // Aggiorno tabella
        listaUtenti.setAll(utenteService.visualizzaUtenti());

        alertConferma("Utente aggiunto correttamente.");
        // Svuota i campi dopo l'inserimento
        txtNome.clear();
        txtCognome.clear();
        txtMatricola.clear();
        txtEmail.clear();

    } catch (ValidazioneException e) {
        alertErrore("Errore: " + e.getMessage());
    } catch( DuplicatoException e1){
        alertErrore("Errore: " + e1.getMessage());
    }
   }
    /**
     * @brief Rimuove un utente utilizzando i dati inseriti nei campi testo.
     *
     * @pre Un utente deve essere selezionato nella tabella.
     * @post Se l'utente non ha prestiti attivi, viene rimosso dall'archivio
     *       e la tabella viene aggiornata.
     * 
     * @throws CancellazionePrestitoAttivoException Se l'utente ha ancora prestiti attivi.
     */
    @FXML
    private void onRimuoviUtente(ActionEvent event) {
        try {
        Utente selezionato = utenteTable.getSelectionModel().getSelectedItem();

        
        if (alertConfermaEliminazione()) {
            utenteService.eliminaUtente(selezionato);
            alertConferma("Utente eliminato con successo.");
            }
        listaUtenti.setAll(utenteService.visualizzaUtenti());

    } catch (CancellazionePrestitoAttivoException e) {
        alertErrore("Errore: " + e.getMessage());
    }
   }
     /**
     * @brief Esegue una ricerca utenti sui campi compilando cognome o matricola
     * 
     * @pre Almeno uno tra cognome o matricola deve essere compilato.
     * @post La tabella viene popolata con gli utenti trovati secondo il criterio di ricerca.
     * 
     * @throws ValidazioneException Se entrambi i campi sono vuoti o non validi.
     * @throws UtenteNonTrovatoException Se non vengono trovati utenti corrispondenti ai criteri.
     */
    @FXML
    private void onRicercaUtente(ActionEvent event){
    String cognome = txtCognome.getText().trim();
    String matricola = txtMatricola.getText().trim();
    try{
        Set<Utente> risultati = utenteService.ricercaUtente(cognome, matricola);
        utenteTable.getItems().setAll(risultati);
        } catch (ValidazioneException e) {
        alertErrore("Errore: " + e.getMessage());
        }catch(UtenteNonTrovatoException e1){
         alertErrore("Errore: " + e1.getMessage());
        }
    }
    /**
     * @brief Mostra l'intero elenco degli utenti presenti nell’archivio.
     * 
     * @post La tabella {@link TableView} mostra tutti gli utenti correnti dell'archivio.
     */
    @FXML
    private void onVisualizzaUtenti(ActionEvent event) {
         Set<Utente> tutti = utenteService.visualizzaUtenti();
        utenteTable.getItems().setAll(tutti);
    }
     /**
     * @brief Gestisce la modifica del nome direttamente nella TableView.
     *
     * Recupera l'utente selezionato e il nuovo valore del nome, prova a salvare la modifica
     * In caso di errore, ripristina il vecchio valore e mostra un messaggio di errore.
     *
     * @post Se non vengono sollevate eccezioni, il nome dell'utente è aggiornato
     *       nell'archivio e la tabella della GUI riflette il cambiamento.
     */
    @FXML
    private void onModificaNome(TableColumn.CellEditEvent<Utente, String> event) {
            Utente utente = event.getRowValue();
            String vecchioNome = utente.getNome();
            String nuovoNome = event.getNewValue();
    try {
        utente.setNome(nuovoNome);
        utenteService.modificaUtente(utente, utente.getMatricola());
        listaUtenti.setAll(utenteService.visualizzaUtenti());
    } catch (BibliotecaException e) {
        // Ripristina il vecchio valore
        utente.setNome(vecchioNome);
        // Aggiorna la tabella graficamente
        utenteTable.refresh();
        alertErrore(e.getMessage());
    }
    }
     /**
     * @brief Gestisce la modifica del cognome direttamente nella TableView.
     *
     * Recupera l'utente selezionato e il nuovo valore del cognome, prova a salvare la modifica
     * In caso di errore, ripristina il vecchio valore e mostra un messaggio di errore.
     *
     * @post Se non vengono sollevate eccezioni, il cognome dell'utente è aggiornato
     *       nell'archivio e la tabella della GUI riflette il cambiamento.
     */
    @FXML
    private void onModificaCognome(TableColumn.CellEditEvent<Utente, String> event) {
            Utente utente = event.getRowValue();
            String vecchioCognome = utente.getCognome();
            String nuovoCognome = event.getNewValue();
    try {
        utente.setCognome(nuovoCognome);
        utenteService.modificaUtente(utente, utente.getMatricola());
        listaUtenti.setAll(utenteService.visualizzaUtenti());
    } catch (BibliotecaException e) {
        // Ripristina il vecchio valore
        utente.setCognome(vecchioCognome);
        // Aggiorna la tabella graficamente
        utenteTable.refresh();
        alertErrore(e.getMessage());
    }
    }

    @FXML
    private void onOriginaleMatricola(TableColumn.CellEditEvent<Utente, String> event) {
        matricolaOriginale = event.getOldValue();
    }
     /**
     * @brief Gestisce la modifica della matricola direttamente nella TableView.
     *
     * Recupera l'utente selezionato e il nuovo valore della matricola, prova a salvare la modifica
     * In caso di errore, ripristina il vecchio valore e mostra un messaggio di errore.
     *
     * @post Se non vengono sollevate eccezioni, la matricola dell'utente è aggiornata
     *       nell'archivio e la tabella della GUI riflette il cambiamento.
     */
    @FXML
    private void onModificaMatricola(TableColumn.CellEditEvent<Utente, String> event) {
        Utente utente = event.getRowValue();
        String vecchiaMatricola = utente.getMatricola();
        String nuovaMatricola = event.getNewValue();
    try {
        utente.setMatricola(nuovaMatricola);
        utenteService.modificaUtente(utente, matricolaOriginale);
        listaUtenti.setAll(utenteService.visualizzaUtenti());
    } catch (BibliotecaException e) {
        // Ripristina il vecchio valore
        utente.setMatricola(vecchiaMatricola);
        // Aggiorna la tabella graficamente
        utenteTable.refresh();
        alertErrore(e.getMessage());
    } finally {
        matricolaOriginale = null;
    }
    }
     /**
     * @brief Gestisce la modifica dell'email direttamente nella TableView.
     *
     * Recupera l'utente selezionato e il nuovo valore dell'email, prova a salvare la modifica
     * In caso di errore, ripristina il vecchio valore e mostra un messaggio di errore.
     *
     * @post Se non vengono sollevate eccezioni, l'email dell'utente è aggiornata
     *       nell'archivio e la tabella della GUI riflette il cambiamento.
     */
    @FXML
    private void onModificaEmail(TableColumn.CellEditEvent<Utente, String> event) {
            Utente utente = event.getRowValue();
            String vecchiaMail = utente.getEmail();
            String nuovaMail = event.getNewValue();
    try {
        utente.setEmail(nuovaMail);
        utenteService.modificaUtente(utente, utente.getMatricola());
        listaUtenti.setAll(utenteService.visualizzaUtenti());
    } catch (BibliotecaException e) {
        // Ripristina il vecchio valore
        utente.setEmail(vecchiaMail);
        // Aggiorna la tabella graficamente
        utenteTable.refresh();
        alertErrore(e.getMessage());
    }
    }
    /**
    * Mostra un alert informativo per confermare che un'operazione
    * è stata completata correttamente.
    *
    * @param messaggio Testo da mostrare nel popup
    */
    private void alertConferma(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Operazione completata");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
    /**
    * Mostra un alert di errore quando si verifica un problema.
    *
    * @param messaggio Testo che descrive l'errore
    */
    private void alertErrore(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
    /**
    * Mostra un alert di conferma per chiedere 
    * se si desidera davvero eliminare un libro.
    *
    * @return true se ha premuto "OK",
    *         false se ha premuto "Cancel" o ha chiuso la finestra.
    */
    private boolean alertConfermaEliminazione() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma eliminazione");
        alert.setHeaderText("Sei sicuro?");
        alert.setContentText("Vuoi davvero eliminare questo utente?");

        Optional<ButtonType> result = alert.showAndWait();
        //verifica che sia stato premuto un pulsante e che sia il bottone ok
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    
}
