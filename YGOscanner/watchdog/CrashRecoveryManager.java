package YGOscanner.watchdog;

import android.content.Context;
import android.content.Intent;

import YGOscanner.MainActivity;

public class CrashRecoveryManager implements Thread.UncaughtExceptionHandler {

    private final Context context;

    public CrashRecoveryManager(Context context) {
        this.context = context;
    }

    public void register() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

        throwable.printStackTrace();

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}