package fr.emevel.locallink.locallinkdesktop;

import fr.emevel.locallink.server.LocalLinkClient;
import fr.emevel.locallink.server.LocalLinkServer;
import fr.emevel.locallink.server.LocalLinkServerData;
import fr.emevel.locallink.server.ServerMain;
import fr.emevel.locallink.server.sync.LocalSyncFolder;
import fr.emevel.locallink.server.sync.SyncFolder;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {

    public static LocalLinkServerData data;

    public static LocalLinkServer server;

    public static Runnable dataSaver;

    public static VBox syncFolderList;

    public static VBox clientList = new VBox();

    private static final UpdateClientThread updateClientThread = new UpdateClientThread();

    public static final List<ClientElement> clients = new ArrayList<>();

    ScrollPane setupScrollPane() throws IOException {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(410, 25 * 5);
        scrollPane.setMinSize(410, 25 * 5);


        syncFolderList = new VBox();
        syncFolderList.setPadding(new Insets(15,15,15,15));

        for (SyncFolder fo : data.getFolders().getFolders()) {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("test.fxml"));
            fxmlLoader.setController(new SyncFolderController(fo));

            syncFolderList.getChildren().add(fxmlLoader.load());
        }

        scrollPane.setContent(syncFolderList);
        return scrollPane;
    }

    public static void addClient(LocalLinkClient client) {

        System.out.println("Adding client " + client.getPrintableAddress());

        ClientElement element = new ClientElement(client);
        clients.add(element);
    }

    public static void removeClient(LocalLinkClient client) {
        ClientElement element = clients.stream().filter(c -> c.client == client).findFirst().orElse(null);
        if (element != null) {
            clients.remove(element);
            clientList.getChildren().remove(element.clientGrid);
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 320, 240);


        GridPane root = new GridPane();
        root.setPadding(new Insets(20));
        root.setHgap(25);
        root.setVgap(15);

        Label label = new Label("Synchronized folders");
        label.setFont(new javafx.scene.text.Font("Calibri", 16));

        GridPane.setHalignment(label, HPos.LEFT);
        root.add(label, 0, 0, 8, 1);

        ScrollPane scrollPane = setupScrollPane();

        root.add(scrollPane, 0, 1, 8, 5);

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
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("test.fxml"));
                    fxmlLoader.setController(new SyncFolderController(fo));

                    try {
                        syncFolderList.getChildren().add(fxmlLoader.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    data.getFolders().addFolder(fo);
                    dataSaver.run();
                }
            }
        });

        GridPane.setHalignment(button, HPos.RIGHT);
        root.add(button, 7, 6);


        clientList.setPadding(new Insets(15,15,15,15));

        ScrollPane clientScroll = new ScrollPane();
        clientScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        clientScroll.setFitToWidth(true);
        //clientScroll.setPrefSize(410, 25 * 5);
        //clientScroll.setMinSize(410, 25 * 5);

        clientScroll.setContent(clientList);

        Label clientLabel = new Label("Connected clients: ");

        clientList.getChildren().add(clientLabel);

        root.add(clientScroll, 9, 0, 1, 9);


        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("LocalLink Desktop");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        updateClientThread.stop();

        server.stop();
    }

    public static void main(String[] args) throws Exception {

        data = ServerMain.loadDataFromFile(new File("server.dat"));

        dataSaver = () -> {
            try {
                ServerMain.saveDataToFile(data, new File("server.dat"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        server = new DesktopLocalLinkServer(data, dataSaver);

        server.start();


        launch();
    }
}