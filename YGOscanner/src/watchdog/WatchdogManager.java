package YGOscanner.watchdog;

import android.os.Handler;
import android.os.Looper;

import YGOscanner.core.ScannerController;

public class WatchdogManager {

    private static final long CHECK_INTERVAL_MS = 5000;

    private final ScannerController scannerController;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private boolean running = false;

    public WatchdogManager(ScannerController controller) {
        this.scannerController = controller;
    }

    private final Runnable watchdogTask = new Runnable() {
        @Override
        public void run() {

            if (!scannerController.isCameraHealthy()) {
                scannerController.restartCamera();
            }

            if (!scannerController.isScannerActive()) {
                scannerController.restartScanner();
            }

            if (running) {
                handler.postDelayed(this, CHECK_INTERVAL_MS);
            }
        }
    };

    public void start() {
        running = true;
        handler.postDelayed(watchdogTask, CHECK_INTERVAL_MS);
    }

    public void stop() {
        running = false;
        handler.removeCallbacks(watchdogTask);
    }
}