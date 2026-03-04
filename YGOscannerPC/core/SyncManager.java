package YGOscannerPC.core;

public class SyncManager {

    public static void startAutoSync(String folder) {
        Thread watcher = new Thread(new FileWatcherService(folder));
        watcher.setDaemon(true);
        watcher.start();
    }
}
