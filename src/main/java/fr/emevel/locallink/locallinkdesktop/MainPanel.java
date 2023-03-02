package fr.emevel.locallink.locallinkdesktop;

import fr.emevel.locallink.server.LocalLinkServerData;
import fr.emevel.locallink.server.sync.LocalSyncFolder;
import fr.emevel.locallink.server.sync.SyncFolder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainPanel {

    private static final Font TITLE = new Font("Calibri", 16);
    private static final Font TEXT = new Font("Calibri", 12);

    public static Label createTitle(String text) {
        Label label = new Label(text);
        label.setFont(TITLE);
        return label;
    }

    public static Label createText(String text) {
        Label label = new Label(text);
        label.setFont(TEXT);
        return label;
    }

    public GridPane root = new GridPane();
    public VBox syncFolderList = new VBox();
    public VBox clientList = new VBox();

    public MainPanel(Stage stage, LocalLinkServerData data, Runnable dataSaver) {
        root.setPadding(new Insets(20));
        root.setHgap(25);
        root.setVgap(15);

        /* TITLE */
        Label label = createTitle("Synchronized folders");
        GridPane.setHalignment(label, HPos.LEFT);
        root.add(label, 0, 0, 8, 1);

        /* FOLDER LIST */
        ScrollPane scrollPane = setupScrollPane(data);
        root.add(scrollPane, 0, 1, 8, 5);

        /* ADD FOLDER BUTTON */
        Button button = addFolderButton(stage, data, dataSaver);
        GridPane.setHalignment(button, HPos.RIGHT);
        root.add(button, 7, 6);

        /* CLIENT LIST */
        ScrollPane clientScroll = clientScrollPane();
        root.add(clientScroll, 9, 0, 1, 9);
    }

    public void addFolder(SyncFolder fo) {
        FXMLLoader fxmlLoader = new FXMLLoader(LocalLinkDesktop.class.getResource("syncFolder.fxml"));
        fxmlLoader.setController(new SyncFolderController(fo));

        try {
            syncFolderList.getChildren().add(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ScrollPane setupScrollPane(LocalLinkServerData data) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(410, 25 * 5);
        scrollPane.setMinSize(410, 25 * 5);

        syncFolderList.setPadding(new Insets(15,15,15,15));

        for (SyncFolder fo : data.getFolders().getFolders()) {
            addFolder(fo);
        }

        scrollPane.setContent(syncFolderList);
        return scrollPane;
    }

    private Button addFolderButton(Stage stage, LocalLinkServerData data, Runnable dataSaver) {
        Button button = new Button("Add folder");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser fileChooser = new DirectoryChooser();
                fileChooser.setTitle("Add a folder to be tracked");
                fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

                File file = fileChooser.showDialog(stage);

                if (file != null) {
                    LocalSyncFolder fo = new LocalSyncFolder(file);
                    addFolder(fo);

                    data.getFolders().addFolder(fo);
                    dataSaver.run();
                }
            }
        });
        return button;
    }

    private ScrollPane clientScrollPane() {
        clientList.setPadding(new Insets(15,15,15,15));

        ScrollPane clientScroll = new ScrollPane();
        clientScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        clientScroll.setFitToWidth(true);
        //clientScroll.setPrefSize(410, 25 * 5);
        //clientScroll.setMinSize(410, 25 * 5);

        clientScroll.setContent(clientList);

        Label clientLabel = new Label("Connected clients: ");

        clientList.getChildren().add(clientLabel);

        return clientScroll;
    }

}
