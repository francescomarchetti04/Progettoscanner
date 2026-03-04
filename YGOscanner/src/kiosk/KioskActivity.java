package YGOscanner.kiosk;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class KioskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        KioskManager kioskManager = new KioskManager(this);
        kioskManager.enableKioskMode();
    }

    @Override
    public void onBackPressed() {
        // Disabilita il tasto back
    }
}