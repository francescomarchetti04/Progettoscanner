package YGOscanner.diagnostic;

public class PerformanceMonitor {

    private long frameStart;
    private int frameCount;

    public void startFrame() {
        frameStart = System.nanoTime();
    }

    public double endFrame() {
        long duration = System.nanoTime() - frameStart;
        frameCount++;
        return duration / 1_000_000.0; // ms
    }

    public int getFrameCount() {
        return frameCount;
    }
}
