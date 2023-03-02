module fr.emevel.locallink.locallinkdesktop {
    requires javafx.controls;
    requires javafx.fxml;


    opens fr.emevel.locallink.locallinkdesktop to javafx.fxml;
    exports fr.emevel.locallink.locallinkdesktop;
}