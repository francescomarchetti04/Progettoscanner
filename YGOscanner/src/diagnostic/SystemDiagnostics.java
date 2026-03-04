package YGOscanner.diagnostic;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;

public class SystemDiagnostics {

    private Context context;

    public SystemDiagnostics(Context context) {
        this.context = context;
    }

    public long getUsedMemory() {
        return Debug.getNativeHeapAllocatedSize() / 1024;
    }

    public long getAvailableMemory() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ActivityManager manager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        manager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem / 1024;
    }

    public boolean isLowMemory() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ActivityManager manager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        manager.getMemoryInfo(memoryInfo);
        return memoryInfo.lowMemory;
    }
}