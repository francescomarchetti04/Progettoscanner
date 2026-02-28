package YGOscanner;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import YGOscanner.core.ScannerController;
import YGOscanner.kiosk.KioskManager;
import YGOscanner.watchdog.WatchdogManager;
import YGOscanner.watchdog.CrashRecoveryManager;
import YGOscanner.diagnostics.SystemDiagnostics;

public class MainActivity extends AppCompatActivity {

    private ScannerController scannerController;
    private KioskManager kioskManager;
    private WatchdogManager watchdogManager;
    private CrashRecoveryManager crashRecoveryManager;
    private SystemDiagnostics diagnostics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 🔒 Modalità kiosk
        kioskManager = new KioskManager(this);
        kioskManager.enableKioskMode();

        // 🩺 Diagnostica
        diagnostics = new SystemDiagnostics(this);

        // 🧠 Scanner Controller
        scannerController = new ScannerController(this);
        scannerController.startScanning();

        // 🛡 Crash Recovery
        crashRecoveryManager = new CrashRecoveryManager(this);
        crashRecoveryManager.register();

        // 🔁 Watchdog
        watchdogManager = new WatchdogManager(scannerController);
        watchdogManager.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (watchdogManager != null) watchdogManager.stop();
        if (scannerController != null) scannerController.stopScanning();
        if (kioskManager != null) kioskManager.disableKioskMode();
    }

    @Override
    public void onBackPressed() {
        // Disabilitato per kiosk
    }
}