package fr.emevel.locallink.locallinkdesktop;

import fr.emevel.locallink.server.sync.LocalSyncFolder;
import fr.emevel.locallink.server.sync.SyncFolder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;

public class AskFolder {

    public AskFolder() {
    }

    public SyncFolder ask() {
        Stage popupwindow = new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Select a folder to sync");

        VBox root = new VBox();

        ObservableList<SyncFolderElement> folders = FXCollections.observableArrayList();

        for (SyncFolder fo : LocalLinkDesktop.serverData.getFolders().getFolders()) {
            folders.add(new SyncFolderElement(fo));
        }

        ListView<SyncFolderElement> listView = new ListView<>(folders);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.getSelectionModel().selectIndices(0);

        Button button1 = new Button("Select");

        AtomicBoolean selected = new AtomicBoolean(false);

        button1.setOnAction(e -> {
            selected.set(true);
            popupwindow.close();
        });

        root.getChildren().add(listView);
        root.getChildren().add(button1);

        Scene scene1 = new Scene(root, 500, 500);

        popupwindow.setScene(scene1);

        popupwindow.showAndWait();

        if (!selected.get()) {
            return null;
        }
        return listView.getSelectionModel().getSelectedItems().get(0).folder;
    }

    private static class SyncFolderElement {
        private final SyncFolder folder;

        public SyncFolderElement(SyncFolder folder) {
            this.folder = folder;
        }

        @Override
        public String toString() {
            if (folder instanceof LocalSyncFolder) {
                return ((LocalSyncFolder) folder).getFolder().getAbsolutePath();
            } else {
                return folder.toString();
            }
        }
    }

}
