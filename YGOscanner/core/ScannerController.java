package YGOscanner.core;

import android.content.Context;
import androidx.lifecycle.LifecycleOwner;

import YGOscanner.audio.AudioCaptureModule;
import YGOscanner.camera.CameraModule;
import YGOscanner.model.CardResult;
import YGOscanner.storage.LocalStorageManager;
import YGOscanner.storage.PendingSyncManager;

public class ScannerController {

    private final AudioCaptureModule audioModule;
    private final CameraModule cameraModule;
    private final LocalStorageManager storage;
    private final PendingSyncManager sync;

    public ScannerController(Context context, LifecycleOwner owner) {

        storage = new LocalStorageManager(context);
        sync = new PendingSyncManager(context, storage);

        cameraModule = new CameraModule(context, owner, this::onCardRecognized);
        audioModule = new AudioCaptureModule(new TriggerSyncModule(cameraModule));
    }

    public void start() {
        audioModule.start();
    }

    public void stop() {
        audioModule.stop();
        cameraModule.shutdown();
    }

    private void onCardRecognized(CardResult result) {
        storage.saveScan(result);
        sync.trySyncPending();
    }
}