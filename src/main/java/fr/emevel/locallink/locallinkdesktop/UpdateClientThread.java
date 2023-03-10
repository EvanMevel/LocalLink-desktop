package fr.emevel.locallink.locallinkdesktop;

import javafx.application.Platform;

import java.util.Timer;
import java.util.TimerTask;

public class UpdateClientThread {

    Timer timer = new Timer();

    public UpdateClientThread() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        for (ClientElement client : LocalLinkDesktop.clients) {
                            client.update();
                        }
                    }
                });

            }
        };
        timer.schedule(task, 0, 60);
    }

    public void stop() {
        timer.cancel();
    }


}
