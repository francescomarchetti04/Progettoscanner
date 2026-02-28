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

## Commenti sulle classi e la struttura 

Ho creato un software modulare così che upgrade in futuro non comportino un lavoro pesante ma solo alcune modifiche.
Già dallo schema si vede questa modulartità.

Questo software deve girare sul *Wiko Robby* per ora, in futuro penso che cambierò il telfono per uno più prestante.
Si deve inserire il codice su ***Andorid studio***  e poi farlo girare sul telefono dopo un root completo per snellirlo dalle funzionalità  intuili. Per vendere le istruzioni per il Root del telefono [cliccare qui](#Root).

### Struttura

Come si vede il codice è diviso in diversi package

- Core
    > Cuore del SF, centro decisionale
- Kiosk
    >package per mantenere attiva e bloccata la camera
- Diagnostic
    >essendo il telefono vecchio e lento meglio avere un sistema di diagnostica
- Camera
    > si occopa soltanto di scattare la foto 
- Audio
    >Gestisce la comunicazione via audio monofrequenza  con **Esp32** e **Arduino**
- Sotrage
    >Modulo che si occupa di salvare in memoria locale e poi definitivamente su database le scansioni
- Recognition
    >Si occpua di riconoscere e trasformare in testo i dati rilevati dallo scan della carta
- Model
    >Gestisce i modelli di
     > -*carta*
     > -*stato*
     > -*Sessione*

## Root del telefono {#Root}