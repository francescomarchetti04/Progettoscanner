package YGOscanner.storage;

import android.content.Context;
import YGOscanner.model.CardResult;
import java.util.List;

public class PendingSyncManager {

    private final Context context;
    private final LocalStorageManager storage;

    public PendingSyncManager(Context context, LocalStorageManager storage) {
        this.context = context;
        this.storage = storage;
    }

    public void trySyncPending() {

        if (!isUsbConnected())
            return;

        List<CardResult> scans = storage.loadAll();

        for (CardResult scan : scans) {

            if (!scan.isSynced()) {
                sendToPc(scan);
                scan.setSynced(true);
            }
        }
    }

    private boolean isUsbConnected() {
        return false; // implementazione reale USB
    }

    private void sendToPc(CardResult result) {
        // implementazione trasferimento
    }
}