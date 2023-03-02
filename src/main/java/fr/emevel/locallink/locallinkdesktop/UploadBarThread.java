package fr.emevel.locallink.locallinkdesktop;

import javafx.scene.control.ProgressBar;

import java.util.*;

public class UploadBarThread {

    List<ProgressBar> bars = new ArrayList<>();

    Random rand = new Random();

    Timer timer = new Timer();

    public UploadBarThread() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                for (ProgressBar bar : bars) {
                    bar.setProgress(rand.nextDouble());
                }
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    public void addBar(ProgressBar bar) {
        bars.add(bar);
    }

    public void stop() {
        timer.cancel();
    }

}
