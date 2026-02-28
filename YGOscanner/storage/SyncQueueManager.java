package YGOscanner.storage;

import android.content.Context;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

public class SyncQueueManager {

    private final Context context;
    private final Queue<File> syncQueue = new LinkedList<>();

    public SyncQueueManager(Context context) {
        this.context = context;
    }

    public void enqueue(File file) {
        syncQueue.add(file);
    }

    public void processQueue() {
        while (!syncQueue.isEmpty()) {
            File file = syncQueue.poll();
            // Qui potrai implementare invio USB / WiFi / Bluetooth
            System.out.println("Sync file: " + file.getName());
        }
    }
}
