package fr.emevel.locallink.locallinkdesktop;

import fr.emevel.locallink.server.LocalLinkClient;
import fr.emevel.locallink.server.LocalLinkServer;
import fr.emevel.locallink.server.LocalLinkServerData;
import fr.emevel.locallink.server.ServerMain;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocalLinkDesktop extends Application {

    public static LocalLinkServerData data;
    public static LocalLinkServer server;
    public static Runnable dataSaver;

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
        mainPanel = new MainPanel(stage, data, dataSaver);

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