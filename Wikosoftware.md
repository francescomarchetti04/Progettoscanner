# Softwarre da installare su wiko

## Schema generale e suddivisione package

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
│   └── ImagePreprocessingModule.java
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
│   ├── LocalStorageManager.java
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
├── watchdog/
│   ├── WatchdogManager.java
│   ├── CameraHealthMonitor.java
│   └── CrashRecoveryManager.java   
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
Si deve inserire il codice su ***Andorid studio***  e poi farlo girare sul telefono dopo un root completo per snellirlo dalle funzionalità  intuili. Per vendere le istruzioni per il Root del telefono [cliccare qui](#root).

### Struttura

Come si vede il codice è diviso in diversi package

- [Core](#pkg_core)
    > Cuore del SF, centro decisionale
- [Kiosk](#pkg_kiosk)
    >package per mantenere attiva e bloccata la camera
- [Diagnostic](#pkg_diagnostic)
    >essendo il telefono vecchio e lento meglio avere un sistema di diagnostica
- [Camera](#pkg_camera)
    > si occopa soltanto di scattare la foto
- [Audio](#pkg_audio)
    >Gestisce la comunicazione via audio monofrequenza  con **Esp32** e **Arduino**
- [Sotrage](#pkg_store)
    >Modulo che si occupa di salvare in memoria locale e poi definitivamente su database le scansioni
- [Recognition](#pkg_reco)
    >Si occpua di riconoscere e trasformare in testo i dati rilevati dallo scan della carta
- [Model](#pkg_model)
    >Gestisce i modelli di
     > -*carta*
     > -*stato*
     > -*Sessione*
- [WatchDog](#pkg_dog)
    >modulo che previene i crash di sistema

### Package `core/` {#pkg_core}

E' il centro di tutto il software.
Ha 4 classi:

- [`EventBus.java`](#eventbus)
- [`ScannerController.java`](#scannercontroller)
- [`TriggerSyncModule.java`](#trigger)
- [`AppLifecycleManager.java`](#lifecycle)

#### `EventBus.java`{#eventbus}

Sistema interno di comunicazione tra moduli (camera → recognition → storage → UI) senza dipendenze dirette.

#### `ScannerController.java`{#scannercontroller}

Questa è la classe centrale del sistema.
Coordina tutti i moduli.
È il cervello del sistema.
Coordina:

- Camera
- Normalizzazione
- Hash
- OCR
- Riconoscimento
- Salvataggio
- Sync

#### `TriggerSyncModule.java`{#trigger}

Avvia sincronizzazione manuale o automatica con PC.

#### `AppLifecycleManager.java`{#lifecycle}

Monitora quando l’app va in background/foreground per gestire pausa e ripresa moduli.

### Package `camera/` {#pkg_camera}

Gestisce le 2 classi  necessarie per utilizzare la camera in andorid studio. Con l'api *CameraX*

- [`CameraModule.java`](#cameramodulo)
- [`ImagePreprocessingModule.java`](#preprocess)

#### `CameraModule.java`{#cameramodulo}

Gestisce CameraX:

- Apertura camera
- Ricezione frame
- Invio immagini al controller

#### `ImagePreprocessingModule.java`{#preprocess}

Pre-elabora l’immagine (crop, resize, ottimizzazioni base).

### Package `audio/` {#pkg_audio}

Ha 3 calssi. Servono per gestire tramite segnali  audio mono-frequenza la comunicazione con Esp e Arduino

- [`AudioCaptureModule.java`](#capture)
- [`SignalDecoderModule.java`](#decoder)
- [`AudioEvent.java`](#audioevent)

#### `AudioCaptreModule.java`{#capture}

Registra audio dal microfono.

#### `SignalDecoderModule.java`{#decoder}

Analizza il segnale audio e rileva eventuale trigger (es. suono di attivazione scansione).

#### `AudioEvent.java` {#audioevent}

Evento generato quando viene rilevato un trigger audio.

### Package `recognition/` {#pkg_reco}

Ha 4 calssi. Servono per riconsocere i dati ricavati dalla foto fatta dallo scan.

- [`ImageNormalizer.java`](#normalizer)
- [`HashGenerator.java`](#hash)
- [`CardRecognizer.java`](#cr)
- [`OcrEngine.java`](#ocr)

#### `ImageNormalizer.java`{#normalizer}

Standardizza le immagini:

- Correzione prospettiva
- Ridimensionamento
- Miglioramento contrasto

Serve per rendere stabile hash e OCR.

#### `HashGenerator.java`{#hash}

Crea hash univoco dell’immagine normalizzata.
Usato per:

- Identificazione carta
- Anti-duplicati

#### `CardRecognizer.java` {#cr}

Combina hash + testo OCR per determinare quale carta è stata scansionata

#### `OcrEngine.java` {#ocr}

Estrae testo dalla carta:

- Titolo
- Codice
- Numero seriale

### Package `storage/` {#pkg_store}

Ha 4 calssi. Servono per salvare in locale i dati scannerizzati.

- [`LocalStorageManager.java`](#local)
- [`ScanRepository.java`](#repo)
- [`SyncQueueManager.java`](#coda)

#### `LocalStorageManager.java`{#local}

Salva fisicamente:

- Immagini
- JSON sessioni
- File temporanei

#### `ScanRepository.java` {#repo}

Gestisce i risultati della scansione in memoria e li salva come sessione.

#### `SyncQueueManager.java` {#coda}

Gestisce coda file da sincronizzare con PC in un secondo momento.

### Package `kiosk/` {#pkg_kiosk}

Ha 2 calssi. Servono per bloccare il telefono in modalità fotocamera.

- [`KioskManager.java`](#kmanager)
- [`KioskActivity.java`](#katt)

#### `KioskManager.java`{#kmanager}

Attiva/disattiva modalità kiosk (blocca uscita dall’app).

#### `KioskActivity.java` {#katt}

Versione activity bloccata per uso chiosco.

### Package `model/` {#pkg_model}

Ha 3 calssi. Servono per modellizzare il problema.

- [`CardResult.java`](#card)
- [`ScannerState.java`](#scanners)
- [`ScanSession.java`](#session)

#### `CardResult.java`{#card}

Rappresenta una carta riconosciuta (hash, nome, codice, timestamp).

#### `ScannerState.java` {#scanners}

Enum che rappresenta lo stato dello scanner:

- IDLE
- ACTIVE
- STOPPED

#### `ScanSession.java` {#session}

Rappresenta una sessione completa di scansioni.

### Package `diagnostic/` {#pkg_diagnostic}

Ha 2 calssi. Servono per modellizzare il problema.

- [`SystemDiagnostics.java`](#sysdiag)
- [`PerformanceMonitor.java`](#monitor)

#### `SystemDiagnostics.java`{#sysdiag}

Monitora memoria e stato sistema.

#### `PerformanceMonitor.java` {#monitor}

Misura tempo elaborazione frame e performance.

### Package `watchdog/` {#pkg_dog}

Ha 4 calssi. Servono per salvare in locale i dati scannerizzati.

- [`WatchdogManager.java`](#dogmanager)
- [`CameraHealthMonitor.java`](#healt)
- [`CrashRecoveryManager.java`](#crashreco)

#### `WatchdogManager.java`{#dogmanager}

Controlla periodicamente:

- Stato camera
- Stato scanner
- Se qualcosa si blocca → riavvia.

#### `CameraHealthMonitor.java` {#healt}

Controlla che i frame arrivino regolarmente.

#### `CrashRecoveryManager.java` {#crashreco}

Intercetta crash non gestiti e riavvia automaticamente l’app.

## Root del telefono {#root}
