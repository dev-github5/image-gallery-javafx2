package org.gallery.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.gallery.demo.disk.util.LinuxDiskUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;

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
