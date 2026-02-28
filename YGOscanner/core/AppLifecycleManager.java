package YGOscanner.core;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

public class AppLifecycleManager implements DefaultLifecycleObserver {

    private static AppLifecycleManager instance;
    private boolean isForeground = false;

    private AppLifecycleManager(Context context) {
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    public static void init(Application application) {
        if (instance == null) {
            instance = new AppLifecycleManager(application);
        }
    }

    public static AppLifecycleManager getInstance() {
        return instance;
    }

    @Override
    public void onStart(LifecycleOwner owner) {
        isForeground = true;
    }

    @Override
    public void onStop(LifecycleOwner owner) {
        isForeground = false;
    }

    public boolean isAppInForeground() {
        return isForeground;
    }
}
