# Softwarre da installare su wiko 

## schema generale e suddivisione package

```md
YGOScanner/
│
├── MainActivity.java
│
├── core/
│   ├── ScannerController.java
│   ├── EventBus.java
│   ├── TriggerSyncModule.java
│   └── AppLifecycleManager.java
│
├── camera/
│   ├── CameraModule.java
│   └── ImageProcessor.java
│
├── audio/
│   ├── AudioCaptureModule.java
│   ├── SignalDecoderModule.java
│   └── AudioEvent.java
│
├── recognition/
│   ├── ImageNormalizer.java
│   ├── HashGenerator.java
│   ├── CardRecognizer.java
│   └── OcrEngine.java
│
├── storage/
│   ├── LocalDatabase.java
│   ├── ScanRepository.java
│   └── SyncQueueManager.java
│
├── kiosk/
│   ├── KioskManager.java
│   └── KioskActivity.java
│
├── diagnostics/
│   ├── SystemDiagnostics.java
│   └── PerformanceMonitor.java
│
└── model/
    ├── CardResult.java
    ├── ScannerState.java
    └── ScanSession.java
    
 ```

### Commenti sulle classi e la struttura 

#### struttura 