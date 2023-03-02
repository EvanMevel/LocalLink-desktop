package fr.emevel.locallink.locallinkdesktop;

import fr.emevel.locallink.network.packets.PacketFolderList;
import fr.emevel.locallink.server.LocalLinkClient;
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

public class AskClientFolder {

    private final LocalLinkClient client;

    public AskClientFolder(LocalLinkClient client) {
        this.client = client;
    }

    private void addFolder(String parent, PacketFolderList.Folder folder, ObservableList<ClientFolderElement> folders) {
        folders.add(new ClientFolderElement(parent, folder));

        for (PacketFolderList.Folder child : folder.getChilds()) {
            addFolder(parent + folder + "/", child, folders);
        }
    }

    public String ask() {
        Stage popupwindow = new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Select the result folder");

        VBox root = new VBox();

        ObservableList<ClientFolderElement> folders = FXCollections.observableArrayList();

        for (PacketFolderList.Folder clientFolder : client.getClientFolders()) {
            addFolder("", clientFolder, folders);
        }

        ListView<ClientFolderElement> listView = new ListView<>(folders);
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
        return listView.getSelectionModel().getSelectedItems().get(0).toString();
    }

    private static class ClientFolderElement {
        private final String parent;
        private final PacketFolderList.Folder folder;

        public ClientFolderElement(String parent, PacketFolderList.Folder folder) {
            this.parent = parent;
            this.folder = folder;
        }

        @Override
        public String toString() {
            return parent + folder.getName();
        }
    }
}
