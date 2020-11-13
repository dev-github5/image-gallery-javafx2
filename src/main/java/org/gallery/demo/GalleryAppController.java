package org.gallery.demo;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.gallery.demo.common.util.CommonUtil;
import org.gallery.demo.common.util.JavaFxCommonUtil;
import org.gallery.demo.constans.ImageGalleryConstants;
import org.gallery.demo.gallery.AddGalleryController;
import org.gallery.demo.gallery.ImageViewerController;
import org.gallery.demo.mo.GalleryDetailMO;
import org.gallery.demo.service.ImageGalleryService;
import org.gallery.demo.task.services.LoadImagesTaskService;
import org.gallery.demo.task.services.MountedDrivesInfoUpdateTaskService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class GalleryAppController implements Initializable {

    private ImageGalleryService imageGalleryService;
    private List<File> allImages = new LinkedList<>();
    private int BATCH_SIZE = 25;
    private int startIndex = 0;
    private int endIndex = 0;
    int row = 0;
    int col = 0;
    int numberOfCols = 0;
    int totalImages = 0;
    GridPane gridPane = new GridPane();
    boolean isInitialized = false;

    @FXML
    private AnchorPane imageGridAnchorPaneId;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private Pane imageGridPaneId;

    @FXML
    private ScrollPane scrollPaneId;

    @FXML
    private Tab tab1;

    @FXML
    private Tab tab2;

    @FXML
    private Tab tab3;

    @FXML
    private TabPane tabPaneId;

    @FXML
    private AnchorPane tab3AnchorId;

    @FXML
    private ScrollPane viewGalleryScrollId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeObjects();
        setupDatabase();
        loadGalleryCards();
        hideTabs(tab2, tab3);
        runBackgroundTasks();
    }

    private void runBackgroundTasks() {
        LoadImagesTaskService loadImagesTaskService = new LoadImagesTaskService(imageGalleryService);
        MountedDrivesInfoUpdateTaskService mountedDrivesInfoUpdateTaskService = new MountedDrivesInfoUpdateTaskService(imageGalleryService);
        mountedDrivesInfoUpdateTaskService.start();

        mountedDrivesInfoUpdateTaskService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                MountedDrivesInfoUpdateTaskService mountedService = (MountedDrivesInfoUpdateTaskService) event.getSource();
                if (mountedService.isAnyDriveMounted()) {
                    System.err.println("Mounted drives info saved successfully at " + (new Date()));
                    if (!loadImagesTaskService.isRunning())
                        loadImagesTaskService.start();
                }else{
                    System.err.println("No mounted drives found at " + (new Date()));
                }
            }
        });

        loadImagesTaskService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                LoadImagesTaskService taskService = (LoadImagesTaskService) event.getSource();
                List<File> files = taskService.getImageFileList();
                if (files.size() > 0) {
                    System.err.println("Images loaded successfully at " + (new Date()));
                    allImages.clear();
                    allImages.addAll(files);
                }
            }
        });

    }

    private void setupDatabase() {
        imageGalleryService.createImageGalleryTable();
        imageGalleryService.createDriveDetailTable();
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

        if (galleryDetailMOS != null && !galleryDetailMOS.isEmpty()) {
            int numberOfCols = (int) maxScreenWidth / 200;
            int spaceLeft = (int) maxScreenWidth % 200;
            gridPane.setPadding(new Insets(10, 0, 10, spaceLeft));
            System.err.println("numberOfCols " + numberOfCols);

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
                addPhotos.prefHeight(35);

                Button removeGallery = new Button("दीर्घा हटाए");
                removeGallery.getStyleClass().add("customLabel2");
                removeGallery.prefHeight(35);
                setActionRemoveGallery(removeGallery, galleryDetailMO.getId());

                vBox.getChildren().addAll(name, tags, addPhotos, removeGallery);

                galleryDetailPane.getChildren().add(vBox);

                if (i != 0 && ((i + 1) % numberOfCols) == 0) {
                    gridPane.add(galleryDetailPane, col, row);
                    row++;
                    col = 0;
                } else {
                    gridPane.add(galleryDetailPane, col, row);
                    col++;
                }
            }
        }

        Pane allPicsPane = JavaFxCommonUtil.createPane(200, 200, ImageGalleryConstants.GLOBAL_PANE_CSS_CLASS);
        VBox allPicVBox = JavaFxCommonUtil.createVBox(5, 200, 200, Pos.CENTER);
        Button allImages = new Button("सभी चित्र");
        allImages.getStyleClass().add("customLabel");
        allPicVBox.getChildren().add(allImages);
        allPicsPane.getChildren().add(allPicVBox);
        setActionOnAllPicBtn(allImages);
        gridPane.add(allPicsPane, col++, row);

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

    private void setActionRemoveGallery(Button removeGallery, Integer id) {
        removeGallery.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imageGalleryService.removeGalleryDetail(id);
                loadGalleryCards();
            }
        });
    }

    private void setActionOnAllPicBtn(Button allImages) {
        allImages.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    tab2.setText("सभी चित्र");
                    addTabs(tab2); // Show tab2 which contains pics for selected gallery(in this case all image gallery)
                    tabPaneId.getSelectionModel().select(tab2); // Select Tab2

                    loadImages();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void hideTabs(Tab... tabs) {
        if (tabs != null && tabs.length > 0) {
            for (Tab tab : tabs) {
                tabPaneId.getTabs().remove(tab);
            }
        }
    }

    private void addTabs(Tab... tabs) {
        if (tabs != null && tabs.length > 0) {
            for (Tab tab : tabs) {
                tabPaneId.getTabs().add(tab);
            }
        }
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

    void loadImages() throws FileNotFoundException {
        if (!allImages.isEmpty()) {
            if (allImages.size() > BATCH_SIZE) {
                endIndex = endIndex + BATCH_SIZE;
                loadGalleryImageByPagination();
            }
        }
    }

    private void loadGalleryImageByPagination() {
        try {
            if (!isInitialized) {
                totalImages = allImages.size();
                double maxScreenWidth = CommonUtil.getScreenDimension().getWidth() - 100;
                numberOfCols = (int) maxScreenWidth / 100;

                //Initialize GridPane
                gridPane.setHgap(10);
                gridPane.setVgap(10);
                gridPane.setMaxWidth(maxScreenWidth);
                gridPane.setPadding(new Insets(10, 0, 10, 25));


            }

            for (int i = startIndex; i < endIndex; i++) {
                //Initialize ImageView
                ImageView imageView = new ImageView();
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                imageView.getStyleClass().add("imageView");
                Image image = new Image(allImages.get(i).toURI().toString(), 100, 100, false, false);
                imageView.setImage(image);
                imageView.setId(String.valueOf(i));
                setActionOnImage(imageView, allImages.get(i), i);
                if (i != 0 && ((i + 1) % numberOfCols) == 0) {
                    gridPane.add(imageView, col, row);
                    row++;
                    col = 0;
                } else {
                    gridPane.add(imageView, col, row);
                    col++;
                }
            }

            //Load More button to add new gallery
            Pane loadMoreButtonPane = JavaFxCommonUtil.createPane(100, 100, ImageGalleryConstants.GLOBAL_PANE_CSS_CLASS);
            VBox vBox = JavaFxCommonUtil.createVBox(5, 100, 100, Pos.CENTER);
            Button loadMoreBtn = new Button("Load More");
            loadMoreBtn.getStyleClass().add("customLabel");
            setActionOnLoadMoreBtn(loadMoreBtn);
            vBox.getChildren().add(loadMoreBtn);
            loadMoreButtonPane.getChildren().add(vBox);
            gridPane.add(loadMoreButtonPane, col, row);

            viewGalleryScrollId.setContent(gridPane);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void setActionOnLoadMoreBtn(Button loadMoreBtn) {
        // Action added to add button
        loadMoreBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if ((endIndex + BATCH_SIZE) < totalImages) {
                    startIndex = startIndex + BATCH_SIZE;
                    endIndex = endIndex + BATCH_SIZE;
                } else {
                    startIndex = endIndex;
                    endIndex = totalImages;
                }
                loadGalleryImageByPagination();
            }
        });
    }

    private void setActionOnImage(ImageView imageView, File file, int currentIndex) {
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/gallery/demo/gallery/image-viewer-view.fxml"));
                        Parent parent = fxmlLoader.load();
                        tab3AnchorId.getChildren().clear();
                        tab3AnchorId.getChildren().add(parent);
                        addTabs(tab3);
                        tabPaneId.getSelectionModel().select(tab3);
                        ImageViewerController imageViewerController = (ImageViewerController) fxmlLoader.getController();
                        imageViewerController.setData(file, currentIndex, allImages, tabPaneId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
