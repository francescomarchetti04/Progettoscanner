package YGOscanner.core;


import android.content.Context;
import android.graphics.Bitmap;

import YGOscanner.camera.CameraModule;
import YGOscanner.hardware.Esp32Manager;
import YGOscanner.hardware.Esp32Response;
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

    private final Esp32Manager esp32Manager;
    private final CameraModule cameraModule;
    private final ImageNormalizer imageNormalizer;
    private final HashGenerator hashGenerator;
    private final OcrEngine ocrEngine;
    private final CardRecognizer cardRecognizer;
    private final ScanRepository scanRepository;
    private final SyncQueueManager syncQueueManager;
    private final CameraHealthMonitor cameraHealthMonitor;

    private ScannerState scannerState = ScannerState.IDLE;
    private boolean hardwareModeEnabled = true;

    public ScannerController(Context context) {
        this.context = context;

        esp32Manager = new Esp32Manager();
        cameraModule = new CameraModule(context, this::onFrameCaptured);
        imageNormalizer = new ImageNormalizer();
        hashGenerator = new HashGenerator();
        ocrEngine = new OcrEngine();
        cardRecognizer = new CardRecognizer();
        scanRepository = new ScanRepository(context);
        syncQueueManager = new SyncQueueManager(context);
        cameraHealthMonitor = new CameraHealthMonitor();

        initHardwareListener();
    }

    // =============================
    // INITIALIZATION
    // =============================

    private void initHardwareListener() {
        esp32Manager.setConnectionListener(new Esp32Manager.Esp32ConnectionListener() {
            @Override
            public void onConnected() {
                hardwareModeEnabled = true;
            }

            @Override
            public void onDisconnected() {
                hardwareModeEnabled = false;
            }

            @Override
            public void onError(String error) {
                hardwareModeEnabled = false;
            }
        });
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

    public boolean isHardwareConnected() {
        return esp32Manager.isConnected();
    }

    // =============================
    // FRAME PIPELINE
    // =============================

    private void onFrameCaptured(Bitmap bitmap) {

        cameraHealthMonitor.onFrameReceived();

        if (scannerState != ScannerState.ACTIVE) return;

        // 1️⃣ Normalizzazione
        Bitmap normalized = imageNormalizer.normalize(bitmap);

        // 2️⃣ Generazione hash
        String hash = hashGenerator.generateHash(normalized);

        // 3️⃣ OCR asincrono
        ocrEngine.recognizeText(normalized, extractedText -> {

            CardResult result = cardRecognizer.recognize(hash, extractedText);

            if (result == null) return;

            // 4️⃣ Controllo duplicato via ESP32 o fallback
            if (hardwareModeEnabled && esp32Manager.isConnected()) {

                esp32Manager.checkHash(hash, response -> handleHardwareResponse(response, result));

            } else {
                handleOfflineMode(result);
            }
        });
    }

    // =============================
    // HARDWARE RESPONSE HANDLER
    // =============================

    private void handleHardwareResponse(Esp32Response response, CardResult result) {

        switch (response.getStatus()) {

            case NEW:
                saveScan(result);
                EventBus.getInstance().post(result);
                break;

            case DUPLICATE:
                EventBus.getInstance().post("DUPLICATE");
                break;

            case NOT_CONNECTED:
            case ERROR:
                handleOfflineMode(result);
                break;
        }
    }

    // =============================
    // OFFLINE MODE
    // =============================

    private void handleOfflineMode(CardResult result) {

        // Salvataggio locale
        saveScan(result);

        // Inserimento in coda per sync futura
        syncQueueManager.enqueue(result);

        EventBus.getInstance().post("OFFLINE_SAVED");
    }

    // =============================
    // STORAGE
    // =============================

    private void saveScan(CardResult result) {
        scanRepository.addResult(result);
    }

    public void saveSession() {
        scanRepository.saveSession();
    }

    // =============================
    // SYNC
    // =============================

    public void syncNow() {
        syncQueueManager.processQueue();
    }
}