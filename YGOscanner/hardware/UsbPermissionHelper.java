package YGOscanner.hardware;

import android.app.PendingIntent;
import android.content.*;
import android.hardware.usb.*;

public class UsbPermissionHelper {

    private static final String ACTION_USB_PERMISSION = "com.ygoscanner.USB_PERMISSION";

    public static void requestPermission(Context context, UsbManager manager, UsbDevice device) {

        PendingIntent permissionIntent = PendingIntent.getBroadcast(
                context,
                0,
                new Intent(ACTION_USB_PERMISSION),
                PendingIntent.FLAG_IMMUTABLE
        );

        manager.requestPermission(device, permissionIntent);
    }
}
