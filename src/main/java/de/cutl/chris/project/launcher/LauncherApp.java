package de.cutl.chris.project.launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LauncherApp extends Application {
    
    static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        stage.setTitle("FXML TableView Example");
        Pane myPane = (Pane) FXMLLoader.load(getClass().getResource("/launcher.fxml"));
        Scene myScene = new Scene(myPane);
        stage.setScene(myScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
