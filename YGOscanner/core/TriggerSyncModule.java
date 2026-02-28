package YGOscanner.core;

import YGOscanner.audio.AudioEvent;
import YGOscanner.audio.AudioEventListener;
import YGOscanner.camera.CameraModule;

public class TriggerSyncModule implements AudioEventListener {

    private final CameraModule camera;
    private long lastTriggerTime = 0;
    private static final long DEBOUNCE_MS = 600;

    public TriggerSyncModule(CameraModule camera) {
        this.camera = camera;
    }

    @Override
    public void onAudioEvent(AudioEvent event) {

        if (event.getType() == AudioEvent.Type.CARD_STABLE) {

            long now = System.currentTimeMillis();

            if (now - lastTriggerTime > DEBOUNCE_MS) {
                camera.capture();
                lastTriggerTime = now;
            }
        }
    }
}