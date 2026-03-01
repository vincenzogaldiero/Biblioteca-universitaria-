package biblioteca.gestioneprestiti;
import biblioteca.Main;
import biblioteca.gestioneeccezioni.BibliotecaException;
import biblioteca.gestionelibri.ArchivioLibri;
import biblioteca.gestioneutenti.ArchivioUtenti;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import javafx.scene.control.TableRow;

/**
 * @class PrestitiViewController
 * @brief Controller della vista FXML dedicata alla gestione dei prestiti.
 *
 * Questa classe gestisce l'interazione dell'interfaccia grafica con:
 * - inserimento di un nuovo prestito
 * - ricerca cronologia e prestiti attivi
 * - rimozione prestito
 * - visualizzazione dell’elenco dei prestiti e della cronologia
 *
 * Tutti i controlli vengono delegati a {@link PrestitiService}.
 */
public class PrestitiViewController implements Initializable{
    // ----------------MENU --------------
    @FXML
    private Label utentiID;
    @FXML
    private Label homeID;
    @FXML
    private Label libriID;
    // ------------------ CAMPI INPUT ---------------
    @FXML
    private TextField textISBN;
    @FXML
    private TextField txtMatricola1;
    @FXML
    private TextField txtDataRestituzione;
    // --------------- BOTTONI --------------------
    @FXML
    private Button searchButton;
    @FXML
    private Button addButton;
    @FXML
    private Button viewButton;
    @FXML
    private Button cronButton;
    @FXML
    private Button backButton;
    // -------------- TABELLA -----------------
    @FXML
    private TableView<Prestito> prestitoTable;
    @FXML
    private TableColumn<Prestito, Integer> colID;
    @FXML
    private TableColumn<Prestito, String> colNome;
    @FXML
    private TableColumn<Prestito, String> colCognome;
    @FXML
    private TableColumn<Prestito, String> colMatricola;
    @FXML
    private TableColumn<Prestito, String> colTitolo;
    @FXML
    private TableColumn<Prestito, String> colAutori;
    @FXML
    private TableColumn<Prestito, String> colEmail;
    @FXML
    private TableColumn<Prestito, String> colSBN;
    @FXML
    private TableColumn<Prestito, LocalDate> colDataInizio;
    @FXML
    private TableColumn<Prestito, LocalDate> colDataFine;
    @FXML
    private TableColumn<Prestito, StatoPrestiti> colStato;
    @FXML
    private TableColumn<Prestito, Integer> colAnnoP;
    
    // ------------- LOGICA --------------
    
    /** Archivio dei prestiti attivi su cui operare */
    private ArchivioPrestitiAttivi archivioPrestitiAttivi;

    /** Archivio della cronologia dei prestiti su cui operare */
    private ArchivioCronologiaPrestiti archivioCronologiaPrestiti;

    /** Archivio dei libri su cui operare */
    private ArchivioLibri archivioLibri;

    /** Archivio degli utenti su cui operare */
    private ArchivioUtenti archivioUtenti;

    /** Service che gestisce la logica di validazione e registrazione dei prestiti */
    private PrestitiService prestitiService;

    /** Lista osservabile per popolare la tabella dei prestiti nella UI */
    private ObservableList<Prestito> listaPrestiti;

    /** Flag che indica se la tabella mostra la cronologia dei prestiti (true) o i prestiti attivi (false) */
    private boolean vistaCronologia = false;

    /**
     * @brief Metodo di inizializzazione del controller dei prestiti
     * 
     * Questo metodo viene eseguito automaticamente all'avvio della schermata.
     * 
     * Le operazioni eseguite comprendono:
     * - Inizializzazione del PrestitiService con gli archivi principali (libri, utenti, prestiti attivi, cronologia)
     * - Creazione e popolazione della lista osservabile dei prestiti
     * - Configurazione delle colonne della tabella (TableView) con le proprietà dei prestiti e degli utenti
     * - Impostazione della TableRowFactory per colorare in rosso i prestiti in ritardo
     * - Disabilitazione del pulsante di aggiunta se uno dei campi obbligatori (ISBN, matricola, data restituzione) è vuoto
     * - Disabilitazione del pulsante di ricerca se sia ISBN sia matricola non sono inseriti
     * 
     * @post Il TableView dei prestiti è popolato con i prestiti attivi,
     *       le colonne sono configurate correttamente,
     *       i prestiti in ritardo sono evidenziati,
     *       il pulsante di aggiunta e il pulsante di ricerca sono correttamente abilitati/disabilitati in base ai campi.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        prestitiService = new PrestitiService(Main.archivioLibri, Main.archivioUtenti,Main.archivioPrestitiAttivi, Main.archivioCronologia);

        listaPrestiti = FXCollections.observableArrayList();
        listaPrestiti.setAll(prestitiService.visualizzaPrestitiAttivi());

        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDataInizio.setCellValueFactory(new PropertyValueFactory<>("dataInizio"));
        colDataFine.setCellValueFactory(new PropertyValueFactory<>("dataFine"));
        colStato.setCellValueFactory(new PropertyValueFactory<>("stato"));
        
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        colMatricola.setCellValueFactory(new PropertyValueFactory<>("matricola"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colAutori.setCellValueFactory(new PropertyValueFactory<>("autori"));
        colSBN.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        colAnnoP.setCellValueFactory(new PropertyValueFactory<>("anno"));
        prestitoTable.setItems(listaPrestiti);
        
        //colora i prestiti in ritardo di rosso
        prestitoTable.setRowFactory(tv -> new TableRow<Prestito>() {
            @Override
            protected void updateItem(Prestito p, boolean empty) {
            super.updateItem(p, empty);

                if (p == null || empty) {
                    setStyle("");
                } else if (p.getStato() == StatoPrestiti.RITARDO) {
                    setStyle("-fx-background-color: #ffcccc;");
                } else {
                    setStyle("");
                }
            }
                });
        
        // Disabilita il pulsante di ricerca se ISBN e matricola sono entrambi vuoti
        addButton.disableProperty().bind(
                textISBN.textProperty().isEmpty()
                        .or(txtMatricola1.textProperty().isEmpty())
                        .or(txtDataRestituzione.textProperty().isEmpty())
        );
        
        // Disabilita il pulsante di ricerca se ISBN e matricola sono entrambi vuoti
        searchButton.disableProperty().bind(
            textISBN.textProperty().isEmpty()
        .       and(txtMatricola1.textProperty().isEmpty())
         );
        
        // Resize proporzionale e minWidth
        prestitoTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colID.prefWidthProperty().bind(prestitoTable.widthProperty().multiply(0.05));
        colNome.prefWidthProperty().bind(prestitoTable.widthProperty().multiply(0.10));
        colCognome.prefWidthProperty().bind(prestitoTable.widthProperty().multiply(0.10));
        colMatricola.prefWidthProperty().bind(prestitoTable.widthProperty().multiply(0.07));
        colEmail.prefWidthProperty().bind(prestitoTable.widthProperty().multiply(0.10));
        colTitolo.prefWidthProperty().bind(prestitoTable.widthProperty().multiply(0.15));
        colAutori.prefWidthProperty().bind(prestitoTable.widthProperty().multiply(0.10));
        colSBN.prefWidthProperty().bind(prestitoTable.widthProperty().multiply(0.08));
        colAnnoP.prefWidthProperty().bind(prestitoTable.widthProperty().multiply(0.05));
        colDataInizio.prefWidthProperty().bind(prestitoTable.widthProperty().multiply(0.10));
        colDataFine.prefWidthProperty().bind(prestitoTable.widthProperty().multiply(0.10));
        colStato.prefWidthProperty().bind(prestitoTable.widthProperty().multiply(0.10));

        colID.setMinWidth(40);
        colNome.setMinWidth(80);
        colCognome.setMinWidth(80);
        colMatricola.setMinWidth(60);
        colEmail.setMinWidth(100);
        colTitolo.setMinWidth(120);
        colAutori.setMinWidth(100);
        colSBN.setMinWidth(80);
        colAnnoP.setMinWidth(50);
        colDataInizio.setMinWidth(80);
        colDataFine.setMinWidth(80);
        colStato.setMinWidth(60);
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
    
     //------------OPERAZIONI UTENTI ------------
    
     /**
     * @brief Aggiunge un nuovo prestito utilizzando i dati inseriti nei campi testo.
     * 
     * @pre I campi `textISBN`, `txtMatricola1` e `txtDataRestituzione` devono contenere valori non vuoti
     * @post Se valido, il prestito viene aggiunto all'archivio, aggiornando la tabella dei prestiti attivi
     * 
     * @throws BibliotecaException Se il prestito non può essere registrato
     *                             (es. dati non validi, libro non disponibile, limite prestiti superato)
     * @throws DateTimeParseException Se il formato della data non è valido
     */
    @FXML
    private void onAggiungiPrestito(ActionEvent event) {
        try {
            String matricola = txtMatricola1.getText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataFine = LocalDate.parse(txtDataRestituzione.getText(), formatter);

            prestitiService.registraPrestito(
                    textISBN.getText(),
                    matricola,
                    dataFine
            );

            listaPrestiti.setAll(prestitiService.visualizzaPrestitiAttivi());
            alertInfo("Prestito registrato correttamente.");

            textISBN.clear();
            txtMatricola1.clear();
            txtDataRestituzione.clear();
        } catch (java.time.format.DateTimeParseException e) {
            alertErrore("Formato data non valido! Usa il formato dd/MM/yyyy.");
        } catch (BibliotecaException e) {
            alertErrore(e.getMessage());
        }
    }
    
    /**
    * @brief Esegue la ricerca nella cronologia dei prestiti
    *        per matricola utente o per ISBN libro.
    * 
    * @pre Almeno uno dei campi `txtMatricola1` o `textISBN` deve essere compilato
    * @post La tabella `listaPrestiti` viene aggiornata con i risultati della ricerca
    * 
    * @throws BibliotecaException Se non viene trovato alcun prestito oppure se i dati non sono validi
    */
    @FXML
    private void onRicercaPrestito(ActionEvent event) {
        try {
            if (vistaCronologia) {
                listaPrestiti.setAll(
                    prestitiService.ricercaPrestitiCronologia(
                        txtMatricola1.getText(),
                        textISBN.getText()
                    )
                );
            } else {
                listaPrestiti.setAll(
                    prestitiService.ricercaPrestitiAttivi(
                        txtMatricola1.getText(),
                        textISBN.getText()
                    )
                );
            }
        } catch (BibliotecaException e) {
            alertErrore(e.getMessage());
        }
    }
   
     /**
     * @brief Rimuove un prestito attivo dall'archivio utilizzando i dati inseriti nei campi testo (ISBN e matricola)
     * 
     * 
     * 
     * @pre Deve essere selezionato un prestito valido nella tabella
     * @post Il prestito selezionato viene rimosso dai prestiti attivi, aggiunto alla cronologia e la tabella dei prestiti viene aggiornata.
     * 
     * @throws BibliotecaException Se il prestito non è valido o se si verificano errori
     */
    @FXML
    private void onRestituzioneLibro(ActionEvent event) {
        Prestito selezionato = prestitoTable.getSelectionModel().getSelectedItem();

        if (selezionato == null) {
            alertErrore("Seleziona un prestito dalla tabella.");
            return;
        }

        try {
            prestitiService.eliminaPrestitoAttivo(selezionato);
            listaPrestiti.setAll(prestitiService.visualizzaPrestitiAttivi());
            alertInfo("Libro restituito correttamente.");
        } catch (BibliotecaException e) {
            alertErrore(e.getMessage());
        }
    }
     /**
     * @brief Mostra l'intero elenco degli prestiti attivi presenti nell’archivio.
     * 
     * @post La tabella dei prestiti mostra tutti i prestiti attivi correnti e la vistaCronologia è impostata a false.
     */
    @FXML
    private void onVisualizzaPrestito(ActionEvent event) {
        vistaCronologia = false;
        listaPrestiti.setAll(prestitiService.visualizzaPrestitiAttivi());

    }
     /**
     * @brief Mostra l'intero elenco della cronologia dei prestiti nell’archivio.
     * 
     * @post La tabella dei prestiti (cronologia) mostra tutti i prestiti chiusi e la vistaCronologia è impostata a true.
     */
    @FXML
    private void onVisualizzaCronologia(ActionEvent event) {
        vistaCronologia = true;
        listaPrestiti.setAll(prestitiService.visualizzaCronologia());

    }

    // ---------------- ALERT ----------------
    /**
    * Mostra un alert informativo per confermare che un'operazione
    * è stata completata correttamente.
    *
    * @param messaggio Testo da mostrare nel popup
    */
    private void alertInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Info");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
    /**
    * Mostra un alert di errore quando si verifica un problema.
    *
    * @param messaggio Testo che descrive l'errore
    */
    private void alertErrore(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Errore");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }




    
}
