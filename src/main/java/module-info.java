module fr.emevel.locallink.locallinkdesktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires locallink.server;
    requires locallink.network;
    requires locallink.client;
    requires org.apache.commons.collections4;
    requires org.apache.commons.lang3;


    opens fr.emevel.locallink.locallinkdesktop to javafx.fxml;
    exports fr.emevel.locallink.locallinkdesktop;
}