package YGOscanner.audio;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioCaptureModule {

    private final SignalDecoderModule decoder;
    private AudioRecord recorder;
    private boolean running = false;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public AudioCaptureModule(AudioEventListener listener) {
        decoder = new SignalDecoderModule(listener);
    }

    public void start() {

        int bufferSize = AudioRecord.getMinBufferSize(
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
        );

        recorder = new AudioRecord.Builder()
                .setAudioSource(MediaRecorder.AudioSource.MIC)
                .setAudioFormat(new AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(44100)
                        .setChannelMask(AudioFormat.CHANNEL_IN_MONO)
                        .build())
                .setBufferSizeInBytes(bufferSize)
                .build();

        recorder.startRecording();
        running = true;

        executor.execute(() -> {
            short[] buffer = new short[1024];

            while (running) {
                int read = recorder.read(buffer, 0, buffer.length);
                if (read > 0) {
                    decoder.process(buffer);
                }
            }
        });
    }

    public void stop() {
        running = false;
        if (recorder != null) {
            recorder.stop();
            recorder.release();
        }
        executor.shutdown();
    }
}