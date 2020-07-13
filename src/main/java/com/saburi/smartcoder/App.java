package com.saburi.smartcoder;

import com.saburi.utils.FXUIUtils;
import com.saburi.utils.Navigation;
import static com.saburi.utils.Navigation.styleControls;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        try {
             scene = new Scene(Navigation.loadFXML("Scene"));
             scene.getStylesheets().add(styleControls);
        stage.setScene(scene);
        stage.setTitle("Smart Coder");
        stage.setScene(scene);
//        stage.setMaximized(true);
//        stage.resizableProperty().setValue(false);
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.show();
        } catch (IOException e) {
            FXUIUtils.errorMessage(e);
        }
    }

   

    

    public static void main(String[] args) {
        launch();
    }

}
