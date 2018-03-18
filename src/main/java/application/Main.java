package application;

import javafx.application.Application;
import javafx.stage.Stage;
import view.View;

public class Main extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        View view = new View(primaryStage);
        view.showStage();
    }
}
