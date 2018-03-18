package presenter;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import model.DataRow;
import presenter.parser.SQLParser;
import presenter.utils.FileLoader;

import java.text.ParseException;

public class InputController {

    private FileLoader fileLoader;
    private SQLParser sqlParser;
    private Alert alert;
    private Alert parsedInformation;
    private GridPane content;
    private TextArea textArea;

    @FXML
    private TextField textField;

    @FXML
    private TableView<DataRow> tableView;

    @FXML
    private TableColumn<DataRow, String> contentColumn;

    @FXML
    public void initialize(){
        this.fileLoader = new FileLoader();
        this.sqlParser = new SQLParser();
        this.alert = new Alert(Alert.AlertType.ERROR);
        this.parsedInformation = new Alert(Alert.AlertType.INFORMATION);
        this.content = new GridPane();
        this.textArea = new TextArea();
        this.contentColumn.setCellValueFactory(new PropertyValueFactory<>("sqlData"));
        setupInformationWindows();
    }

    private void setupInformationWindows(){
        this.alert.setHeaderText("Exception occurred");
        this.parsedInformation.setHeaderText("Parsed data row into SQL Query");
        this.parsedInformation.setContentText("Expand to see parsed data");
        this.textArea.setEditable(false);
        this.textArea.setWrapText(true);
        this.textArea.setMaxWidth(Double.MAX_VALUE);
        this.textArea.setMaxHeight(Double.MAX_VALUE);
        this.content.setMaxWidth(Double.MAX_VALUE);
        this.content.add(textArea, 0 ,0);
    }

    public void fileButtonHandler() {
        try {
            fileLoader.load(tableView, textField);
        }
        catch (ParseException e){
            printUserError(e);
        }
    }

    public void parseButtonHandler(){
        try{
            String data = tableView.getSelectionModel().getSelectedItem().getSqlData();
            showParsedSQL(sqlParser.parseRow(data));
        }catch (IllegalArgumentException e){
            printUserError(e);
        }catch (NullPointerException e){
            printUserError(new IllegalArgumentException("Load file before parsing"));
        }
    }

    private void printUserError(Exception e) {
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    private void showParsedSQL(String parsedData){
        textArea.clear();
        textArea.insertText(0, parsedData);
        parsedInformation.getDialogPane().setExpandableContent(content);
        parsedInformation.showAndWait();

    }
}
