package org.gallery.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GalleryApp extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("gallery-app-view.fxml"));
        Scene scene = new Scene(parent);

        scene.getStylesheets().add("org/gallery/demo/common-style.css");
        primaryStage.setTitle("चित्र दीर्घा");
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException {

        launch(args);
    }


}
