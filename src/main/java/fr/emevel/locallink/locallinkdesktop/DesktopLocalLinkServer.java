package fr.emevel.locallink.locallinkdesktop;

import fr.emevel.locallink.network.LinkSocket;
import fr.emevel.locallink.server.LocalLinkClient;
import fr.emevel.locallink.server.LocalLinkServer;
import fr.emevel.locallink.server.LocalLinkServerData;

import java.io.IOException;
import java.net.Socket;

public class DesktopLocalLinkServer extends LocalLinkServer {

    public DesktopLocalLinkServer(LocalLinkServerData data, Runnable dataSaver) throws IOException {
        super(data, dataSaver);
    }

    @Override
    protected void clientDisconnected(LinkSocket client) {
        LocalLinkClient cli = (LocalLinkClient) client;
        HelloApplication.removeClient(cli);
    }

    @Override
    protected LinkSocket createClient(Socket sock) throws IOException {
        LocalLinkClient cli = (LocalLinkClient) super.createClient(sock);
        HelloApplication.addClient(cli);
        return cli;
    }
}
