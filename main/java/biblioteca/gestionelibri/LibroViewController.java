package biblioteca.gestionelibri;

import biblioteca.Main;
import biblioteca.gestioneeccezioni.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import java.util.Set;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.util.converter.IntegerStringConverter;
/**
 * @class LibroViewController
 * @brief Controller della vista FXML dedicata alla gestione dei libri.
 *
 * Questa classe gestisce l'interazione dell'interfaccia grafica con:
 * - inserimento di un nuovo libro
 * - ricerca libro
 * - rimozione libro
 * - visualizzazione dell’elenco dei libri
 *
 * Tutti i controlli vengono delegati a {@link LibriService}.
 */
public class LibroViewController implements Initializable{
    //--------------- MENU ---------------
    @FXML
    private Label homeID;
    @FXML
    private Label utentiID;
    @FXML
    private Label prestitiID;
     // ------------------ CAMPI INPUT ---------------
    @FXML
    private TextField txtTitolo;
    @FXML
    private TextField txtAutori;
    @FXML
    private TextField txtISBN;
    @FXML
    private TextField txtCopie;
    @FXML
    private TextField txtAnno;
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
    private TableView<Libro> libroTable;
    @FXML
    private TableColumn<Libro, String> colTitolo;
    @FXML
    private TableColumn<Libro, String> colAutore;
    @FXML
    private TableColumn<Libro, String> colISBN;
    @FXML
    private TableColumn<Libro, Integer> colNumCopie;
    @FXML
    private TableColumn<Libro, Integer> colAnno;
    
    // ------------- LOGICA -------------
    
    /** Servizio per la gestione della logica applicativa dei libri. */
    private LibriService libroService;

    /** Lista osservabile dei libri, utilizzata per il binding con la tabella GUI. */
    private ObservableList<Libro> listaLibri;

    /** ISBN originale del libro selezionato, usato per il controllo dei duplicati in fase di modifica. */
    private String isbnOriginale;

    /**
     * @brief Metodo di inizializzazione del controller.
     *
     * Viene invocato automaticamente dal framework JavaFX al caricamento
     * della vista FXML. Il metodo si occupa di:
     * - inizializzare il {@link LibriService};
     * - caricare l'elenco dei libri e popolare la {@link TableView};
     * - configurare le {@code CellFactory} e le {@code ValueFactory} delle colonne;
     * - impostare i {@code binding} per l'abilitazione dei pulsanti in base allo stato dell'interfaccia.
     * @post La tabella dei libri è inizializzata, popolata e pronta per l'interazione dell'utente.
     */ 
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Inizializzazione service e lista
        // crea il service UNA SOLA VOLTA

        libroService = new LibriService(Main.archivioLibri, Main.archivioPrestitiAttivi);
        listaLibri = FXCollections.observableArrayList();
        listaLibri.setAll(libroService.visualizzaLibri());

        // La tabella deve essere editabile !
        libroTable.setEditable(true);

        //   CELL FACTORY

        // Colonne STRINGA
        colTitolo.setCellFactory(TextFieldTableCell.forTableColumn());
        colAutore.setCellFactory(TextFieldTableCell.forTableColumn());
        colISBN.setCellFactory(TextFieldTableCell.forTableColumn());

        // Colonne INTERE (con IntegerStringConverter)
        colAnno.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colNumCopie.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        //   VALUE FACTORY

        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colAutore.setCellValueFactory(new PropertyValueFactory<>("autore"));
        colAnno.setCellValueFactory(new PropertyValueFactory<>("annoPubblicazione"));
        colISBN.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        colNumCopie.setCellValueFactory(new PropertyValueFactory<>("copieDisponibili"));

        // Imposta dati nella tabella
        libroTable.setItems(listaLibri);
        //   BINDINGS 

       // Aggiungi
       addButton.disableProperty().bind(
           txtTitolo.textProperty().isEmpty()
               .or(txtAutori.textProperty().isEmpty())
               .or(txtAnno.textProperty().isEmpty())
               .or(txtISBN.textProperty().isEmpty())
               .or(txtCopie.textProperty().isEmpty())
       );

       // Elimina
       removeButton.disableProperty().bind(
           libroTable.getSelectionModel().selectedItemProperty().isNull()
       );

       // Cerca
       searchButton.disableProperty().bind(
           txtTitolo.textProperty().isEmpty()
               .and(txtAutori.textProperty().isEmpty())
               .and(txtISBN.textProperty().isEmpty())
       );

        // Resize proporzionale e minWidth
        libroTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colTitolo.prefWidthProperty().bind(libroTable.widthProperty().multiply(0.25));
        colAutore.prefWidthProperty().bind(libroTable.widthProperty().multiply(0.20));
        colISBN.prefWidthProperty().bind(libroTable.widthProperty().multiply(0.20));
        colNumCopie.prefWidthProperty().bind(libroTable.widthProperty().multiply(0.15));
        colAnno.prefWidthProperty().bind(libroTable.widthProperty().multiply(0.20));

        colTitolo.setMinWidth(120);
        colAutore.setMinWidth(100);
        colISBN.setMinWidth(100);
        colNumCopie.setMinWidth(60);
        colAnno.setMinWidth(60);
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
    //------------OPERAZIONI UTENTI ------------
    
     /**
     * @brief Aggiunge un nuovo libro utilizzando i dati inseriti nei campi testo.
     * 
     * @pre Tutti i campi testo relativi al libro ({@code txtTitolo, txtAutori, txtAnno, txtISBN, txtCopie})
     *      devono essere compilati in modo corretto e coerente.
     * @post Se non vengono sollevate eccezioni, il libro è aggiunto all'archivio
     *       e la tabella nella GUI viene aggiornata.
     * @throws ValidazioneException Se uno o più campi del libro non sono validi.
     * @throws DuplicatoException Se esiste già un libro con lo stesso ISBN.
     */
    @FXML
    private void onAggiungiLibro(ActionEvent event) {
        try {
        // Creazione oggetto libro
            Libro nuovo = new Libro(
            txtTitolo.getText(),
            txtAutori.getText(),
            Integer.parseInt(txtAnno.getText()),
            txtISBN.getText(),
            Integer.parseInt(txtCopie.getText())
        );

        // Aggiunta tramite service
        libroService.registraLibro(nuovo);

        // Aggiorno tabella
        listaLibri.setAll(libroService.visualizzaLibri());

        alertConferma("Libro aggiunto correttamente.");
        // Svuota i campi dopo l'inserimento
        txtTitolo.clear();
        txtAutori.clear();
        txtAnno.clear();
        txtISBN.clear();
        txtCopie.clear();

    } catch (ValidazioneException e) {
        alertErrore("Errore: " + e.getMessage());
    } catch( DuplicatoException e1){
        alertErrore("Errore: " + e1.getMessage());
    }
        
    }
    /**
    * @brief Rimuove un libro utilizzando i dati inseriti nei campi testo.
    * 
    * @pre Un libro deve essere selezionato nella tabella.
    * @post Se non solleva eccezioni, il libro è rimosso dall'archivio
    *       e la tabella della GUI è aggiornata.
    * @throws CancellazionePrestitoAttivoException Se il libro ha prestiti attivi.
    */ 
    @FXML
    private void onRimuoviLibro(ActionEvent event) {
    try {
        Libro selezionato = libroTable.getSelectionModel().getSelectedItem();

        
        if (alertConfermaEliminazione()) {
            libroService.eliminaLibro(selezionato);
            alertConferma("Libro eliminato con successo.");
            }
        listaLibri.setAll(libroService.visualizzaLibri());

    } catch (CancellazionePrestitoAttivoException e) {
        alertErrore("Errore: " + e.getMessage());
    }
   }
     /**
     * @brief Esegue una ricerca libro sui campi compilando titolo, autore o ISBN
     * 
     * @pre Almeno uno tra i campi titolo, autore o ISBN deve essere compilato.
     * @post La tabella {@link TableView} mostra i libri corrispondenti ai criteri.
     * @throws ValidazioneException Se tutti i campi sono vuoti o non validi.
     * @throws LibroNonTrovatoException Se non vengono trovati libri corrispondenti.
     */
    @FXML
    private void onRicercaLibro(ActionEvent event) {
    String titolo = txtTitolo.getText().trim();
    String autore = txtAutori.getText().trim();
    String isbn = txtISBN.getText().trim();
    try{
        Set<Libro> risultati = libroService.ricercaLibro(titolo, autore, isbn);
        libroTable.getItems().setAll(risultati);
        } catch (ValidazioneException e) {
        alertErrore("Errore: " + e.getMessage());
        }catch(LibroNonTrovatoException e1){
         alertErrore("Errore: " + e1.getMessage());
        }
    }
    /**    
     * @brief Mostra l'intero elenco dei libri presenti nell’archivio.
     * 
     * @post La tabella {@link TableView} mostra tutti i libri correnti dell'archivio.
     */
    @FXML
    private void onVisualizzaLibri(ActionEvent event) {
        Set<Libro> tutti = libroService.visualizzaLibri();
        libroTable.getItems().setAll(tutti);
    }
     /**
     * @brief Gestisce la modifica del titolo direttamente nella TableView.
     *
     * Recupera il libro selezionato e il nuovo valore del titolo, prova a salvare la modifica
     * In caso di errore, ripristina il vecchio valore e mostra un messaggio di errore.
     *
     * @post Se non vengono sollevate eccezioni, il titolo del libro è aggiornato
     *       nell'archivio e la tabella della GUI riflette il cambiamento.
     */
       @FXML
    private void onModificaTitolo(TableColumn.CellEditEvent<Libro, String> event) {
            Libro libro = event.getRowValue();
            String vecchioTitolo = libro.getTitolo();
            String nuovoTitolo = event.getNewValue();
    try {
        libro.setTitolo(nuovoTitolo);
        libroService.modificaLibro(libro, isbnOriginale != null ? isbnOriginale : libro.getISBN());
        listaLibri.setAll(libroService.visualizzaLibri());
    } catch (BibliotecaException e) {
        // Ripristina il vecchio valore
        libro.setTitolo(vecchioTitolo);
        // Aggiorna la tabella graficamente
        libroTable.refresh();
        alertErrore(e.getMessage());
    }
    }
     /**
     * @brief Gestisce la modifica dell'autore direttamente nella TableView.
     *
     * Recupera il libro selezionato e il nuovo valore dell'autore, prova a salvare la modifica
     * In caso di errore, ripristina il vecchio valore e mostra un messaggio di errore.
     *
     * @post Se non vengono sollevate eccezioni, l'autore del libro è aggiornato
     *       nell'archivio e la tabella della GUI riflette il cambiamento.
     */
    @FXML
    private void onModificaAutore(TableColumn.CellEditEvent<Libro, String> event) {
        Libro libro = event.getRowValue();
        String vecchioAutore = libro.getAutore();
        String nuovoAutore = event.getNewValue();
    try {
        libro.setAutore(nuovoAutore);
        libroService.modificaLibro(libro, isbnOriginale != null ? isbnOriginale : libro.getISBN());
        listaLibri.setAll(libroService.visualizzaLibri());
    } catch (BibliotecaException e) {
        // Ripristina il vecchio valore
        libro.setAutore(vecchioAutore);
        // Aggiorna la tabella graficamente
        libroTable.refresh();
        alertErrore(e.getMessage());
    }
    }
     /**
     * @brief Memorizza l'ISBN originale del libro prima di una modifica.
     *
     * Questo metodo viene richiamato automaticamente quando si inizia
     * l'editing della cella ISBN nella TableView. Salva il valore
     * corrente in {@link #isbnOriginale} per poterlo usare in seguito
     * durante la modifica.
     *
     * @post isbnOriginale contiene il valore precedente dell'ISBN.
     */
    @FXML
    private void onOriginaleISBN(TableColumn.CellEditEvent<Libro, String> event) {
         isbnOriginale = event.getOldValue();
    }
     /**
     * @brief Gestisce la modifica dell'ISBN direttamente nella TableView.
     *
     * Recupera il libro selezionato e il nuovo valore dell'ISBN, prova a salvare la modifica
     * In caso di errore, ripristina il vecchio valore e mostra un messaggio di errore.
     *
     * @post Se non vengono sollevate eccezioni, l'ISBN del libro è aggiornato
     *       nell'archivio e la tabella della GUI riflette il cambiamento.
     */
    @FXML
    private void onModificaISBN(TableColumn.CellEditEvent<Libro, String> event) {
        Libro libro = event.getRowValue();
        String vecchioISBN = libro.getISBN();
        String nuovoISBN = event.getNewValue();
    try {
        libro.setISBN(nuovoISBN);
        libroService.modificaLibro(libro, isbnOriginale);
        listaLibri.setAll(libroService.visualizzaLibri());
    } catch (BibliotecaException e) {
        // Ripristina il vecchio valore
        libro.setISBN(vecchioISBN);
        // Aggiorna la tabella graficamente
        libroTable.refresh();
        alertErrore(e.getMessage());
    } finally {
        isbnOriginale = null;
    }
    }
    /**
     * @brief Gestisce la modifica delle copie direttamente nella TableView.
     *
     * Recupera il libro selezionato e il nuovo valore delle copie, prova a salvare la modifica
     * In caso di errore, ripristina il vecchio valore e mostra un messaggio di errore.
     *
     * @post Se non vengono sollevate eccezioni, le copie del libro sono aggiornate
     *       nell'archivio e la tabella della GUI riflette il cambiamento.
     */
    @FXML
    private void onModificaCopie(TableColumn.CellEditEvent<Libro, Integer> event) {
        Libro libro = event.getRowValue();
        int vecchiaCopia = libro.getCopieDisponibili();
        int nuovaCopia = event.getNewValue();
    try {
        libro.setCopieDisponibili(nuovaCopia);
        libroService.modificaLibro(libro, isbnOriginale != null ? isbnOriginale : libro.getISBN());
        listaLibri.setAll(libroService.visualizzaLibri());
    } catch (BibliotecaException e) {
        // Ripristina il vecchio valore
        libro.setCopieDisponibili(vecchiaCopia);
        // Aggiorna la tabella graficamente
        libroTable.refresh();
        alertErrore(e.getMessage());
    }
    }
    /**
     * @brief Gestisce la modifica dell'anno direttamente nella TableView.
     *
     * Recupera il libro selezionato e il nuovo valore dell'anno, prova a salvare la modifica
     * In caso di errore, ripristina il vecchio valore e mostra un messaggio di errore.
     *
     * @post Se non vengono sollevate eccezioni, l'anno del libro è aggiornato
     *       nell'archivio e la tabella della GUI riflette il cambiamento.
     */
    @FXML
    private void onModificaAnno(TableColumn.CellEditEvent<Libro, Integer> event) {
        Libro libro = event.getRowValue();
        int vecchioAnno = libro.getAnnoPubblicazione();
        int nuovoAnno = event.getNewValue();
    try {
        libro.setAnnoPubblicazione(nuovoAnno);
        libroService.modificaLibro(libro, isbnOriginale != null ? isbnOriginale : libro.getISBN());
        listaLibri.setAll(libroService.visualizzaLibri());
    } catch (BibliotecaException e) {
        // Ripristina il vecchio valore
        libro.setAnnoPubblicazione(vecchioAnno);
        // Aggiorna la tabella graficamente
        libroTable.refresh();
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
        alert.setContentText("Vuoi davvero eliminare questo libro?");

        Optional<ButtonType> result = alert.showAndWait();
        //verifica che sia stato premuto un pulsante e che sia il bottone ok
        return result.isPresent() && result.get() == ButtonType.OK;
    }

}
