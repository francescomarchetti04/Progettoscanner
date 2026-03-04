package YGOscanner.storage;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class LocalStorageManager {

    private final Context context;

    public LocalStorageManager(Context context) {
        this.context = context;
    }

    public File saveImage(byte[] data, String fileName) throws IOException {
        File dir = new File(context.getFilesDir(), "scans");
        if (!dir.exists()) dir.mkdirs();

        File file = new File(dir, fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(data);
        fos.close();

        return file;
    }

    public File saveJson(String json, String fileName) throws IOException {
        File dir = new File(context.getFilesDir(), "sessions");
        if (!dir.exists()) dir.mkdirs();

        File file = new File(dir, fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(json.getBytes());
        fos.close();

        return file;
    }
}