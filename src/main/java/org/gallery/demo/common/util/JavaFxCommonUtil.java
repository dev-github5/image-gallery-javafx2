package org.gallery.demo.common.util;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class JavaFxCommonUtil {
    public static VBox createVBox(int vSpace, double width, double height, Pos pos) {
        VBox vBox = new VBox(5);
        vBox.setAlignment(pos);
        vBox.setPrefWidth(width);
        vBox.setPrefHeight(height);
        return vBox;
    }

    public static Pane createPane(double width, double height, String cssClass) {
        Pane pane = new Pane();
        pane.setPrefWidth(width);
        pane.setPrefHeight(width);
        pane.getStyleClass().add(cssClass);
        return pane;
    }

    public static Dialog createDialog(String title, String dialogContent, Alert.AlertType alertType) {
        Dialog dialog = new Alert(alertType);
        dialog.setTitle(title);
        dialog.setContentText(dialogContent);

        return dialog;
    }
}
