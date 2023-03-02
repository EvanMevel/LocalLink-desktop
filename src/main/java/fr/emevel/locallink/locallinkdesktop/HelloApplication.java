package fr.emevel.locallink.locallinkdesktop;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class HelloApplication extends Application {

    public static VBox syncFolderList;

    private static final UploadBarThread uploadBarThread = new UploadBarThread();

    ScrollPane setupScrollPane() throws IOException {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(410, 25 * 5);
        scrollPane.setMinSize(410, 25 * 5);


        syncFolderList = new VBox();
        syncFolderList.setPadding(new Insets(15,15,15,15));

        scrollPane.setContent(syncFolderList);
        return scrollPane;
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
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("test.fxml"));
                    fxmlLoader.setController(new SyncFolderController(file));

                    try {
                        syncFolderList.getChildren().add(fxmlLoader.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        GridPane.setHalignment(button, HPos.RIGHT);
        root.add(button, 7, 6);


        GridPane clientGrid = new GridPane();
        root.setHgap(5);
        root.setVgap(10);

        Label label2 = new Label("My Phone (122.33.32.25)");
        label2.setPadding(new Insets(0, 0, 10, 0));
        label2.setFont(new javafx.scene.text.Font("Calibri", 16));

        GridPane.setHalignment(label2, HPos.LEFT);
        clientGrid.add(label2, 0, 0, 3, 1);

        Button button2 = new Button("Folders");
        clientGrid.add(button2, 0, 1, 1, 1);

        File f = new File("src/main/java/fr/emevel/locallink/locallinkdesktop");

        File[] files = f.listFiles();

        Random rand = new Random();

        for (int i = 0 ; i < 5 ; i++) {
            Label label3 = new Label(files[i].getName());
            label3.setFont(new javafx.scene.text.Font("Calibri", 12));
            label3.setTextOverrun(OverrunStyle.LEADING_ELLIPSIS);
            label3.setPrefWidth(100);
            clientGrid.add(label3, 0, 2 + i, 1, 1);

            ProgressBar bar = new ProgressBar();
            bar.setProgress(rand.nextDouble());

            uploadBarThread.addBar(bar);

            GridPane.setHalignment(bar, HPos.RIGHT);
            clientGrid.add(bar, 1, 2 + i, 1, 1);
        }

        root.add(clientGrid, 0, 7, 3, 7);


        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("LocalLink Desktop");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        uploadBarThread.stop();
    }

    public static void main(String[] args) throws Exception {


        launch();
    }
}