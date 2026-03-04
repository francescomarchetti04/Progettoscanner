package YGOscannerPC.util;


import java.io.*;
import java.nio.file.*;
import java.util.Properties;

public class ConfigManager {

    private static final String CONFIG_FOLDER = System.getProperty("user.home") + File.separator + "YGOScanner";
    private static final String CONFIG_FILE = CONFIG_FOLDER + File.separator + "config.properties";

    private final Properties properties = new Properties();

    public ConfigManager() {
        load();
    }

    private void load() {
        try {
            Files.createDirectories(Paths.get(CONFIG_FOLDER));
            File file = new File(CONFIG_FILE);

            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    properties.load(fis);
                }
            } else {
                setDefaultValues();
                save();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setDefaultValues() {
        properties.setProperty("database.path", CONFIG_FOLDER + File.separator + "collection.db");
        properties.setProperty("esp32.port", "AUTO");
        properties.setProperty("auto.sync", "true");
        properties.setProperty("last.export.path", CONFIG_FOLDER);
    }

    public void save() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            properties.store(fos, "YGO Scanner Configuration");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =========================
    // GETTERS & SETTERS
    // =========================

    public String getDatabasePath() {
        return properties.getProperty("database.path");
    }

    public void setDatabasePath(String path) {
        properties.setProperty("database.path", path);
        save();
    }

    public String getEsp32Port() {
        return properties.getProperty("esp32.port");
    }

    public void setEsp32Port(String port) {
        properties.setProperty("esp32.port", port);
        save();
    }

    public boolean isAutoSyncEnabled() {
        return Boolean.parseBoolean(properties.getProperty("auto.sync"));
    }

    public void setAutoSyncEnabled(boolean enabled) {
        properties.setProperty("auto.sync", String.valueOf(enabled));
        save();
    }

    public String getLastExportPath() {
        return properties.getProperty("last.export.path");
    }

    public void setLastExportPath(String path) {
        properties.setProperty("last.export.path", path);
        save();
    }
}