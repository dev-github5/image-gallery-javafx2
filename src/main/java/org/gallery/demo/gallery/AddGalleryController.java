package org.gallery.demo.gallery;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.stage.Stage;
import org.gallery.demo.mo.GalleryDetailMO;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class AddGalleryController implements Initializable {

    private String[] predefinedTags = { "अन्य","परिवार", "दोस्त", "यात्रा", "शादी", "जन्मदिन"};
    private ObservableList<String> selectedCountries;

    private boolean isCleared = false;
    private GalleryDetailMO galleryDetailMO;
    private Stage stage;

    @FXML
    private AnchorPane addGalleryRootId;

    @FXML
    private TextField galleryNameId;

    @FXML
    private ComboBox<String> galleryTagsId;

    @FXML
    private Label selectedTagsLabelId;

    @FXML
    void addGallery(ActionEvent event) {
        String name = galleryNameId.getText();
        String tags = selectedTagsLabelId.getText();

        if (name == null || name.isBlank()) {

        } else if (tags == null || tags.isBlank()) {

        } else {
            //Save gallery
            galleryDetailMO = new GalleryDetailMO(name,tags);

            stage.hide();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTags();
    }

    @FXML
    void clearTags(ActionEvent event) {
        isCleared = true;
        if (selectedCountries != null) {
            selectedCountries.clear();
            galleryTagsId.getSelectionModel().clearSelection();
        }

    }

    private void initializeTags() {
        ObservableList<String> tags = FXCollections.observableList(Arrays.asList(predefinedTags));
        galleryTagsId.setItems(tags);
        galleryTagsId.getSelectionModel().select(0);

        selectedCountries = FXCollections.observableList(new ArrayList<>(Arrays.asList(galleryTagsId.getSelectionModel().getSelectedItem())));

        galleryTagsId.setOnAction(event -> {
            selectedCountries.add(galleryTagsId.getSelectionModel().getSelectedItem());
        });

        selectedTagsLabelId.textProperty().bind(Bindings.createStringBinding(() -> {
            if (isCleared) {
                isCleared = false;
                return "";
            } else if (selectedCountries.isEmpty()) {
                return "";
            } else if (selectedCountries.size() == 1) {
                return selectedCountries.get(0) == null?"":selectedCountries.get(0);
            } else {
                return String.join(", ", selectedCountries);
            }
        }, selectedCountries));
    }

    public GalleryDetailMO getGalleryDetailMO() {
        return galleryDetailMO;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
