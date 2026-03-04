package YGOscanner.watchdog;

public class CameraHealthMonitor {

    private long lastFrameTimestamp = 0;
    private static final long MAX_FRAME_DELAY = 3000;

    public void onFrameReceived() {
        lastFrameTimestamp = System.currentTimeMillis();
    }

    public boolean isCameraAlive() {
        long now = System.currentTimeMillis();
        return (now - lastFrameTimestamp) < MAX_FRAME_DELAY;
    }
}