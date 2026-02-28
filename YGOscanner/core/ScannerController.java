package YGOscanner.core;

import android.content.Context;
import android.graphics.Bitmap;

import YGOscanner.camera.CameraModule;
import YGOscanner.recognition.ImageNormalizer;
import YGOscanner.recognition.HashGenerator;
import YGOscanner.recognition.OcrEngine;
import YGOscanner.recognition.CardRecognizer;
import YGOscanner.storage.ScanRepository;
import YGOscanner.storage.SyncQueueManager;
import YGOscanner.model.CardResult;
import YGOscanner.model.ScannerState;
import YGOscanner.watchdog.CameraHealthMonitor;

public class ScannerController {

    private final Context context;

    private CameraModule cameraModule;
    private ImageNormalizer imageNormalizer;
    private HashGenerator hashGenerator;
    private OcrEngine ocrEngine;
    private CardRecognizer cardRecognizer;
    private ScanRepository scanRepository;
    private SyncQueueManager syncQueueManager;
    private CameraHealthMonitor cameraHealthMonitor;

    private ScannerState scannerState = ScannerState.IDLE;

    public ScannerController(Context context) {
        this.context = context;

        cameraModule = new CameraModule(context, this::onFrameCaptured);
        imageNormalizer = new ImageNormalizer();
        hashGenerator = new HashGenerator();
        ocrEngine = new OcrEngine();
        cardRecognizer = new CardRecognizer();
        scanRepository = new ScanRepository(context);
        syncQueueManager = new SyncQueueManager(context);
        cameraHealthMonitor = new CameraHealthMonitor();
    }

    // =============================
    // PUBLIC API
    // =============================

    public void startScanning() {
        scannerState = ScannerState.ACTIVE;
        cameraModule.startCamera();
    }

    public void stopScanning() {
        scannerState = ScannerState.STOPPED;
        cameraModule.stopCamera();
    }

    public boolean isScannerActive() {
        return scannerState == ScannerState.ACTIVE;
    }

    public boolean isCameraHealthy() {
        return cameraHealthMonitor.isCameraAlive();
    }

    public void restartCamera() {
        cameraModule.stopCamera();
        cameraModule.startCamera();
    }

    public void restartScanner() {
        stopScanning();
        startScanning();
    }

    // =============================
    // FRAME PIPELINE
    // =============================

    private void onFrameCaptured(Bitmap bitmap) {

        cameraHealthMonitor.onFrameReceived();

        if (scannerState != ScannerState.ACTIVE) return;

        // 1️⃣ Normalizzazione immagine
        Bitmap normalized = imageNormalizer.normalize(bitmap);

        // 2️⃣ Generazione hash immagine
        String hash = hashGenerator.generateHash(normalized);

        // 3️⃣ OCR
        ocrEngine.recognizeText(normalized, extractedText -> {

            // 4️⃣ Riconoscimento carta
            CardResult result = cardRecognizer.recognize(hash, extractedText);

            if (result != null) {

                // 5️⃣ Salvataggio
                scanRepository.addResult(result);

                // 6️⃣ EventBus notifica UI
                EventBus.getInstance().post(result);
            }
        });
    }

    // =============================
    // SYNC
    // =============================

    public void saveSession() {
        scanRepository.saveSession();
    }

    public void syncNow() {
        syncQueueManager.processQueue();
    }
}