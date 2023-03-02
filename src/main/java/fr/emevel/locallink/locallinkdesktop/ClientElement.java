package fr.emevel.locallink.locallinkdesktop;

import fr.emevel.locallink.server.LocalLinkClient;
import fr.emevel.locallink.server.sync.FileSender;
import fr.emevel.locallink.server.sync.SyncFolder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class ClientElement {

    GridPane clientGrid = new GridPane();
    Label clientName = new Label("...");
    LocalLinkClient client;

    List<Pair<Label, ProgressBar>> progressBars = new ArrayList<>(5);

    public ClientElement(LocalLinkClient client) {
        this.client = client;

        clientName.setPadding(new Insets(0, 0, 10, 0));
        clientName.setFont(new javafx.scene.text.Font("Calibri", 16));

        GridPane.setHalignment(clientName, HPos.LEFT);
        clientGrid.add(clientName, 0, 0, 3, 1);

        Button button2 = new Button("Add Folder");
        clientGrid.add(button2, 0, 1, 1, 1);
        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AskFolder askFolder = new AskFolder();

                SyncFolder folder = askFolder.ask();

                if (folder == null) {
                    return;
                }

                AskClientFolder askClientFolder = new AskClientFolder(client);

                String clientFolder = askClientFolder.ask();

                if (clientFolder == null) {
                    return;
                }

                client.createLink(folder.getUuid(), clientFolder);
            }
        });

        for (int i = 0 ; i < 5 ; i++) {
            Label fileName = new Label("File");
            fileName.setFont(new javafx.scene.text.Font("Calibri", 12));
            fileName.setTextOverrun(OverrunStyle.LEADING_ELLIPSIS);
            fileName.setPrefWidth(100);
            fileName.setVisible(false);
            clientGrid.add(fileName, 0, 2 + i, 1, 1);

            ProgressBar bar = new ProgressBar();
            bar.setVisible(false);
            bar.setProgress(0.0);

            GridPane.setHalignment(bar, HPos.RIGHT);
            clientGrid.add(bar, 1, 2 + i, 1, 1);

            progressBars.add(Pair.of(fileName, bar));
        }
    }

    public void update() {
        if (client.getName() != null) {
            clientName.setText(client.getName() + " (" + client.getPrintableAddress() + ")");
        }
        List<FileSender> fileSenders = client.getFileSenderExecutor().getCurrentlySending();

        for (int i = 0; i < 5; i++) {
            if (i < fileSenders.size()) {
                FileSender fileSender = fileSenders.get(i);
                progressBars.get(i).getLeft().setText(fileSender.getFile().getName());
                progressBars.get(i).getLeft().setVisible(true);
                progressBars.get(i).getRight().setProgress(fileSender.getCurrent() / (double) fileSender.getLength());
                progressBars.get(i).getRight().setVisible(true);
            } else {
                progressBars.get(i).getLeft().setVisible(false);
                progressBars.get(i).getRight().setVisible(false);
            }
        }
    }

}
