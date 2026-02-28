package YGOscanner.kiosk;



import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;

public class KioskManager {

    private Activity activity;

    public KioskManager(Activity activity) {
        this.activity = activity;
    }

    public void enableKioskMode() {
        activity.startLockTask();
    }

    public void disableKioskMode() {
        activity.stopLockTask();
    }
}