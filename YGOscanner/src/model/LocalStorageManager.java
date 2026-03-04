package YGOscanner.storage;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import YGOscanner.model.CardResult;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class LocalStorageManager {

    private final File file;
    private final Gson gson = new Gson();

    public LocalStorageManager(Context context) {
        file = new File(context.getFilesDir(), "scans.json");
    }

    public void saveScan(CardResult result) {

        List<CardResult> list = loadAll();
        list.add(result);

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(list, writer);
        } catch (Exception ignored) {}
    }

    public List<CardResult> loadAll() {

        if (!file.exists())
            return new ArrayList<>();

        try (FileReader reader = new FileReader(file)) {
            Type type = new TypeToken<List<CardResult>>(){}.getType();
            return gson.fromJson(reader, type);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}