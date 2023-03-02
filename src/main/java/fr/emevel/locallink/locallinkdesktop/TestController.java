package fr.emevel.locallink.locallinkdesktop;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class TestController {

    private final String fp;

    public TestController(String fp) {
        this.fp = fp;
    }

    @FXML
    public ImageView deleteImage;

    @FXML
    public Label filePath;


    public void initialize() {
        deleteImage.setImage(new Image(getClass().getResourceAsStream("bin.png")));
        deleteImage.setOnMouseClicked(event -> onDeleteClick());
        filePath.setText(fp);
    }

    public void onDeleteClick() {
        System.out.println("Delete");
    }

}
