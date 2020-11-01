package org.gallery.demo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.gallery.demo.common.util.CommonUtil;
import org.gallery.demo.common.util.JavaFxCommonUtil;
import org.gallery.demo.constans.ImageGalleryConstants;
import org.gallery.demo.db.transactions.ImageGalleryDao;
import org.gallery.demo.disk.util.LinuxDiskUtil;
import org.gallery.demo.gallery.AddGalleryController;
import org.gallery.demo.mo.GalleryDetailMO;
import org.gallery.demo.service.ImageGalleryService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

public class GalleryAppController implements Initializable {

    @FXML
    private AnchorPane imageGridAnchorPaneId;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private Pane imageGridPaneId;

    @FXML
    private ScrollPane scrollPaneId;

    private ImageGalleryService imageGalleryService;

    @FXML
    void loadImages(ActionEvent event) throws FileNotFoundException {
        List<File> fileList = new ArrayList<>();
        for (String path : LinuxDiskUtil.getDrivesRootPath()) {
            fileList.addAll(readFiles(path));
        }

        if (!fileList.isEmpty()) {
            double imageWidth = imageGridPaneId.getMaxWidth();
            double imageHeight = imageGridPaneId.getMaxWidth();

            GridPane gridPane = new GridPane();
            gridPane.setMaxWidth(imageWidth);
            gridPane.setMaxHeight(imageHeight);

            int row = 0;
            int col = 0;

            for (int i = 0; i < fileList.size(); i++) {
                if (i == 10) {
                    break;
                }
                ImageView imageView = new ImageView();
                imageView.setImage(new Image(new FileInputStream(fileList.get(i))));
                imageView.setFitWidth(200);
                imageView.setFitHeight(100);
                if (i % 5 == 0 && i != 0) {
                    gridPane.add(imageView, col, row);
                    col = 0;
                    row++;
                } else {
                    col++;
                    gridPane.add(imageView, col, row);
                }
            }

            imageGridPaneId.getChildren().clear();
            imageGridPaneId.getChildren().add(gridPane);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeObjects();
        setupDatabase();
        loadGalleryCards();
    }

    @FXML
    void refreshGallery(ActionEvent event) {
        loadGalleryCards();
    }

    private void setupDatabase() {
        imageGalleryService.createImageGalleryTable();
    }

    private void initializeObjects() {
        imageGalleryService = new ImageGalleryService();
    }

    private void loadGalleryCards() {
        List<GalleryDetailMO> galleryDetailMOS = imageGalleryService.getGalleryDetails();
        double maxScreenWidth = CommonUtil.getScreenDimension().getWidth() - 100;
        double maxScreenHeight = CommonUtil.getScreenDimension().getHeight() - 100;
        GridPane gridPane = createGridPane(maxScreenWidth, maxScreenHeight);
        int row = 0;
        int col = 0;

        if (!galleryDetailMOS.isEmpty()) {
            int numberOfCols = (int) maxScreenWidth / 200;
            int spaceLeft = (int) maxScreenWidth % 200;
            gridPane.setPadding(new Insets(10,0,10,spaceLeft));
            System.err.println("numberOfCols "+numberOfCols);

            for (int i = 0; i < galleryDetailMOS.size(); i++) {
                GalleryDetailMO galleryDetailMO = galleryDetailMOS.get(i);
                Pane galleryDetailPane = JavaFxCommonUtil.createPane(200, 200, ImageGalleryConstants.GLOBAL_PANE_CSS_CLASS);

                Label name = new Label(galleryDetailMO.getGalleryName());
                name.setPrefHeight(20);

                Label tags = new Label("[" + String.join(",", galleryDetailMO.getGalleryTags()) + "]");
                tags.setPrefHeight(60);

                name.getStyleClass().add(ImageGalleryConstants.GLOBAL_LABEL_CSS_CLASS);
                tags.getStyleClass().add("customLabel1");

                VBox vBox = JavaFxCommonUtil.createVBox(10, 200, 200, Pos.CENTER);
                Button addPhotos = new Button("चित्र जोड़ें");
                addPhotos.getStyleClass().add("customLabel2");
                addPhotos.prefHeight(70);

                vBox.getChildren().addAll(name, tags,addPhotos);

                galleryDetailPane.getChildren().add(vBox);

                if (i != 0 && ((i+1) % numberOfCols) == 0) {
                    gridPane.add(galleryDetailPane, col, row);
                    row++;
                    col = 0;
                } else {
                    gridPane.add(galleryDetailPane, col, row);
                    col++;
                }
            }
        }

        //Add button to add new gallery
        Pane addButtonPane = JavaFxCommonUtil.createPane(200, 200, ImageGalleryConstants.GLOBAL_PANE_CSS_CLASS);
        VBox vBox = JavaFxCommonUtil.createVBox(5, 200, 200, Pos.CENTER);
        Button addGalleryBtn = new Button("+");
        addGalleryBtn.getStyleClass().add("customLabel");
        vBox.getChildren().add(addGalleryBtn);
        addButtonPane.getChildren().add(vBox);
        gridPane.add(addButtonPane, col, row);

        // Action added to add button
        addGalleryBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    setActionOnGalleryButton();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        scrollPaneId.setContent(gridPane);

    }

    private GridPane createGridPane(double maxScreenWidth, double maxScreenHeight) {
        GridPane gridPane = new GridPane();
        gridPane.setMaxWidth(maxScreenWidth);
        //gridPane.setMaxHeight(maxScreenHeight);
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        return gridPane;
    }

    private void setActionOnGalleryButton() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/gallery/demo/gallery/add-gallery-view.fxml"));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("दीर्घा जोड़ें");

        stage.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                AddGalleryController addGalleryController = (AddGalleryController) fxmlLoader.getController();
                addGalleryController.setStage(stage);
            }
        });

        stage.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                AddGalleryController addGalleryController = (AddGalleryController) fxmlLoader.getController();
                imageGalleryService.saveGalleryDetail(addGalleryController.getGalleryDetailMO());
                loadGalleryCards();
            }
        });
        stage.show();

    }


    private List<File> readFiles(String name) {
        Collection<File> files = FileUtils.listFiles(
                new File(name),
                new RegexFileFilter(".*\\.jpg"),
                DirectoryFileFilter.DIRECTORY
        );
        System.err.println(files);

        return new ArrayList<>(files);

    }


}
