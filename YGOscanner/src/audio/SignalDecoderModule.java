package YGOscanner.audio;

public class SignalDecoderModule {

    private final AudioEventListener listener;
    private double noiseFloor = 0;

    public SignalDecoderModule(AudioEventListener listener) {
        this.listener = listener;
    }

    public void process(short[] buffer) {

        double sum = 0;
        for (short s : buffer) {
            sum += Math.abs(s);
        }

        double magnitude = sum / buffer.length;

        noiseFloor = (noiseFloor * 0.95) + (magnitude * 0.05);
        double threshold = noiseFloor * 1.8;

        if (magnitude > threshold)
            listener.onAudioEvent(new AudioEvent(AudioEvent.Type.CARD_STABLE));
    }
}