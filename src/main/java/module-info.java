module fr.emevel.locallink.locallinkdesktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires locallink.server;
    requires locallink.network;


    opens fr.emevel.locallink.locallinkdesktop to javafx.fxml;
    exports fr.emevel.locallink.locallinkdesktop;
}