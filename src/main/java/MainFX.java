import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainFX extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        URL url = getClass().getResource("/View.fxml");
        System.out.println(url);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/View.fxml"));
        stage.setTitle("work scheduler");
        stage.setResizable(false);
        Scene scene = new Scene(root, 700,500);
        scene.getStylesheets().addAll(this.getClass().getResource("work.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


