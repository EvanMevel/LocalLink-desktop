package fr.emevel.locallink.locallinkdesktop;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class SyncFolderController {

    private final File fp;

    public SyncFolderController(File fp) {
        this.fp = fp;
    }

    @FXML
    public ImageView deleteImage;

    @FXML
    public Label filePath;


    public void initialize() {
        deleteImage.setImage(new Image(getClass().getResourceAsStream("bin.png")));
        deleteImage.setOnMouseClicked(event -> onDeleteClick());
        filePath.setText(fp.getAbsolutePath());
    }

    public void onDeleteClick() {
        HelloApplication.syncFolderList.getChildren().remove(deleteImage.getParent());
    }
}
