package org.gallery.demo.gallery;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.gallery.demo.common.util.CommonUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ImageViewerController implements Initializable {

    private File imageFile;
    private int currentIndex;
    private List<File> allImageFiles;
    private int totalCount;
    private TabPane tabPane;

    @FXML
    private ImageView imageViewId;

    @FXML
    private AnchorPane imageViewerAnchorId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageViewerAnchorId.setPrefWidth(CommonUtil.getScreenDimension().getWidth());
        imageViewerAnchorId.setPrefHeight(CommonUtil.getScreenDimension().getHeight());
    }

    @FXML
    void showNext(ActionEvent event) {
        if ((currentIndex + 1) < totalCount) {
            setImage(allImageFiles.get(++currentIndex));
        } else {

        }
    }

    @FXML
    void backTab2(ActionEvent event) {
        if (tabPane != null) {
            tabPane.getTabs().remove(2);
            tabPane.getSelectionModel().select(1);
        }
    }

    @FXML
    void showPrev(ActionEvent event) {
        if (currentIndex > 0) {
            setImage(allImageFiles.get(--currentIndex));
        } else {

        }
    }


    private void setImage(File file) {
        try {
            imageViewId.setImage(new Image(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setData(File file, int currentIndex, List<File> allImages, TabPane tabPaneId) {
        this.imageFile = file;
        this.allImageFiles = allImages;
        this.totalCount = allImageFiles.size();
        this.currentIndex = currentIndex;
        this.tabPane = tabPaneId;
        setImage(imageFile);
    }
}
