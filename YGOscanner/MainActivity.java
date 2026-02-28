package YGOscanner;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import YGOscanner.core.ScannerController;
import YGOscanner.kiosk.KioskManager;
import YGOscanner.diagnostics.SystemDiagnostics;

public class MainActivity extends AppCompatActivity {

    private ScannerController scannerController;
    private KioskManager kioskManager;
    private SystemDiagnostics diagnostics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerController = new ScannerController(this);
        kioskManager = new KioskManager(this);
        diagnostics = new SystemDiagnostics(this);

        kioskManager.enableKioskMode();

        scannerController.startScanning();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerController.stopScanning();
        kioskManager.disableKioskMode();
    }
}