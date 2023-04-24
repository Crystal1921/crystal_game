package test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class JavaFXTest extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Button playButton = new Button("Play");

        playButton.setOnAction(e -> {
        });

        StackPane root = new StackPane();
        root.getChildren().add(playButton);
        Scene scene = new Scene(root, 200, 100);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
