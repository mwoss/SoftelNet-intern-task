package presenter.utils;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import model.DataRow;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.stream.Stream;

public class FileLoader {

    public FileLoader() {
    }

    public void load(TableView<DataRow> tableView, TextField textField) throws ParseException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose log file");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Log file", "*.txt", "*"));

        File selectedFile = fileChooser.showOpenDialog(null);

        if(selectedFile != null){
            try(Stream<String> file = Files.lines(Paths.get(selectedFile.toURI()))) {
                textField.insertText(0, selectedFile.getName());
                for(String line : (Iterable<String>) file::iterator){
                    tableView.getItems().add(new DataRow(line));
                }

            }catch (IOException e){
                throw new ParseException(e.getMessage(), 0);
            }
        }
    }
}
