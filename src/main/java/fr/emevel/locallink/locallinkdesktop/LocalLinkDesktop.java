package fr.emevel.locallink.locallinkdesktop;

import fr.emevel.locallink.client.LocalLinkClientData;
import fr.emevel.locallink.network.DataSaving;
import fr.emevel.locallink.server.LocalLinkClient;
import fr.emevel.locallink.server.LocalLinkServer;
import fr.emevel.locallink.server.LocalLinkServerData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocalLinkDesktop extends Application {

    public static LocalLinkServerData serverData;
    public static Runnable saveServerData;
    public static LocalLinkServer server;

    public static LocalLinkClientData clientData;
    public static Runnable saveClientData;
    public static fr.emevel.locallink.client.LocalLinkClient client;

    public static final List<ClientElement> clients = new ArrayList<>();
    private static final UpdateClientThread updateClientThread = new UpdateClientThread();

    public static MainPanel mainPanel;

    public static void addClient(LocalLinkClient client) {
        ClientElement element = new ClientElement(client);
        clients.add(element);

        Platform.runLater(() -> {
            mainPanel.clientList.getChildren().add(element.clientGrid);
        });
    }

    public static void removeClient(LocalLinkClient client) {
        ClientElement element = clients.stream().filter(c -> c.client == client).findFirst().orElse(null);
        if (element != null) {
            clients.remove(element);
            Platform.runLater(() -> {
                mainPanel.clientList.getChildren().remove(element.clientGrid);
            });
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        mainPanel = new MainPanel(stage, serverData, saveServerData);

        Scene scene = new Scene(mainPanel.root, 800, 600);

        stage.setTitle("LocalLink Desktop");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        updateClientThread.stop();

        server.stop();

        client.stop();
    }

    public static void main(String[] args) throws Exception {

        DataSaving<LocalLinkServerData> dataSaver =  DataSaving.localFile(new File("server.dat"));
        serverData = dataSaver.load(LocalLinkServerData::new);
        saveServerData = dataSaver.saver(serverData);

        DataSaving<LocalLinkClientData> clientDataSaver = DataSaving.localFile(new File("client.dat"));
        clientData = clientDataSaver.load(LocalLinkClientData::new);
        saveClientData = clientDataSaver.saver(clientData);

        if (!clientData.getUuid().equals(serverData.getUuid())) {
            clientData.setUuid(serverData.getUuid());
            saveClientData.run();
        }

        server = new DesktopLocalLinkServer(serverData, saveServerData);
        server.start();

        client = new fr.emevel.locallink.client.LocalLinkClient(clientData, new File("."), saveClientData);
        client.start();

        launch();
    }
}