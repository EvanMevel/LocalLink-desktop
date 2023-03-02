package fr.emevel.locallink.locallinkdesktop;

import fr.emevel.locallink.server.sync.LocalSyncFolder;
import fr.emevel.locallink.server.sync.SyncFolder;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SyncFolderController {

    private final SyncFolder fp;

    public SyncFolderController(SyncFolder fp) {
        this.fp = fp;
    }

    @FXML
    public ImageView deleteImage;

    @FXML
    public Label filePath;


    public void initialize() {
        deleteImage.setImage(new Image(getClass().getResourceAsStream("bin.png")));
        deleteImage.setOnMouseClicked(event -> onDeleteClick());
        if (fp instanceof LocalSyncFolder) {
            filePath.setText(((LocalSyncFolder) fp).getFolder().getAbsolutePath());
        }
    }

    public void onDeleteClick() {
        HelloApplication.data.getFolders().removeFolder(fp);
        HelloApplication.dataSaver.run();

        HelloApplication.syncFolderList.getChildren().remove(deleteImage.getParent());
    }
}
